package countPattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class LineCountStrategy implements CountStrategy {
    @Override
    public long count(BufferedReader reader) throws IOException {
        long lineCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            lineCount++;
        }
        return lineCount;
    }

    @Override
    public long count(Path path) throws IOException {
         try (Stream<String> lines = Files.lines(path)) {
             return lines.count();
         }
    }
}
