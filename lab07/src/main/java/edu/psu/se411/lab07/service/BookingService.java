package edu.psu.se411.lab07.service;

import edu.psu.se411.lab07.model.Booking;
import edu.psu.se411.lab07.util.InvalidArgumentException;
import edu.psu.se411.lab07.util.MissingInformationException;

public class BookingService {

	// This single method works for ANY booking subtype: it only knows about
	// the abstract Booking type, and Java calls the correct overridden
	// computeTotalPrice() at runtime depending on the actual object passed in.
	// That dynamic dispatch is the polymorphism this lab is about.
	public double computeTotalPrice(Booking booking) throws MissingInformationException, InvalidArgumentException {
		return booking.computeTotalPrice();
	}
}
