package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "Group", strict = false)
public class Group implements KeePassFileElement {
    //@Transient
    //private KeePassFileElement parent;

    @Element(name = "UUID", required = false)
    private String uuid;

    @Element(name = "Name", required = false)
    private String name;

    @Element(name = "Notes", required = false)
    private String notes;

    @Element(name = "IconID", required = false)
    private String IconID;

    @ElementList(name = "Group", required = false, inline = true)
    private List<Group> groups = new ArrayList<>();

    @ElementList(name = "Entry", required = false, inline = true)
    private List<Entry> entries = new ArrayList<>();

    /*public void setParent(KeePassFileElement element) {
        //this.parent = element;

        if (groups != null) {
            for (Group group : this.groups) {
                group.setParent(this);
            }
        }

        if (entries != null) {
            for (Entry entry : this.entries) {
                entry.setParent(this);
            }
        }
    }*/

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Group> getAllGroups() {
        List<Group> groups = new ArrayList<>();
        for (Group group : this.groups) {
            groups.add(group);
            groups.addAll(group.getAllGroups());
        }
        return groups;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getAllEntries() {
        List<Entry> entries = new ArrayList<>();
        entries.addAll(this.entries);
        for (Group group : this.groups) {
            entries.addAll(group.getAllEntries());
        }
        return entries;
    }

    public Group getGroupByUUID(String UUID) {
        for (Group group : this.groups) {
            if (group.getUuid().equals(UUID)) {
                return group;
            }
            Group subGroup = group.getGroupByUUID(UUID);
            if (subGroup != null) {
                return subGroup;
            }
        }
        return null;
    }

    public Entry getEntryByUUID(String UUID) {
        for (Entry entry : this.entries) {
            if (entry.getUuid().equals(UUID)) {
                return entry;
            }
        }
        for (Group group : this.groups) {
            Entry subEntry = group.getEntryByUUID(UUID);
            if (subEntry != null) {
                return subEntry;
            }
        }
        return null;
    }
}
