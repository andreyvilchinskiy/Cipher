package cipher;

import cipher.analysis.FrequencyAnalyzer;
import cipher.caesar.BruteForceCracker;
import cipher.caesar.CaesarCipher;
import cipher.caesar.StatisticalCracker;
import cipher.io.FileProcessor;
import cipher.io.InputValidator;
import cipher.vigenere.VigenereCipher;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;

public final class Main {

    private static final Cipher CAESAR = new CaesarCipher();
    private static final Cipher VIGENERE = new VigenereCipher();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in, StandardCharsets.UTF_8);
        Cipher current = CAESAR;

        while (true) {
            System.out.println("\n==============================");
            System.out.println(" Текущий шифр: " + current.name());
            System.out.println("==============================");
            System.out.println("1. Выбрать шифр Цезаря");
            System.out.println("2. Выбрать шифр Виженера");
            System.out.println("3. Зашифровать файл");
            System.out.println("4. Расшифровать файл (ключ)");
            if (current instanceof CaesarCipher) {
                System.out.println("5. Взлом brute force (Цезарь)");
                System.out.println("6. Взлом стат. анализом (Цезарь)");
            }
            System.out.println("0. Выход");
            System.out.print("Выбор: ");

            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> { current = CAESAR; System.out.println("Выбран шифр Цезаря."); }
                    case "2" -> { current = VIGENERE; System.out.println("Выбран шифр Виженера."); }
                    case "3" -> doEncrypt(sc, current);
                    case "4" -> doDecrypt(sc, current);
                    case "5" -> {
                        if (current instanceof CaesarCipher) doBruteForce(sc);
                        else System.out.println("Brute force доступен только для Цезаря.");
                    }
                    case "6" -> {
                        if (current instanceof CaesarCipher) doStatistical(sc);
                        else System.out.println("Стат. анализ доступен только для Цезаря.");
                    }
                    case "0" -> { System.out.println("Выход..."); return; }
                    default -> System.out.println("Неверный пункт меню.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    // ============ ОБЩИЕ РЕЖИМЫ ============
    private static void doEncrypt(Scanner sc, Cipher cipher) throws IOException {
        Path in = InputValidator.readExistingPath(sc, "Входной файл: ");
        Path out = InputValidator.readPath(sc, "Выходной файл: ");
        String key = readKeyFor(sc, cipher);

        String text = FileProcessor.loadText(in);

        // Локальный try-catch для обработки throws Exception из cipher.encrypt
        try {
            String result = cipher.encrypt(text, key);
            FileProcessor.saveText(out, result);
            System.out.println("Готово. Зашифровано в " + out);
        } catch (Exception e) {
            System.out.println("Ошибка при шифровании: " + e.getMessage());
        }
    }

    private static void doDecrypt(Scanner sc, Cipher cipher) throws IOException {
        Path in = InputValidator.readExistingPath(sc, "Зашифрованный файл: ");
        Path out = InputValidator.readPath(sc, "Выходной файл: ");
        String key = readKeyFor(sc, cipher);

        String text = FileProcessor.loadText(in);

        // Локальный try-catch для обработки throws Exception из cipher.decrypt
        try {
            String result = cipher.decrypt(text, key);
            FileProcessor.saveText(out, result);
            System.out.println("Готово. Расшифровано в " + out);
        } catch (Exception e) {
            System.out.println("Ошибка при расшифровке: " + e.getMessage());
        }
    }

    // ============ ТОЛЬКО ЦЕЗАРЬ ============
    private static void doBruteForce(Scanner sc) throws IOException {
        Path in = InputValidator.readExistingPath(sc, "Зашифрованный файл: ");
        Path out = InputValidator.readPath(sc, "Выходной файл: ");
        Map<Character, Double> ref = maybeLoadReference(sc);

        String cipherText = FileProcessor.loadText(in);
        System.out.println("Перебор ключей (33×26)...");
        BruteForceCracker.Result r = BruteForceCracker.crack(cipherText, ref);
        System.out.printf("Лучший ключ: RU=%d, EN=%d (оценка %.4f)%n", r.keyRu(), r.keyEn(), r.score());

        System.out.println("--- Фрагмент ---");
        System.out.println(truncate(r.text()));

        FileProcessor.saveText(out, r.text());
        System.out.println("Готово. Результат записан в " + out);
    }

    private static void doStatistical(Scanner sc) throws IOException {
        Path in = InputValidator.readExistingPath(sc, "Зашифрованный файл: ");
        Path out = InputValidator.readPath(sc, "Выходной файл: ");
        Map<Character, Double> ref = maybeLoadReference(sc);

        String cipherText = FileProcessor.loadText(in);
        StatisticalCracker.Result r = StatisticalCracker.crack(cipherText, ref);
        System.out.printf("Определён сдвиг: RU=%d, EN=%d%n", r.keyRu(), r.keyEn());

        System.out.println("--- Фрагмент ---");
        System.out.println(truncate(r.text()));

        FileProcessor.saveText(out, r.text());
        System.out.println("Готово. Результат записан в " + out);
    }

    // ============ ВСПОМОГАТЕЛЬНОЕ ============
    private static String readKeyFor(Scanner sc, Cipher cipher) {
        if (cipher instanceof CaesarCipher) {
            while (true) {
                System.out.print("Ключ Цезаря (целое ≥ 0): ");
                String s = sc.nextLine().trim();
                try {
                    int k = Integer.parseInt(s);
                    if (k < 0) { System.out.println("Должно быть ≥ 0."); continue; }
                    return String.valueOf(k);
                } catch (NumberFormatException e) {
                    System.out.println("Введите целое число.");
                }
            }
        }
        // Виженер
        while (true) {
            System.out.print("Ключ Виженера (латинские буквы A-Z): ");
            String s = sc.nextLine().trim();
            if (s.matches("[A-Za-z]+")) return s;
            System.out.println("Ключ должен состоять только из латинских букв.");
        }
    }

    private static Map<Character, Double> maybeLoadReference(Scanner sc) throws IOException {
        System.out.print("Есть файл-образец языка? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            Path sample = InputValidator.readExistingPath(sc, "Файл-образец: ");
            return FrequencyAnalyzer.computeFrequency(FileProcessor.loadText(sample));
        }
        return FrequencyAnalyzer.defaultFrequency();
    }

    private static String truncate(String s) {
        return s.length() <= 300 ? s : s.substring(0, 300) + "...";
    }

    private Main() {}
}