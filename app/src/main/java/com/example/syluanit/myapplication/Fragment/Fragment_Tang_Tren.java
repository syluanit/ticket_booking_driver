package com.example.syluanit.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.syluanit.myapplication.Activity.So_Do_Xe_Activity;
import com.example.syluanit.myapplication.Adapter.So_Do_Xe_Adapter;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Fragment_Tang_Tren extends Fragment {

    View view;
    public static ArrayList<GheNgoi> gheNgoiArrayList;
    public  static So_Do_Xe_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.giuong_nam, container, false);

        gheNgoiArrayList = new ArrayList<>();

        SetSeatPosition();

        adapter = new So_Do_Xe_Adapter(view.getContext(), gheNgoiArrayList);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_GiuongNam);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public void SetSeatPosition(){
        if (So_Do_Xe_Activity.TicketMap != null) {
            try {
                JSONObject jsonObject = new JSONObject(So_Do_Xe_Activity.TicketMap);
                JSONArray jsonArray = jsonObject.getJSONArray("ve");
                JSONArray jsonArraySoDo = jsonObject.getJSONArray("sodo");
                JSONObject jsonObject1 = (JSONObject) jsonArraySoDo.get(0);
                String s = jsonObject1.getString("Sơ_đồ");
                Log.d("AAA", "SetSeatPosition: " + s);
                String[] sodo = s.substring(35, s.length()).split("(?!^)");
                int j = 20;

                for (int i = 0; i < sodo.length; i++) {
//                    if (i == 0) {
//                        gheNgoiArrayList.add(new GheNgoi(null, R.mipmap.ic_driver, 0, "", 0));
//                    } else
                        if (sodo[i].equals("1")) {
                        JSONObject jsonObjectTicket = (JSONObject) jsonArray.get(j);
                        String seatId = jsonObjectTicket.getString("Mã");
                        if (jsonObjectTicket.getString("Trạng_thái").equals("1")) {
                            gheNgoiArrayList.add(new GheNgoi(seatId, R.drawable.custom_seat, 0, jsonObjectTicket.getString("Vị_trí_ghế"), 1));
                            j++;
                        } else if (jsonObjectTicket.getString("Trạng_thái").equals("0")) {

//                            //ghế trống
//                            if (yeah == 0) {
                            gheNgoiArrayList.add(new GheNgoi(seatId, R.drawable.custom_seat, 0, jsonObjectTicket.getString("Vị_trí_ghế"), 0));
                            j++;
//                            }
                        }
                        else {
                            gheNgoiArrayList.add(new GheNgoi(seatId, R.drawable.custom_seat, 0, jsonObjectTicket.getString("Vị_trí_ghế"), 1));
                            j++;
                        }
                    } else {
                        gheNgoiArrayList.add(new GheNgoi(0, ""));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
