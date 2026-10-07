package edu.psu.se411.lab09_inventory.strategy;

import edu.psu.se411.lab09_inventory.model.Book;
import edu.psu.se411.lab09_inventory.model.Item;

/**
 * Book-specific criterion. Still implements SearchStrategy<Item> (not
 * SearchStrategy<Book>) so it can run against the same mixed
 * Inventory<Item> as every other strategy -- it simply matches nothing
 * for items that aren't Books.
 */
public class SearchByAuthor implements SearchStrategy<Item> {

	private final String authorName;

	public SearchByAuthor(String authorName) {
		this.authorName = authorName;
	}

	@Override
	public boolean matches(Item item) {
		return item instanceof Book && ((Book) item).getAuthorName().equalsIgnoreCase(authorName);
	}
}
