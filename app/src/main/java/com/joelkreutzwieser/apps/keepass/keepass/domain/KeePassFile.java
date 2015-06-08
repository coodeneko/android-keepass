package com.joelkreutzwieser.apps.keepass.keepass.domain;

import com.joelkreutzwieser.apps.keepass.keepass.crypto.ProtectedStringCrypto;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root(name = "KeePassFile", strict = false)
public class KeePassFile implements KeePassFileElement {

    @Element(name = "Meta")
    private Meta meta;

    @Element(name = "Root")
    private KeePassFileRoot root;

    @Transient
    public ProtectedStringCrypto protectedStringCrypto;

    @Transient
    public byte[] key;

    public Meta getMeta() {
        return this.meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public KeePassFileRoot getRoot() {
        return this.root;
    }

    public void setRoot(KeePassFileRoot root) {
        this.root = root;
    }

    public Group getMainGroup() {
        return this.root.getGroup();
    }

    /*public List<Group> getTopGroups() {
        if (root != null && root.getGroups() != null && root.getGroups().size() == 1) {
            return root.getGroups().get(0).getGroups();
        }
        return new ArrayList<Group>();
    }

    public List<Entry> getTopEntries() {
        if (root != null && root.getGroups() != null && root.getGroups().size() == 1) {
            return root.getGroups().get(0).getEntries();
        }
        return new ArrayList<Entry>();
    }

    public List<Entry> getEntriesByTitle(final String title, final boolean matchExactly) {
        List<Entry> allEntries = new ArrayList<Entry>();

        if (this.root != null) {
            getEntries(this.root, allEntries);
        }

        return ListFilter.filter(allEntries, new Filter<Entry>() {

            @Override
            public boolean matches(Entry item) {
                if (matchExactly) {
                    if (item.getTitle() != null && item.getTitle().equalsIgnoreCase(title)) {
                        return true;
                    }
                } else {
                    if (item.getTitle() != null && item.getTitle().contains(title)) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    public void init() {
        //this.root.setParent(this);
    }

    public List<Entry> getEntries() {
        List<Entry> allEntries = new ArrayList<Entry>();

        if (this.root != null) {
            getEntries(this.root, allEntries);
        }

        return allEntries;
    }

    private void getEntries(Group parentGroup, List<Entry> entries) {
        List<Group> groups = parentGroup.getGroups();
        entries.addAll(parentGroup.getEntries());

        if (groups.size() != 0) {
            for (Group group : groups) {
                getEntries(group, entries);
            }
        }
    }*/
}
