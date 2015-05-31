package com.joelkreutzwieser.apps.keepass.keepass;


import com.joelkreutzwieser.apps.keepass.keepass.util.ByteUtils;
import com.joelkreutzwieser.apps.keepass.keepass.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class KeePassDatabase {

    // Signature for KeePass 2.X
    private static final int KEEPASS_2_FILE_SIGNATURE_1 = 0x9AA2D903 & 0xFF;
    private static final int KEEPASS_2_FILE_SIGNATURE_2 = 0xB54BFB67 & 0xFF;
    private static final int VERSION_SIGNATURE_LENGTH = 12;

    private byte[] keePassFile;

    private KeePassDatabase(InputStream inputStream) {
        try {
            keePassFile = StreamUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static KeePassDatabase getInstance(String keePassDatabaseFileName) {
        return getInstance(new File(keePassDatabaseFileName));
    }

    public static KeePassDatabase getInstance(File keePassDatabaseFile) {
        if (keePassDatabaseFile == null) {
            throw new IllegalArgumentException("Need valid file");
        }

        try {
            return getInstance(new FileInputStream(keePassDatabaseFile));
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("File not found");
        }
    }

    public static KeePassDatabase getInstance(InputStream keePassDatabaseStream) {
        if (keePassDatabaseStream == null) {
            throw new IllegalArgumentException("Need non-empty stream");
        }
        KeePassDatabase keePassDatabase = new KeePassDatabase(keePassDatabaseStream);

        try {
            keePassDatabase.checkVersionSupport();

            return keePassDatabase;
        } catch (IOException e) {
            throw new RuntimeException("Could not read stream", e);
        }
    }

    private void checkVersionSupport() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(keePassFile));
        byte[] signature = new byte[VERSION_SIGNATURE_LENGTH];
        bufferedInputStream.read(signature);
        ByteBuffer signatureBuffer = ByteBuffer.wrap(signature);

        int signaturePart1 = ByteUtils.toUnsignedInt(signatureBuffer.getInt());
        int signaturePart2 = ByteUtils.toUnsignedInt(signatureBuffer.getInt());

        if (signaturePart1 == KEEPASS_2_FILE_SIGNATURE_1 && signaturePart2 == KEEPASS_2_FILE_SIGNATURE_2) {
            return;
        }
        throw new UnsupportedOperationException("Not a KeePass 2.X File");
    }
}
