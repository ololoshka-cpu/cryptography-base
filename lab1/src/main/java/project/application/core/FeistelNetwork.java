package project.application.core;

import project.algorythm.des.DESKeySchedule;
import project.algorythm.des.DESRoundFunction;
import project.interfaces.RoundFunction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FeistelNetwork {

    static {
        System.loadLibrary("crypto_native");
    }

    private final RoundFunction roundFunction;

    public static native byte[] applyFeistelNetwork(byte[] plaintext, byte[][] keys, long roundFunction);

    public byte[] apply(byte[] plaintext, byte[][] keys) {
        return applyFeistelNetwork(plaintext, keys, roundFunction.getFunctionAddress());
    }

    public static void main(String[] args) {
        DESRoundFunction des = new DESRoundFunction();
        DESKeySchedule keySchedule = new DESKeySchedule();

        byte[] masterKey = new byte[]{
                0b00010011,
                0b00110100,
                0b01010111,
                0b01111001,
                (byte) 0b10011011,
                (byte) 0b10111100,
                (byte) 0b11011111,
                (byte) 0b11110001
        };

        byte[] plaintext = new byte[]{
                (byte) 0b10010011,
                0b01110100,
                0b01010111,
                0b01111001,
                (byte) 0b10011011,
                (byte) 0b10111100,
                (byte) 0b11011111,
                (byte) 0b11110001
        };

        byte[][] keys = keySchedule.generateRoundKeys(masterKey);
        byte[][] decryptKeys = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
            decryptKeys[i] = keys[keys.length - 1 - i];
        }

        FeistelNetwork feistelNetwork = new FeistelNetwork(des);

        for (byte e : plaintext) {
            System.out.print(e + " ");
        }
        System.out.println();

        byte[] cyphertext = feistelNetwork.apply(plaintext, keys);

        for (byte e : cyphertext) {
            System.out.print(e + " ");
        }
        System.out.println();
        byte[] message = feistelNetwork.apply(cyphertext, decryptKeys);
        for (byte e : message) {
            System.out.print(e + " ");
        }
    }


}
