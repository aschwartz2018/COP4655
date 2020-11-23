package com.alexschwartz.finalproject;

import java.io.Serializable;

public class YelpData implements Serializable {

    //Compares each variable to the variables of "y" object
    public boolean equals(YelpData y) {
        if(!this.name.equals(y.name)) {
            return false;
        }
        if(this.rating != y.rating) {
            return false;
        }
        if(!this.address.equals(y.address)) {
            return false;
        }
        if(!this.phone_num.equals(y.phone_num)) {
            return false;
        }
        if(this.closed != y.closed) {
            return false;
        }
        if(this.lat != y.lat) {
            return false;
        }
        if(this.lon != y.lon) {
            return false;
        }
        return true;
    }

    //Stores all the public variables needed for Yelp API
    public String name;
    public double rating;
    public String address;
    public String phone_num;
    public boolean closed;
    public double lat;
    public double lon;
}
