package com.minhtam.petsworld.Class;

import com.google.gson.Gson;

/**
 * Created by st on 6/21/2017.
 */

public class Report {
    private int id,userid,petid,typepost;
    private String msg,datecreated;

    public Report() {
    }

    public Report(int id, int userid, int petid, int typepost, String msg,String datecreated) {
        this.id = id;
        this.userid = userid;
        this.petid = petid;
        this.typepost = typepost;
        this.msg = msg;
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

    public int getTypepost() {
        return typepost;
    }

    public void setTypepost(int typepost) {
        this.typepost = typepost;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public void setDatecreated(String datecreated) {
        this.datecreated = datecreated;
    }

    @Override
    public String toString() {
        return "id=" + id +
                "\n userid=" + userid +
                "\n petid=" + petid +
                "\n typepost=" + typepost +
                "\n msg='" + msg + '\'' +
                "\n datecreated='" + datecreated;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
