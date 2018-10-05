package com.example.syluanit.myapplication.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.syluanit.myapplication.Activity.Chon_Dia_Diem;
import com.example.syluanit.myapplication.Activity.Ghe_Ngoi_Activity;
import com.example.syluanit.myapplication.Model.CONSTANT;
import com.example.syluanit.myapplication.Model.CurrentTicket;
import com.example.syluanit.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fragment_So_Do_Xe extends Fragment {

    View view;
    EditText et_from, et_to, pickDay;
    private RadioGroup radioGroupTypeSeat;
    public static CurrentTicket currentTicket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_so_do_xe, container, false);

        Button btn_ticketSearch = view.findViewById(R.id.btn_ticketSearch);
        et_from = (EditText) view.findViewById(R.id.et_from);
        et_to = (EditText) view.findViewById(R.id.et_to);
        pickDay = (EditText) view.findViewById(R.id.pickDay);

        btn_ticketSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent (getActivity(), Ghe_Ngoi_Activity.class);
                startActivity(intent);
            }
        });
        et_from.setFocusable(false);
        et_from.setClickable(true);
        et_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Chon_Dia_Diem.class);
                startActivityForResult(intent, CONSTANT.DIA_DIEM_ACTIVITY_REQUEST_CODE);
            }
        });
        et_to.setFocusable(false);
        et_to.setClickable(true);
        et_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Chon_Dia_Diem.class);
                startActivityForResult(intent, CONSTANT.DIA_DIEM_TO_ACTIVITY_REQUEST_CODE);
            }
        });
        pickDay.setFocusable(false);
        pickDay.setClickable(true);
        pickDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDay();
            }
        });

        radioGroupTypeSeat = (RadioGroup) view.findViewById(R.id.radioGroup) ;
        currentTicket = new CurrentTicket();
        currentTicket.setTypeSeat(1);
        radioGroupTypeSeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        currentTicket.setTypeSeat(0);
                        break;
                    case R.id.radioButton2:
                        currentTicket.setTypeSeat(1);
                        break;
                }
            }
        });

        return view;
    }

    private void pickDay() {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                pickDay.setText(format.format(calendar.getTime()));
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONSTANT.DIA_DIEM_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                String dia_diem = data.getStringExtra("diadiem");
                et_from.setText(dia_diem);
            }
        }
        else if (requestCode ==  CONSTANT.DIA_DIEM_TO_ACTIVITY_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK) {
                String dia_diem = data.getStringExtra("diadiem");
                et_to.setText(dia_diem);
            }
        }
    }



}
