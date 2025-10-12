package project.interfaces;

public interface RoundFunction {
    /**
     * Применить раундовую функцию к входному блоку и раундовому ключу.
     * @param inputBlock входной блок (половина блока, например 32 бита)
     * @param roundKey ключ конкретного раунда
     * @return результат преобразования
     */
    byte[] apply(byte[] inputBlock, byte[] roundKey);
}
