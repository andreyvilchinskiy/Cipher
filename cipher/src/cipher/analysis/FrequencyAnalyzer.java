package cipher.analysis;

import cipher.caesar.Alphabet;
import java.util.*;

// Математика для статистического взлома
public final class FrequencyAnalyzer {
    private FrequencyAnalyzer() {}
    // подсчет частоты каждой буквы в тексте
    public static Map<Character, Double> computeFrequency(String text) {
        Map<Character, Integer> counts = new HashMap<>();
        int total = 0;
        for (char ch : text.toCharArray()) {
            if (Alphabet.isRu(ch) || Alphabet.isEn(ch)) {
                counts.merge(Character.toLowerCase(ch), 1, Integer::sum);
                total++;
            }
        }
        Map<Character, Double> freq = new HashMap<>();
        if (total == 0) return freq;
        for (Map.Entry<Character, Integer> e : counts.entrySet())
            freq.put(e.getKey(), (double) e.getValue() / total);
        return freq;
    }
    // сравнение частоты текста с эталонными (из defaultFrequency)
    public static double scoreText(String text, Map<Character, Double> ref) {
        Map<Character, Double> freq = computeFrequency(text);
        double s = 0;
        for (Map.Entry<Character, Double> e : ref.entrySet()) {
            double p = freq.getOrDefault(e.getKey(), 0.0);
            double q = e.getValue();
            s -= (p - q) * (p - q);
        }
        return s;
    }

    public static Map<Character, Double> defaultFrequency() {
        Map<Character, Double> m = new HashMap<>();
        String ru = "оеаитнсрвлкмдпуяыьгзбчйхжшюцщэфъё";
        double[] ruFreq = {0.1097,0.0845,0.0801,0.0670,0.0626,0.0626,0.0547,0.0474,0.0463,0.0442,
                0.0415,0.0352,0.0320,0.0298,0.0264,0.0251,0.0230,0.0201,0.0188,0.0174,
                0.0165,0.0164,0.0144,0.0121,0.0097,0.0095,0.0072,0.0048,0.0039,0.0036,
                0.0028,0.0009,0.0004};
        for (int i = 0; i < ru.length(); i++) m.put(ru.charAt(i), ruFreq[i]);

        String en = "etaoinshrdlucmfwypvbgkjqxz";
        double[] enFreq = {0.1270,0.0906,0.0817,0.0751,0.0697,0.0675,0.0633,0.0609,0.0599,0.0425,
                0.0403,0.0278,0.0276,0.0241,0.0236,0.0223,0.0202,0.0197,0.0193,0.0149,
                0.0098,0.0077,0.0015,0.0015,0.0010,0.0007};
        for (int i = 0; i < en.length(); i++) m.put(en.charAt(i), enFreq[i]);
        return m;
    }
}