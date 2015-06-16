package com.joelkreutzwieser.apps.keepass.keepass;

import com.joelkreutzwieser.apps.keepass.keepass.crypto.SHA256;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;

import org.junit.Test;
import org.junit.Assert;

import org.spongycastle.crypto.engines.Salsa20Engine;
import org.spongycastle.crypto.params.KeyParameter;
import org.spongycastle.crypto.params.ParametersWithIV;
import org.spongycastle.util.encoders.Base64;
import org.spongycastle.util.encoders.Hex;

public class KeePassDatabaseTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonKeePassFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("src\\test\\res\\test.txt");
    }

    @Test
    public void testKeePassFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("src\\test\\res\\testDatabase.kdbx");
    }

    @Test
    public void testOpenDatabase() throws Exception {
        KeePassFile keePassDatabase = KeePassDatabase.getInstance("src\\test\\res\\testDatabase.kdbx").openDatabase("abcdefg");
        Assert.assertEquals("KeePass",keePassDatabase.getMeta().getGenerator());
        Assert.assertEquals("TestDatabase",keePassDatabase.getMeta().getDatabaseName());
    }

    @Test
    public void testSalsa20Stability() throws Exception {
        KeePassDatabase reader = KeePassDatabase.getInstance("src\\test\\res\\testDatabase.kdbx");

        byte[] salsaKey = SHA256.hash(reader.keePassHeader.getProtectionStreamKey());
        Salsa20Engine salsa20Engine  = new Salsa20Engine();
        salsa20Engine.init(true, new ParametersWithIV(new KeyParameter(salsaKey), Hex.decode("E830094B97205D2A")));

        String string = "U39tKvVEn9E=";
        byte[] protectedBuffer = Base64.decode(string.getBytes());
        byte[] plainText = new byte[protectedBuffer.length];
        salsa20Engine.processBytes(protectedBuffer, 0, protectedBuffer.length, plainText, 0);

        string = "WF6UJaw=";
        protectedBuffer = Base64.decode(string.getBytes());
        plainText = new byte[protectedBuffer.length];
        salsa20Engine.processBytes(protectedBuffer, 0, protectedBuffer.length, plainText, 0);

        string = "eh+khWVYN73GMA1d8g61X8Uh6Ck=";
        protectedBuffer = Base64.decode(string.getBytes());
        plainText = new byte[protectedBuffer.length];
        salsa20Engine.processBytes(protectedBuffer, 0, protectedBuffer.length, plainText, 0);
        Assert.assertEquals("1v4QKuIUT6HHRkbq0MPL", new String(plainText, "UTF-8"));
    }

    @Test
    public void testDatabaseDecryption() throws Exception {
        KeePassFile keePassDatabase = KeePassDatabase.getInstance("src\\test\\res\\testDatabase.kdbx").openDatabase("abcdefg");
        Assert.assertEquals("1v4QKuIUT6HHRkbq0MPL", keePassDatabase.getMainGroup().getGroups().get(0).getEntries().get(0).getPassword());
    }

}
