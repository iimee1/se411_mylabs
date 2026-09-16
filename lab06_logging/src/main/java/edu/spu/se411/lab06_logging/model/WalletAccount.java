package edu.spu.se411.lab06_logging.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.spu.se411.lab06_logging.exceptions.InsufficientFundsException;

public class WalletAccount {

	static Logger logger = LoggerFactory.getLogger(WalletAccount.class);

	private double balance;

    public WalletAccount(double balance) {
        setBalance(balance);
        logger.debug("Wallet account created with balance: {}", balance);
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        logger.debug("Attempting to withdraw: {}", amount);
        if(amount < 0) {
			throw new IllegalArgumentException("Cannot withdraw negative number: " + amount);
		} else if (amount > balance) {
            logger.warn("InsufficientFundsException object created for withdraw amount {} against balance {}", amount, balance);
            throw new InsufficientFundsException("Insufficient funds. Your balance is " + balance);
        } else {
            balance -= amount;
            System.out.println("Withdrawal successful. Remaining balance: " + balance);
        }
    }
    
    public void deposit(double amount) throws IllegalArgumentException {
        logger.debug("Attempting to deposit: {}", amount);
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot deposit negative number: " + amount);
        } else {
            balance += amount;
            System.out.println("Deposit successful. New balance: " + balance);
        }
    }

    public void setBalance(double balance) {
    	if (balance < 0) {
			throw new IllegalArgumentException("Balance cannot be negative: " + balance);
		}
    	
		this.balance = balance;
	}
	
}
