package com.joelkreutzwieser.apps.keepass.keepass.crypto;

import android.app.ProgressDialog;

import com.joelkreutzwieser.apps.keepass.keepass.KeePassDatabase;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassHeader;
import com.joelkreutzwieser.apps.keepass.keepass.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Decrypter {
    public byte[] decryptDatabase(byte[] password, KeePassHeader header, byte[] database, ProgressDialog progressDialog) throws IOException {
        byte[] aesKey = createAesKey(password, header, progressDialog);

        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(database));
        bufferedInputStream.skip(KeePassDatabase.VERSION_SIGNATURE_LENGTH + header.getHeaderSize());

        byte[] payload = StreamUtils.toByteArray(bufferedInputStream);

        return AES.decrypt(aesKey, header.getEncryptionIV(), payload);
    }

    private byte[] createAesKey(byte[] password, KeePassHeader header, ProgressDialog progressDialog) {
        byte[] hashedPwd = SHA256.hash(password);

        byte[] transformedPwd = AES.transformKey(header.getTransformSeed(), hashedPwd, header.getTransformRounds(), progressDialog);
        byte[] transformedHashedPwd = SHA256.hash(transformedPwd);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        stream.write(header.getMasterSeed(), 0, 32);
        stream.write(transformedHashedPwd, 0, 32);

        return SHA256.hash(stream.toByteArray());
    }
}
