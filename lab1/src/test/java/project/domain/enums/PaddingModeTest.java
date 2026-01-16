package project.domain.enums;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class PaddingModeTest {

    private static final int BLOCK_SIZE = 8;

    @Test
    public void zerosPaddingKeepsBlockAlignedData() {
        byte[] input = "12345678".getBytes();

        byte[] padded = PaddingMode.ZEROS.setPadding(input, BLOCK_SIZE);

        assertSame(input, padded);
        assertArrayEquals(input, padded);
        assertEquals(BLOCK_SIZE, padded.length);
    }

    @Test
    public void zerosPaddingAddsZerosAndRemovesThem() {
        byte[] input = "1234567890".getBytes();

        byte[] padded = PaddingMode.ZEROS.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        for (int i = input.length; i < padded.length; i++) {
            assertEquals(0, padded[i]);
        }

        byte[] unpadded = PaddingMode.ZEROS.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void zerosPaddingRemovesAllTrailingZerosFromOriginalData() {
        byte[] input = new byte[]{1, 2, 0, 0};

        byte[] padded = PaddingMode.ZEROS.setPadding(input, BLOCK_SIZE);
        byte[] unpadded = PaddingMode.ZEROS.removePadding(padded);

        assertArrayEquals(new byte[]{1, 2}, unpadded);
    }

    @Test
    public void zerosPaddingDoesNotPadEmptyInput() {
        byte[] input = new byte[0];

        byte[] padded = PaddingMode.ZEROS.setPadding(input, BLOCK_SIZE);

        assertEquals(0, padded.length);
    }

    @Test
    public void ansiX923PaddingAddsFullBlockWhenAligned() {
        byte[] input = "12345678".getBytes();

        byte[] padded = PaddingMode.ANSI_X_923.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        for (int i = input.length; i < padded.length - 1; i++) {
            assertEquals(0, padded[i]);
        }
        assertEquals(BLOCK_SIZE, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.ANSI_X_923.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void ansiX923PaddingAddsZerosWhenNotAligned() {
        byte[] input = "1234567890".getBytes();

        byte[] padded = PaddingMode.ANSI_X_923.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        for (int i = input.length; i < padded.length - 1; i++) {
            assertEquals(0, padded[i]);
        }
        assertEquals(6, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.ANSI_X_923.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void ansiX923PaddingKeepsDataWhenPaddingIsInvalid() {
        byte[] input = "1234567890".getBytes();
        byte[] padded = PaddingMode.ANSI_X_923.setPadding(input, BLOCK_SIZE);
        padded[padded.length - 2] = 7;

        byte[] unpadded = PaddingMode.ANSI_X_923.removePadding(padded);

        assertArrayEquals(padded, unpadded);
    }

    @Test
    public void ansiX923PaddingHandlesEmptyInput() {
        byte[] input = new byte[0];

        byte[] padded = PaddingMode.ANSI_X_923.setPadding(input, BLOCK_SIZE);

        assertEquals(BLOCK_SIZE, padded.length);
        assertEquals(BLOCK_SIZE, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.ANSI_X_923.removePadding(padded);
        assertEquals(0, unpadded.length);
    }

    @Test
    public void pkcs7PaddingAddsFullBlockWhenAligned() {
        byte[] input = "12345678".getBytes();

        byte[] padded = PaddingMode.PKCS7.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        for (int i = input.length; i < padded.length; i++) {
            assertEquals(BLOCK_SIZE, padded[i]);
        }

        byte[] unpadded = PaddingMode.PKCS7.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void pkcs7PaddingAddsControlByteWhenNotAligned() {
        byte[] input = "1234567890".getBytes();

        byte[] padded = PaddingMode.PKCS7.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        for (int i = input.length; i < padded.length; i++) {
            assertEquals(6, padded[i]);
        }

        byte[] unpadded = PaddingMode.PKCS7.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void pkcs7PaddingHandlesEmptyInput() {
        byte[] input = new byte[0];

        byte[] padded = PaddingMode.PKCS7.setPadding(input, BLOCK_SIZE);

        assertEquals(BLOCK_SIZE, padded.length);
        assertEquals(BLOCK_SIZE, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.PKCS7.removePadding(padded);
        assertEquals(0, unpadded.length);
    }

    @Test
    public void iso10126PaddingAddsRandomBytesAndRemovesCorrectly() {
        byte[] input = "1234567890".getBytes();

        byte[] padded = PaddingMode.ISO_10126.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        assertEquals(6, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.ISO_10126.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void iso10126PaddingAddsFullBlockWhenAligned() {
        byte[] input = "12345678".getBytes();

        byte[] padded = PaddingMode.ISO_10126.setPadding(input, BLOCK_SIZE);

        assertEquals(16, padded.length);
        assertEquals(BLOCK_SIZE, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.ISO_10126.removePadding(padded);
        assertArrayEquals(input, unpadded);
    }

    @Test
    public void iso10126PaddingHandlesEmptyInput() {
        byte[] input = new byte[0];

        byte[] padded = PaddingMode.ISO_10126.setPadding(input, BLOCK_SIZE);

        assertEquals(BLOCK_SIZE, padded.length);
        assertEquals(BLOCK_SIZE, padded[padded.length - 1]);

        byte[] unpadded = PaddingMode.ISO_10126.removePadding(padded);
        assertEquals(0, unpadded.length);
    }
}
