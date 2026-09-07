package edu.psu.se411;

import edu.psu.se411.exceptions.InsufficientFundsException;
import edu.psu.se411.exceptions.InvalidAgeException;

public class App {

    /**
     * Exercise 1: throws InvalidAgeException when age is below 18,
     * otherwise prints a valid-age message.
     */
    public static void validateAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Age must be at least 18, but was " + age + ".");
        }
        System.out.println("Age valid message.");
    }

    public static void main(String[] args) {
        System.out.println("=== Exercise 1: validateAge ===");
        int[] agesToTest = { 15, 20 };
        for (int age : agesToTest) {
            try {
                validateAge(age);
            } catch (InvalidAgeException e) {
                System.out.println("Invalid age rejected: " + e.getMessage());
            }
        }

        System.out.println();
        System.out.println("=== Exercise 2: Wallet withdrawal ===");
        Wallet wallet = new Wallet(100.0);
        System.out.println("Starting balance: " + wallet.getBalance());

        try {
            wallet.withdraw(40.0, "SA0312345678901234567890");
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }

        try {
            wallet.withdraw(1000.0, "SA0312345678901234567890");
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }

        System.out.println("Final balance: " + wallet.getBalance());
    }
}
