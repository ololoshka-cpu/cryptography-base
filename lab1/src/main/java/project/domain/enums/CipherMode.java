package project.domain.enums;

import project.interfaces.SymmetricCipher;

public enum CipherMode {
    ECB {
        @Override
        public byte[] applyMode(byte[] paddingPlaintext, SymmetricCipher symmetricCipher) {
            return applyEcbMode(paddingPlaintext, symmetricCipher);
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            return cancelEcbMode(ciphertext, symmetricCipher);
        }
    },
    CBC {
        @Override
        public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("CBC mode is not implemented");
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("CBC mode is not implemented");
        }
    },
    PCBC {
        @Override
        public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("PCBC mode is not implemented");
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("PCBC mode is not implemented");
        }
    },
    CFB {
        @Override
        public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("CFB mode is not implemented");
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("CFB mode is not implemented");
        }
    },
    OFB {
        @Override
        public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("OFB mode is not implemented");
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("OFB mode is not implemented");
        }
    },
    CTR {
        @Override
        public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("CTR mode is not implemented");
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("CTR mode is not implemented");
        }
    },
    RANDOM_DELTA {
        @Override
        public byte[] applyMode(byte[] plaintext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("RANDOM_DELTA mode is not implemented");
        }

        @Override
        public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
            throw new UnsupportedOperationException("RANDOM_DELTA mode is not implemented");
        }
    };

    public byte[] applyMode(byte[] paddingPlaintext, SymmetricCipher symmetricCipher) {
        return applyEcbMode(paddingPlaintext, symmetricCipher);
    }

    public byte[] cancelMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
        return cancelEcbMode(ciphertext, symmetricCipher);
    }

    private static byte[] applyEcbMode(byte[] paddingPlaintext, SymmetricCipher symmetricCipher) {
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

    private static byte[] cancelEcbMode(byte[] ciphertext, SymmetricCipher symmetricCipher) {
        int blockSize = symmetricCipher.getBlockSize();
        byte[] decryptedCiphertext = new byte[ciphertext.length];
        for (int i = 0; i < decryptedCiphertext.length / blockSize; i++) {
            byte[] word = new byte[blockSize];
            System.arraycopy(ciphertext, i * blockSize, word, 0, blockSize);
            byte[] decryptedWord = symmetricCipher.decrypt(word);
            System.arraycopy(decryptedWord, 0, decryptedCiphertext, i * blockSize, blockSize);
        }
        return decryptedCiphertext;
    }
}
