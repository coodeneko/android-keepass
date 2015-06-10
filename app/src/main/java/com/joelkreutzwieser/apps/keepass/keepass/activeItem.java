package com.joelkreutzwieser.apps.keepass.keepass;

import com.joelkreutzwieser.apps.keepass.keepass.domain.Entry;
import com.joelkreutzwieser.apps.keepass.keepass.domain.Group;
import com.joelkreutzwieser.apps.keepass.keepass.domain.KeePassFileElement;

public class ActiveItem {
    public String name;
    public String type;
    public KeePassFileElement item;

    public ActiveItem(String name, String type, KeePassFileElement item) {
        this.name = name;
        this.type = type;
        this.item = item;
    }

    public String getUUID() {
        if(type == "Entry") {
            return ((Entry)this.item).getUuid();
        }
        return ((Group)this.item).getUuid();
    }
}
