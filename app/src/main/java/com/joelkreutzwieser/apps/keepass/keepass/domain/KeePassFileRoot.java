package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class KeePassFileRoot implements KeePassFileElement {
    @Element(name = "Group")
    private Group Group;

    public Group getGroup() {
        return this.Group;
    }
}
