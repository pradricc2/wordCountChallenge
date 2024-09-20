package countPattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class ByteCountStrategy implements CountStrategy {
    @Override
    public long count(BufferedReader reader) throws IOException {
        long byteCount = 0;
        String line;
        while ((line = reader.readLine()) != null) {
            byteCount += line.getBytes(StandardCharsets.UTF_8).length;
        }
        return byteCount;
    }

    @Override
    public long count(Path path) throws IOException {
        return Files.size(path);
    }
}


