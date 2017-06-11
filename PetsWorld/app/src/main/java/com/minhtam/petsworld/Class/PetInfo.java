package com.minhtam.petsworld.Class;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class PetInfo implements Parcelable{
    private int id,userid,typeid,vaccine;
    private String name, vaccinedate;

    public PetInfo(int id, int userid, int typeid, int vacine, String name, String vaccinedate) {
        this.id = id;
        this.userid = userid;
        this.typeid = typeid;
        this.vaccine = vacine;
        this.name = name;
        this.vaccinedate = vaccinedate;
    }

    public PetInfo() {
        super();
    }

    protected PetInfo(Parcel in) {
        id = in.readInt();
        userid = in.readInt();
        typeid = in.readInt();
        vaccine = in.readInt();
        name = in.readString();
        vaccinedate = in.readString();
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
        return vaccine;
    }

    public void setVacine(int vacine) {
        this.vaccine = vacine;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVaccinedate() {
        return vaccinedate;
    }

    public void setVaccinedate(String vaccinedate) {
        this.vaccinedate = vaccinedate;
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
        dest.writeInt(vaccine);
        dest.writeString(name);
        dest.writeString(vaccinedate);
    }
}
