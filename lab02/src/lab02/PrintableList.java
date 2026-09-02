package lab02;

import java.util.Arrays;
import java.util.List;

/**
 * Exercise 1:
 *
 * A generic class that stores a list of items of type T and can print them.
 */
public class PrintableList<T> {

    private List<T> items;

    // Constructor accepts an array of items and stores them in the list attribute.
    public PrintableList(T[] items) {
        this.items = Arrays.asList(items);
    }

    // Prints every item in the list, one per line.
    public void printAll() {
        for (T item : items) {
            System.out.println(item);
        }
    }

    public static void main(String[] args) {
        String[] names = {"Alice", "Bob", "Charlie"};
        PrintableList<String> stringList = new PrintableList<>(names);

        System.out.println("PrintableList<String> contents:");
        stringList.printAll();

        // Works with any reference type, e.g. Integer
        Integer[] numbers = {1, 2, 3, 4, 5};
        PrintableList<Integer> intList = new PrintableList<>(numbers);

        System.out.println("\nPrintableList<Integer> contents:");
        intList.printAll();
    }
}
