package project.interfaces;

public interface SymmetricCipher {

    byte[] encrypt(byte[] plaintext);

    byte[] decrypt(byte[] ciphertext);

    int getBlockSize();

}
