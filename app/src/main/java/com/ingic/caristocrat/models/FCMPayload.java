package com.ingic.caristocrat.models;

import java.io.Serializable;

public class FCMPayload implements Serializable {
    private int id, action_id;
    private String title, message, action_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAction_id() {
        return action_id;
    }

    public void setAction_id(int action_id) {
        this.action_id = action_id;
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

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public class ExtraPayload{
        int ref_id;

        public int getRef_id() {
            return ref_id;
        }

        public void setRef_id(int ref_id) {
            this.ref_id = ref_id;
        }
    }
}
