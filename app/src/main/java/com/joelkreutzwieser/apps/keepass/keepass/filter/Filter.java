package com.joelkreutzwieser.apps.keepass.keepass.filter;

public interface Filter<T> {
    boolean matches(T item);
}
