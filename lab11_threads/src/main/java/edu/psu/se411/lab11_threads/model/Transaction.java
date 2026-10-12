package edu.psu.se411.lab11_threads.model;

import java.util.concurrent.atomic.AtomicInteger;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class Transaction implements Runnable {

	private static final AtomicInteger SEQUENCE = new AtomicInteger(0);

	private final String id;
	private final CryptoAccount sender;
	private final CryptoAccount receiver;
	private final Double amount;
	private final boolean synchronizedMode;

	/**
	 * Matches the exact call the lab specifies:
	 * new Transaction(randomA, randomB, randomAmount)
	 * Runs unsynchronized (part 1) by default.
	 */
	public Transaction(CryptoAccount sender, CryptoAccount receiver, Double amount) {
		this(sender, receiver, amount, false);
	}

	public Transaction(CryptoAccount sender, CryptoAccount receiver, Double amount, boolean synchronizedMode) {
		if (sender == null || receiver == null || amount == null || amount < 0) {
			throw new IllegalArgumentException("Invalid transaction arguments");
		}
		this.id = "Trx_" + SEQUENCE.getAndIncrement();
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
		this.synchronizedMode = synchronizedMode;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (InsufficientBalanceException e) {
			// A random amount can legitimately exceed a random sender's balance --
			// not an application error, just a transaction that doesn't go through.
		}
	}

	/**
	 * Part 1 (synchronizedMode == false): sender.retrieveFromBalance(...) and
	 * receiver.addToBalance(...) run as two separate, unsynchronized steps --
	 * this is exactly the race condition the lab asks us to demonstrate.
	 *
	 * Part 2 (synchronizedMode == true): delegates to the single synchronized
	 * CryptoAccount.transfer(...) method instead, making the whole transfer
	 * atomic.
	 */
	public void execute() throws InsufficientBalanceException {
		if (synchronizedMode) {
			CryptoAccount.transfer(sender, receiver, amount);
		} else {
			if (sender.getBalance() >= amount) {
				sender.retrieveFromBalance(amount);
				receiver.addToBalance(amount);
			} else {
				throw new InsufficientBalanceException(
						"Insufficient funds in account " + sender.getId() + ": " + sender.getBalance());
			}
		}
	}

	public CryptoAccount getSender() {
		return sender;
	}

	public CryptoAccount getReceiver() {
		return receiver;
	}

	public Double getAmount() {
		return amount;
	}

	public String getId() {
		return id;
	}
}
