package edu.psu.se411.lab11_threads.model;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class CryptoAccount {

	private final String id;
	private double balance;

	public CryptoAccount(String id, double balance) {
		this.id = id;
		this.balance = balance;
	}

	public void addToBalance(double amount) {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount must be positive");
		}
		double current = balance; // READ
		widenRaceWindow();
		balance = current + amount; // WRITE (separate step on purpose, see widenRaceWindow)
	}

	public void retrieveFromBalance(double amount) throws InsufficientBalanceException {
		if (amount < 0) {
			throw new IllegalArgumentException("Amount must be positive");
		}
		double current = balance; // READ
		if (current < amount) {
			throw new InsufficientBalanceException(
					"Insufficient funds in account " + id + ": balance=" + current + ", requested=" + amount);
		}
		widenRaceWindow();
		balance = current - amount; // WRITE
	}

	/**
	 * The read-then-write gap above is naturally only a few CPU instructions
	 * wide, so even with 10,000 real threads hammering 10 shared accounts,
	 * two threads rarely land on the exact same account at the exact same
	 * instant -- the demo would "pass" (CONSISTENT) most runs purely by luck,
	 * which hides the bug instead of demonstrating it. This tiny deliberate
	 * pause widens the window between reading balance and writing it back so
	 * concurrent interleaving on a shared account is reliably observable in
	 * run 1 (unsynchronized), while run 2 stays correct regardless of the
	 * pause, because the whole transfer() call is already serialized by the
	 * class-level lock before any of these methods run.
	 */
	private static void widenRaceWindow() {
		try {
			Thread.sleep(0, 200_000); // 0.2ms
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public String getId() {
		return id;
	}

	public double getBalance() {
		return balance;
	}

	/**
	 * Part 2 of the lab: a SINGLE synchronized method that performs a whole
	 * transfer atomically. It is static, so the lock is CryptoAccount.class
	 * itself -- one global lock shared by every call from every thread, no
	 * matter which two accounts are involved. That is what actually fixes
	 * the double-spending race: making Transaction.execute() synchronized
	 * instead would NOT work, because every thread runs its own separate
	 * Transaction object with its own monitor, so those locks would never
	 * contend with each other.
	 */
	public static synchronized void transfer(CryptoAccount sender, CryptoAccount receiver, double amount)
			throws InsufficientBalanceException {
		sender.retrieveFromBalance(amount);
		receiver.addToBalance(amount);
	}
}
