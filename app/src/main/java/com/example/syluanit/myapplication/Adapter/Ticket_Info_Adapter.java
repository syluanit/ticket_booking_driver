package com.example.syluanit.myapplication.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.syluanit.myapplication.Interface.ItemClickListener;
import com.example.syluanit.myapplication.Model.TicketInfo;
import com.example.syluanit.myapplication.R;

import java.util.ArrayList;

public class Ticket_Info_Adapter extends RecyclerView.Adapter<Ticket_Info_Adapter.ViewHolder> {

    Context context;
    ArrayList<TicketInfo> ticketInfoList;

    public Ticket_Info_Adapter(Context context, ArrayList<TicketInfo> TicketList) {
        this.context = context;
        this.ticketInfoList = TicketList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.ticket_info_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TicketInfo ticket = ticketInfoList.get(position);

            holder.tvType.setText(ticket.getType());
            holder.tvContent.setText(ticket.getContent());
            if (ticket.getType() == "Tổng tiền") {
                holder.tvContent.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
                holder.tvContent.setTypeface(Typeface.defaultFromStyle(Typeface.DEFAULT.BOLD));
                holder.tvContent.setTextSize(20);
            }
    }

    @Override
    public int getItemCount() {
        return ticketInfoList == null ? 0 : ticketInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
//            implements View.OnClickListener
    {

        public TextView tvType;
        public TextView tvContent;
        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvContent = itemView.findViewById(R.id.tvContent);
//            itemView.setOnClickListener(this);
        }
    }
}
