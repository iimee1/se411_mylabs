package edu.psu.se411.lab09_inventory.strategy;

/**
 * Strategy Pattern: encapsulates one way of deciding whether an item
 * matches a search, so Inventory.findItems() doesn't need to know
 * anything about how the matching is done.
 */
public interface SearchStrategy<T> {
	boolean matches(T item);
}
