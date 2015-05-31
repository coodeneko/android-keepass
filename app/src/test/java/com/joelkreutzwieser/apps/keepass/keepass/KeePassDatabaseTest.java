package com.joelkreutzwieser.apps.keepass.keepass;

import org.junit.*;

public class KeePassDatabaseTest {

    @Test(expected=IllegalArgumentException.class)
    public void testEmptyFile() throws Exception {
        KeePassDatabase keePassDatabase = KeePassDatabase.getInstance("");
    }
}
