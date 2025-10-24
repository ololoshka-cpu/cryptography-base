package project.application.context;

import project.domain.enums.CipherMode;
import project.domain.enums.PaddingMode;
import project.interfaces.SymmetricCipher;

public class SymmetricCipherContext {



    private final CipherMode cipherMode;
    private final PaddingMode paddingMode;
    private final SymmetricCipher symmetricCipher;

    public SymmetricCipherContext(CipherMode cipherMode,
                                  PaddingMode paddingMode,
                                  SymmetricCipher symmetricCipher) {
        this.cipherMode = cipherMode;
        this.paddingMode = paddingMode;
        this.symmetricCipher = symmetricCipher;
    }

    public byte[] encrypt(byte[] plaintext) {

        int blockSize = symmetricCipher.getBlockSize();
        byte[] paddingPlaintext = paddingMode.setPadding(plaintext, blockSize);
        for (byte b : paddingPlaintext) {
            System.out.print(b + " ");
        }
        System.out.println();
        byte[] encryptedPlaintext = new byte[paddingPlaintext.length];

        for (int i = 0; i < paddingPlaintext.length / blockSize; i++) {
            byte[] word = new byte[blockSize];
            System.arraycopy(paddingPlaintext, i * blockSize, word, 0, blockSize);
            System.arraycopy(symmetricCipher.encrypt(word), 0, encryptedPlaintext, i * blockSize, blockSize);
        }

        return encryptedPlaintext;
    }

    public byte[] decrypt(byte[] ciphertext) {

        int blockSize = symmetricCipher.getBlockSize();
        byte[] decryptedCiphertext = new byte[ciphertext.length];

        for (int i = 0; i < decryptedCiphertext.length / blockSize; i++) {
            byte[] word = new byte[blockSize];
            System.arraycopy(ciphertext, i * blockSize, word, 0, blockSize);
            System.arraycopy(symmetricCipher.decrypt(word), 0, decryptedCiphertext, i * blockSize, blockSize);
        }

        return paddingMode.removePadding(decryptedCiphertext, blockSize);
    }
}
