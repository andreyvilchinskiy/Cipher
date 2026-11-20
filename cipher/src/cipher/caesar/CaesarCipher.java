package cipher.caesar;

import cipher.Cipher;

// Шифр Цезаря. Ключ — целое число (сдвиг)
public final class CaesarCipher implements Cipher {

    @Override public String name() { return "Шифр Цезаря"; }

    @Override
    public String encrypt(String text, String key) {
        int k = parseKey(key);
        StringBuilder sb = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) sb.append(Alphabet.shift(ch, k));
        return sb.toString();
    }

    @Override
    public String decrypt(String text, String key) {
        int k = parseKey(key);
        StringBuilder sb = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) sb.append(Alphabet.shift(ch, -k));
        return sb.toString();
    }

    public String decryptWithKeys(String text, int keyRu, int keyEn) {
        StringBuilder sb = new StringBuilder(text.length());
        for (char ch : text.toCharArray()) {
            if (Alphabet.isRu(ch)) sb.append(Alphabet.shiftRu(ch, -keyRu));
            else if (Alphabet.isEn(ch)) sb.append(Alphabet.shiftEn(ch, -keyEn));
            else sb.append(ch);
        }
        return sb.toString();
    }

    public static int parseKey(String key) {
        try { return Math.max(0, Integer.parseInt(key.trim())); }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException("Ключ Цезаря должен быть целым числом ≥ 0");
        }
    }
}