package edu.psu.se411.lab12_threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.LockedTransaction;

/**
 * Lab 12, parts 2 and 3: replace "synchronized" with Locks (part 2), show
 * what can go wrong (deadlock), then fix it with resource ordering (part 3).
 *
 * The adversarial scenario in both phases is the classic one: two accounts,
 * two threads transferring in OPPOSITE directions between them, repeatedly.
 * Thread A always does acc_0 -&gt; acc_1 (locks acc_0 first); thread B always
 * does acc_1 -&gt; acc_0 (locks acc_1 first). If both happen to grab their
 * first lock at the same time, each then blocks forever waiting for the
 * other's lock -&gt; deadlock.
 */
public class App3 {

	static Logger logger = LoggerFactory.getLogger(App3.class);

	private static final int ITERATIONS = 2000;
	private static final long DEADLOCK_TIMEOUT_MS = 4000;

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");

		System.out.println("=== Part 2: naive locking (lock sender, then receiver) ===");
		runAdversarialPair(false);

		System.out.println("\n=== Part 3: resource-ordered locking (always lock lower id first) ===");
		runAdversarialPair(true);

		logger.info("Application is closing...");
	}

	private static void runAdversarialPair(boolean orderedLocking) throws InterruptedException {
		CryptoAccount accA = new CryptoAccount("acc_0", 10000.0);
		CryptoAccount accB = new CryptoAccount("acc_1", 10000.0);
		double expectedTotal = accA.getBalance() + accB.getBalance();

		Thread threadAtoB = new Thread(() -> {
			for (int i = 0; i < ITERATIONS; i++) {
				new LockedTransaction("AtoB_" + i, accA, accB, 1.0, orderedLocking).run();
			}
		}, "A-to-B");
		Thread threadBtoA = new Thread(() -> {
			for (int i = 0; i < ITERATIONS; i++) {
				new LockedTransaction("BtoA_" + i, accB, accA, 1.0, orderedLocking).run();
			}
		}, "B-to-A");

		// Daemon: if this really deadlocks, these threads must not prevent the
		// JVM from moving on once we've detected and reported the hang.
		threadAtoB.setDaemon(true);
		threadBtoA.setDaemon(true);

		long startTime = System.currentTimeMillis();
		threadAtoB.start();
		threadBtoA.start();

		threadAtoB.join(DEADLOCK_TIMEOUT_MS);
		threadBtoA.join(DEADLOCK_TIMEOUT_MS);
		long elapsed = System.currentTimeMillis() - startTime;

		boolean hungAtoB = threadAtoB.isAlive();
		boolean hungBtoA = threadBtoA.isAlive();

		if (hungAtoB || hungBtoA) {
			System.out.printf("DEADLOCK DETECTED: thread A-to-B alive=%s, thread B-to-A alive=%s after %dms timeout%n",
					hungAtoB, hungBtoA, DEADLOCK_TIMEOUT_MS);
			logger.error("Deadlock detected (orderedLocking={}): A-to-B alive={}, B-to-A alive={} after {}ms",
					orderedLocking, hungAtoB, hungBtoA, DEADLOCK_TIMEOUT_MS);
		} else {
			double finalTotal = accA.getBalance() + accB.getBalance();
			boolean consistent = Math.abs(finalTotal - expectedTotal) < 1e-6;
			System.out.printf(
					"Both threads finished in %dms. acc_0=%.2f acc_1=%.2f total=%.2f (expected %.2f) -> %s%n",
					elapsed, accA.getBalance(), accB.getBalance(), finalTotal, expectedTotal,
					consistent ? "CONSISTENT" : "INCONSISTENT");
			logger.info("Both threads finished (orderedLocking={}) in {}ms, total={} (expected {}) -> {}",
					orderedLocking, elapsed, finalTotal, expectedTotal, consistent ? "CONSISTENT" : "INCONSISTENT");
		}
	}
}
