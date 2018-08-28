package com.example.syluanit.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.syluanit.myapplication.Model.DiaDiem;
import com.example.syluanit.myapplication.R;

import java.util.List;

public class Chon_Dia_Diem_Adapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<DiaDiem> diaDiemList;

    public Chon_Dia_Diem_Adapter(Context context, int layout, List<DiaDiem> diaDiemList) {
        this.context = context;
        this.layout = layout;
        this.diaDiemList = diaDiemList;
    }

    @Override
    public int getCount() {
        return diaDiemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout,null);

        // Ánh xạ
        TextView tv_time = (TextView) convertView.findViewById(R.id.tv_diadiem);
        ImageView iv_icon = (ImageView) convertView.findViewById(R.id.iv_diadiem);

        // Gán giá trị
        DiaDiem diaDiem = diaDiemList.get(position);

        tv_time.setText(diaDiem.getPlace());
        iv_icon.setImageResource(diaDiem.getIcon());

        return convertView;
    }
}
