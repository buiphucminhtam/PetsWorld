package com.minhtam.petsworld.Class;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class Photo {
    private int id,userid,petid;
    private String url,dateuploaded;

    public Photo() {
        super();
    }

    public Photo(int id, int userid, int petid, String url, String dateuploaded) {
        this.id = id;
        this.userid = userid;
        this.petid = petid;
        this.url = url;
        this.dateuploaded = dateuploaded;
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
