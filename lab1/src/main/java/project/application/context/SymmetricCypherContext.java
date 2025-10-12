package project.application.context;

import project.domain.enums.CypherMode;
import project.domain.enums.PaddingMode;
import project.interfaces.SymmetricCypher;

public class SymmetricCypherContext implements SymmetricCypher {

    private final byte[] key;
    private final CypherMode cypherMode;
    private final PaddingMode paddingMode;

    public SymmetricCypherContext(byte[] key,
                                  CypherMode cypherMode,
                                  PaddingMode paddingMode) {
        this.key = key;
        this.cypherMode = cypherMode;
        this.paddingMode = paddingMode;
    }

    @Override
    public void setKey(byte[] masterKey) {

    }

    @Override
    public byte[] encrypt(byte[] plaintext) {
        return new byte[0];
    }

    @Override
    public byte[] decrypt(byte[] ciphertext) {
        return new byte[0];
    }
}
