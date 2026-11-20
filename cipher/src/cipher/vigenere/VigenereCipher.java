package cipher.vigenere;

import cipher.Cipher;

//Шифр Виженера, работает с EU языком, ключ слово из латинских букв.

public final class VigenereCipher implements Cipher {

    @Override public String name() { return "Шифр Виженера"; }

    @Override
    public String encrypt(String plaintext, String key) {
        return process(plaintext, key, +1);
    }

    @Override
    public String decrypt(String ciphertext, String key) {
        return process(ciphertext, key, -1);
    }

    private String process(String text, String key, int direction) {
        int[] shifts = keyShifts(key);
        if (shifts.length == 0) throw new IllegalArgumentException("Ключ Виженера не должен быть пустым");

        StringBuilder sb = new StringBuilder(text.length());
        int keyPos = 0;
        for (char ch : text.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                int shift = shifts[keyPos % shifts.length] * direction;
                char c = (char) ('A' + Math.floorMod((ch - 'A') + shift, 26));
                sb.append(c);
                keyPos++;
            } else if (ch >= 'a' && ch <= 'z') {
                int shift = shifts[keyPos % shifts.length] * direction;
                char c = (char) ('a' + Math.floorMod((ch - 'a') + shift, 26));
                sb.append(c);
                keyPos++;
            } else {
                sb.append(ch); // пробелы/пунктуация сохраняются, ключ не сдвигается
            }
        }
        return sb.toString();
    }

    private int[] keyShifts(String key) {
        if (key == null) return new int[0];
        String k = key.trim().toUpperCase();
        int[] res = new int[k.length()];
        int n = 0;
        for (char ch : k.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') res[n++] = ch - 'A';
        }
        return java.util.Arrays.copyOf(res, n);
    }
}