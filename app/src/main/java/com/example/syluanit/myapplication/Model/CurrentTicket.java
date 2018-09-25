package com.example.syluanit.myapplication.Model;

public class CurrentTicket {
    private String startDestination;
    private String endDestination;
    private String day;
    private String timeDep;
    private String timeArr;
    private String seat;
    private String price;
    private int numSeat;
    private int typeSeat;

    public String getStartDestination() {
        return startDestination;
    }

    public void setStartDestination(String startDestination) {
        this.startDestination = startDestination;
    }

    public int getTypeSeat() {
        return typeSeat;
    }

    public void setTypeSeat(int typeSeat) {
        this.typeSeat = typeSeat;
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

    public String getSeat() {
        return seat;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getNumSeat() {
        return numSeat;
    }

    public void setNumSeat(int numSeat) {
        this.numSeat = numSeat;
    }
}
