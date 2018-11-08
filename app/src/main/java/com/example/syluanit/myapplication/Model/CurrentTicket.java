package com.example.syluanit.myapplication.Model;

public class CurrentTicket {
    private String Id;
    private String startDestination;
    private String endDestination;
    private String day;
    private String timeDep;
    private String timeArr;
    private String price;
    private String seat; // position
    private String seatId;
    private int numSeat;
    private int typeSeat;

    public CurrentTicket(String id, String startDestination, String endDestination, String day, String timeDep, String timeArr, String price, String seat, int numSeat, int typeSeat) {
        Id = id;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.day = day;
        this.timeDep = timeDep;
        this.timeArr = timeArr;
        this.price = price;
        this.seat = seat;
        this.numSeat = numSeat;
        this.typeSeat = typeSeat;
    }

    public CurrentTicket(String id, String startDestination, String endDestination, String day, String timeDep, String timeArr, String price, String seat, String seatId, int numSeat, int typeSeat) {
        Id = id;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.day = day;
        this.timeDep = timeDep;
        this.timeArr = timeArr;
        this.price = price;
        this.seat = seat;
        this.seatId = seatId;
        this.numSeat = numSeat;
        this.typeSeat = typeSeat;
    }

    public CurrentTicket() {

    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

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
