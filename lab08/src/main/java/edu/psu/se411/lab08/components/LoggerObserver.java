package edu.psu.se411.lab08.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab08.observer.IObserver;
import edu.psu.se411.lab08.observer.ISubject;

/**
 * Second observer required by Lab 08: logs every sensor reading change
 * via SLF4J instead of printing to the dashboard.
 */
public class LoggerObserver implements IObserver {

	private static final Logger logger = LoggerFactory.getLogger(LoggerObserver.class);

	@Override
	public void update(ISubject subject) {
		if (subject instanceof Sensor) {
			Sensor sensor = (Sensor) subject;
			logger.info("{} reading changed to {}", sensor.getName(), sensor.getData());
			System.out.printf("Logger: %s reading changed to %.2f%n", sensor.getName(), sensor.getData());
		}
	}
}
