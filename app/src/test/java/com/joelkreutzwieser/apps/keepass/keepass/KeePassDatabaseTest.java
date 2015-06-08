package com.joelkreutzwieser.apps.keepass.keepass;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFile;

import org.junit.Test;
import org.junit.Assert;

public class KeePassDatabaseTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testNonKeePassFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("C:\\Users\\Joel\\Documents\\Repos\\android-keepass\\app\\src\\test\\res\\test.txt");
    }

    @Test
    public void testKeePassFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("C:\\Users\\Joel\\Documents\\Repos\\android-keepass\\app\\src\\test\\res\\testDatabase.kdbx");
    }

    @Test
    public void testOpenDatabase() throws Exception {
        KeePassFile keePassDatabase = KeePassDatabase.getInstance("C:\\Users\\Joel\\Documents\\Repos\\android-keepass\\app\\src\\test\\res\\testDatabase.kdbx").openDatabase("abcdefg");
        Assert.assertEquals("KeePass",keePassDatabase.getMeta().getGenerator());
        Assert.assertEquals("TestDatabase",keePassDatabase.getMeta().getDatabaseName());
    }

    @Test
    public void testDatabaseGroups() throws Exception {
        KeePassFile keePassDatabase = KeePassDatabase.getInstance("C:\\Users\\Joel\\Documents\\Repos\\android-keepass\\app\\src\\test\\res\\testDatabase.kdbx").openDatabase("abcdefg");
        System.out.println(keePassDatabase.getRoot().group.getUuid());
        System.out.println(keePassDatabase.getRoot().group.getEntries().size());
        for(Group group : keePassDatabase.getRoot().group.getGroups()) {
            System.out.println(group.getUuid());
            System.out.println(group.getName());
        }
    }
}
