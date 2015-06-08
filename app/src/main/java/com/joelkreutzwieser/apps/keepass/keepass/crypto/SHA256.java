package com.joelkreutzwieser.apps.keepass.keepass.crypto;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    public static byte[] hash(String text) {
        if (text == null) {
            throw new IllegalArgumentException("Text must not be null");
        }

        try {
            return hash(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("The encoding is not supported");
        }
    }

    public static byte[] hash(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException("Bytes must not be null");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bytes);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("The algorithm is not supported");
        }
    }
}
