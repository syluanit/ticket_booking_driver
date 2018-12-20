package com.example.syluanit.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.syluanit.myapplication.Activity.So_Do_Xe_Activity;
import com.example.syluanit.myapplication.Activity.Home;
import com.example.syluanit.myapplication.R;
import com.example.syluanit.myapplication.Activity.RouteActivity;
import com.example.syluanit.myapplication.Model.RouteTicket;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder>{

    Context context;
    ArrayList<RouteTicket> routeArrayList;
//    String url = "http://192.168.43.218/busmanager/public/chonveAndroid";
    String url;

    public RouteAdapter(Context context, ArrayList<RouteTicket> routeArrayList) {
        this.context = context;
        this.routeArrayList = routeArrayList;
    }

    @NonNull
    @Override
    public RouteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.route_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteAdapter.ViewHolder holder, int position) {
        final RouteTicket route = routeArrayList.get(position);
        holder.tv_price.setText(route.getPrice());
        holder.tv_timeArr.setText(route.getTimeArr());
        holder.tv_timeDep.setText(route.getTimeDep());

        holder.btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Server Request
                String ip = context.getResources().getString(R.string.ip);
                String address = context.getResources().getString(R.string.address);
                url = ip + address + "/chonveAndroid";

                final RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("AAA", "onResponse: yeahyeah");
                                Log.d("AAA", "onResponse: " + response.toString());

                                RouteActivity.noRoute.setVisibility(View.GONE);

                                Home.currentTicket.setTimeDep(route.getTimeDep());
                                Home.currentTicket.setPrice(currencyFormat(route.getPrice()));
                                Home.currentTicket.setId(route.getId());

                                    Intent i = new Intent(context, So_Do_Xe_Activity.class);
                                    i.putExtra("ticketMap", response);
                                    context.startActivity(i);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                RouteActivity.noRoute.setVisibility(View.VISIBLE);
                                Toast.makeText(context, "Vui lòng kiểm tra kết nối mạng hoặc thử lại", Toast.LENGTH_SHORT).show();
                                Log.d("AAA", "onErrorResponse: " + error.toString());
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("ID", route.getId());
                        Log.d("AAA", "getParams: OK!!!");
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_price, tv_timeDep, tv_timeArr;
        public Button btn_book;

        public ViewHolder(final View itemView) {
            super(itemView);

            tv_price = (TextView) itemView.findViewById(R.id.tv_priceTicket);
            tv_timeDep = (TextView) itemView.findViewById(R.id.tv_timeDep);
            tv_timeArr = (TextView) itemView.findViewById(R.id.tv_timeArr);
            btn_book = (Button) itemView.findViewById(R.id.btnBookThis);


        }
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("#########");
        return formatter.format(Double.parseDouble(amount));
    }
}
