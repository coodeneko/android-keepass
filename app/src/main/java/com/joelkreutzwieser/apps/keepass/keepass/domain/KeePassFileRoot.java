package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class KeePassFileRoot implements KeePassFileElement {

    @Element(name = "Group")
    public Group group;
}
