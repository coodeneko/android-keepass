package com.joelkreutzwieser.apps.keepass.keepass.util;

public class ByteUtils {

    public static int toUnsignedInt(int value) {
        return value & 0xFF;
    }
}
