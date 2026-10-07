package edu.psu.se411.lab09_inventory.strategy;

import edu.psu.se411.lab09_inventory.model.Item;

/** Generic: works for any Item (Book, ElectronicDevice, or any future type). */
public class SearchByName implements SearchStrategy<Item> {

	private final String name;

	public SearchByName(String name) {
		this.name = name;
	}

	@Override
	public boolean matches(Item item) {
		return item.getName().equalsIgnoreCase(name);
	}
}
