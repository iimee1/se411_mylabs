package edu.psu.se411.lab08.components;

import edu.psu.se411.lab08.observer.IObserver;
import edu.psu.se411.lab08.observer.ISubject;

public class Dashboard implements IObserver{

	private String name;
	private double sensor_data;
	
	public Dashboard(String name) {
		this.name = name;
	}
	public void display() {
		System.out.println("Dashboard " + name + " displaying sensor data: " + sensor_data);
	}
	@Override
	public void update(ISubject subject) {
		if (subject instanceof Sensor) {
			Sensor sensor = (Sensor) subject;
			
			this.sensor_data = ((Sensor) subject).getData();
			display();
		}
	}
}
