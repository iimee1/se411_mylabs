package edu.psu.se411.lab07.util;

public class Config {

	private Config() {
		// private constructor to prevent instantiation
	}

	// Flight booking
	public static final double EXTRA_LUGGAGE_RATE = 5.0;      // price per kg
	public static final double MIN_LUGGAGE_WEIGHT = 0.0;
	public static final double MAX_LUGGAGE_WEIGHT = 40.0;

	// Train booking
	public static final double TRAIN_STANDARD_RATE = 0.15;     // price per km
	public static final double TRAIN_FIRST_CLASS_RATE = 0.30;  // price per km
	public static final double MIN_TRAIN_DISTANCE = 1.0;
	public static final double MAX_TRAIN_DISTANCE = 2000.0;

	// Car rental booking
	public static final int MIN_RENTAL_DAYS = 1;
	public static final int MAX_RENTAL_DAYS = 30;
}
