package edu.psu.se411.lab12_threads.model;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

public class Transaction implements Runnable {
	private String id;
	private CryptoAccount sender;
	private CryptoAccount receiver;
	private Double amount;

	public Transaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
		super();
		if (amount < 0)
			throw new IllegalArgumentException("Amount used in transaction cannot be negative");

		if (id == null || sender == null || receiver == null || amount < 0)
			throw new IllegalArgumentException();
		this.id = id;
		this.sender = sender;
		this.receiver = receiver;
		this.amount = amount;
	}

	@Override
	public void run() {
		try {
			execute();
		} catch (InsufficientBalanceException e) {
			// System.out.println(e.getMessage());
		}
	}

	public void execute() throws InsufficientBalanceException {
		if (sender == receiver)
			throw new IllegalArgumentException("Cannot make a transaction where the sender and the receiver are the same account");
		if (sender.getBalance() < amount)
			throw new InsufficientBalanceException(sender.getId());

		sender.retrieveFromBalance(amount);
		receiver.addToBalance(amount);
	}

	public CryptoAccount getSender() {
		return sender;
	}

	public void setSender(CryptoAccount sender) {
		this.sender = sender;
	}

	public CryptoAccount getReceiver() {
		return receiver;
	}

	public void setReceiver(CryptoAccount receiver) {
		this.receiver = receiver;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getId() {
		return id;
	}

}
