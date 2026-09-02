package lab02;

import java.util.List;

/**
 * Exercise 2: 
 *
 * A generic wrapper class that can only hold a subclass of Number
 * (e.g. Integer, Double), since arithmetic only makes sense on numbers.
 */
public class NumberBox<T extends Number> {

    private T item;

    // Store an item of type T in the wrapper.
    public void setItem(T item) {
        this.item = item;
    }

    // Retrieve the stored item.
    public T getItem() {
        return item;
    }

    // Adds the wrapped item to another number and returns the result as a double.
    public double add(Number other) {
        return item.doubleValue() + other.doubleValue();
    }

    // Static helper: calculates the sum of a list of any Number subtype.
    public static double sum(List<? extends Number> numbers) {
        double total = 0.0;
        for (Number n : numbers) {
            total += n.doubleValue();
        }
        return total;
    }

    public static void main(String[] args) {
        // Test with Integer
        NumberBox<Integer> intBox = new NumberBox<>();
        intBox.setItem(10);
        System.out.println("Integer box item: " + intBox.getItem());
        System.out.println("10 + 5 = " + intBox.add(5));

        // Test with Double
        NumberBox<Double> doubleBox = new NumberBox<>();
        doubleBox.setItem(3.5);
        System.out.println("Double box item: " + doubleBox.getItem());
        System.out.println("3.5 + 2.5 = " + doubleBox.add(2.5));

        // Test sum() with a mixed-but-bounded list
        List<Integer> intList = List.of(1, 2, 3, 4, 5);
        System.out.println("Sum of ints: " + NumberBox.sum(intList));

        List<Double> doubleList = List.of(1.1, 2.2, 3.3);
        System.out.println("Sum of doubles: " + NumberBox.sum(doubleList));
    }
}
