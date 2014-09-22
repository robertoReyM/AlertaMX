package com.smartplace.alerta.cap;

/**
 * Created by Roberto on 12/07/2014.
 */
public class CapEntry {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public CapEntryContent getContent() {
        return content;
    }

    public void setContent(CapEntryContent content) {
        this.content = content;
    }

    private String id;
    private String title;
    private String updated;
    private CapEntryContent content;
}
