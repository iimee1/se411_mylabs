package edu.psu.se411.lab08.observer;

public interface IObserver {

	/** Update the data with the new data. */
	void update(ISubject subject);
}
