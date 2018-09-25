package com.example.syluanit.myapplication.Activity;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.syluanit.myapplication.Adapter.So_Do_Xe_Adapter;
import com.example.syluanit.myapplication.Adapter.ViewPagerAdapter;
import com.example.syluanit.myapplication.Fragment.Fragment_So_Do_Xe;
import com.example.syluanit.myapplication.Fragment.Fragment_Tang_Duoi;
import com.example.syluanit.myapplication.Fragment.Fragment_Tang_Tren;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class Ghe_Ngoi_Activity extends AppCompatActivity {

    public ArrayList<GheNgoi> gheNgoiArrayList;
    LinearLayout giuong, ghe;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghe__ngoi_);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_GheNgoi);
        giuong = (LinearLayout) findViewById(R.id.layout_giuong_nam);
        ghe = (LinearLayout ) findViewById(R.id.layout_ghe_ngoi);
        back = (ImageView) findViewById(R.id.so_do_back_pressed);

        if (Fragment_So_Do_Xe.currentTicket.getTypeSeat() == 1 ) {
            // Giuong nam
            giuong.setVisibility(View.VISIBLE);
            ghe.setVisibility(View.GONE);
            TabLayout tabLayout =  findViewById(R.id.myTabLayout);
            ViewPager viewPager = findViewById(R.id.myViewPager);
            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPagerAdapter.addFragment(new Fragment_Tang_Tren(), "Tầng Trên");
            viewPagerAdapter.addFragment(new Fragment_Tang_Duoi(), "Tầng Dưới");
            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        else {
            giuong.setVisibility(View.GONE);
            ghe.setVisibility(View.VISIBLE);
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
            LinearLayoutManager layoutManager = new GridLayoutManager(this, 5);
            recyclerView.setLayoutManager(layoutManager);

            So_Do_Xe_Adapter so_do_xe_adapter = new So_Do_Xe_Adapter(this,gheNgoiArrayList);

            recyclerView.setAdapter(so_do_xe_adapter);
        }



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ghe_Ngoi_Activity.super.onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) Ghe_Ngoi_Activity.this).onBackPressed();
            }
        });

    }
}
