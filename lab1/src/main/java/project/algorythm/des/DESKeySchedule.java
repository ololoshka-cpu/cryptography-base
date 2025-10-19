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

//    public static void main(String[] args) {
//        byte[] masterKey = new byte[]{
//                0b00010011,
//                0b00110100,
//                0b01010111,
//                0b01111001,
//                (byte) 0b10011011,
//                (byte) 0b10111100,
//                (byte) 0b11011111,
//                (byte) 0b11110001
//        };
//        DESKeySchedule keySchedule = new DESKeySchedule();
//        byte[][] roundKeys = keySchedule.generateRoundKeys(masterKey);
//        for (int i = 0; i < 16; i++) {
//            for (int j = 0; j < 6; j++) {
//                System.out.print(roundKeys[i][j] + " ");
//            }
//            System.out.print("\n");
//        }
//    }
}
