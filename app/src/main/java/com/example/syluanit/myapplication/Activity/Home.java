package com.example.syluanit.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.syluanit.myapplication.Adapter.HomeViewPagerAdapter;
import com.example.syluanit.myapplication.Fragment.Fragment_Ban_Do;
import com.example.syluanit.myapplication.Fragment.Fragment_Driver_Map;
import com.example.syluanit.myapplication.Fragment.Fragment_Duong_Di_Moi;
import com.example.syluanit.myapplication.Fragment.Fragment_So_Do_Xe;
import com.example.syluanit.myapplication.Fragment.Fragment_Thong_Tin;
import com.example.syluanit.myapplication.Model.CurrentTicket;
import com.example.syluanit.myapplication.R;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TabLayout tabLayout;
    ViewPager viewPager;
    TextView title;
    NavigationView navigationView;
    ImageView edit_user_info;
    Button btn_SOS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.bringToFront();

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();

        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);
        title = (TextView) findViewById(R.id.toolbar_title);
        edit_user_info = (ImageView) findViewById(R.id.user_info_edit);
        btn_SOS = (Button) findViewById(R.id.SOS);

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragment(new Fragment_So_Do_Xe(), "Sơ Đồ Xe");
        homeViewPagerAdapter.addFragment(new Fragment_Ban_Do(), "Bản Đồ");
        homeViewPagerAdapter.addFragment(new Fragment_Driver_Map(), "Tài xế chỉ đường");
        homeViewPagerAdapter.addFragment(new Fragment_Duong_Di_Moi(), "Đường Đi Mới");
        homeViewPagerAdapter.addFragment(new Fragment_Thong_Tin(), "Thông Tin");
        viewPager.setAdapter(homeViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.searchicon);
        tabLayout.getTabAt(1).setIcon(R.drawable.mapicon);
        tabLayout.getTabAt(2).setIcon(R.drawable.drivemapicon);
        tabLayout.getTabAt(3).setIcon(R.drawable.newroadicon);
        tabLayout.getTabAt(4).setIcon(R.drawable.infoicon);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        title.setText("Tìm chuyến xe");
                        navigationView.getMenu().getItem(1).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        break;
                    case 1:
                        title.setText("Bản đồ");
                        navigationView.getMenu().getItem(2).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        break;
                    case 2:
                        navigationView.getMenu().getItem(3).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        title.setText("Tài xế chỉ đường");
                        break;
                    case 3:
                        navigationView.getMenu().getItem(4).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        title.setText("Thêm đường đi mới");
                        break;
                    case 4:
                        navigationView.getMenu().getItem(5).setChecked(true);
                        title.setText("Thông tin tài xế");
                        btn_SOS.setVisibility(View.GONE);
                        edit_user_info.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        edit_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(Home.this);
                dialog.setContentView(R.layout.dialog_edit);
                TextView tvEditInfo = (TextView) dialog.findViewById(R.id.edit_thong_tin);
                TextView tvPassword = (TextView) dialog.findViewById(R.id.change_password);
                TextView tvBack = (TextView) dialog.findViewById(R.id.back_edit);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                tvBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                tvEditInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, EditInfoForm.class);
                        startActivity(intent);
                    }
                });

                tvPassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Home.this, ChangePasswordForm.class);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_logout) {
            // Handle the camera action
        } else if (id == R.id.nav_route) {
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(0).select();
            title.setText("Tìm chuyến xe");
        } else if (id == R.id.nav_map) {
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(1).select();
            title.setText("Bản đồ");
        } else if (id == R.id.nav_drivermap) {
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(2).select();
            title.setText("Tài xế chỉ đường");
        } else if (id == R.id.nav_new_road) {
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(3).select();
            title.setText("Thêm đường đi mới");
        } else if (id == R.id.nav_info) {
            tabLayout.getTabAt(4).select();
            btn_SOS.setVisibility(View.GONE);
            edit_user_info.setVisibility(View.VISIBLE);
            title.setText("Thông tin tài xế");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
