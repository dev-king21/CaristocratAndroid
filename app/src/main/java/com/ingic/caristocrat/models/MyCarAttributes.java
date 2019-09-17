package com.ingic.caristocrat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 */
public class MyCarAttributes implements Serializable{
    @SerializedName("value")
    @Expose
    private String value;
    @SerializedName("attr_id")
    @Expose
    private int attr_id;
    @SerializedName("attr_name")
    @Expose
    private String attr_name;
    @SerializedName("attr_option")
    @Expose
    private String attr_option;
    @SerializedName("attr_icon")
    @Expose
    private String attrIcon;

    private String categoryKey;

    private String valueUnit;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getAttr_id() {
        return attr_id;
    }

    public void setAttr_id(int attr_id) {
        this.attr_id = attr_id;
    }

    public String getAttr_name() {
        return attr_name;
    }

    public void setAttr_name(String attr_name) {
        this.attr_name = attr_name;
    }

    public String getAttr_option() {
        return attr_option;
    }

    public void setAttr_option(String attr_option) {
        this.attr_option = attr_option;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getValueUnit() {
        return valueUnit;
    }

    public void setValueUnit(String valueUnit) {
        this.valueUnit = valueUnit;
    }

    public String getAttrIcon() {
        return attrIcon;
    }

    public void setAttrIcon(String attrIcon) {
        this.attrIcon = attrIcon;
    }
}
