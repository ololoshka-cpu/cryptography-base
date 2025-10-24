package project.algorythm.des;

import project.interfaces.RoundFunction;

public class DESRoundFunction implements RoundFunction {

    static {
        System.loadLibrary("crypto_native");
    }

    public static native byte[] applyDesRoundFunction(byte[] inputBlock, byte[] roundKey);

    public static native long getFunctionAddressInLibrary();

    @Override
    public byte[] apply(byte[] inputBlock, byte[] roundKey) {
        return applyDesRoundFunction(inputBlock, roundKey);
    }

    @Override
    public long getFunctionAddress() {
        return getFunctionAddressInLibrary();
    }
}
