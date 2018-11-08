package com.example.syluanit.myapplication.Model;

public class RouteTicket {
    private String id;
    private String startDestination;
    private String endDestination;
    private String day;
    private String timeDep;
    private String timeArr;
    private String price;
    private String seat;

    public RouteTicket(String id, String startDestination, String endDestination, String day, String timeDep, String timeArr, String price, String seat) {
        this.id = id;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.day = day;
        this.timeDep = timeDep;
        this.timeArr = timeArr;
        this.price = price;
        this.seat = seat;
    }

    public RouteTicket(String startDestination, String endDestination, String day, String timeDep, String timeArr, String price, String seat) {
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.day = day;
        this.timeDep = timeDep;
        this.timeArr = timeArr;
        this.price = price;
        this.seat = seat;
    }

    public RouteTicket(String startDestination, String endDestination, String day, String timeDep, String timeArr, String price) {
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.day = day;
        this.timeDep = timeDep;
        this.timeArr = timeArr;
        this.price = price;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RouteTicket(String timeDep, String timeArr, String price, String id) {
        this.timeDep = timeDep;
        this.timeArr = timeArr;
        this.price = price;
        this.id = id;
    }

    public String getStartDestination() {
        return startDestination;
    }

    public void setStartDestination(String startDestination) {
        this.startDestination = startDestination;
    }



    public String getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(String endDestination) {
        this.endDestination = endDestination;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTimeDep() {
        return timeDep;
    }

    public void setTimeDep(String timeDep) {
        this.timeDep = timeDep;
    }

    public String getTimeArr() {
        return timeArr;
    }

    public void setTimeArr(String timeArr) {
        this.timeArr = timeArr;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }
}
