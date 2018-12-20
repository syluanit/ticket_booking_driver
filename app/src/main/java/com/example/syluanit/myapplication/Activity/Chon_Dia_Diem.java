package com.example.syluanit.myapplication.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.syluanit.myapplication.Adapter.Chon_Dia_Diem_Adapter;
import com.example.syluanit.myapplication.Model.DiaDiem;
import com.example.syluanit.myapplication.R;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;

public class Chon_Dia_Diem extends AppCompatActivity {

    ListView listView;
    TextView  noPlace;
    Chon_Dia_Diem_Adapter chon_dia_diem_adapter;
    ArrayList<DiaDiem> diaDiemArrayList;
    ImageView iv_back;
    MaterialSearchView searchView;
//    String url = "http://192.168.43.218/busmanager/public/gettinh";
    String url;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon__dia__diem);

        listView = (ListView) findViewById(R.id.lv_chon_dia_diem);
        noPlace = (TextView) findViewById(R.id.noPlace);
        iv_back = (ImageView) findViewById(R.id.back_pressed);

        diaDiemArrayList = new ArrayList<>();

        String ip = getResources().getString(R.string.ip);
        String address = getResources().getString(R.string.address);
        url = ip + address + "/gettinh";
        receiveUserData(url);

        dialog = new Dialog(Chon_Dia_Diem.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progressdialog);
        ImageView iv_rotate = (ImageView) dialog.findViewById(R.id.iv_rotate);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        Animation animationRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_not_reverse);
        iv_rotate.startAnimation(animationRotate);
        dialog.show();

        chon_dia_diem_adapter = new Chon_Dia_Diem_Adapter(this, R.layout.dia_diem_item ,diaDiemArrayList);
        listView.setAdapter(chon_dia_diem_adapter);


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) Chon_Dia_Diem.this).onBackPressed();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_place);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);


        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                listView = (ListView) findViewById(R.id.lv_chon_dia_diem);
                chon_dia_diem_adapter = new Chon_Dia_Diem_Adapter(getApplicationContext(), R.layout.dia_diem_item ,diaDiemArrayList);
                listView.setAdapter(chon_dia_diem_adapter);
                // set click event aph ter back search
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
        });


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty()){
                    final ArrayList<DiaDiem> diaDiemSearch = new ArrayList<>();
                    for( DiaDiem item:diaDiemArrayList){
//                        if (Normalizer.normalize(item.getPlace().toLowerCase(),Normalizer.Form.NFD).
////                                contains(Normalizer.normalize(newText.toLowerCase(),Normalizer.Form.NFD))){
                        if (Normalizer.normalize(item.getPlace().toLowerCase(),Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]","").
                                contains(Normalizer.normalize(newText.toLowerCase(),Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]",""))){
                            diaDiemSearch.add(item);
                        }
                        chon_dia_diem_adapter = new Chon_Dia_Diem_Adapter(getApplicationContext(), R.layout.dia_diem_item ,diaDiemSearch);
                        listView.setAdapter(chon_dia_diem_adapter);
                        //set Click event again
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String dia_diem = diaDiemSearch.get(position).getPlace();
                                Intent intent = new Intent();
                                intent.putExtra("diadiem", dia_diem);
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        });
                    }
                }
                else {
                    chon_dia_diem_adapter = new Chon_Dia_Diem_Adapter(getApplicationContext(), R.layout.dia_diem_item ,diaDiemArrayList);
                    listView.setAdapter(chon_dia_diem_adapter);
                }
                return  true;
            }
        });

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

    private void receiveUserData (String url){
        RequestQueue requestQueue = Volley.newRequestQueue(Chon_Dia_Diem.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        if (response != null) {
                            noPlace.setVisibility(View.GONE);
                            try {
                                JSONArray jsonArray = response.getJSONArray("kq");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                    String province = jsonObject.getString("Tên");
                                    Intent intent = getIntent();
                                    if (province.equals(intent.getStringExtra("to")) ||
                                            province.equals(intent.getStringExtra("from")) )
                                    {} else {
                                        diaDiemArrayList.add(new DiaDiem(province));
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            chon_dia_diem_adapter.notifyDataSetChanged();
                        }
                        else {
                            noPlace.setVisibility(View.VISIBLE);
                            Toast.makeText(Chon_Dia_Diem.this, "Vui lòng kiểm tra kết nối mạng!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.cancel();
                noPlace.setVisibility(View.VISIBLE);
                Log.d("AAA", "onErrorResponse: " + error.toString());
                Toast.makeText(Chon_Dia_Diem.this, "Vui lòng kiểm tra kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

}
