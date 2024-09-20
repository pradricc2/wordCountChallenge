import countPattern.CountStrategy;
import countPattern.CountStrategyFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.Files.setPosixFilePermissions;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class ccwcTest {

    @TempDir
    Path tempDir;

    private Path testFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("testFile.txt");
        Files.write(testFile, "Hello World\nThis is a test file.\n".getBytes());
    }

    @Test
    void testCountBytes() throws IOException {
        CountStrategy strategy = CountStrategyFactory.getStrategy("-c");
        long byteCount = strategy.count(testFile);
        assertEquals(33, byteCount);
    }

    @Test
    void testCountLines() throws IOException {
        CountStrategy strategy = CountStrategyFactory.getStrategy("-l");
        long lineCount = strategy.count(testFile);
        assertEquals(2, lineCount);
    }

    @Test
    void testCountWords() throws IOException {
        CountStrategy strategy = CountStrategyFactory.getStrategy("-w");
        long wordCount = strategy.count(testFile);
        assertEquals(7, wordCount);
    }

    @Test
    void testCountCharacters() throws IOException {
        CountStrategy strategy = CountStrategyFactory.getStrategy("-m");
        long charCount = strategy.count(testFile);
        assertEquals(31, charCount);
    }

    @Test
    void testInvalidFilePath() {
        CountStrategy strategy = CountStrategyFactory.getStrategy("-c");
        assertThrows(IOException.class, () -> strategy.count(Path.of("invalidPath.txt")));
    }

    @Test
    void testUnreadableFile() throws IOException {
        Path unreadableFile = Path.of("unreadableFile.txt");
        Files.writeString(unreadableFile, "Content");
        File file = unreadableFile.toFile();
        file.setReadable(false);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.exists(unreadableFile)).thenReturn(true);
            mockedFiles.when(() -> Files.isReadable(unreadableFile)).thenReturn(false);

            System.out.println("isReadable: " + Files.isReadable(unreadableFile));
            CountStrategy strategy = CountStrategyFactory.getStrategy("-c");
            assertDoesNotThrow(() -> strategy.count(unreadableFile));
        }
    }

    @Test
    void testCountBytesFromInput() throws IOException {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        CountStrategy strategy = CountStrategyFactory.getStrategy("-c");
        long byteCount = strategy.count(reader);
        assertEquals(31, byteCount);
    }

    @Test
    void testCountWordsFromInput() throws IOException {
        String input = "This is a test\nAnother line of text";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        CountStrategy strategy = CountStrategyFactory.getStrategy("-w");
        long wordCount = strategy.count(reader);
        assertEquals(8, wordCount);
    }

    @Test
    void testCountCharactersFromInput() throws IOException {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        CountStrategy strategy = CountStrategyFactory.getStrategy("-m");
        long charCount = strategy.count(reader);
        assertEquals(31, charCount);
    }

    @Test
    void testCountLinesFromInput() throws IOException {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        CountStrategy strategy = CountStrategyFactory.getStrategy("-l");
        long lineCount = strategy.count(reader);
        assertEquals(2, lineCount);
    }

    @Test
    void testProcessInput() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        Method method = ccwc.class.getDeclaredMethod("processInput", String.class, BufferedReader.class);
        method.setAccessible(true);

        method.invoke(null, "-c", reader);
        method.invoke(null, "-l", reader);
        method.invoke(null, "-w", reader);
        method.invoke(null, "-m", reader);
    }

    @Test
    void testProcessFile() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ccwc.class.getDeclaredMethod("processFile", String.class, String.class);
        method.setAccessible(true);

        method.invoke(null, "-c", testFile.toString());
        method.invoke(null, "-l", testFile.toString());
        method.invoke(null, "-w", testFile.toString());
        method.invoke(null, "-m", testFile.toString());
        method.invoke(null, "", testFile.toString());
    }

    @Test
    void testGetPath() {
        assertThrows(IOException.class, () -> ccwc.getPath("nonExistentFile.txt"));
        assertThrows(IOException.class, () -> {
            Path unreadableFile = Path.of("unreadableFile.txt");
            Files.writeString(unreadableFile, "Content");
            try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
                mockedFiles.when(() -> Files.exists(unreadableFile)).thenReturn(true);
                mockedFiles.when(() -> Files.isReadable(unreadableFile)).thenReturn(false);
                ccwc.getPath(unreadableFile.toString());
            }
        });
    }
}