# Lab 12 - Written Answers

## Part 1: Fixed vs cached thread pool - performance comparison

Measured with `App` (fixed pool, sized to the number of CPU cores = 8 on this
machine) and `App2` (cached pool), each run 3 times per scenario:

| Scenario                     | Fixed pool (avg)   | Cached pool (avg)   |
|-------------------------------|---------------------|----------------------|
| 1000 accounts, 1,000,000 txns | ~783 ms (587/902/859)  | ~1845 ms (1925/1813/1798) |
| 10 accounts, 1,000,000 txns   | ~677 ms (636/692/704)  | ~1864 ms (1841/1900/1852) |

All runs verified correct: final money volume in the system always matched
the initial volume exactly (no lost/duplicated balance).

**What do you observe?**

The fixed pool is consistently ~2.3-2.7x faster than the cached pool, for
both account counts. The workload here is a huge number (1 million) of
very short-lived tasks (each transaction is just a couple of arithmetic
operations guarded by a `synchronized` method). A fixed pool bounded to a
small number of worker threads (one per CPU core) pays the cost of creating
a thread only 8 times total and then reuses those same 8 threads for all
1,000,000 tasks. A cached pool, by contrast, creates a brand new OS thread
for every task that arrives while no existing thread is idle -- and since
tasks are submitted in a tight loop much faster than a brand-new OS thread
can be created, scheduled and torn down, the cached pool ends up creating
and destroying a very large number of real OS threads over the run. That
thread lifecycle overhead, not lock contention, is what dominates the
cached pool's time.

Interestingly, the number of accounts (10 vs 1000, i.e. the level of lock
contention) barely changes either pool's timing. That's because the
critical section each transaction locks is extremely short (a couple of
field reads/writes), so even with only 10 shared accounts under heavy
concurrent load, threads spend a negligible amount of time actually
waiting on a lock. Contention would matter far more if the critical
section were larger/slower -- here, thread-creation overhead swamps it
completely.

**Conclusion:** for a workload made of many small, short-lived tasks, a
bounded/fixed thread pool is clearly the better choice; a cached pool is
better suited to a smaller number of longer-lived or bursty tasks where
there's a real benefit to not pre-allocating a fixed set of workers and
where thread creation cost is small relative to task duration.

(Note: the starter's `App` sized the fixed pool to `nbTransactions` itself,
i.e. it would have tried to create up to 1,000,000 native OS threads for
the required test cases -- this would exhaust the OS/JVM's native thread
limit and crash. Fixed to `Runtime.getRuntime().availableProcessors()`,
which is the standard sizing for a fixed pool.)

## Part 2: replacing synchronized with locks - "what could go wrong?"

Replacing the per-method `synchronized` with explicit
`java.util.concurrent.locks.ReentrantLock` objects, and making a whole
transfer atomic by holding BOTH the sender's and the receiver's lock for
the duration of the transfer (not just one balance update at a time, which
is all `synchronized` ever did), introduces a classic **deadlock**:

- Thread A repeatedly transfers acc_0 -> acc_1: locks acc_0 first, then
  tries to lock acc_1.
- Thread B repeatedly transfers acc_1 -> acc_0: locks acc_1 first, then
  tries to lock acc_0.

If both threads grab their first lock at close to the same moment, each
then blocks forever waiting for the lock the other thread is holding --
a circular wait, i.e. a deadlock. Neither thread can ever make progress
again.

This was not just reasoned about -- it was actually reproduced (`App3`,
part 2): two threads doing opposite-direction transfers between the same
two accounts, 2000 iterations each. Across 3 separate runs, every single
run deadlocked (both threads still alive/blocked after a 4-second timeout),
confirming this is a reliable, reproducible failure mode of the naive
locking order, not a rare fluke.

## Part 3: fix - resource ordering

Fix: before acquiring any lock, always compare the two accounts by a fixed,
total order (their `id`, compared lexicographically) and lock the
lower-ordered account first, regardless of which account is the sender and
which is the receiver. Now both threads -- no matter which direction their
transfer goes -- always compete for the SAME first lock (`acc_0`), so there
is never a situation where one thread holds `acc_0` while waiting for
`acc_1`, and another holds `acc_1` while waiting for `acc_0`. The circular
wait is structurally impossible.

Verified (`App3`, part 3): the identical adversarial two-thread scenario
that reliably deadlocked under naive locking, run with resource-ordered
locking instead, completed successfully in every one of 3 runs (~6 seconds
each, since each run still does 2000 x 2 transactions with the same
deliberately-widened 0.5ms lock-acquisition gap used to make the deadlock
reproducible), with the final balances exactly conserved (acc_0=10000.00,
acc_1=10000.00, total=20000.00, matching the initial total every time).
