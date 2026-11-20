package cipher.io;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

// Потоковая обработка больших файлов
public final class FileProcessor {
    private FileProcessor() {}

    public static String loadText(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    public static void saveText(Path path, String text) throws IOException {
        Files.writeString(path, text, StandardCharsets.UTF_8);
    }
}