package edu.psu.se411.lab08.observer;

public interface ISubject {

	/** Register an observer to the subject. */
	void registerObserver(IObserver o);

	/** Remove an observer from the subject. */
	void removeObserver(IObserver o);

	/** Notify all observers of a change in the subject. */
	void notifyObservers();
}
