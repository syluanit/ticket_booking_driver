package com.example.syluanit.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.syluanit.myapplication.Adapter.So_Do_Xe_Adapter;
import com.example.syluanit.myapplication.Adapter.ViewPagerAdapter;
import com.example.syluanit.myapplication.Fragment.Fragment_Tang_Duoi;
import com.example.syluanit.myapplication.Fragment.Fragment_Tang_Tren;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class So_Do_Xe_Activity extends AppCompatActivity {

    public static ArrayList<GheNgoi> gheNgoiArrayList;
    LinearLayout giuong, ghe;
    ImageView back;
    public static So_Do_Xe_Adapter adapter;
    public static String TicketMap = "";
    TextView noRoute;
    RelativeLayout rl_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghe__ngoi_);

        Button btn_back = (Button) findViewById(R.id.btn_back);
        giuong = (LinearLayout) findViewById(R.id.layout_giuong_nam);
        ghe = (LinearLayout ) findViewById(R.id.layout_ghe_ngoi);
        back = (ImageView) findViewById(R.id.so_do_back_pressed);
        noRoute = (TextView) findViewById(R.id.noRoute);
        rl_note = (RelativeLayout) findViewById(R.id.rl_note);

        Intent intent = getIntent();
        TicketMap  = intent.getStringExtra("ticketMap");
        gheNgoiArrayList = new ArrayList<>();

        try {
            JSONObject jsonObjectT = new JSONObject(TicketMap);
            JSONArray jsonArrayT = jsonObjectT.getJSONArray("chonve");
            if (jsonArrayT.toString().equals("[]")){
                noRoute.setVisibility(View.VISIBLE);
                giuong.setVisibility(View.GONE);
                ghe.setVisibility(View.GONE);
                rl_note.setVisibility(View.GONE);
//                Home.currentTicket.setStartDestination(from);
//                Home.currentTicket.setEndDestination(to);
//                Home.currentTicket.setDay(date);
            }
            else {
                // set value to Home.currentTicket here
                JSONObject Ticket = (JSONObject) jsonArrayT.get(0);
                Home.currentTicket.setTypeSeat(Integer.parseInt(Ticket.getString("Loại_ghế")));
                noRoute.setVisibility(View.GONE);
                if (Home.currentTicket.getTypeSeat() == 1 ) {
                    // Giuong nam
                    giuong.setVisibility(View.VISIBLE);
                    ghe.setVisibility(View.GONE);

                    TabLayout tabLayout =  findViewById(R.id.myTabLayout);
                    ViewPager viewPager = findViewById(R.id.myViewPager);
                    ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
                    viewPagerAdapter.addFragment(new Fragment_Tang_Duoi(), "Tầng Dưới");
                    viewPagerAdapter.addFragment(new Fragment_Tang_Tren(), "Tầng Trên");
                    viewPager.setAdapter(viewPagerAdapter);
                    tabLayout.setupWithViewPager(viewPager);
                }
                else {
                    giuong.setVisibility(View.GONE);
                    ghe.setVisibility(View.VISIBLE);
                    // Ghe Ngoi
                    try {
                        JSONObject jsonObject = new JSONObject(TicketMap);
                        JSONArray jsonArray = jsonObject.getJSONArray("ve");
                        JSONArray jsonArraySoDo = jsonObject.getJSONArray("sodo");
                        JSONObject jsonObject1 = (JSONObject) jsonArraySoDo.get(0);
                        String s = jsonObject1.getString("Sơ_đồ");

                        String [] sodo = s.split("(?!^)");
                        int j = 0;
                        for (int i = 0; i < sodo.length; i++) {
                            if (i == 0){
                                gheNgoiArrayList.add(new GheNgoi(null,R.mipmap.ic_driver, 0, "", 0));
                            }
                            else
                            if (sodo[i].equals("1")){
                                JSONObject jsonObjectTicket = (JSONObject) jsonArray.get(j);
                                String seatId = jsonObjectTicket.getString("Mã");
                                if (jsonObjectTicket.getString("Trạng_thái").equals("1")) {
                                    gheNgoiArrayList.add(new GheNgoi(seatId, R.drawable.custom_seat, 0, jsonObjectTicket.getString("Vị_trí_ghế"), 1));
                                    j++;
                                }
                                else if (jsonObjectTicket.getString("Trạng_thái").equals("0")) {
                                    gheNgoiArrayList.add(new GheNgoi(seatId, R.drawable.custom_seat, 0, jsonObjectTicket.getString("Vị_trí_ghế"), 0));
                                    j++;
                                }
                                else {
                                    gheNgoiArrayList.add(new GheNgoi(seatId, R.drawable.custom_seat, 0, jsonObjectTicket.getString("Vị_trí_ghế"), 1));
                                    j++;
                                }
                            }
                            else {
                                gheNgoiArrayList.add(new GheNgoi(0, "" ));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapter = new So_Do_Xe_Adapter(this,  gheNgoiArrayList);
                    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_GheNgoi);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager layoutManager = new GridLayoutManager(this, 6);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                So_Do_Xe_Activity.super.onBackPressed();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) So_Do_Xe_Activity.this).onBackPressed();
            }
        });
    }
}
