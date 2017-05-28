package com.minhtam.petsworld.Class;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by st on 5/9/2017.
 */

public class FindOwner implements Parcelable {
    private int id,userid,petid;
    private String description,requirement,datecreated;

    public FindOwner() {
        super();
    }

    public FindOwner(int id, int userid, int petid, String description, String requirement, String datecreated) {
        this.id = id;
        this.userid = userid;
        this.petid = petid;
        this.description = description;
        this.requirement = requirement;
        this.datecreated = datecreated;
    }

    protected FindOwner(Parcel in) {
        id = in.readInt();
        userid = in.readInt();
        petid = in.readInt();
        description = in.readString();
        requirement = in.readString();
        datecreated = in.readString();
    }

    public static final Creator<FindOwner> CREATOR = new Creator<FindOwner>() {
        @Override
        public FindOwner createFromParcel(Parcel in) {
            return new FindOwner(in);
        }

        @Override
        public FindOwner[] newArray(int size) {
            return new FindOwner[size];
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

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
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
        dest.writeInt(petid);
        dest.writeString(description);
        dest.writeString(requirement);
        dest.writeString(datecreated);
    }
}
