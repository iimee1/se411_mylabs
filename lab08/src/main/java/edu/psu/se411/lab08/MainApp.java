package edu.psu.se411.lab08;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab08.components.Dashboard;
import edu.psu.se411.lab08.components.LoggerObserver;
import edu.psu.se411.lab08.components.Sensor;

public class MainApp {

	private static final Logger logger = LoggerFactory.getLogger(MainApp.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		Sensor temperatureSensor = new Sensor("Temperature Sensor");
		Sensor humiditySensor = new Sensor("Humidity Sensor");

		Dashboard dashboard = new Dashboard("Main Dashboard");
		LoggerObserver loggerObserver = new LoggerObserver();

		temperatureSensor.registerObserver(dashboard);
		temperatureSensor.registerObserver(loggerObserver);
		humiditySensor.registerObserver(dashboard);
		humiditySensor.registerObserver(loggerObserver);

		Random random = new Random();

		for (int i = 0; i < 10; i++) {
			temperatureSensor.setData(20 + random.nextDouble() * 15);
			humiditySensor.setData(40 + random.nextDouble() * 20);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Task 3: cloning a sensor must NOT carry over the original's observers.
		System.out.println("\n--- Cloning demo ---");
		Sensor clonedTemperatureSensor = temperatureSensor.clone();
		System.out.println("Clone starts with no observers -- updating it notifies nobody:");
		clonedTemperatureSensor.setData(99.9);

		Dashboard cloneDashboard = new Dashboard("Clone Dashboard");
		clonedTemperatureSensor.registerObserver(cloneDashboard);
		System.out.println("Registering a NEW observer only on the clone, then updating the clone:");
		clonedTemperatureSensor.setData(42.0);

		System.out.println("Updating the ORIGINAL again to show the clone's new observer is unaffected:");
		temperatureSensor.setData(temperatureSensor.getData() + 1);

		logger.info("Application is ending...");
	}
}
