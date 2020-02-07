import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.RecursiveAction;

public class NumberCreator extends RecursiveAction {
    public static final int BUFFER_SIZE = 1_000_000;
    public static final char letters[] = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

    private int fromRegionCode;
    private int toRegionCode;
    private int threshold;
    private String fileName;

    private NumberCreator(String fileName, int fromRegionCode, int toRegionCode, int threshold) {
        this.fileName = fileName;
        this.fromRegionCode = fromRegionCode;
        this.toRegionCode = toRegionCode;
        this.threshold = threshold;
    }

    public NumberCreator(int toRegionCode, int threshold) {
        this("0", 1, toRegionCode, threshold);
    }

    @Override
    protected void compute() {
        int regionCodeAmount = toRegionCode - fromRegionCode;
        if (regionCodeAmount <= threshold) {
            createNumbers(fromRegionCode, toRegionCode);
        } else {
            NumberCreator firstCreator = new NumberCreator(
                    (fileName + 1),
                    fromRegionCode,
                    (fromRegionCode + regionCodeAmount / 2),
                    threshold);

            NumberCreator secondCreator = new NumberCreator(
                    (fileName + 2),
                    (fromRegionCode + regionCodeAmount / 2 + 1),
                    toRegionCode,
                    threshold);

            invokeAll(firstCreator, secondCreator);
        }

    }

    private void createNumbers(int fromRegionCode, int toRegionCode) {
        StringBuilder buffer = new StringBuilder();
        try (PrintWriter writer = new PrintWriter("res/numbers_" + fileName + ".txt")) {
            for (int regionCode = fromRegionCode; regionCode <= toRegionCode; regionCode++) {
                for (int number = 1; number < 1000; number++) {
                    for (char firstLetter : letters) {
                        for (char secondLetter : letters) {
                            for (char thirdLetter : letters) {
                                if (buffer.length() > BUFFER_SIZE) {
                                    writer.write(buffer.toString());
                                    buffer = new StringBuilder();
                                }
                                buffer.append(firstLetter);
                                if (number < 10)
                                    buffer.append("00");
                                else if (number < 100)
                                    buffer.append("0");
                                buffer.append(number).
                                        append(secondLetter).
                                        append(thirdLetter);
                                if (regionCode < 10)
                                    buffer.append("0");
                                buffer.append(regionCode).
                                        append('\n');
                            }
                        }
                    }
                }

            }
            writer.write(buffer.toString());
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
