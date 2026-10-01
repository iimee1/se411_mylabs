package edu.psu.se411.lab08.components;

import java.util.ArrayList;
import java.util.List;

import edu.psu.se411.lab08.observer.IObserver;
import edu.psu.se411.lab08.observer.ISubject;

public class Sensor implements ISubject, Cloneable {

	private String name;
	private double data = 0;

	private List<IObserver> observers = new ArrayList<IObserver>();

	public Sensor(String name) {
		this.name = name;
		this.data = 0;
	}

	public void setData(double data) {
		this.data = data;
		notifyObservers();
	}

	public double getData() {
		return data;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void registerObserver(IObserver o) {
		observers.add(o);
	}

	@Override
	public void removeObserver(IObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for (IObserver o : observers) {
			o.update(this);
		}
	}

	/**
	 * Clones this sensor. The clone gets its own, independent, empty
	 * observer list -- it is NOT observed by the original sensor's
	 * observers (per Lab 08 task 3).
	 */
	@Override
	public Sensor clone() {
		try {
			Sensor copy = (Sensor) super.clone();
			copy.observers = new ArrayList<IObserver>();
			return copy;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError("Sensor should be cloneable", e);
		}
	}
}
