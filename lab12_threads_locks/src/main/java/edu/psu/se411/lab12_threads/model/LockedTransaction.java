package edu.psu.se411.lab12_threads.model;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

/**
 * Lab 12, parts 2/3: the "synchronized" based transfer replaced by explicit
 * java.util.concurrent.locks.Lock objects. To make a transfer fully atomic
 * (not just each individual balance update), both the sender's and the
 * receiver's locks are held for the whole operation -- which is exactly
 * what synchronized per-method could never do (a synchronized method only
 * ever holds ONE object's monitor at a time). Holding two locks at once is
 * also exactly what opens the door to deadlock, which is the point of this
 * part of the lab.
 */
public class LockedTransaction implements Runnable {

	private final String id;
	private final CryptoAccount sender;
	private final CryptoAccount receiver;
	private final Double amount;
	private final boolean orderedLocking;

	public LockedTransaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount,
			boolean orderedLocking) {
		if (id == null || sender == null || receiver == null || amount == null || amount < 0) {
			throw new IllegalArgumentException("Invalid transaction arguments");
		}
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.orderedLocking = orderedLocking;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (InsufficientBalanceException e) {
			// not an application error, just a transaction that doesn't go through
		}
	}

	public void execute() throws InsufficientBalanceException {
		if (sender == receiver) {
			throw new IllegalArgumentException("sender and receiver must be different accounts");
		}

		CryptoAccount first;
		CryptoAccount second;

		if (orderedLocking) {
			// Part 3 fix: always acquire locks in the SAME global order (here, by
			// account id), no matter which account is the sender and which is the
			// receiver. Two threads transferring in opposite directions between the
			// same two accounts then always compete for the SAME first lock instead
			// of each grabbing a different one -- removing the circular wait.
			if (sender.getId().compareTo(receiver.getId()) <= 0) {
				first = sender;
				second = receiver;
			} else {
				first = receiver;
				second = sender;
			}
		} else {
			// Part 2 (naive, "what could go wrong"): lock sender first, then
			// receiver -- the order depends on the transaction's own direction,
			// which varies from transaction to transaction.
			first = sender;
			second = receiver;
		}

		first.getLock().lock();
		try {
			widenDeadlockWindow();
			second.getLock().lock();
			try {
				sender.withdraw(amount);
				receiver.deposit(amount);
			} finally {
				second.getLock().unlock();
			}
		} finally {
			first.getLock().unlock();
		}
	}

	/**
	 * Same lesson as lab 11's race condition: the gap between acquiring the
	 * first lock and acquiring the second is naturally only a few CPU
	 * instructions wide, so even an adversarial pattern may not deadlock
	 * reliably within a short demo by pure luck. This deliberately widens
	 * that gap so two threads that are going to collide actually do.
	 */
	private static void widenDeadlockWindow() {
		try {
			Thread.sleep(0, 500_000); // 0.5ms
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public String getId() {
		return id;
	}
}
