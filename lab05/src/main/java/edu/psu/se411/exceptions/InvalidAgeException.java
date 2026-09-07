package edu.psu.se411.exceptions;

/**
 * Exercise 1: Custom checked exception thrown when an age fails
 * validation (i.e. is below the required minimum).
 */
public class InvalidAgeException extends Exception {

    public InvalidAgeException(String message) {
        super(message);
    }
}
