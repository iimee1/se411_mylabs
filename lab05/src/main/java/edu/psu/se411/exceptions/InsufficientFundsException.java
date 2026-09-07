package edu.psu.se411.exceptions;

/**
 * Exercise 2: Custom checked exception thrown when a wallet withdrawal
 * is attempted for more than the current balance.
 */
public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
