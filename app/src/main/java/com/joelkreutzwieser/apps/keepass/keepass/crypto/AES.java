package com.joelkreutzwieser.apps.keepass.keepass.crypto;

import com.joelkreutzwieser.apps.keepass.keepass.exception.KeePassDatabaseUnreadable;

import org.spongycastle.crypto.BlockCipher;
import org.spongycastle.crypto.engines.AESFastEngine;
import org.spongycastle.crypto.params.KeyParameter;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static final String KEY_ALGORITHM = "AES";

    public static byte[] decrypt(byte[] key, byte[] ivRaw, byte[] encryptedData) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        if (ivRaw == null) {
            throw new IllegalArgumentException("IV must not be null");
        }
        if (encryptedData == null) {
            throw new IllegalArgumentException("EncryptedData must not be null");
        }

        try {
            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            Key aesKey = new SecretKeySpec(key, KEY_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(ivRaw);
            c.init(Cipher.DECRYPT_MODE, aesKey, iv);
            return c.doFinal(encryptedData);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchPaddingException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw createCryptoException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw createCryptoException(e);
        } catch (IllegalBlockSizeException e) {
            throw createCryptoException(e);
        } catch (BadPaddingException e) {
            throw createCryptoException(e);
        }
    }

    public static byte[] transformKey(byte[] key, byte[] data, long rounds) {
        if (key == null) {
            throw new IllegalArgumentException("Key must not be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("Data must not be null");
        }
        if (rounds < 1) {
            throw new IllegalArgumentException("Rounds must be > 1");
        }

        try {
            byte[] clone = data.clone();
            BlockCipher aesFastEngine = new AESFastEngine();
            aesFastEngine.init(true, new KeyParameter(key));
            System.out.println("Start");

            for (long i = 0; i < rounds; ++i) {
                aesFastEngine.processBlock(clone, 0, clone, 0);
                aesFastEngine.processBlock(clone, 16, clone, 16);
            }

            System.out.println("Stop");

            return clone;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeePassDatabaseUnreadable createCryptoException(Throwable e) {
        return new KeePassDatabaseUnreadable("Could not decrypt keepass file. Master key wrong?", e);
    }
}
