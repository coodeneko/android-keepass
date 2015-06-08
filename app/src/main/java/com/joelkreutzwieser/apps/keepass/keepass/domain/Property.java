package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root(name = "String", strict = false)
public class Property {
    @Transient
    private KeePassFileElement parent;

    @Element(name = "Key", required = false)
    private String key;

    @Element(name = "Value", required = false)
    private PropertyValue propertyValue;

    Property() {
    }

    public Property(String key, String value, boolean isProtected) {
        setKey(key);
        setValue(new PropertyValue(isProtected, value));
    }

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.propertyValue.getValue();
    }

    public void setValue(PropertyValue value) {
        this.propertyValue = value;
    }

    public boolean isProtected() {
        return propertyValue.isProtected();
    }

    public void setParent(KeePassFileElement element) {
        this.parent = element;
    }

    public PropertyValue getPropertyValue() {
        return propertyValue;
    }

    @Override
    public String toString() {
        return "Property [key=" + this.key + ", propertyValue=" + this.propertyValue + "]";
    }
}
