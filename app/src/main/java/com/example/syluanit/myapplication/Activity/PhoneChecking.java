package com.example.syluanit.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syluanit.myapplication.Adapter.Ticket_Info_Adapter;
import com.example.syluanit.myapplication.Model.TicketInfo;
import com.example.syluanit.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneChecking extends AppCompatActivity {

    Button btn_phoneChecking;
    TextView tv_phoneChecking;
    String url = "http://192.168.43.218/busmanager/public/checkphoneAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_checking);

        btn_phoneChecking = (Button) findViewById(R.id.btn_phonechecking);
        tv_phoneChecking = (TextView) findViewById(R.id.phone_checking);

        Intent intent = getIntent();
        final String seatId = intent.getStringExtra("seatId");
        final int position = intent.getIntExtra("position", 0);

        btn_phoneChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_phoneChecking.getText().toString().equals("")){
                    Toast.makeText(PhoneChecking.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
                } else
                if (!detect_number(tv_phoneChecking.getText().toString())) {
                    Toast.makeText(PhoneChecking.this, "Vui lòng nhập đúng số điện thoại", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendData(url, seatId, position);
                }
            }
        });
    }

    private void sendData(String url, final String seatId, final int position){

        final RequestQueue requestQueue = Volley.newRequestQueue(PhoneChecking.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AAA", "onResponse: yeahyeah" + response.toString());
//
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("kq");
                            if( result.equals("1")){
                                final Dialog dialog = new Dialog(PhoneChecking.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_outday);
                                TextView tv_annoucement = dialog.findViewById(R.id.tv_OK_outday);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                TextView content = dialog.findViewById(R.id.tv_content);
                                content.setText("Cập nhật vé thành công");
                                tv_annoucement.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        So_Do_Xe_Activity.gheNgoiArrayList.get(position).setTrangThai(1);
                                        So_Do_Xe_Activity.adapter.notifyDataSetChanged();
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                            else if (result.equals("0")){
                                final Dialog dialog = new Dialog(PhoneChecking.this);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.dialog_outday);
                                TextView tv_annoucement = dialog.findViewById(R.id.tv_OK_outday);
                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                TextView content = dialog.findViewById(R.id.tv_content);
                                content.setText("Cập nhật vé thất bại");
                                tv_annoucement.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });
                                dialog.show();
                            }
                            else if (result.equals("2")){
                                Intent intent = new Intent(PhoneChecking.this, EditTicketForm.class);
                                intent.putExtra("phone",tv_phoneChecking.getText().toString());
                                intent.putExtra("seatId", seatId);
                                intent.putExtra("position", position);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PhoneChecking.this, "Vui lòng kiểm tra kết nối sau đó thử lại!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idve", seatId);
                params.put("phone", tv_phoneChecking.getText().toString());
                Log.d("AAA", "getParams: OK!!!");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public Boolean detect_number (String number) {
        number  = number.replace("\"", "");
        number = number.replace("-","");
        number = number.trim();
        number = number.replaceAll("\\.","");
        // $number is not a phone number
        if (!number.matches("(0[3578]|09)[0-9]{8}"))
            return false;

        // if not found, return false
        return true;
    }
}
