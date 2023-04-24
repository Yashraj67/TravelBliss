
package com.example.version1;

public class TripData {

    public String img;
    public String title;
    public String rating;
    public String distance;
    public String descrip;
    public String fsq_id;

    TripData(String img , String title , String rating , String distance , String descrip , String fsq_id){
        this.img = img;
        this.descrip = descrip;
        this.distance = distance;
        this.rating = rating;
        this.title = title;
        this.fsq_id = fsq_id;
    }

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getRating() {
        return rating;
    }

    public String getDistance() {
        return distance;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getFsq_id() {return fsq_id;}

}
