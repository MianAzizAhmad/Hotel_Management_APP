package com.example.smdproject;

import android.net.Uri;
import android.os.Parcelable;

import java.io.Serializable;

public class Room implements Serializable{
    public String ID;
    public Boolean AC;
    public Boolean WiFi;
    public Boolean Status;
    public String Dimensions;
    public String Price;
    public String Hostel_ID;
    public String Location;
    public String Ratings;
    public String totalresidents;
    public String imageUrl;

    Room(Boolean ac,Boolean net,String ID, Boolean stat,String dimensions, String price,String Url, String Rates, String loc, String hostel)
    {
        this.ID = ID;
        AC = ac;
        WiFi = net;
        Status = stat;
        this.Dimensions = dimensions;
        this.Price = price;
        this.imageUrl = Url;
        this.Location = loc;
        this.Ratings = Rates;
        this.Hostel_ID = hostel;
        this.totalresidents = "0";
    }

    Room()
    { }

    String getID(){
        return this.ID;
    }
    void setImage(String image){
        this.imageUrl = image;
    }

    String getImage(){
        return this.imageUrl;
    }

    String getPrice(){
        return this.Price;
    }

    String getDimensions(){
        return this.Dimensions;
    }

    Boolean getAC(){
        return this.AC;
    }

    Boolean getWiFi(){
        return this.WiFi;
    }

    Boolean getStatus(){
        return this.Status;
    }
}
