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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syluanit.myapplication.Activity.Chon_Dia_Diem;
import com.example.syluanit.myapplication.Activity.Home;
import com.example.syluanit.myapplication.Activity.RouteActivity;
import com.example.syluanit.myapplication.Activity.So_Do_Xe_Activity;
import com.example.syluanit.myapplication.Model.CONSTANT;
import com.example.syluanit.myapplication.Model.CurrentTicket;
import com.example.syluanit.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Fragment_So_Do_Xe extends Fragment {

    View view;
    EditText et_from, et_to, pickDay, et_ma_chuyen;
    Button btn_ticketSearch;
    private RadioGroup radioGroupTypeSeat;
    private String typeSeat;
//    String url = "http://192.168.43.218/busmanager/public/chuyenxeAndroid";
//    String urlMa = "http://192.168.43.218/busmanager/public/chonveAndroid";
    String url, urlMa;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_so_do_xe, container, false);

        btn_ticketSearch = view.findViewById(R.id.btn_ticketSearch);
        et_from = (EditText) view.findViewById(R.id.et_from);
        et_to = (EditText) view.findViewById(R.id.et_to);
        pickDay = (EditText) view.findViewById(R.id.pickDay);
        et_ma_chuyen = (EditText) view.findViewById(R.id.et_ma_chuyen_xe);

        btn_ticketSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String from = et_from.getText().toString();
                String to = et_to.getText().toString();
                String date = pickDay.getText().toString();
                String routeId = et_ma_chuyen.getText().toString();

                Home.currentTicket.setStartDestination(from);
                Home.currentTicket.setEndDestination(to);
                Home.currentTicket.setDay(date);

                // TODO Checking infomation
                if (!routeId.equals("")) {
                    String ip = getResources().getString(R.string.ip);
                    String address = getResources().getString(R.string.address);
                    urlMa = ip + address + "/chonveAndroid";
                    sendMaChuyenXe(urlMa);
                }else{
                if (from.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập thông tin!", Toast.LENGTH_LONG).show();
                } else if (to.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng chọn điểm đến!", Toast.LENGTH_LONG).show();
                } else if (date.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng chọn ngày đi!", Toast.LENGTH_LONG).show();
                } else {
                    String ip = getResources().getString(R.string.ip);
                    String address = getResources().getString(R.string.address);
                    url = ip + address + "/chuyenxeAndroid";
                    sendUserData(url);
                }
                }
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
        Home.currentTicket.setTypeSeat(1);
        typeSeat = "1";
        radioGroupTypeSeat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton:
                        //ghe
                        Home.currentTicket.setTypeSeat(0);
                        typeSeat = "0";
                        break;
                    case R.id.radioButton2:
                        Home.currentTicket.setTypeSeat(1);
                        //giuong
                        typeSeat = "1";
                        break;
                }
            }
        });
        return view;
    }

    private void sendUserData(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("AAA", "onResponse: " + response.toString());
                        Bundle ticket = new Bundle();
                        ticket.putString("ticketJson",response);
                        Intent intent = new Intent(getActivity(), RouteActivity.class);
                        intent.putExtras(ticket);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Vui lòng kiểm tra kết nối mạng hoặc thử lại!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("Noidi", et_from.getText().toString().trim());
                params.put("Noiden", et_to.getText().toString().trim());
                params.put("Loaighe", typeSeat);
                params.put("Ngaydi",pickDay.getText().toString().trim());
                Log.d("AAA", "getParams: OK!!!");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void pickDay(){
        final Calendar calendar = Calendar.getInstance();
        int ngay, thang, nam = 0;
        if (pickDay.getText().toString().matches("")) {
            ngay = calendar.get(Calendar.DATE);
            thang = calendar.get(Calendar.MONTH);
            nam = calendar.get(Calendar.YEAR);
        }
        else {
            String s = pickDay.getText().toString();
            String [] arrayString = s.split("-");
            ngay = Integer.parseInt(arrayString[0]);
            thang = Integer.parseInt(arrayString[1]) - 1;
            nam = Integer.parseInt(arrayString[2]);
        }

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month,dayOfMonth);
                // TODO Time format and set picked day to the edittext

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                pickDay.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang , ngay);

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

    private void sendMaChuyenXe(String url){
        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AAA", "onResponse: " + response.toString());

                        Intent i = new Intent(getActivity(), So_Do_Xe_Activity.class);
                        i.putExtra("ticketMap", response);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "Vui lòng kiểm tra kết nối mạng hoặc thử lại!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
//                params.put("X-CSRF-Token", accessToken);
                params.put("ID", et_ma_chuyen.getText().toString().trim());
                Log.d("AAA", "getParams: OK!!!");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
