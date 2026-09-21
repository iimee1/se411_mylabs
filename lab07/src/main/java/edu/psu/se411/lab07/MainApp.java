package edu.psu.se411.lab07;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab07.model.Booking;
import edu.psu.se411.lab07.model.CarRentalBooking;
import edu.psu.se411.lab07.model.FlightBooking;
import edu.psu.se411.lab07.model.TrainBooking;
import edu.psu.se411.lab07.service.BookingService;
import edu.psu.se411.lab07.util.InvalidArgumentException;
import edu.psu.se411.lab07.util.MissingInformationException;
import edu.psu.se411.lab07.util.SeatClass;

public class MainApp {

	static Logger logger = LoggerFactory.getLogger(MainApp.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		BookingService bookingService = new BookingService();

		try {
			// Flight booking - happy path
			FlightBooking flight = new FlightBooking("F-001", "Laith Dahlan", "2026-10-01", "Paris", 450.0);
			flight.setLuggageWeight(20.0);
			printPrice(bookingService, flight);

			// Train booking - happy path
			TrainBooking train = new TrainBooking("T-001", "Laith Dahlan", "2026-10-05", "Riyadh",
					SeatClass.FIRST_CLASS);
			train.setDistance(300.0);
			printPrice(bookingService, train);

			// Car rental booking - happy path
			CarRentalBooking car = new CarRentalBooking("C-001", "Laith Dahlan", "2026-10-10", "Jeddah", 120.0);
			car.setRentalDays(5);
			printPrice(bookingService, car);

			// Missing information: price requested before luggage weight was set
			FlightBooking incompleteFlight = new FlightBooking("F-002", "Sara Ahmed", "2026-11-01", "London", 500.0);
			printPrice(bookingService, incompleteFlight);

		} catch (InvalidArgumentException e) {
			logger.error("Booking setup error: {}", e.getMessage(), e);
			System.out.println("Error: " + e.getMessage());
		}

		try {
			// Invalid argument: luggage weight over the 40kg max
			FlightBooking badFlight = new FlightBooking("F-003", "Omar Khalid", "2026-12-01", "Dubai", 300.0);
			badFlight.setLuggageWeight(100.0);
		} catch (InvalidArgumentException e) {
			logger.error("Booking setup error: {}", e.getMessage(), e);
			System.out.println("Error: " + e.getMessage());
		}

		logger.info("Application is ending...");
	}

	private static void printPrice(BookingService bookingService, Booking booking) {
		try {
			double price = bookingService.computeTotalPrice(booking);
			System.out.println(booking.getBookingId() + " total price: " + price);
		} catch (MissingInformationException | InvalidArgumentException e) {
			logger.error("Price calculation error: {}", e.getMessage(), e);
			System.out.println("Error: " + e.getMessage());
		}
	}
}
