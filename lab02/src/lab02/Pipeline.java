package lab02;

/**
 * Exercise 3:
 *
 * T = the type the pipeline currently accepts as input.
 * R = the type the pipeline currently produces as output.
 *
 * Each call to addTransformer() does not run anything immediately - it just
 * composes the new transformer with the ones already stored, and hands back
 * a NEW Pipeline<T, V> typed with the updated output type V. Nothing actually
 * runs until execute(input) is called, at which point every transformer added
 * so far runs in the order it was added.
 */
public class Pipeline<T, R> {

    private final Transformer<T, R> composed;

    private Pipeline(Transformer<T, R> composed) {
        this.composed = composed;
    }

    // Start a pipeline with an initial type T (output type == input type, i.e. identity).
    public static <T> Pipeline<T, T> start() {
        return new Pipeline<>(input -> input);
    }

    // Add a transformer that turns the current output type R into a new type V.
    // Returns a new pipeline with the updated output type - the original pipeline
    // is left untouched, so pipelines can be branched and reused safely.
    public <V> Pipeline<T, V> addTransformer(Transformer<R, V> next) {
        Transformer<T, R> previous = this.composed;
        Transformer<T, V> newComposed = input -> next.transform(previous.transform(input));
        return new Pipeline<>(newComposed);
    }

    // Runs every transformer added so far, in order, on the given input.
    public R execute(T input) {
        return composed.transform(input);
    }

    public static void main(String[] args) {
        // In-place transformations: String -> String
        Pipeline<String, String> textPipeline = Pipeline.<String>start()
                .addTransformer(String::trim)
                .addTransformer(String::toUpperCase);

        System.out.println(textPipeline.execute("  hello generics  "));

        // Type-changing transformations: String -> Integer -> Double -> String
        Pipeline<String, String> mixedPipeline = Pipeline.<String>start()
                .addTransformer(String::length)              // String -> Integer
                .addTransformer(len -> len * 2.5)             // Integer -> Double
                .addTransformer(value -> "Result: " + value); // Double -> String

        System.out.println(mixedPipeline.execute("SE411"));

        // Numeric pipeline: Integer -> Integer -> Double
        Pipeline<Integer, Double> numberPipeline = Pipeline.<Integer>start()
                .addTransformer(n -> n + 10)          // Integer -> Integer (in-place)
                .addTransformer(n -> n / 2.0);          // Integer -> Double (type-changing)

        System.out.println(numberPipeline.execute(20));
    }
}
