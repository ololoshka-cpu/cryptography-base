package project.algorythm.des;

import project.interfaces.KeySchedule;

public class DESKeySchedule implements KeySchedule {

    static {
        System.loadLibrary("crypto_native");
    }

    private static native byte[][] generateRoundKeysNative(byte[] masterKey);

    @Override
    public byte[][] generateRoundKeys(byte[] masterKey) {
        if (masterKey == null || masterKey.length != 8)
            throw new IllegalArgumentException("DES key must be 8 bytes long");
        return generateRoundKeysNative(masterKey);
    }
}
