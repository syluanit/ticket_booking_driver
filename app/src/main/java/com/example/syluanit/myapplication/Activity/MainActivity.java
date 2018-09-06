package com.example.syluanit.myapplication.Activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.syluanit.myapplication.Adapter.HomeViewPagerAdapter;
import com.example.syluanit.myapplication.Fragment.Fragment_Ban_Do;
import com.example.syluanit.myapplication.Fragment.Fragment_Duong_Di_Moi;
import com.example.syluanit.myapplication.Fragment.Fragment_So_Do_Xe;
import com.example.syluanit.myapplication.Fragment.Fragment_Thong_Tin;
import com.example.syluanit.myapplication.Model.CONSTANT;
import com.example.syluanit.myapplication.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MainActivity extends FragmentActivity{

    public TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragment(new Fragment_So_Do_Xe(), "Sơ Đồ Xe");
        homeViewPagerAdapter.addFragment(new Fragment_Ban_Do(), "Bản Đồ");
        homeViewPagerAdapter.addFragment(new Fragment_Duong_Di_Moi(), "Đường Đi Mới");
        homeViewPagerAdapter.addFragment(new Fragment_Thong_Tin(), "Thông Tin");
        viewPager.setAdapter(homeViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
