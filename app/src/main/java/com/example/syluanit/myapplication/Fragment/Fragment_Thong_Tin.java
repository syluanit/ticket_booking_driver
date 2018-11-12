package com.example.syluanit.myapplication.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.syluanit.myapplication.Adapter.Ticket_Info_Adapter;
import com.example.syluanit.myapplication.Model.TicketInfo;
import com.example.syluanit.myapplication.R;
import com.example.syluanit.myapplication.Service.Database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Fragment_Thong_Tin extends Fragment {
    View view;
    RecyclerView rv_ticket;
    Ticket_Info_Adapter adapter;
    ArrayList<TicketInfo> ticketInfoArrayList;
    Database database;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_thong_tin, container, false);

        rv_ticket = (RecyclerView) view.findViewById(R.id.rv_ticketInfo);
        rv_ticket.setHasFixedSize(true);
        ticketInfoArrayList = new ArrayList<>();

        database = new Database(getContext(), "driver.sqlite", null, 1);

        adapter = new Ticket_Info_Adapter(getContext(), ticketInfoArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_ticket.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), layoutManager.getOrientation());
        rv_ticket.addItemDecoration(dividerItemDecoration);
        rv_ticket.setAdapter(adapter);
        prepareTicketInfo();

        return view;
    }

    private void prepareTicketInfo() {
        Cursor data = database.getDaTa("SELECT * FROM sqlite_master WHERE name ='User' and type='table'");

        if (data.getCount() > 0) {
            Cursor currentUserDB = database.getDaTa("Select * from User");
            while (currentUserDB.moveToNext()) {
                String name = currentUserDB.getString(2);
                String doB = currentUserDB.getString(3);
                String gender = currentUserDB.getString(4);
                String address = currentUserDB.getString(5);
                String username = currentUserDB.getString(6);
                String email = currentUserDB.getString(7);
                String branch = currentUserDB.getString(8);
                String licence = currentUserDB.getString(9);
                String phone = currentUserDB.getString(10);
                String date_Starting = currentUserDB.getString(11);
                if (gender.equals("1")) {
                    gender = "Nam";
                } else gender = "Nữ";
                //reverse dob string
                String[] s = doB.split("-");
                List<String> s1 = Arrays.asList(s);
                Collections.reverse(s1);
                doB = TextUtils.join("-", s1);

                s = date_Starting.split("-");
                s1 = Arrays.asList(s);
                Collections.reverse(s1);
                date_Starting = TextUtils.join("-", s1);

                if (username.equals("null")) {
                    username = "";
                }
                if (doB.equals("null")) {
                    doB = "";
                }
                if (gender.equals("null")) {
                    gender = "";
                }
                if (email.equals("null")) {
                    email = "";
                }
                if (address.equals("null")) {
                    address = "";
                }


                ticketInfoArrayList.add(new TicketInfo("Họ và tên", name));
                ticketInfoArrayList.add(new TicketInfo("Email", email));
                ticketInfoArrayList.add(new TicketInfo("Điện thoại", phone));
                ticketInfoArrayList.add(new TicketInfo("Địa chỉ", address));
                ticketInfoArrayList.add(new TicketInfo("Ngày sinh", doB));
                ticketInfoArrayList.add(new TicketInfo("Giới tính", gender));
//                ticketInfoArrayList.add(new TicketInfo("Username", email));
                ticketInfoArrayList.add(new TicketInfo("Chi nhánh", branch));
                ticketInfoArrayList.add(new TicketInfo("Số bằng lái", licence));
                ticketInfoArrayList.add(new TicketInfo("Ngày làm việc", date_Starting));
//                ticketInfoArrayList.add(new TicketInfo("Giới tính", gender));

            }
        }
    }
}
