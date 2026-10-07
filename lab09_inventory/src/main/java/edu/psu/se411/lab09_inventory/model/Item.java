package edu.psu.se411.lab09_inventory.model;

/**
 * Common base for anything the inventory can store: a unique id and a
 * display name. Book and ElectronicDevice each add their own fields.
 */
public abstract class Item {

	private final String id;
	private final String name;

	public Item(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "[" + id + "] " + name;
	}
}
