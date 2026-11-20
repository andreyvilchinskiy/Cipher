package cipher.caesar;

import cipher.analysis.FrequencyAnalyzer;
import java.util.Map;
// Взлом Цезаря через частотный анализ (33 + 26 проходов)
public final class StatisticalCracker {
    public record Result(int keyRu, int keyEn, String text) {}
    private StatisticalCracker() {}

    public static Result crack(String cipherText, Map<Character, Double> reference) {
        if (reference == null || reference.isEmpty()) reference = FrequencyAnalyzer.defaultFrequency();
        int kRu = bestShift(cipherText, true, reference);
        int kEn = bestShift(cipherText, false, reference);
        return new Result(kRu, kEn, new CaesarCipher().decryptWithKeys(cipherText, kRu, kEn));
    }

    private static int bestShift(String text, boolean ru, Map<Character, Double> ref) {
        int size = ru ? Alphabet.RU_SIZE : Alphabet.EN_SIZE;
        int bestK = 0;
        double best = Double.NEGATIVE_INFINITY;
        for (int k = 0; k < size; k++) {
            StringBuilder sb = new StringBuilder(text.length());
            for (char ch : text.toCharArray()) {
                if (ru && Alphabet.isRu(ch)) sb.append(Alphabet.shiftRu(ch, -k));
                else if (!ru && Alphabet.isEn(ch)) sb.append(Alphabet.shiftEn(ch, -k));
                else sb.append(ch);
            }
            double s = FrequencyAnalyzer.scoreText(sb.toString(), ref);
            if (s > best) { best = s; bestK = k; }
        }
        return bestK;
    }
}