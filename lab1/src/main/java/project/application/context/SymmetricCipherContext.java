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
        return cipherMode.applyMode(paddingPlaintext, symmetricCipher);
    }

    public byte[] decrypt(byte[] ciphertext) {
        return paddingMode.removePadding(cipherMode.cancelMode(ciphertext, symmetricCipher));
    }
}
