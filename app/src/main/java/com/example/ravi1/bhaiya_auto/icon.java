package com.example.ravi1.bhaiya_auto;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by ravi1 on 3/23/2017.
 */

public class icon {
    String id;
    String lat;
    String lang;
    String init;

    public String getInit() {
        return init;
    }

    public void setInit(String init) {
        this.init = init;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getDr_lng() {
        return dr_lng;
    }

    public void setDr_lng(String dr_lng) {
        this.dr_lng = dr_lng;
    }

    public String getDr_lat() {
        return dr_lat;
    }

    public void setDr_lat(String dr_lat) {
        this.dr_lat = dr_lat;
    }

    public String getP_lng() {
        return p_lng;
    }

    public void setP_lng(String p_lng) {
        this.p_lng = p_lng;
    }

    public String getP_lat() {
        return p_lat;
    }

    public void setP_lat(String p_lat) {
        this.p_lat = p_lat;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    String type;
    String color;
    String passenger,dr_lng,dr_lat,p_lng,p_lat,driver;

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    Marker marker;
    String dest,price,size,status;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
