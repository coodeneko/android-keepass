package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class KeePassFileRoot implements KeePassFileElement {
    @Element(name = "Group")
    private Group Group;

    public Group getGroup() {
        return this.Group;
    }

    public List<Entry> getAllEntries() {
        List<Entry> entries = new ArrayList<Entry>();
        entries.addAll(this.Group.getEntries());
        for(Group group : this.Group.getGroups()) {
            entries.addAll(group.getAllEntries());
        }
        return entries;
    }
}
