package cipher.io;

import java.nio.file.*;
import java.util.Scanner;

// Валидация пользовательского ввода с консоли
public final class InputValidator {
    private InputValidator() {}

    public static Path readExistingPath(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) { System.out.println("Путь не может быть пустым."); continue; }
            Path p = Path.of(s);
            if (!Files.exists(p) || !Files.isRegularFile(p)) {
                System.out.println("Файл не существует: " + p); continue;
            }
            return p;
        }
    }

    public static Path readPath(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) { System.out.println("Путь не может быть пустым."); continue; }
            Path p = Path.of(s);
            Path parent = p.toAbsolutePath().getParent();
            if (parent != null && !Files.isDirectory(parent)) {
                System.out.println("Каталог не существует: " + parent); continue;
            }
            return p;
        }
    }
}