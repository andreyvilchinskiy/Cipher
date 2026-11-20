package cipher.caesar;

import cipher.analysis.FrequencyAnalyzer;
import java.util.Map;
//Взлом Цезаря полным перебором сдвигов (33 для RU и 26 для EN)
public final class BruteForceCracker {
    public record Result(int keyRu, int keyEn, double score, String text) {}
    private BruteForceCracker() {}

    public static Result crack(String cipherText, Map<Character, Double> reference) {
        if (reference == null || reference.isEmpty()) reference = FrequencyAnalyzer.defaultFrequency();
        CaesarCipher c = new CaesarCipher();
        int bestRu = 0, bestEn = 0;
        double best = Double.NEGATIVE_INFINITY;
        for (int kRu = 0; kRu < Alphabet.RU_SIZE; kRu++) {
            for (int kEn = 0; kEn < Alphabet.EN_SIZE; kEn++) {
                String cand = c.decryptWithKeys(cipherText, kRu, kEn);
                double s = FrequencyAnalyzer.scoreText(cand, reference);
                if (s > best) { best = s; bestRu = kRu; bestEn = kEn; }
            }
        }
        return new Result(bestRu, bestEn, best, c.decryptWithKeys(cipherText, bestRu, bestEn));
    }
}