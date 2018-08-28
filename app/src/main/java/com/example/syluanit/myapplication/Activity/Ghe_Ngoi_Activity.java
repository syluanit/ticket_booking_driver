package com.example.syluanit.myapplication.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.syluanit.myapplication.Adapter.So_Do_Xe_Adapter;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class Ghe_Ngoi_Activity extends AppCompatActivity {

    public ArrayList<GheNgoi> gheNgoiArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghe__ngoi_);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_GheNgoi);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(layoutManager);

        gheNgoiArrayList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i < 25 && (i % 5) % 2 != 0) {
                gheNgoiArrayList.add(new GheNgoi(0, "" + i));
            } else {
                gheNgoiArrayList.add(new GheNgoi(R.drawable.front_bus, "" + i,0));
            }
        }

        So_Do_Xe_Adapter so_do_xe_adapter = new So_Do_Xe_Adapter(this,gheNgoiArrayList);
        recyclerView.setAdapter(so_do_xe_adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ghe_Ngoi_Activity.super.onBackPressed();
            }
        });

    }
}
