package com.example.syluanit.myapplication.Model;

public class Tracking    {
    private String email,  uid, lat, lng, solveUser;
    private int sos;

    public Tracking() {
    }

//    public Tracking(String email, String uid, String lat, String lng) {
//        this.email = email;
//        this.uid = uid;
//        this.lat = lat;
//        this.lng = lng;
//    }

    public Tracking(String email, String uid, String lat, String lng, int sos) {
        this.email = email;
        this.uid = uid;
        this.lat = lat;
        this.lng = lng;
        this.sos = sos;
    }

    public String getSolveUser() {
        return solveUser;
    }

    public void setSolveUser(String solveUser) {
        this.solveUser = solveUser;
    }

    public int getSos() {
        return sos;
    }

    public void setSos(int sos) {
        this.sos = sos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}
