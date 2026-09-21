package edu.psu.se411.lab07.model;

import edu.psu.se411.lab07.util.InvalidArgumentException;
import edu.psu.se411.lab07.util.MissingInformationException;

public abstract class Booking {

	private final String bookingId;
	private final String customerName;
	private final String travelDate;
	private final String destinationCity;

	public Booking(String bookingId, String customerName, String travelDate, String destinationCity)
			throws InvalidArgumentException {
		if (bookingId == null || bookingId.isBlank()) {
			throw new InvalidArgumentException("Booking ID cannot be empty.");
		}
		if (customerName == null || customerName.isBlank()) {
			throw new InvalidArgumentException("Customer name cannot be empty.");
		}
		if (travelDate == null || travelDate.isBlank()) {
			throw new InvalidArgumentException("Travel date cannot be empty.");
		}
		if (destinationCity == null || destinationCity.isBlank()) {
			throw new InvalidArgumentException("Destination city cannot be empty.");
		}
		this.bookingId = bookingId;
		this.customerName = customerName;
		this.travelDate = travelDate;
		this.destinationCity = destinationCity;
	}

	public String getBookingId() {
		return bookingId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getTravelDate() {
		return travelDate;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	// Every subclass provides its own price logic - this is the polymorphism.
	public abstract double computeTotalPrice() throws MissingInformationException, InvalidArgumentException;

	@Override
	public String toString() {
		return "Booking{id='" + bookingId + "', customer='" + customerName +
				"', date='" + travelDate + "', destination='" + destinationCity + "'}";
	}
}
