package com.joelkreutzwieser.apps.keepass.keepass.crypto;

import android.app.ProgressDialog;

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

    public static byte[] transformKey(byte[] key, byte[] data, long rounds, ProgressDialog progressDialog) {
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
            BlockCipher aesFastEngine = new AESFastEngine();
            aesFastEngine.init(true, new KeyParameter(key));
            byte[] part1 = new byte[16];
            byte[] part2 = new byte[16];
            for(int i = 0; i < 16; i++) {
                part1[i] = data[i];
                part2[i] = data[16+i];
            }

            AESThread thread1 = new AESThread(key, part1, rounds, progressDialog);
            AESThread thread2 = new AESThread(key, part2, rounds, null);
            Thread t1 = new Thread(thread1);
            Thread t2 = new Thread(thread2);
            t1.setPriority(Thread.MAX_PRIORITY);
            t2.setPriority(Thread.MAX_PRIORITY);
            t1.start();
            t2.start();
            while (true) {
                try {
                    t1.join();
                    t2.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            part1 = thread1.getData();
            part2 = thread2.getData();

            for(int i = 0; i < 16; i++) {
                data[i] = part1[i];
                data[16+i] = part2[i];
            }

            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeePassDatabaseUnreadable createCryptoException(Throwable e) {
        return new KeePassDatabaseUnreadable("Could not decrypt keepass file. Master key wrong?", e);
    }
}

class AESThread implements Runnable {

    byte[] key;
    byte[] data;
    long rounds;
    boolean done;
    ProgressDialog progressDialog;

    AESThread(byte[] key, byte[] data, long rounds, ProgressDialog progressDialog) {
        this.key = key.clone();
        this.data = data.clone();
        this.rounds = rounds;
        this.done = false;
        this.progressDialog = progressDialog;
    }

    @Override
    public void run() {
        boolean updater = false;
        if(progressDialog != null && rounds <= Integer.MAX_VALUE && progressDialog.isShowing()) {
            progressDialog.setMax((int) rounds);
            updater = true;
        }
        BlockCipher aesFastEngine = new AESFastEngine();
        aesFastEngine.init(true, new KeyParameter(key));
        for (long i = 0; i < rounds; ++i) {
            if(updater && (i % 10000) == 0 && progressDialog.isShowing()) {
                progressDialog.setProgress((int)i);
            }
            aesFastEngine.processBlock(data, 0, data, 0);
        }
        done = true;
    }

    public byte[] getData() {
        return data;
    }
}