package project.algorythm.des;

import org.springframework.stereotype.Component;
import project.application.core.FeistelNetwork;
import project.interfaces.KeySchedule;
import project.interfaces.RoundFunction;
import project.interfaces.SymmetricCipher;

import static project.application.core.NativeFunctions.bitPermutation;

@Component
public class DESCipher extends FeistelNetwork implements SymmetricCipher {

    private static final long[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17,  9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final long[] IP_INV = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41,  9, 49, 17, 57, 25
    };

    private byte[] masterKey;
    private byte[][] encryptKeys;
    private byte[][] decryptKeys;


    public DESCipher(KeySchedule keySchedule, RoundFunction roundFunction, byte[] masterKey) {
        super(roundFunction);
        this.masterKey = masterKey;
        this.encryptKeys = keySchedule.generateRoundKeys(masterKey);
        initDecryptKeys(encryptKeys);
    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        byte[] permutedBlock = bitPermutation(plaintext, 64, IP, 64,  false, true, 64);
        byte[] encryptedBlock = apply(permutedBlock, encryptKeys);
        return bitPermutation(encryptedBlock, 64, IP_INV, 64, false, true, 64);
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        byte[] permutedBlock = bitPermutation(ciphertext, 64, IP, 64, false, true, 64);
        byte[] decryptedBlock = apply(permutedBlock, decryptKeys);
        return bitPermutation(decryptedBlock, 64, IP_INV, 64, false, true, 64);
    }   

    @Override
    public int getBlockSize() {
        return 8;
    }

    private void initDecryptKeys(byte[][] encryptKeys) {
        decryptKeys = new byte[encryptKeys.length][];
        for (int i = 0; i < encryptKeys.length; i++) {
            decryptKeys[i] = encryptKeys[encryptKeys.length - 1 - i];
        }
    }
}
