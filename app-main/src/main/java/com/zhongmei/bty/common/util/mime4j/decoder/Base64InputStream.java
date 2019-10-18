



package com.zhongmei.bty.common.util.mime4j.decoder;

import java.io.IOException;
import java.io.InputStream;


public class Base64InputStream extends InputStream {
    private final InputStream s;
    private int outCount = 0;
    private int outIndex = 0;
    private final int[] outputBuffer = new int[3];
    private final byte[] inputBuffer = new byte[4];
    private boolean done = false;

    public Base64InputStream(InputStream s) {
        this.s = s;
    }


    @Override
    public void close() throws IOException {
        s.close();
    }

    @Override
    public int read() throws IOException {
        if (outIndex == outCount) {
            fillBuffer();
            if (outIndex == outCount) {
                return -1;
            }
        }

        return outputBuffer[outIndex++];
    }


    private void fillBuffer() throws IOException {
        outCount = 0;
        outIndex = 0;
        int inCount = 0;

        int i;
                while (!done) {
            switch (i = s.read()) {
                case -1:
                                                            return;
                case '=':
                                        done = true;
                    decodeAndEnqueue(inCount);
                    return;
                default:
                    byte sX = TRANSLATION[i];
                    if (sX < 0)
                        continue;
                    inputBuffer[inCount++] = sX;
                    if (inCount == 4) {
                        decodeAndEnqueue(inCount);
                        return;
                    }
                    break;
            }
        }
    }

    private void decodeAndEnqueue(int len) {
        int accum = 0;
        accum |= inputBuffer[0] << 18;
        accum |= inputBuffer[1] << 12;
        accum |= inputBuffer[2] << 6;
        accum |= inputBuffer[3];

                                if (len == 4) {
            outputBuffer[0] = (accum >> 16) & 0xFF;
            outputBuffer[1] = (accum >> 8) & 0xFF;
            outputBuffer[2] = (accum) & 0xFF;
            outCount = 3;
            return;
        } else if (len == 3) {
            outputBuffer[0] = (accum >> 16) & 0xFF;
            outputBuffer[1] = (accum >> 8) & 0xFF;
            outCount = 2;
            return;
        } else {             outputBuffer[0] = (accum >> 16) & 0xFF;
            outCount = 1;
            return;
        }
    }

    private static byte[] TRANSLATION = {-1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63,
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1,
            -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14,
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1,
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,
            41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
    };

}
