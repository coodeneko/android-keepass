package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
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

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        for (Group group : this.Group.getGroups()) {
            groups.add(group);
            groups.addAll(group.getAllGroups());
        }
        return groups;
    }

    public List<Entry> getAllEntries() {
        List<Entry> entries = new ArrayList<>();
        entries.addAll(this.Group.getEntries());
        for(Group group : this.Group.getGroups()) {
            entries.addAll(group.getAllEntries());
        }
        return entries;
    }
}
