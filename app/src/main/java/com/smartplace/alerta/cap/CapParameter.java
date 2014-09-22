package com.smartplace.alerta.cap;

/**
 * Created by Roberto on 12/07/2014.
 */
public class CapParameter {
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    private String value;
    private String valueName;
}
