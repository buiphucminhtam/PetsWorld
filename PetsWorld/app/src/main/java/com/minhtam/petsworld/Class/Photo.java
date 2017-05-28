package com.minhtam.petsworld.Class;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class Photo {
    private String id,userid,petid;
    private String url,dateuploaded;

    public Photo() {
        super();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPetid() {
        return petid;
    }

    public void setPetid(String petid) {
        this.petid = petid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateuploaded() {
        return dateuploaded;
    }

    public void setDateuploaded(String dateuploaded) {
        this.dateuploaded = dateuploaded;
    }
    public String toJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
