package com.minhtam.petsworld.Class;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class PetInfo implements Parcelable{
    private int id,userid,typeid,vacines;
    private String name, datecreated;

    public PetInfo(int id, int userid, int typeid, int vacine, String name, String datecreated) {
        this.id = id;
        this.userid = userid;
        this.typeid = typeid;
        this.vacines = vacine;
        this.name = name;
        this.datecreated = datecreated;
    }

    public PetInfo() {
        super();
    }

    protected PetInfo(Parcel in) {
        id = in.readInt();
        userid = in.readInt();
        typeid = in.readInt();
        vacines = in.readInt();
        name = in.readString();
        datecreated = in.readString();
    }

    public static final Creator<PetInfo> CREATOR = new Creator<PetInfo>() {
        @Override
        public PetInfo createFromParcel(Parcel in) {
            return new PetInfo(in);
        }

        @Override
        public PetInfo[] newArray(int size) {
            return new PetInfo[size];
        }
    };

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
        return vacines;
    }

    public void setVacine(int vacine) {
        this.vacines = vacine;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userid);
        dest.writeInt(typeid);
        dest.writeInt(vacines);
        dest.writeString(name);
        dest.writeString(datecreated);
    }
}
