package cipher.caesar;

public final class Alphabet {
    public static final String RU_UPPER = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    public static final String RU_LOWER = RU_UPPER.toLowerCase();
    public static final String EN_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String EN_LOWER = EN_UPPER.toLowerCase();
    public static final int RU_SIZE = RU_UPPER.length();
    public static final int EN_SIZE = EN_UPPER.length();

    private Alphabet() {}

    public static boolean isRu(char ch) { return RU_UPPER.indexOf(Character.toUpperCase(ch)) >= 0; }
    public static boolean isEn(char ch) { return EN_UPPER.indexOf(Character.toUpperCase(ch)) >= 0; }

    public static char shift(char ch, int key) {
        if (isRu(ch)) return shiftIn(ch, key, RU_UPPER, RU_LOWER);
        if (isEn(ch)) return shiftIn(ch, key, EN_UPPER, EN_LOWER);
        return ch;
    }
    public static char shiftRu(char ch, int key) { return isRu(ch) ? shiftIn(ch, key, RU_UPPER, RU_LOWER) : ch; }
    public static char shiftEn(char ch, int key) { return isEn(ch) ? shiftIn(ch, key, EN_UPPER, EN_LOWER) : ch; }

    private static char shiftIn(char ch, int key, String upper, String lower) {
        int size = upper.length();
        int k = ((key % size) + size) % size;
        int idx = upper.indexOf(Character.toUpperCase(ch));
        if (idx < 0) return ch;
        char s = upper.charAt((idx + k) % size);
        return Character.isLowerCase(ch) ? Character.toLowerCase(s) : s;
    }
}