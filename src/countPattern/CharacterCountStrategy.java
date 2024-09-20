package countPattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class CharacterCountStrategy implements CountStrategy {
    @Override
    public long count(BufferedReader reader) throws IOException {
        long charCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            charCount += line.length();
        }
        return charCount;
    }

    @Override
    public long count(Path path) throws IOException {
        try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
            return lines.mapToLong(String::length).sum();
        }
    }
}
