package com.minhtam.petsworld.Class;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class FindPetLost {
    private int id,userid,petid;
    private String description,lostlocation,datecreated;

    public FindPetLost() {
        super();
    }

    public FindPetLost(int id, int userid, int petid, String description, String lostlocation, String datecreated) {
        this.id = id;
        this.userid = userid;
        this.petid = petid;
        this.description = description;
        this.lostlocation = lostlocation;
        this.datecreated = datecreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getPetid() {
        return petid;
    }

    public void setPetid(int petid) {
        this.petid = petid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLostlocation() {
        return lostlocation;
    }

    public void setLostlocation(String lostlocation) {
        this.lostlocation = lostlocation;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
