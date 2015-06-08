package com.joelkreutzwieser.apps.keepass.keepass.domain;

public enum CRSAlgorithm {

    Null,
    ArcFourVariant,
    Salsa20;

    public static CRSAlgorithm parseValue(int value) {
        switch (value) {
            case 0:
                return Null;
            case 1:
                return ArcFourVariant;
            case 2:
                return Salsa20;
            default:
                throw new IllegalArgumentException(String.format("Value %i is not a valid CRSAlgorithm", value));
        }
    }
}
