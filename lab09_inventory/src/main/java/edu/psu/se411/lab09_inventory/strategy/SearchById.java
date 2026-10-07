package edu.psu.se411.lab09_inventory.strategy;

import edu.psu.se411.lab09_inventory.model.Item;

/** Generic: works for any Item (Book, ElectronicDevice, or any future type). */
public class SearchById implements SearchStrategy<Item> {

	private final String id;

	public SearchById(String id) {
		this.id = id;
	}

	@Override
	public boolean matches(Item item) {
		return item.getId().equals(id);
	}
}
