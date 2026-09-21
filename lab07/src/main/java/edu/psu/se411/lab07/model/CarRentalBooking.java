package edu.psu.se411.lab07.model;

import edu.psu.se411.lab07.util.Config;
import edu.psu.se411.lab07.util.InvalidArgumentException;
import edu.psu.se411.lab07.util.MissingInformationException;

public class CarRentalBooking extends Booking {

	private final double dailyRate;
	private Integer rentalDays; // entered later by the customer; null until set

	public CarRentalBooking(String bookingId, String customerName, String travelDate, String destinationCity,
			double dailyRate) throws InvalidArgumentException {
		super(bookingId, customerName, travelDate, destinationCity);
		if (dailyRate < 0) {
			throw new InvalidArgumentException("Daily rate cannot be negative: " + dailyRate);
		}
		this.dailyRate = dailyRate;
	}

	public void setRentalDays(int rentalDays) throws InvalidArgumentException {
		if (rentalDays < Config.MIN_RENTAL_DAYS || rentalDays > Config.MAX_RENTAL_DAYS) {
			throw new InvalidArgumentException(
					"Number of rental days must be between " + Config.MIN_RENTAL_DAYS +
							" and " + Config.MAX_RENTAL_DAYS + ", was " + rentalDays);
		}
		this.rentalDays = rentalDays;
	}

	@Override
	public double computeTotalPrice() throws MissingInformationException {
		if (rentalDays == null) {
			throw new MissingInformationException(
					"Number of rental days has not been set for booking " + getBookingId());
		}
		return dailyRate * rentalDays;
	}
}
