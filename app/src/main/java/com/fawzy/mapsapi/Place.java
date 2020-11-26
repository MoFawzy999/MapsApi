package com.fawzy.mapsapi;

public class Place {

    private String name , longitude , latitude ;
    private int id ;

    public Place() {
    }

    public Place(String name, String longitude, String latitude) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Place(String name, String longitude, String latitude, int id) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
