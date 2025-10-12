package project.interfaces;

public interface SymmetricCypher {
    /**
     * Установить ключ и сгенерировать все раундовые ключи.
     * @param masterKey исходный ключ
     */
    void setKey(byte[] masterKey);

    /**
     * Шифрует один блок.
     * @param plaintext открытый текст (входной блок)
     * @return зашифрованный блок
     */
    byte[] encrypt(byte[] plaintext);

    /**
     * Расшифровывает один блок.
     * @param ciphertext зашифрованный блок
     * @return расшифрованный блок
     */
    byte[] decrypt(byte[] ciphertext);
}
