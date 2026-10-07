package edu.psu.se411.lab09_inventory.strategy;

import edu.psu.se411.lab09_inventory.model.ElectronicDevice;
import edu.psu.se411.lab09_inventory.model.Item;

/** ElectronicDevice-specific criterion, same reasoning as SearchByAuthor. */
public class SearchByCategory implements SearchStrategy<Item> {

	private final String category;

	public SearchByCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean matches(Item item) {
		return item instanceof ElectronicDevice
				&& ((ElectronicDevice) item).getCategory().equalsIgnoreCase(category);
	}
}
