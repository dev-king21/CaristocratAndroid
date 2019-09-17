package com.ingic.caristocrat.firebase;

import java.io.Serializable;


public class GcmDataObject implements Serializable {

    String title = "";
    String message = "";
    String action_type = "";
    Integer ref_id ;

    public Integer getRef_id() {
        return ref_id;
    }

    public void setRef_id(Integer ref_id) {
        this.ref_id = ref_id;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
