package com.minhtam.petsworld.Model;

import com.minhtam.petsworld.Class.Photo;

import java.util.ArrayList;

/**
 * Created by st on 5/24/2017.
 */

public class FindOwnerPost {
    private String id;
    private String fullname;
    private String datecreated;
    private String petname;
    private String typename;
    private String vaccine;
    private String petId;

    private ArrayList<Photo> listPhoto;

    public FindOwnerPost() {
        super();
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    public String getPetname() {
        return petname;
    }

    public void setPetname(String petname) {
        this.petname = petname;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public ArrayList<Photo> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(ArrayList<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }
}
