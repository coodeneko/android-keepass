package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

import java.util.ArrayList;
import java.util.List;

@Root(strict = false)
public class Entry implements KeePassFileElement {
    private static final String USER_NAME = "UserName";
    private static final String NOTES = "Notes";
    private static final String URL = "URL";
    private static final String PASSWORD = "Password";
    private static final String TITLE = "Title";

    @Transient
    private KeePassFileElement parent;

    @Element(name = "UUID", required = false)
    private String uuid;

    @Element(name = "String", required = false)
    private List<Property> properties = new ArrayList<Property>();

    @Element(name = "History", required = false)
    private History history;

    Entry() {
    }

    public Entry(String uuid) {
        setUuid(uuid);
    }

    public void setParent(KeePassFileElement element) {
        this.parent = element;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public String getTitle() {
        return getValueFromProperty(TITLE);
    }

    public void setTitle(String title) {
        setValue(TITLE, title);
    }

    public String getPassword() {
        return getValueFromProperty(PASSWORD);
    }

    public void setPassword(String password) {
        setValue(PASSWORD, password);
    }

    public String getUrl() {
        return getValueFromProperty(URL);
    }

    public void setUrl(String url) {
        setValue(URL, url);
    }

    public String getUserName() {
        return getValueFromProperty(USER_NAME);
    }

    public void setUserName(String userName) {
        setValue(USER_NAME, userName);
    }

    public boolean isTitleProtected() {
        return getPropertyByName(TITLE).isProtected();
    }

    public boolean isPasswordProtected() {
        return getPropertyByName(PASSWORD).isProtected();
    }

    public History getHistory() {
        return this.history;
    }

    public void setHistory(History history) {
        this.history = history;
    }

    private String getValueFromProperty(String name) {
        Property property = getPropertyByName(name);
        if (property != null) {
            return property.getValue();
        }
        return null;
    }

    private Property getPropertyByName(String name) {
        for (Property property : this.properties) {
            if (property.getKey().equalsIgnoreCase(name)) {
                return property;
            }
        }
        return null;
    }

    private void setValue(String propertyName, String propertyValue) {
        Property property = getPropertyByName(propertyName);
        if (property == null) {
            property = new Property(propertyName, propertyValue, false);
            this.properties.add(property);
        } else {
            property.setValue(new PropertyValue(false, propertyValue));
        }
    }

    @Override
    public String toString() {
        return "Entry [uuid=" + this.uuid + ", getTitle()=" + this.getTitle() + ", getPassword()=" + this.getPassword() + ", getUsername()=" + this.getUserName() + "]";
    }
}
