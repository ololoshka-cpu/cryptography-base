package project.application.core;

import lombok.AllArgsConstructor;
import project.interfaces.RoundFunction;

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
}
