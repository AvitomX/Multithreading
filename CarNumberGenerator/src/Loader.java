import java.util.concurrent.ForkJoinPool;

public class Loader {
    public static final int NUMBER_CODE_AMOUNT = 20;

    public static void main(String[] args) {
        int cores = Runtime.getRuntime().availableProcessors() + 1;
        int threshold = NUMBER_CODE_AMOUNT / cores;

        if (NUMBER_CODE_AMOUNT > 0) {
            long start = System.currentTimeMillis();
            new ForkJoinPool(cores).invoke(new NumberCreator(NUMBER_CODE_AMOUNT, threshold));
            System.out.println(((System.currentTimeMillis() - start)) + " ms");
        }
    }
}
