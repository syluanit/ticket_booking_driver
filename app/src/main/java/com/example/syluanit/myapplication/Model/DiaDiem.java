package com.example.syluanit.myapplication.Model;

public class DiaDiem {
    private String place;
    private int icon;

    public DiaDiem(String place, int icon) {
        this.place = place;
        this.icon = icon;
    }

    public DiaDiem(String place) {
        this.place = place;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}

