package project.application.core;

import project.interfaces.RoundFunction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FeistelNetwork {

    private final RoundFunction roundFunction;

    public byte[] apply(byte[] plaintext, byte[][] keys) {
        byte[] result = new byte[0];
        //вызов функции на языке си, в которую я передам ссылку на roundFunction (так же на языке си) , массив байтов и количество раундов
        return result;
    }
}
