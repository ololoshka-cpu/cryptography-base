package project.domain.enums;

import project.interfaces.SymmetricCipher;

public enum CipherMode {
    ECB,
    CBC {
       @Override
       public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
           return null;
       }
    },
    PCBC,
    CFB,
    OFB,
    CTR,
    RANDOM_DELTA;

    public byte[] applyMode(byte[] paddingPlaintext, SymmetricCipher symmetricCipher) {
        int blockSize = symmetricCipher.getBlockSize();
        byte[] encryptedPlaintext = new byte[paddingPlaintext.length];
        for (int i = 0; i < paddingPlaintext.length / blockSize; i++) {
            byte[] word = new byte[blockSize];
            System.arraycopy(paddingPlaintext, i * blockSize, word, 0, blockSize);
            byte[] encryptedWord = symmetricCipher.encrypt(word);
            System.arraycopy(encryptedWord, 0, encryptedPlaintext, i * blockSize, blockSize);
        }
        return encryptedPlaintext;
    }

    public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
        int blockSize = symmetricCipher.getBlockSize();
        byte[] decryptedCiphertext = new byte[ciphertext.length];
        for (int i = 0; i < decryptedCiphertext.length / blockSize; i++) {
            byte[] word = new byte[blockSize];
            System.arraycopy(ciphertext, i * blockSize, word, 0, blockSize);
            byte [] decryptedWord = symmetricCipher.decrypt(word);
            System.arraycopy(decryptedWord, 0, decryptedCiphertext, i * blockSize, blockSize);
        }
        return decryptedCiphertext;
    }
}
