package com.smartplace.alerta.cap;

import java.util.ArrayList;

/**
 * Created by Roberto on 12/07/2014.
 */
public class CapAlertInfo {

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<String>  getCategory() {
        return category;
    }

    public void setCategory(ArrayList<String>  category) {
        this.category = category;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public ArrayList<String> getResponseType() {
        return responseType;
    }

    public void setResponseType(ArrayList<String> responseType) {
        this.responseType = responseType;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getCertainty() {
        return certainty;
    }

    public void setCertainty(String certainty) {
        this.certainty = certainty;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ArrayList<CapParameter> getParameter() {
        return parameter;
    }

    public void setParameter(ArrayList<CapParameter> parameter) {
        this.parameter = parameter;
    }

    public ArrayList<CapAlertArea> getArea() {
        return area;
    }

    public void setArea(ArrayList<CapAlertArea> area) {
        this.area = area;
    }
    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public ArrayList<String> getEventCode() {
        return eventCode;
    }

    public void setEventCode(ArrayList<String> eventCode) {
        this.eventCode = eventCode;
    }

    public String getOnset() {
        return onset;
    }

    public void setOnset(String onset) {
        this.onset = onset;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    private String language;
    private ArrayList<String>  category;
    private String event;
    private ArrayList<String> responseType;
    private String urgency;
    private String severity;
    private String certainty;
    private String audience;
    private ArrayList<String> eventCode;
    private String effective;
    private String onset;
    private String expires;
    private String senderName;
    private String headline;
    private String description;
    private String instruction;
    private String web;
    private String contact;
    private ArrayList<CapParameter> parameter;
    private ArrayList<CapAlertArea> area;

    public ArrayList<CapAlertResource> getResource() {
        return resource;
    }

    public void setResource(ArrayList<CapAlertResource> resource) {
        this.resource = resource;
    }

    private ArrayList<CapAlertResource> resource;
}
