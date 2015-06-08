package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root(strict = false)
public class PropertyValue {
    @Attribute(name = "Protected")
    private String isProtected;

    @Text
    private String value;

    PropertyValue() {
    }

    public PropertyValue(boolean isProtected, String value) {
        setProtected(isProtected);
        setValue(value);
    }

    public boolean isProtected() {
        return this.isProtected != null && this.isProtected.equalsIgnoreCase("true");
    }

    public void setProtected(boolean isProtected) {
        if (isProtected) {
            this.isProtected = "true";
        } else {
            this.isProtected = "false";
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyValue [value=" + this.value + "]";
    }
}
