package countPattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;

public interface CountStrategy {
    long count(BufferedReader reader) throws IOException;
    long count(Path path) throws IOException;
}
