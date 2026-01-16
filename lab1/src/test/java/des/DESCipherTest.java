package des;

import org.junit.jupiter.api.Test;
import project.algorythm.des.DESCipher;
import project.algorythm.des.DESKeySchedule;
import project.algorythm.des.DESRoundFunction;
import project.application.context.SymmetricCipherContext;
import project.domain.enums.CipherMode;
import project.domain.enums.PaddingMode;
import project.interfaces.KeySchedule;
import project.interfaces.RoundFunction;
import project.interfaces.SymmetricCipher;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class DESCipherTest {

    private static final byte[] key = "rquipted".getBytes();
    private static final KeySchedule keySchedule = new DESKeySchedule();
    private static final RoundFunction roundFunction = new DESRoundFunction();
    private static final SymmetricCipher symmetricCipher = new DESCipher(keySchedule, roundFunction, key);


    @Test
    public void testEncryptDecryptRandomBytes() {
//        SecureRandom random = new SecureRandom();
//        byte[] input = "new phrase base 64".getBytes();
        byte[] input = "constant constant consta".getBytes();
//        random.nextBytes(input);

        SymmetricCipherContext context = new SymmetricCipherContext(
                CipherMode.ECB,
                PaddingMode.ZEROS,
                symmetricCipher
        );

        byte[] encrypted = context.encrypt(input);

//        for (byte e: encrypted) {
//            System.out.print(e + " ");
//        }

        byte[] decrypted = context.decrypt(encrypted);
        String word = Base64.getEncoder().encodeToString(encrypted);


//        System.out.println(new String(decrypted));

        assertArrayEquals(input, decrypted);
    }

    @Test
    public void testEncryptDecryptRandomBytesANSI() {
//        SecureRandom random = new SecureRandom();
//        byte[] input = "new phrase base 64".getBytes();
        byte[] input = "constant constant constant".getBytes();
//        random.nextBytes(input);
//        input = Arrays.copyOf(input, input.length + 8 - input.length % 8);
//        input[input.length - 1] = (byte) 6;
        SymmetricCipherContext context = new SymmetricCipherContext(
                CipherMode.ECB,
                PaddingMode.ISO_10126,
                symmetricCipher
        );

        byte[] encrypted = context.encrypt(input);
//        System.out.println(new String(encrypted));
        byte[] decrypted = context.decrypt(encrypted);
//        String word = Base64.getEncoder().encodeToString(encrypted);


//        System.out.println(new String(decrypted));

        assertArrayEquals(input, decrypted);
        System.out.println("TEST DONE");
    }
}
