package com.example.syluanit.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.syluanit.myapplication.Adapter.So_Do_Xe_Adapter;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class Fragment_Ghe_Ngoi extends Fragment {
    View view;
    public ArrayList<GheNgoi> gheNgoiArrayList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.hang_ghe_ngoi, container, false);

        Button btn_back = view.findViewById(R.id.btn_back);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_GheNgoi);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(view.getContext(), 5);
        recyclerView.setLayoutManager(layoutManager);

        gheNgoiArrayList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            if (i < 25 && (i % 5) % 2 != 0) {
                gheNgoiArrayList.add(new GheNgoi(0, "" + i));
            } else {
                gheNgoiArrayList.add(new GheNgoi(R.drawable.front_bus, "" + i,0));
            }
        }

        So_Do_Xe_Adapter so_do_xe_adapter = new So_Do_Xe_Adapter(view.getContext(),gheNgoiArrayList);
        recyclerView.setAdapter(so_do_xe_adapter);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment_Ghe_Ngoi fragmentGheNgoi = (Fragment_Ghe_Ngoi) getFragmentManager().findFragmentByTag("ghe_ngoi");

                if (fragmentGheNgoi != null){
                    fragmentTransaction.remove(fragmentGheNgoi);
                    fragmentTransaction.commit();
                }
                else {
                    Toast.makeText(getContext(),"Không tôn tại ghe ngoi", Toast.LENGTH_SHORT);
                }
            }
        });

        return view;
    }
}
