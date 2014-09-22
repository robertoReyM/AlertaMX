package com.smartplace.alerta.admin;

import java.util.ArrayList;

/**
 * Created by Roberto on 09/09/2014.
 */
public class AdminInfo {

    public ArrayList<CapInfo> getCaps() {
        return caps;
    }

    public void setCaps(ArrayList<CapInfo> caps) {
        this.caps = caps;
    }

    private ArrayList<CapInfo> caps;

    public ArrayList<AtlasInfo> getAtlas() {
        return atlas;
    }

    public void setAtlas(ArrayList<AtlasInfo> atlas) {
        this.atlas = atlas;
    }

    private ArrayList<AtlasInfo> atlas;
}
