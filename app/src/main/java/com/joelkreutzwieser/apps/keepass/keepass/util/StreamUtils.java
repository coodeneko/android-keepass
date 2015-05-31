package com.joelkreutzwieser.apps.keepass.keepass.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamUtils {
    private static final int BUFFER_SIZE = 4096;
    private static final int EOF = -1;

    public static int read(InputStream inputStream, byte[] buffer) throws IOException {
        int remaining = buffer.length;
        while (remaining > 0) {
            int location = buffer.length - remaining;
            int count = inputStream.read(buffer, location, remaining);
            if (EOF == count) {
                break;
            }
            remaining -= count;
        }
        return buffer.length - remaining;
    }

    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        copyLarge(inputStream, outputStream, new byte[BUFFER_SIZE]);
        return outputStream.toByteArray();
    }

    private static long copyLarge(InputStream inputStream, OutputStream outputStream, byte[] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
