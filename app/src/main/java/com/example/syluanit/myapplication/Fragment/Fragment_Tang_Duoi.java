package com.example.syluanit.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.syluanit.myapplication.Adapter.So_Do_Xe_Adapter;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class Fragment_Tang_Duoi extends Fragment {
    View view;
    private ArrayList<GheNgoi> gheNgoiArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.giuong_nam, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_GiuongNam);
        gheNgoiArrayList = new ArrayList<>();
        int j = 1;
        for (int i = 0; i < 30; i++) {
            if (i < 25 && (i % 5) % 2 != 0) {
                gheNgoiArrayList.add(new GheNgoi(0, "" ));
            } else {
                gheNgoiArrayList.add(new GheNgoi(R.drawable.custom_seat, "A" + j));
                j++;
            }
        }
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(layoutManager);
        So_Do_Xe_Adapter so_do_xe_adapter = new So_Do_Xe_Adapter(getContext(),gheNgoiArrayList);
        recyclerView.setAdapter(so_do_xe_adapter);

        return view;
    }
}
