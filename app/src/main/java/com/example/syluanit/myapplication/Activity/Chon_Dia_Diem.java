package com.example.syluanit.myapplication.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.syluanit.myapplication.Adapter.Chon_Dia_Diem_Adapter;
import com.example.syluanit.myapplication.Model.DiaDiem;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class Chon_Dia_Diem extends AppCompatActivity {

    ListView listView;
    Chon_Dia_Diem_Adapter chon_dia_diem_adapter;
    ArrayList<DiaDiem> diaDiemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon__dia__diem);
        listView = (ListView) findViewById(R.id.lv_dia_diem);

        diaDiemArrayList = new ArrayList<>();
//        diaDiemArrayList.add(new DiaDiem("Binh Định",R.mipmap.ic_dia_diem));
//        diaDiemArrayList.add(new DiaDiem("Đa Nang",R.mipmap.ic_dia_diem));
//        diaDiemArrayList.add(new DiaDiem("Nha Trang",R.mipmap.ic_dia_diem));
//        diaDiemArrayList.add(new DiaDiem("Sai Gon",R.mipmap.ic_dia_diem));
//        diaDiemArrayList.add(new DiaDiem("Ha Noi",R.mipmap.ic_dia_diem));
        diaDiemArrayList.add(new DiaDiem("Binh Định"));
        diaDiemArrayList.add(new DiaDiem("HCM"));
        diaDiemArrayList.add(new DiaDiem("Nha Trang"));
        diaDiemArrayList.add(new DiaDiem("Phan Thiết"));
        diaDiemArrayList.add(new DiaDiem("Đa Nẵng"));

        chon_dia_diem_adapter = new Chon_Dia_Diem_Adapter(this, R.layout.dia_diem_item ,diaDiemArrayList);
        listView.setAdapter(chon_dia_diem_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String dia_diem = diaDiemArrayList.get(position).getPlace();
                Intent intent = new Intent();
                intent.putExtra("diadiem", dia_diem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
