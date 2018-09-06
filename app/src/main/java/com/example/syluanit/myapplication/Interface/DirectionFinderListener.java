package com.example.syluanit.myapplication.Interface;

import com.example.syluanit.myapplication.Model.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
