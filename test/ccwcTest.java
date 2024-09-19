import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        long byteCount = ccwc.countBytes(testFile.toString());
        assertEquals(33, byteCount);
    }

    @Test
    void testCountLines() throws IOException {
        long lineCount = ccwc.countLines(testFile.toString());
        assertEquals(2, lineCount);
    }

    @Test
    void testCountWords() throws IOException {
        long wordCount = ccwc.countWords(testFile.toString());
        assertEquals(7, wordCount);
    }

    @Test
    void testCountCharacters() throws IOException {
        long charCount = ccwc.countCharacters(testFile.toString());
        assertEquals(31, charCount);
    }

    @Test
    void testInvalidFilePath() {
        // Verifica che venga lanciata un'eccezione se il file non esiste
        assertThrows(IOException.class, () -> ccwc.countBytes("invalidPath.txt"));
    }

    @Test
    void testUnreadableFile() throws IOException {
        Path unreadableFile = Path.of("unreadableFile.txt");
        Files.writeString(unreadableFile, "Content");

        // Usa Mockito per simulare Files.isReadable() che restituisce false
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            // Simula che il file esista e che non sia leggibile
            mockedFiles.when(() -> Files.exists(unreadableFile)).thenReturn(true);
            mockedFiles.when(() -> Files.isReadable(unreadableFile)).thenReturn(false);

            // Esegui il test e verifica che venga lanciata un'IOException
            assertThrows(IOException.class, () -> ccwc.countBytes(unreadableFile.toString()));
        }
    }

    @Test
    void testCountBytesFromInput() throws Exception {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        Method method = ccwc.class.getDeclaredMethod("countBytesFromInput", BufferedReader.class);
        method.setAccessible(true);
        long byteCount = (long) method.invoke(null, reader);
        assertEquals(31, byteCount);
    }

    @Test
    void testCountWordsFromInput() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        BufferedReader reader = new BufferedReader(new StringReader("This is a test\nAnother line of text"));

        // Usa la reflection java standard per ottenere il metodo privato statico
        Method method = ccwc.class.getDeclaredMethod("countWordsFromInput", BufferedReader.class);
        method.setAccessible(true);

        // Invoca il metodo privato statico con reflection
        long wordCount = (long) method.invoke(null, reader); // Poiché è statico, il primo parametro è null

        assertEquals(8, wordCount, "Il numero di parole conteggiato è corretto");
    }

    @Test
    void testCountCharactersFromInput() throws Exception {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        Method method = ccwc.class.getDeclaredMethod("countCharactersFromInput", BufferedReader.class);
        method.setAccessible(true); // Rendi accessibile il metodo privato
        long charCount = (long) method.invoke(null, reader);
        assertEquals(31, charCount);
    }

    @Test
    void testCountLinesFromInput() throws Exception {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        Method method = ccwc.class.getDeclaredMethod("countLinesFromInput", BufferedReader.class);
        method.setAccessible(true); // Rendi accessibile il metodo privato

        long lineCount = (long) method.invoke(null, reader);
        assertEquals(2, lineCount);
    }

    @Test
    void testProcessInput() throws Exception {
        String input = "Hello World\nThis is a test file.\n";
        BufferedReader reader = new BufferedReader(new StringReader(input));

        Method method = ccwc.class.getDeclaredMethod("processInput", String.class, BufferedReader.class);
        method.setAccessible(true);

        method.invoke(null, "-c", reader);
        method.invoke(null, "-l", reader);
        method.invoke(null, "-w", reader);
        method.invoke(null, "-m", reader);
        method.invoke(null, "", reader);
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
