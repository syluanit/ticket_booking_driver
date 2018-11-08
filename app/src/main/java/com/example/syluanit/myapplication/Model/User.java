package com.example.syluanit.myapplication.Model;

//class use in Drive Map SOS, use store status user
public class User {
    private String email, status, carNumber;

    public User() {
    }

    public User(String email, String status, String carNumber) {
        this.email = email;
        this.status = status;
        this.carNumber = carNumber;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

