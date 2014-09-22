package com.smartplace.alerta.family.members;

import java.util.ArrayList;

/**
 * Created by Roberto on 19/09/2014.
 */
public class FamilyInfo {
    public ArrayList<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(ArrayList<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    private ArrayList<FamilyMember> familyMembers;
}
