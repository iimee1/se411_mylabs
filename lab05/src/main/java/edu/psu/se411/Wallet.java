package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;

/**
 * Exercise 2: A very small online wallet simulation. Money can only leave
 * the wallet through withdraw(), which sends it to a bank account and
 * refuses the operation (via InsufficientFundsException) if the wallet
 * does not hold enough balance to cover it.
 */
public class Wallet {

    private double balance;

    public Wallet(double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    /**
     * Withdraws money from the wallet to the given bank account.
     *
     * @throws InsufficientFundsException if amount is greater than the
     *         current wallet balance.
     */
    public void withdraw(double amount, String bankAccountNumber) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    "Cannot withdraw " + amount + ": wallet balance is only " + balance);
        }

        balance -= amount;
        System.out.println("Withdrew " + amount + " to bank account " + bankAccountNumber
                + ". Remaining balance: " + balance);
    }
}
