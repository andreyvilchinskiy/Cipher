package cipher;

/** Общий контракт для всех шифров. */
public interface Cipher {
    String name();
    String encrypt(String text, String key) throws Exception;
    String decrypt(String text, String key) throws Exception;
}