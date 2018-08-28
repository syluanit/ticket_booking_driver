package com.example.syluanit.myapplication.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.syluanit.myapplication.R;

public class Fragment_So_Do_Xe extends Fragment {

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_so_do_xe, container, false);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment_tim_chuyen_xe fragment = new Fragment_tim_chuyen_xe();
        transaction.add(R.id.layout, fragment,"chuyen_xe" );
//        transaction.addToBackStack("first");
        transaction.commit();

        return view;
    }

    public void replaceFragment(Fragment someFragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.layout, someFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
