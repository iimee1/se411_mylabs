package edu.psu.se411.lab09_inventory;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab09_inventory.core.Inventory;
import edu.psu.se411.lab09_inventory.model.Book;
import edu.psu.se411.lab09_inventory.model.ElectronicDevice;
import edu.psu.se411.lab09_inventory.model.Item;
import edu.psu.se411.lab09_inventory.strategy.SearchByAuthor;
import edu.psu.se411.lab09_inventory.strategy.SearchByCategory;
import edu.psu.se411.lab09_inventory.strategy.SearchById;
import edu.psu.se411.lab09_inventory.strategy.SearchByName;

public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static void main(String[] args) {
		logger.info("Application is starting...");

		try {
			Inventory<Item> inventory = new Inventory<>();

			inventory.addItem(new Book("B-001", "Concurrency in Java", "Doug Lea"));
			inventory.addItem(new Book("B-002", "Effective Java", "Joshua Bloch"));
			inventory.addItem(new ElectronicDevice("E-001", "Galaxy Tab", "Tablet"));
			inventory.addItem(new ElectronicDevice("E-002", "ThinkPad X1", "Laptop"));

			System.out.println("--- Full inventory ---");
			displayInventory(inventory);

			System.out.println("\n--- findItems: SearchByName(\"Concurrency in Java\") ---");
			inventory.findItems(new SearchByName("Concurrency in Java")).forEach(System.out::println);

			System.out.println("\n--- findItems: SearchByName(\"ThinkPad X1\") ---");
			inventory.findItems(new SearchByName("ThinkPad X1")).forEach(System.out::println);

			System.out.println("\n--- findItems: SearchById(\"E-001\") ---");
			inventory.findItems(new SearchById("E-001")).forEach(System.out::println);

			System.out.println("\n--- findItems: SearchByAuthor(\"Joshua Bloch\") ---");
			inventory.findItems(new SearchByAuthor("Joshua Bloch")).forEach(System.out::println);

			System.out.println("\n--- findItems: SearchByCategory(\"Laptop\") ---");
			inventory.findItems(new SearchByCategory("Laptop")).forEach(System.out::println);

			List<Item> toRemove = inventory.findItems(new SearchById("B-002"));
			toRemove.forEach(inventory::removeItem);

			System.out.println("\n--- Inventory after removing B-002 ---");
			displayInventory(inventory);

		} catch (Exception e) {
			logger.error("Unexpected error while managing the inventory: {}", e.getMessage(), e);
		}

		logger.info("Application is closing...");
	}

	/** Part 3: works for an Inventory of any product type. */
	private static void displayInventory(Inventory<?> inventory) {
		for (Object item : inventory.getAllItems()) {
			System.out.println(item);
		}
	}
}
