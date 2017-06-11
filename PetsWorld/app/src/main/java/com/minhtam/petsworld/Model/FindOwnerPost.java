package com.minhtam.petsworld.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.minhtam.petsworld.Class.Photo;

import java.util.ArrayList;

/**
 * Created by st on 5/24/2017.
 */

public class FindOwnerPost implements Parcelable{
    private String id;
    private String fullname;
    private String datecreated;
    private String petname;
    private String typename;
    private String vaccine;
    private String petId;
    private String userId;
    private String description;
    private String requirement;
    private String vaccinedate;

    private ArrayList<Photo> listPhoto;

    public FindOwnerPost() {
        super();
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

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getVaccinedate() {
        return vaccinedate;
    }

    public void setVaccinedate(String vaccinedate) {
        this.vaccinedate = vaccinedate;
    }

    public ArrayList<Photo> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(ArrayList<Photo> listPhoto) {
        this.listPhoto = listPhoto;
    }

    public static Creator<FindOwnerPost> getCREATOR() {
        return CREATOR;
    }

    protected FindOwnerPost(Parcel in) {
        id = in.readString();
        fullname = in.readString();
        datecreated = in.readString();
        petname = in.readString();
        typename = in.readString();
        vaccine = in.readString();
        petId = in.readString();
        userId = in.readString();
        description = in.readString();
        requirement = in.readString();
        vaccinedate = in.readString();
        listPhoto = in.createTypedArrayList(Photo.CREATOR);
    }

    public static final Creator<FindOwnerPost> CREATOR = new Creator<FindOwnerPost>() {
        @Override
        public FindOwnerPost createFromParcel(Parcel in) {
            return new FindOwnerPost(in);
        }

        @Override
        public FindOwnerPost[] newArray(int size) {
            return new FindOwnerPost[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(fullname);
        dest.writeString(datecreated);
        dest.writeString(petname);
        dest.writeString(typename);
        dest.writeString(vaccine);
        dest.writeString(petId);
        dest.writeString(userId);
        dest.writeString(description);
        dest.writeString(requirement);
        dest.writeString(vaccinedate);
        dest.writeTypedList(listPhoto);
    }
}
