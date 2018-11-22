package com.example.syluanit.myapplication.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syluanit.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditTicketForm extends AppCompatActivity {

    ImageView back;
    Button btn_edit;
    EditText realname, dob;
    String gender = "0";
    String url = "http://192.168.43.218/busmanager/public/dangkyveAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ticket_form);

        back = (ImageView) findViewById(R.id.back_pressed_addTicket);
        btn_edit = (Button) findViewById(R.id.btn_addTicket);
        realname = (EditText) findViewById(R.id.name_signup);

        dob = (EditText) findViewById(R.id.dob_signup);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) EditTicketForm.this).onBackPressed();
            }
        });

        dob.setFocusable(false);
        dob.setClickable(true);
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDay();
            }
        });

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroupSeat_signup) ;

        gender = "0";

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonSeatMen_signup:
                        gender = "1";
                        break;
                    case R.id.radioButtonWomen_signup:
                        gender = "0";
                        break;
                }
            }
        });

        Intent intent = getIntent();
        final String phone = intent.getStringExtra("phone");
        final String seatId = intent.getStringExtra("seatId");
        final int position = intent.getIntExtra("position", 0);

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = realname.getText().toString();
                String doB = dob.getText().toString();


                if (name.equals("") || doB.equals("") ) {
                    Toast.makeText(EditTicketForm.this, "Vui lòng nhập đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else if (!detect_name(name)) {
                    Toast.makeText(EditTicketForm.this, "Vui lòng nhập tên chỉ chứa các ký tự!", Toast.LENGTH_SHORT).show();
                }
                else {

                    sendUserData(url, phone, seatId, position);

                }
            }
        });
    }

    private void pickDay(){
        final Calendar calendar = Calendar.getInstance();
        int ngay, thang, nam = 0;
        if (dob.getText().toString().matches("")) {
            ngay = calendar.get(Calendar.DATE);
            thang = calendar.get(Calendar.MONTH);
            nam = calendar.get(Calendar.YEAR);
        }
        else {
            String s = dob.getText().toString();
            String [] arrayString = s.split("-");
            ngay = Integer.parseInt(arrayString[0]);
            thang = Integer.parseInt(arrayString[1]) - 1;
            nam = Integer.parseInt(arrayString[2]);
        }

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month,dayOfMonth);
                // TODO Time format and set picked day to the edittext

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                dob.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, nam, thang , ngay);
//        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    public Boolean detect_name (String number) {

        if (!number.matches("[a-zA-Z][^#&<>\\\"~;$^%{}?]{1,50}$"))
            return false;

        return true;
    }

    //put parameters
    private void sendUserData(String url, final String phone, final String seatId, final int position){

        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AAA", "onResponse: yeahyeah" + response.toString());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("kq");
                            if( result.equals("1")){
                                final Dialog dialog = new Dialog(EditTicketForm.this);
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
                                final Dialog dialog = new Dialog(EditTicketForm.this);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditTicketForm.this, "Vui lòng kiểm tra kết nối sau đó thử lại!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("X-CSRF-Token", accessToken);
//                String [] s = dob.getText().toString().split("-");
//                List<String> s1 = Arrays.asList(s);
//                Collections.reverse(s1);
//                String doB = TextUtils.join("-", s1);

                String ten_khong_dau = Normalizer.normalize(realname.getText().toString(),
                        Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","");

                params.put("name", realname.getText().toString());
                params.put("namekd", ten_khong_dau);
                params.put("ngaysinh", dob.getText().toString());
                params.put("gender", gender);
                params.put("phone", phone);
                params.put("idve", seatId);


                Log.d("AAA", "getParams: OK!!!");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
