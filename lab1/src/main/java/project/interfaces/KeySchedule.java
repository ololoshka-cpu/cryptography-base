package project.interfaces;

public interface KeySchedule {
    /**
     * Генерация всех раундовых ключей по мастер-ключу.
     * @param masterKey входной ключ
     * @return массив раундовых ключей (каждый — массив байтов)
     */
    byte[][] generateRoundKeys(byte[] masterKey);
}
