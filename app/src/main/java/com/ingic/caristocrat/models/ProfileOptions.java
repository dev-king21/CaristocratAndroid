package com.ingic.caristocrat.models;

/**
 */
public class ProfileOptions {
    String tag;
    String name;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProfileOptions(String tag, String name) {
        this.tag = tag;
        this.name = name;
    }
}
