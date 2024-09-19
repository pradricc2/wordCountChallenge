import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
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

        // Se non viene fornito nessun file, leggi dallo standard input
        boolean readFromStdin = (args.length == 0 || (args.length == 1 && args[0].startsWith("-")));
        String option = (args.length == 2) ? args[0] : (args.length == 1 && args[0].startsWith("-") ? args[0] : "");
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
        switch (option) {
            case "-c":
                logResult(countBytesFromInput(reader), "");
                break;
            case "-l":
                logResult(countLinesFromInput(reader), "");
                break;
            case "-w":
                logResult(countWordsFromInput(reader), "");
                break;
            case "-m":
                logResult(countCharactersFromInput(reader), "");
                break;
            case "":
                logResult(countBytesFromInput(reader), countLinesFromInput(reader),countWordsFromInput(reader),"");
                break;
            default:
                printUsageAndExit();
        }
    }

    // Conta il numero di byte dallo standard input
    private static long countBytesFromInput(BufferedReader reader) throws IOException {
        long byteCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            byteCount += line.getBytes(StandardCharsets.UTF_8).length;
        }
        return byteCount;
    }

    // Conta il numero di parole dallo standard input
    private static long countWordsFromInput(BufferedReader reader) throws IOException {
        long wordCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            wordCount += Stream.of(line.trim().split("\\s+")).filter(word -> !word.isEmpty()).count();
        }
        return wordCount;
    }

    // Conta il numero di caratteri dallo standard input
    private static long countCharactersFromInput(BufferedReader reader) throws IOException {
        long charCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            charCount += line.length();
        }
        return charCount;
    }

    // Conta il numero di righe dallo standard input
    private static long countLinesFromInput(BufferedReader reader) throws IOException {
        long lineCount = 0;
        while (reader.readLine() != null) {
            lineCount++;
        }
        return lineCount;
    }

    private static void printUsageAndExit() {
        logger.log(Level.SEVERE, () -> "Usage: ccwc [-c, -l, -w, -m] <file_name>");
        System.exit(1);
    }

    private static void processFile(String option, String fileName) throws IOException {
        switch (option) {
            case "-c":
                logResult(countBytes(fileName), fileName);
                break;
            case "-l":
                logResult(countLines(fileName), fileName);
                break;
            case "-w":
                logResult(countWords(fileName), fileName);
                break;
            case "-m":
                logResult(countCharacters(fileName), fileName);
                break;
            case "":
                logResult(countLines(fileName),countWords(fileName), countBytes(fileName),   fileName);
                break;
            default:
                printUsageAndExit();
        }
    }

    private static void logResult(long count, String filename) {
        System.out.println(String.format("%d %s", count, filename));
    }

    private static void logResult(long byteCount, long lineCount, long wordCount, String filename) {
        System.out.println(String.format("%d %d %d %s", byteCount, lineCount, wordCount, filename));
    }


    static long countLines(String fileName) throws IOException {
        Path path = getPath(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.count();
        }
    }

    static long countBytes(String fileName) throws IOException {
        return Files.size(getPath(fileName));
    }

    static long countWords(String fileName) throws IOException {
        Path path = getPath(fileName);
        try (Stream<String> lines = Files.lines(path)) {
            return lines.flatMap(line -> Stream.of(line.trim().split("\\s+")))
                        .filter(word -> !word.isEmpty())
                        .count();
        }
    }

    static long countCharacters(String fileName) throws IOException {
        Path path = getPath(fileName);
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.mapToLong(String::length).sum();
        }
    }

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