package edu.psu.se411.lab07.model;

import edu.psu.se411.lab07.util.Config;
import edu.psu.se411.lab07.util.InvalidArgumentException;
import edu.psu.se411.lab07.util.MissingInformationException;

public class FlightBooking extends Booking {

	private final double basePrice;
	private Double luggageWeight; // entered later by the customer; null until set

	public FlightBooking(String bookingId, String customerName, String travelDate, String destinationCity,
			double basePrice) throws InvalidArgumentException {
		super(bookingId, customerName, travelDate, destinationCity);
		if (basePrice < 0) {
			throw new InvalidArgumentException("Base price cannot be negative: " + basePrice);
		}
		this.basePrice = basePrice;
	}

	public void setLuggageWeight(double luggageWeight) throws InvalidArgumentException {
		if (luggageWeight < Config.MIN_LUGGAGE_WEIGHT || luggageWeight > Config.MAX_LUGGAGE_WEIGHT) {
			throw new InvalidArgumentException(
					"Luggage weight must be between " + Config.MIN_LUGGAGE_WEIGHT +
							" and " + Config.MAX_LUGGAGE_WEIGHT + " kg, was " + luggageWeight);
		}
		this.luggageWeight = luggageWeight;
	}

	@Override
	public double computeTotalPrice() throws MissingInformationException {
		if (luggageWeight == null) {
			throw new MissingInformationException(
					"Luggage weight has not been set for booking " + getBookingId());
		}
		return basePrice + (luggageWeight * Config.EXTRA_LUGGAGE_RATE);
	}
}
