package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "History", strict = false)
public class History {
    @ElementList(name = "Entry", required = false, inline = true)
    private List<Entry> entries = new ArrayList<>();

    public List<Entry> getHistoricalEntries() {
        return entries;
    }

    public void setHistoricalEntries(List<Entry> historicalEntries) {
        this.entries = historicalEntries;
    }
}
