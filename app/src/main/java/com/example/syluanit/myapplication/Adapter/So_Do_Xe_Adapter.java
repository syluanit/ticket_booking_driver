package com.example.syluanit.myapplication.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.syluanit.myapplication.Interface.ItemClickListener;
import com.example.syluanit.myapplication.Model.GheNgoi;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class So_Do_Xe_Adapter extends RecyclerView.Adapter<So_Do_Xe_Adapter.ViewHolder>{

    private Context context;
    ArrayList<GheNgoi> mangGheNgoi;

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
        GheNgoi gheNgoi = mangGheNgoi.get(position);
        holder.iv_seat.setImageResource(gheNgoi.getHinhAnh());
        // TODO set seat' status
        // Invisible
        if (gheNgoi.getHinhAnh() == 0) {
            holder.iv_seat.setVisibility(View.GONE);
            holder.iv_seat.setEnabled(false);
            holder.iv_seat.setOnClickListener(null);
        }
        else {
            holder.iv_seat.setImageResource(gheNgoi.getHinhAnh());
        }
        // Available
        if (gheNgoi.getTrangThai() == 0){
            holder.iv_seat.setBackgroundColor(Color.TRANSPARENT);
        } // Occuppied
        else if (gheNgoi.getTrangThai() == 1){
            holder.iv_seat.setBackgroundResource(R.color.colorPrimary);
            holder.iv_seat.setOnClickListener(null);
        }

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick){
                    int seatStatus = mangGheNgoi.get(position).getTrangThai();
                    if (seatStatus == 0){
                        // sua thong tin khach doc duong
                    } else {
                        // hien thi thong tin khach da dat ve
                    }
                }
                else {
                    int seatStatus = mangGheNgoi.get(position).getTrangThai();
                    if (seatStatus == 0){
                        Toast.makeText(context, "Ghế trống", Toast.LENGTH_SHORT).show();
                    } else {
                        // hien thi thong tin khach da dat ve
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangGheNgoi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        public ImageView iv_seat;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_seat = (ImageView) itemView.findViewById(R.id.seat);

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
}
