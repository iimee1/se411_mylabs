package lab02;

/**
 * Exercise 3: 
 */
public interface Transformer<T, R> {
    R transform(T input);
}
