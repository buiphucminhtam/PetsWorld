package com.minhtam.petsworld.Class;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class PetInfo {
    private int id,userid,typeid,vacine;
    private String name, datecreated;

    public PetInfo(int id, int userid, int typeid, int vacine, String name, String datecreated) {
        this.id = id;
        this.userid = userid;
        this.typeid = typeid;
        this.vacine = vacine;
        this.name = name;
        this.datecreated = datecreated;
    }

    public PetInfo() {
        super();
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

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public int getVacine() {
        return vacine;
    }

    public void setVacine(int vacine) {
        this.vacine = vacine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
