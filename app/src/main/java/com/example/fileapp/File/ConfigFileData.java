package com.example.fileapp.File;

public class ConfigFileData {

    private int id;
    private String key;
    private int valueType;
    private String parent;

    public ConfigFileData(int id, String key, int value, String parent) {
        this.id = id;
        this.key = key;
        this.valueType = value;
        this.parent = parent;
    }

    public int getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public int getValueType() {
        return valueType;
    }

    public String getParent() {
        return parent;
    }
}
