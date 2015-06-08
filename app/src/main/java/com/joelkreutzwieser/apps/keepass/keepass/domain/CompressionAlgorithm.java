package com.joelkreutzwieser.apps.keepass.keepass.domain;

public enum CompressionAlgorithm {

    None,
    Gzip;

    public static CompressionAlgorithm parseValue(int value) {
        switch (value) {
            case 0:
                return None;
            case 1:
                return Gzip;
            default:
                throw new IllegalArgumentException(String.format("Value %d is not a valud Compression Algorithm", value));
        }
    }
}
