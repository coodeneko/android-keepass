package com.joelkreutzwieser.apps.keepass.keepass.crypto;

import org.spongycastle.crypto.engines.Salsa20Engine;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;

public class Salsa20 implements ProtectedStringCrypto {
    private static final String SALSA20IV = "E830094B97205D2A";

    private Salsa20Engine salsa20Engine;

    private Salsa20() {
    }

    public static Salsa20 createInstance(byte[] protectedStreamKey) {
        if (protectedStreamKey == null) {
            throw new IllegalArgumentException("ProtectedStreamKey must not be null");
        }

        Salsa20 salsa20 = new Salsa20();
        salsa20.initialize(protectedStreamKey);

        return salsa20;
    }

    private void initialize(byte[] protectedStreamKey) {
        byte[] salsaKey = SHA256.hash(protectedStreamKey);
        salsa20Engine = new Salsa20Engine();
        salsa20Engine.init(true, new ParametersWithIV(new KeyParameter(salsaKey), Hex.decode(SALSA20IV)));
    }

    public String decrypt(String protectedString) {
        if (protectedString == null) {
            throw new IllegalArgumentException("ProtectedString must not be null");
        }

        byte[] protectedBuffer = Base64.decode(protectedString.getBytes());
        byte[] plainText = new byte[protectedBuffer.length];

        salsa20Engine.processBytes(protectedBuffer, 0, protectedBuffer.length, plainText, 0);

        try {
            return new String(plainText, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("The encoding UTF-8 is not supported");
        }
    }
}
