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

    public static void main(String[] args) {
        DESRoundFunction desRoundFunction = new DESRoundFunction();
        System.out.println(desRoundFunction.getFunctionAddress());
//        byte[] round_key = {
//                0b01010110,
//                0b01111011,
//                (byte) 0b11011010,
//                (byte) 0b11010111,
//                (byte) 0b11110111,
//                (byte) 0b11111111
//        };
//        byte[] r_block = {
//                0b01010110,
//                0b01111011,
//                (byte) 0b11011010,
//                (byte) 0b11010111
//        };
//
//        byte[] result = desRoundFunction.apply(r_block, round_key);
//
//        for (int i = 0; i < 4; i++) {
//            System.out.println(result[i]);
//        }
    }
}
