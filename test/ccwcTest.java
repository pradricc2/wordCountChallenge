import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import java.io.IOException;
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
        assertThrows(IOException.class, () -> ccwc.countBytes("invalidPath.txt"));
    }

    @Test
    void testUnreadableFile() throws IOException {
        // Crea un file temporaneo
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

}
