package edu.psu.se411.lab07.model;

import edu.psu.se411.lab07.util.Config;
import edu.psu.se411.lab07.util.InvalidArgumentException;
import edu.psu.se411.lab07.util.MissingInformationException;
import edu.psu.se411.lab07.util.SeatClass;

public class TrainBooking extends Booking {

	private final SeatClass seatClass;
	private Double distance; // entered later by the system; null until set

	public TrainBooking(String bookingId, String customerName, String travelDate, String destinationCity,
			SeatClass seatClass) throws InvalidArgumentException {
		super(bookingId, customerName, travelDate, destinationCity);
		if (seatClass == null) {
			throw new InvalidArgumentException("Seat class cannot be null.");
		}
		this.seatClass = seatClass;
	}

	public void setDistance(double distance) throws InvalidArgumentException {
		if (distance < Config.MIN_TRAIN_DISTANCE || distance > Config.MAX_TRAIN_DISTANCE) {
			throw new InvalidArgumentException(
					"Distance must be between " + Config.MIN_TRAIN_DISTANCE +
							" and " + Config.MAX_TRAIN_DISTANCE + " km, was " + distance);
		}
		this.distance = distance;
	}

	@Override
	public double computeTotalPrice() throws MissingInformationException {
		if (distance == null) {
			throw new MissingInformationException(
					"Distance has not been set for booking " + getBookingId());
		}
		double rate = (seatClass == SeatClass.FIRST_CLASS)
				? Config.TRAIN_FIRST_CLASS_RATE
				: Config.TRAIN_STANDARD_RATE;
		return distance * rate;
	}
}
