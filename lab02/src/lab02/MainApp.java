package lab02;

import java.util.List;

/**
 * Exercise 4: 
 */
public class MainApp {

    // Accepts a list of ANY type and prints each item.
    // Since we only ever read from the list, an unbounded wildcard is enough.
    public static void printList(List<?> list) {
        for (Object item : list) {
            System.out.println(item);
        }
    }

    // Accepts a list of Number or any subclass of Number (Integer, Double, ...)
    // and calculates the sum. "? extends Number" lets callers pass
    // List<Integer>, List<Double>, etc. without needing List<Number> exactly.
    public static double sumNumbers(List<? extends Number> list) {
        double total = 0.0;
        for (Number n : list) {
            total += n.doubleValue();
        }
        return total;
    }

    public static void main(String[] args) {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        System.out.println("printList with List<String>:");
        printList(names);

        List<Integer> ints = List.of(1, 2, 3);
        System.out.println("\nprintList with List<Integer>:");
        printList(ints);

        System.out.println("\nsumNumbers with List<Integer>: " + sumNumbers(ints));

        List<Double> doubles = List.of(1.5, 2.5, 3.0);
        System.out.println("sumNumbers with List<Double>: " + sumNumbers(doubles));
    }
}
