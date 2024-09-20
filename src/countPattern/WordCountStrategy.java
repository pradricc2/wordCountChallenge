package countPattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WordCountStrategy implements CountStrategy {
    @Override
    public long count(BufferedReader reader) throws IOException {
        long wordCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            wordCount += Stream.of(line.trim().split("\\s+")).filter(word -> !word.isEmpty()).count();
        }
        return wordCount;
    }

    @Override
    public long count(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path)) {
            return lines.flatMap(line -> Stream.of(line.trim().split("\\s+")))
                    .filter(word -> !word.isEmpty())
                    .count();
        }
    }
}

