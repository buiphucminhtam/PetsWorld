package com.minhtam.petsworld.Class;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class PetType {
    private int id;
    private String typename,datecreated;

    public PetType() {
        super();
    }

    public PetType(int id, String name,String namechild, String datecreated) {
        this.id = id;
        this.typename = name;
        this.datecreated = datecreated;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
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
