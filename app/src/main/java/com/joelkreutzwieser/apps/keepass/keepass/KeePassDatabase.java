package com.joelkreutzwieser.apps.keepass.keepass;

import com.google.common.io.ByteStreams;
import com.joelkreutzwieser.apps.keepass.keepass.crypto.Decrypter;
import com.joelkreutzwieser.apps.keepass.keepass.crypto.ProtectedStringCrypto;
import com.joelkreutzwieser.apps.keepass.keepass.crypto.SHA256;
import com.joelkreutzwieser.apps.keepass.keepass.domain.CRSAlgorithm;
import com.joelkreutzwieser.apps.keepass.keepass.domain.CompressionAlgorithm;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassHeader;
import com.joelkreutzwieser.apps.keepass.keepass.exception.KeePassDatabaseUnreadable;
import com.joelkreutzwieser.apps.keepass.keepass.parser.KeePassDatabaseXmlParser;
import com.joelkreutzwieser.apps.keepass.keepass.stream.HashedBlockInputStream;
import com.joelkreutzwieser.apps.keepass.keepass.util.ByteUtils;
import com.joelkreutzwieser.apps.keepass.keepass.util.StreamUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

/**
 * Based off of OpenKeePass
 */
public class KeePassDatabase {

    public static final int VERSION_SIGNATURE_LENGTH = 12;
    // Signature for KeePass 2.X
    private static final int KEEPASS_2_FILE_SIGNATURE_1 = 0x9AA2D903 & 0xFF;
    private static final int KEEPASS_2_FILE_SIGNATURE_2 = 0xB54BFB67 & 0xFF;
    protected Decrypter decrypter = new Decrypter();
    protected KeePassDatabaseXmlParser keePassDatabaseXmlParser = new KeePassDatabaseXmlParser();
    public KeePassHeader keePassHeader = new KeePassHeader();
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
            keePassDatabase.readHeader();
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
        signatureBuffer.order(ByteOrder.LITTLE_ENDIAN);

        int signaturePart1 = ByteUtils.toUnsignedInt(signatureBuffer.getInt());
        int signaturePart2 = ByteUtils.toUnsignedInt(signatureBuffer.getInt());

        if (signaturePart1 == KEEPASS_2_FILE_SIGNATURE_1 && signaturePart2 == KEEPASS_2_FILE_SIGNATURE_2) {
            return;
        }
        throw new UnsupportedOperationException("Not a KeePass 2.X File");
    }

    private void readHeader() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(keePassFile));
        bufferedInputStream.skip(VERSION_SIGNATURE_LENGTH);

        while (true) {
            try {
                int fieldID = bufferedInputStream.read();

                byte[] fieldLength = new byte[2];
                bufferedInputStream.read(fieldLength);
                ByteBuffer fieldLengthBuffer = ByteBuffer.wrap(fieldLength);
                fieldLengthBuffer.order(ByteOrder.LITTLE_ENDIAN);
                int fieldLengthInt = ByteUtils.toUnsignedInt(fieldLengthBuffer.getShort());

                if (fieldLengthInt > 0) {
                    byte[] data = new byte[fieldLengthInt];
                    bufferedInputStream.read(data);
                    keePassHeader.setValue(fieldID, data);
                    keePassHeader.increaseHeaderSize(fieldLengthInt + 3);
                }

                if (fieldID == 0) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException("Could not read header input", e);
            }
        }
    }

    public KeePassFile openDatabase(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        try {
            byte[] passwordByte = password.getBytes("UTF-8");
            byte[] hashedPassword = SHA256.hash(passwordByte);

            return decryptAndParseDatabase(hashedPassword);
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException("The encoding UTF-8 is not supported");
        }
    }

    private KeePassFile decryptAndParseDatabase(byte[] key) {
        try {
            byte[] aesDecryptedFile = decrypter.decryptDatabase(key, this.keePassHeader, this.keePassFile);

            byte[] startBytes = new byte[32];
            ByteArrayInputStream decryptedStream = new ByteArrayInputStream(aesDecryptedFile);
            decryptedStream.read(startBytes);


            if (!Arrays.equals(keePassHeader.getStreamStartBytes(), startBytes)) {
                throw new KeePassDatabaseUnreadable("The database is corrupt");
            }
            HashedBlockInputStream hashedBlockInputStream = new HashedBlockInputStream(decryptedStream);
            byte[] hasedBlockBytes = ByteStreams.toByteArray(hashedBlockInputStream);
            byte[] decompressed = hasedBlockBytes;

            if (keePassHeader.getCompressionFlag().equals(CompressionAlgorithm.Gzip)) {
                GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(hasedBlockBytes));
                decompressed = ByteStreams.toByteArray(gzipInputStream);
            }

            ProtectedStringCrypto protectedStringCrypto;
            if (keePassHeader.getCRSAlgorithm().equals(CRSAlgorithm.Salsa20)) {
                protectedStringCrypto = com.joelkreutzwieser.apps.keepass.keepass.crypto.Salsa20.createInstance(keePassHeader.getProtectionStreamKey());
            } else {
                throw new UnsupportedOperationException("Only Salsa20 is supported");
            }

            /*for (byte line : decompressed
                    ) {

                System.out.print((char)line);
            }*/
            return keePassDatabaseXmlParser.parse(new ByteArrayInputStream(decompressed), protectedStringCrypto, keePassHeader.getProtectionStreamKey());
        } catch (IOException e) {
            throw new RuntimeException("Could not open database file", e);
        }
    }
}
