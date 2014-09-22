package com.smartplace.alerta.cap;

import java.util.ArrayList;

/**
 * Created by Roberto on 12/07/2014.
 */
public class CapFeed {


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public ArrayList<CapEntry> getEntries() {
        return entry;
    }

    public void setEntries(ArrayList<CapEntry> entry) {
        this.entry = entry;
    }

    private String title;
    private String id;
    private String rights;
    private String updated;
    private ArrayList<CapEntry> entry = new ArrayList<CapEntry>();
}
