import java.io.File;
import java.util.Random;
import java.util.Scanner;

public class WordGenerator {
    private static final int WORD_COUNT = 1000;
    private static final Random random = new Random();

    public static String getRandomWord() {
        final File words = new File("src/words.txt");
        try {
            int line = random.nextInt(WORD_COUNT);
            Scanner scanner = new Scanner(words);
            String word;
            do {
                word = scanner.nextLine();
            } while (line-- > 0);
            return word;
        } catch (Exception ignore){return null;}
    }
}
