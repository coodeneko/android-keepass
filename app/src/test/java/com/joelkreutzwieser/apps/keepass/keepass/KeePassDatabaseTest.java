package com.joelkreutzwieser.apps.keepass.keepass;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Property;

import org.junit.Test;
import org.junit.Assert;
import org.spongycastle.util.encoders.Base64;

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
    public void testDatabaseGroups() throws Exception {
        KeePassFile keePassDatabase = KeePassDatabase.getInstance("src\\test\\res\\testDatabase.kdbx").openDatabase("abcdefg");
        for(Group group : keePassDatabase.getMainGroup().getGroups()) {
            System.out.println(group.getName());
            for(Entry entry : group.getEntries()) {
                System.out.println(entry.getTitle());
                System.out.println(entry.getPassword());
                System.out.println(keePassDatabase.protectedStringCrypto.decrypt(entry.getPassword()));
                System.out.println("Done");
            }
        }
        Assert.assertEquals(1, 2);
    }
}
