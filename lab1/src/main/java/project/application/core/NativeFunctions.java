package project.application.core;

public class NativeFunctions {

    static {
        System.loadLibrary("crypto_native");
    }

    public static native byte[] bitPermutation(byte[] source,
                                               int sourceLength,
                                               long[] permutation,
                                               int permutationLength,
                                               boolean isReverseOrder,
                                               boolean indexStartedAtOne,
                                               int resultLength);

}
