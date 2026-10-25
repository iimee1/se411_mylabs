package edu.psu.se411.lab12_threads.model;

import java.util.concurrent.locks.ReentrantLock;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

public class CryptoAccount {
	private String id;
	private Double balance;

	/**
	 * Part 2/3 of the lab (locks): this account's own lock, used instead of
	 * the synchronized keyword. Unlike synchronized, a Lock is acquired and
	 * released explicitly (see withdraw/deposit below), which is what lets a
	 * caller hold TWO accounts' locks at once for a whole transfer -- and is
	 * exactly what opens the door to the deadlock this lab asks us to find.
	 */
	private final ReentrantLock lock = new ReentrantLock();

	public CryptoAccount(String id, Double balance) {
		if (balance < 0)
			throw new IllegalArgumentException("Account balance cannot be negative");
		this.id = id;
		this.balance = balance;
	}

	// --- Part 1 (lab 11 solution, unchanged): synchronized per-method locking,
	// used by App (fixed thread pool) and App2 (cached thread pool). ---

	public synchronized void addToBalance(Double amount) {
		if (amount < 0)
			throw new IllegalArgumentException("Amount added to balance cannot be negative");
		balance += amount;
	}

	public synchronized void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
		if (amount > balance)
			throw new InsufficientBalanceException(id);
		balance -= amount;
	}

	// --- Part 2/3 (locks): explicit Lock, used by LockedTransaction/App3.
	// These do NOT synchronize themselves -- the caller must hold this
	// account's lock (via getLock()) before calling them. ---

	public ReentrantLock getLock() {
		return lock;
	}

	/** Caller must already hold this.getLock(). */
	public void deposit(Double amount) {
		if (amount < 0)
			throw new IllegalArgumentException("Amount added to balance cannot be negative");
		balance += amount;
	}

	/** Caller must already hold this.getLock(). */
	public void withdraw(Double amount) throws InsufficientBalanceException {
		if (amount > balance)
			throw new InsufficientBalanceException(id);
		balance -= amount;
	}

	public String getId() {
		return id;
	}

	public Double getBalance() {
		return balance;
	}

}
