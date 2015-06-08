package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class History {
    @Element(name = "Entry")
    private List<Entry> entries = new ArrayList<Entry>();

    public List<Entry> getHistoricalEntries() {
        return entries;
    }

    public void setHistoricalEntries(List<Entry> historicalEntries) {
        this.entries = historicalEntries;
    }
}
