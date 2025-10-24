package project.interfaces;

public interface RoundFunction {
    // надо значит надо
    byte[] apply(byte[] inputBlock, byte[] roundKey);

    long getFunctionAddress();

}
