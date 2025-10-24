package project.interfaces;

public interface KeySchedule {

    byte[][] generateRoundKeys(byte[] masterKey);

}
