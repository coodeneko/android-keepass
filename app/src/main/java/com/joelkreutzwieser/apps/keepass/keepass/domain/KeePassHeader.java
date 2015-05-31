package com.joelkreutzwieser.apps.keepass.keepass.domain;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KeePassHeader {

    private int headerSize = 0;
    private byte[] cipher;
    private CompressionAlgorithm compression;
    private byte[] masterSeed;
    private byte[] transformSeed;
    private long transformRounds;
    private byte[] encryptionIV;
    private byte[] protectedStreamKey;
    private byte[] streamStartBytes;
    private CRSAlgorithm crsAlgorithm;

    public void increaseHeaderSize(int numBytes) {
        this.headerSize += numBytes;
    }

    public int getHeaderSize() {
        return this.headerSize;
    }

    public void setValue(int headerID, byte[] value) {
        switch (headerID) {
            case 2:
                setCipher(value);
                break;
            case 3:
                setCompressionFlag(value);
                break;
            case 4:
                setMasterSeed(value);
                break;
            case 5:
                setTransformSeed(value);
                break;
            case 6:
                setTransformRounds(value);
                break;
            case 7:
                setEncryptionIV(value);
                break;
            case 8:
                setProtectionStreamKey(value);
                break;
            case 9:
                setStreamStartBytes(value);
                break;
            case 10:
                setInnerRandomStreamID(value);
                break;
        }
    }

    private void setCipher(byte[] value) {
        if (value == null || value.length != 16) {
            throw new IllegalArgumentException("The encryption cipher must contain 16 bytes");
        }
        this.cipher = value;
    }

    public byte[] getCipher() {
        return this.cipher;
    }

    private void setCompressionFlag(byte[] value) {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        int intVale = buffer.getInt();
        this.compression = CompressionAlgorithm.parseValue(intVale);
    }

    public CompressionAlgorithm getCompressionFlag() {
        return this.compression;
    }

    private void setMasterSeed(byte[] value) {
        this.masterSeed = value;
    }

    public byte[] getMasterSeed() {
        return this.masterSeed;
    }

    private void setTransformSeed(byte[] value) {
        this.transformSeed = value;
    }

    public byte[] getTransformSeed() {
        return this.transformSeed;
    }

    private void setTransformRounds(byte[] value) {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        this.transformRounds = buffer.getLong();
    }

    public long getTransformRounds() {
        return this.transformRounds;
    }

    private void setEncryptionIV(byte[] value) {
        this.encryptionIV = value;
    }

    public byte[] getEncryptionIV() {
        return this.encryptionIV;
    }

    private void setProtectionStreamKey(byte[] value) {
        this.protectedStreamKey = value;
    }

    public byte[] getProtectionStreamKey() {
        return this.protectedStreamKey;
    }

    private void setStreamStartBytes(byte[] value) {
        this.streamStartBytes = value;
    }

    public byte[] getStreamStartBytes() {
        return this.streamStartBytes;
    }

    private void setInnerRandomStreamID(byte[] value) {
        ByteBuffer buffer = wrapInBuffer(value);
        int intValue = buffer.getInt();
        this.crsAlgorithm = CRSAlgorithm.parseValue(intValue);
    }

    public CRSAlgorithm getInnerRandomStreamID() {
        return this.crsAlgorithm;
    }

    private ByteBuffer wrapInBuffer(byte[] value) {
        ByteBuffer buffer = ByteBuffer.wrap(value);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }
}
