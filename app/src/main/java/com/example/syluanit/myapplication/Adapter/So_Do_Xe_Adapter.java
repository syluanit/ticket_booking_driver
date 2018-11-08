package com.example.syluanit.myapplication.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syluanit.myapplication.Activity.EditTicketForm;
import com.example.syluanit.myapplication.Activity.PhoneChecking;
import com.example.syluanit.myapplication.Activity.SignIn;
import com.example.syluanit.myapplication.Interface.ItemClickListener;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.Model.TicketInfo;
import com.example.syluanit.myapplication.R;
import com.example.syluanit.myapplication.Service.Database;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class So_Do_Xe_Adapter extends RecyclerView.Adapter<So_Do_Xe_Adapter.ViewHolder>{

    private Context context;
    ArrayList<GheNgoi> mangGheNgoi;
    String url = "http://192.168.43.218/busmanager/public/ticketInfoAndroid";

    public So_Do_Xe_Adapter(Context context, ArrayList<GheNgoi> mangGheNgoi) {
        this.context = context;
        this.mangGheNgoi = mangGheNgoi;
    }

    @NonNull
    @Override
    public So_Do_Xe_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_ghe_ngoi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final So_Do_Xe_Adapter.ViewHolder holder, int position) {
        final GheNgoi gheNgoi = mangGheNgoi.get(position);
        holder.setIsRecyclable(false);
//        holder.iv_seat.setImageResource(gheNgoi.getHinhAnh());
//        holder.tv_seat.setText(gheNgoi.getViTri());
        // TODO set seat' status
        // Invisible
        if (gheNgoi.getHinhAnh() == 0) {
            holder.iv_seat.setVisibility(View.GONE);
            holder.iv_seat.setEnabled(false);
            holder.iv_seat.setOnClickListener(null);
            holder.setItemClickListener(null);
            holder.tv_seat.setVisibility(View.GONE);
            holder.tv_seat.setEnabled(false);
            holder.tv_seat.setOnClickListener(null);
        } else {
            holder.iv_seat.setImageResource(gheNgoi.getHinhAnh());
            holder.tv_seat.setText(gheNgoi.getViTri());
        }
        //show sơ đô
        // Available
        if (gheNgoi.getTrangThai() == 0) {
            holder.iv_seat.setBackgroundColor(Color.TRANSPARENT);
        } // Occuppied
        else if (gheNgoi.getTrangThai() == 1) {
            holder.iv_seat.setEnabled(false);
        }
        // click event
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                int seatStatus = gheNgoi.getTrangThai();
                int seatVisibility = gheNgoi.getHinhAnh();
                // vị trí đương đi
                if (seatVisibility == 0) {
                    return;
                    // vị trí la ghế
                } else if (seatStatus == 1) {
                    // ghế đã đặt click hoặc longclick đêu hiện thông tin
                    sendData(url, gheNgoi.getId());
                } else if (isLongClick) {
                    // ghế chưa đặt, longclick sửa thông tin
//                    gheNgoi.setTrangThai(1);
                    Intent intent = new Intent(context, PhoneChecking.class);
                    intent.putExtra("seatId",gheNgoi.getId());
                    context.startActivity(intent);
                } else {
                    // ghế chưa đặt click hiện ghế đang trống
                    Toast.makeText(context, "Ghế đang trống", Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });
        //end click event
    }

    @Override
    public int getItemCount() {
        return mangGheNgoi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView iv_seat;
        public TextView tv_seat;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_seat = (ImageView) itemView.findViewById(R.id.seat);
            tv_seat = (TextView) itemView.findViewById(R.id.seatposition);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }
        //setter itemClickListener
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }
        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition(), true);
            return true;
        }
    }

    private void sendData(String url, final String ticketId){

        final RequestQueue requestQueue = Volley.newRequestQueue(context);
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
                                //json request
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                String email = jsonObject1.getString("Email");
                                String phone = jsonObject1.getString("Sđt");
                                String gender = jsonObject1.getString("Giới tính");
                                String dob = jsonObject1.getString("Ngày_sinh");
                                String address = jsonObject1.getString("Địa chỉ");
                                String name = jsonObject1.getString("Tên");

                                //dialog
                                final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.dialog_ticket);
                                TextView back = (TextView) dialog.findViewById(R.id.back_ticket_dialog);
                                // dialog recycler view
                                RecyclerView rv_ticket = (RecyclerView) dialog.findViewById(R.id.rv_ticketInfo);
                                rv_ticket.setHasFixedSize(true);
                                ArrayList<TicketInfo> ticketInfoArrayList = new ArrayList<>();

                                Ticket_Info_Adapter adapter = new Ticket_Info_Adapter(context, ticketInfoArrayList);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                rv_ticket.setLayoutManager(layoutManager);
                                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context, layoutManager.getOrientation());
                                rv_ticket.addItemDecoration(dividerItemDecoration);

                                //prepare data array list
                                if (gender.equals("1")) {
                                    gender = "Nam";
                                } else gender = "Nữ";
                                //reverse dob string
                                String[] s = dob.split("-");
                                List<String> s1 = Arrays.asList(s);
                                Collections.reverse(s1);
                                dob = TextUtils.join("-", s1);

                                if (email.equals("null")) {
                                    email = "";
                                }
                                if (address.equals("null")) {
                                    address = "";
                                }

                                ticketInfoArrayList.add(new TicketInfo("Họ tên", name));
                                ticketInfoArrayList.add(new TicketInfo("Điện thoại", phone));
                                ticketInfoArrayList.add(new TicketInfo("Ngày sinh", dob));
                                ticketInfoArrayList.add(new TicketInfo("Giới tính", gender));
                                ticketInfoArrayList.add(new TicketInfo("Email", email));
                                ticketInfoArrayList.add(new TicketInfo("Địa chỉ", address));

                                rv_ticket.setAdapter(adapter);

                                back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.cancel();
                                    }
                                });

                                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
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
                        Toast.makeText(context, "Vui lòng kiểm tra kết nối sau đó thử lại!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "onErrorResponse: " + error.toString());
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("idve", ticketId);

                Log.d("AAA", "getParams: OK!!!");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
