package com.joelkreutzwieser.apps.keepass.keepass.exception;

public class KeePassDatabaseUnreadable extends RuntimeException {
    public KeePassDatabaseUnreadable(String message) {
        super(message);
    }

    public KeePassDatabaseUnreadable(String message, Throwable e) {
        super(message, e);
    }
}
