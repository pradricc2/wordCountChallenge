import countPattern.CountStrategy;
import countPattern.CountStrategyFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class ccwc {

    private static final Logger logger = Logger.getLogger(ccwc.class.getName());

    public static void main(String[] args) {
        if (args.length > 2) {
            printUsageAndExit();
        }

        // Verifica se leggere da stdin o da un file
        boolean readFromStdin = (args.length == 0 || (args.length == 1 && args[0].startsWith("-")));

        // Verifica se è stato specificato un'opzione
        String option = (args.length == 2) ? args[0] : (args.length == 1 && args[0].startsWith("-") ? args[0] : "");

        // Verifica se è stato specificato un file
        String fileName = (args.length == 2) ? args[1] : (args.length == 1 && !args[0].startsWith("-") ? args[0] : null);

        try {
            if (readFromStdin) {
                // Legge dallo standard input
                processInput(option, new BufferedReader(new InputStreamReader(System.in)));
            } else {
                // Legge da un file
                processFile(option, fileName);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> "Error reading file: " + fileName);
        }

    }

    private static void processInput(String option, BufferedReader reader) throws IOException {
            CountStrategy strategy = CountStrategyFactory.getStrategy(option);
            long count = strategy.count(reader);
            logResult(count, "");
    }

    private static void printUsageAndExit() {
        logger.log(Level.SEVERE, () -> "Usage: ccwc [-c, -l, -w, -m] <file_name>");
        System.exit(1);
    }

    private static void processFile(String option, String fileName) throws IOException {
        if (option.isEmpty()) {
            CountStrategy[] strategies = new CountStrategy[]{
                    CountStrategyFactory.getStrategy("-c"),
                    CountStrategyFactory.getStrategy("-l"),
                    CountStrategyFactory.getStrategy("-w"),
            };
            long[] counts = Stream.of(strategies).mapToLong(strategy -> {
                try {
                    return strategy.count(getPath(fileName));
                } catch (IOException e) {
                    logger.log(Level.SEVERE, e, () -> "Error reading input");
                    return 0;
                }
            }).toArray();
            logResult(counts[0], counts[1], counts[2], fileName);
        } else {
            CountStrategy strategy = CountStrategyFactory.getStrategy(option);
            long count = strategy.count(getPath(fileName));
            logResult(count, fileName);
        }
    }

    private static void logResult(long count, String filename) {
        System.out.println(String.format("%d %s", count, filename));
    }

    private static void logResult(long byteCount, long lineCount, long wordCount, String filename) {
        System.out.println(String.format("%d %d %d %s", byteCount, lineCount, wordCount, filename));
    }


    // Ottiene il percorso di un file
    static Path getPath(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        if (!Files.exists(path)) {
            throw new IOException("File not found: " + fileName);
        }
        if (!Files.isReadable(path)) {
            throw new IOException("File is not readable: " + fileName);
        }
        return path;
    }
}