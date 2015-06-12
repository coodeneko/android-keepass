package com.joelkreutzwieser.apps.keepass.keepass.domain;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class Meta {
    @Element(name = "Generator")
    private String generator;

    @Element(name = "DatabaseName")
    private String databaseName;

    @Element(name = "DatabaseNameChanged")
    private String databaseNameChanged;

    @Element(name = "DatabaseDescription")
    private String databaseDescription;

    @Element(name = "DatabaseDescriptionChanged")
    private String databaseDescriptionChanged;

    @Element(name = "MaintenanceHistoryDays")
    private int maintenanceHistoryDays;

    @Element(name = "RecycleBinUUID")
    private String recycleBinUuid;

    @Element(name = "RecycleBinChanged")
    private String recycleBinChanged;

    @Element(name = "RecycleBinEnabled")
    private String recycleBinEnabled;

    @Element(name = "HistoryMaxItems")
    private long historyMaxItems;

    @Element(name = "HistoryMaxSize")
    private long historyMaxSize;

    public String getGenerator() {
        return this.generator;
    }

    public void setGenerator(String generator) {
        this.generator = generator;
    }

    public String getDatabaseName() {
        return this.databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseDescription() {
        return this.databaseDescription;
    }

    public void setDatabaseDescription(String databaseDescription) {
        this.databaseDescription = databaseDescription;
    }

    public String getDatabaseNameChanged() {
        return this.databaseNameChanged;
    }

    public void setDatabaseNameChanged(String databaseNameChanged) {
        this.databaseNameChanged = databaseNameChanged;
    }

    public String getDatabaseDescriptionChanged() {
        return this.databaseDescriptionChanged;
    }

    public void setDatabaseDescriptionChanged(String databaseDescriptionChanged) {
        this.databaseDescriptionChanged = databaseDescriptionChanged;
    }

    public int getMaintenanceHistoryDays() {
        return this.maintenanceHistoryDays;
    }

    public void setMaintenanceHistoryDays(int maintenanceHistoryDays) {
        this.maintenanceHistoryDays = maintenanceHistoryDays;
    }

    public String getRecycleBinUuid() {
        return this.recycleBinUuid;
    }

    public void setRecycleBinUuid(String recycleBinUuid) {
        this.recycleBinUuid = recycleBinUuid;
    }

    public String getRecycleBinChanged() {
        return this.recycleBinChanged;
    }

    public void setRecycleBinChanged(String recycleBinChanged) {
        this.recycleBinChanged = recycleBinChanged;
    }

    public boolean getRecycleBinEnabled() {
        return recycleBinEnabled != null && recycleBinEnabled.equalsIgnoreCase("true");
    }

    public void setRecycleBinEnabled(boolean recycleBinEnabled) {
        if (recycleBinEnabled) {
            this.recycleBinEnabled = "true";
        } else {
            this.recycleBinEnabled = "false";
        }
    }

    public long getHistoryMaxItems() {
        return this.historyMaxItems;
    }

    public void setHistoryMaxItems(long historyMaxItems) {
        this.historyMaxItems = historyMaxItems;
    }

    public long getHistoryMaxSize() {
        return this.historyMaxSize;
    }

    public void setHistoryMaxSize(long historyMaxSize) {
        this.historyMaxSize = historyMaxSize;
    }
}
