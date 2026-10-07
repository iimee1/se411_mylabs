package edu.psu.se411.lab09_inventory.core;

import java.util.ArrayList;
import java.util.List;

import edu.psu.se411.lab09_inventory.strategy.SearchStrategy;

/** Generic container: holds a list of any type T (part 1). */
public class Inventory<T> {

	private final List<T> items = new ArrayList<>();

	public void addItem(T item) {
		items.add(item);
	}

	public void removeItem(T item) {
		items.remove(item);
	}

	public List<T> getAllItems() {
		return new ArrayList<>(items);
	}

	/** Strategy Pattern (part 2): matching logic is supplied by the caller. */
	public List<T> findItems(SearchStrategy<T> strategy) {
		List<T> result = new ArrayList<>();
		for (T item : items) {
			if (strategy.matches(item)) {
				result.add(item);
			}
		}
		return result;
	}
}
