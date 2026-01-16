package project.domain.enums;

import java.security.SecureRandom;
import java.util.Arrays;

public enum PaddingMode {
    ZEROS {
        @Override
        public byte[] setPadding(byte[] data, int blockSize) {
            return data.length % blockSize == 0
                    ? data
                    : Arrays.copyOf(data, data.length + blockSize - data.length % blockSize);
        }

        @Override
        public byte[] removePadding(byte[] data) {
            int newLength = data.length;
            for (int i = data.length - 1; data[i] == 0; i--) {
                newLength = i;
            }
            return Arrays.copyOf(
                    data,
                    newLength
            );
        }
    },
    ANSI_X_923 {
        @Override
        public byte[] setPadding(byte[] data, int blockSize) {
            byte controlByte;
            byte[] result;
            if (data.length % blockSize == 0) {
                controlByte = (byte) blockSize;
                result = Arrays.copyOf(data, data.length + blockSize);
            } else {
                controlByte = (byte) (blockSize - data.length % blockSize);
                result = Arrays.copyOf(data, data.length + blockSize - data.length % blockSize);
            }
            result[result.length - 1] = controlByte;
            return result;
        }

        @Override
        public byte[] removePadding(byte[] data) {
            byte controlByte = data[data.length - 1];
            for (int i = data.length - 2; i > data.length - 1 - (int) controlByte; i--) {
                if (data[i] != 0) {
                    return data;
                }
            }
            return Arrays.copyOf(data, data.length - (int) controlByte);
        }
    },
    PKCS7 {
        @Override
        public byte[] setPadding(byte[] data, int blockSize) {
            byte controlByte;
            byte[] result;
            if (data.length % blockSize == 0) {
                controlByte = (byte) blockSize;
                result = Arrays.copyOf(data, data.length + blockSize);
            } else {
                controlByte = (byte) (blockSize - data.length % blockSize);
                result = Arrays.copyOf(data, data.length + blockSize - data.length % blockSize);
            }
            for (int i = data.length; i < result.length; i++) {
                result[i] = controlByte;
            }
            return result;
        }

        @Override
        public byte[] removePadding(byte[] data) {
            return Arrays.copyOf(data, data.length - data[data.length - 1]);
        }
    },
    ISO_10126 {
        @Override
        public byte[] setPadding(byte[] data, int blockSize) {
            byte controlByte;
            byte[] result;
            if (data.length % blockSize == 0) {
                controlByte = (byte) blockSize;
                result = Arrays.copyOf(data, data.length + blockSize);
            } else {
                controlByte = (byte) (blockSize - data.length % blockSize);
                result = Arrays.copyOf(data, data.length + blockSize - data.length % blockSize);
            }
            for (int i = data.length; i < result.length - 1; i++) {
                result[i] = (byte) rnd.nextInt(256);
            }
            result[result.length - 1] = controlByte;
//            System.out.printf("Old len = %s, new len = %s%n", data.length, result.length);
            return result;
        }

        @Override
        public byte[] removePadding(byte[] data) {
            return Arrays.copyOf(data, data.length - data[data.length - 1]);
        }
    };

    private static final SecureRandom rnd = new SecureRandom();

    public byte[] setPadding(byte[] data, int blockSize) {
        return data;
    }

    public byte[] removePadding(byte[] data) {
        return data;
    }
}
