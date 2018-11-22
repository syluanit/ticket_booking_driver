package com.example.syluanit.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
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
import android.view.Window;
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
import com.example.syluanit.myapplication.Service.Database;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static TabLayout tabLayout;
    ViewPager viewPager;
    TextView title;
    NavigationView navigationView;
    ImageView edit_user_info;
    Button btn_SOS;
    public static int tablayout_position = 0;
    TextView nav_username;
    Database database;
    public static CurrentTicket currentTicket;
    Dialog dialog;

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
        currentTicket = new CurrentTicket();

        database = new Database(this, "driver.sqlite", null, 1);

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
        nav_username = (TextView)findViewById(R.id.nav_username);

        HomeViewPagerAdapter homeViewPagerAdapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        homeViewPagerAdapter.addFragment(new Fragment_So_Do_Xe(), "Sơ Đồ Xe");
        homeViewPagerAdapter.addFragment(new Fragment_Ban_Do(), "Bản Đồ");
        homeViewPagerAdapter.addFragment(new Fragment_Driver_Map(), "Tài xế quanh bạn");
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
                        tablayout_position = 0;
                        title.setText("Tìm chuyến xe");
                        navigationView.getMenu().getItem(1).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        break;
                    case 1:
                        tablayout_position = 1;
                        title.setText("Bản đồ");
                        navigationView.getMenu().getItem(2).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        break;
                    case 2:
                        tablayout_position = 2;
                        navigationView.getMenu().getItem(3).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        title.setText("Tài xế quanh bạn");
                        break;
                    case 3:
                        tablayout_position = 3;
                        navigationView.getMenu().getItem(4).setChecked(true);
                        btn_SOS.setVisibility(View.VISIBLE);
                        edit_user_info.setVisibility(View.GONE);
                        title.setText("Thêm đường đi mới");
                        break;
                    case 4:
                        tablayout_position = 4;
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

        btn_SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationView.getMenu().getItem(3).setChecked(true);
                tabLayout.getTabAt(2).select();
            }
        });

        Cursor data = database.getDaTa("SELECT * FROM sqlite_master WHERE name ='User' and type='table'");
        // checking the table User null? login or not?
        if (data.getCount() > 0){
            Cursor currentUserDB = database.getDaTa("Select * from User");
            while (currentUserDB.moveToNext()) {
                TextView tv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.nav_username);
                tv.setText(currentUserDB.getString(2));
//                Toast.makeText(this, "Co dữ liệu" + currentUserDB.getString(8), Toast.LENGTH_SHORT).show();
            }
        }

        dialog = new Dialog(Home.this);
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

    DatabaseReference currentUser;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {

        currentUser = FirebaseDatabase.getInstance().getReference("lastOnline").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        if (id == R.id.nav_logout) {

            currentUser.removeValue();

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_exit);
            TextView content = dialog.findViewById(R.id.content);
            Button btn_exit = dialog.findViewById(R.id.btn_cancel);
            Button btn_accept = dialog.findViewById(R.id.btn_accept);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            content.setText("Bạn có muốn đăng xuất và thoát ứng dụng?");
            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            btn_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                    mFirebaseAuth.signOut();
                    database.queryData("Drop table IF exists User");
                    Intent intent = new Intent(Home.this, SignIn.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.show();
        } else if (id == R.id.nav_route) {
            tablayout_position = 0;
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(0).select();
            title.setText("Tìm chuyến xe");
        } else if (id == R.id.nav_map) {
            tablayout_position = 1;
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(1).select();
            title.setText("Bản đồ");
        } else if (id == R.id.nav_drivermap) {
            tablayout_position= 2;
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(2).select();
            title.setText("Tài xế quanh bạn");
        } else if (id == R.id.nav_new_road) {
            tablayout_position = 3;
            btn_SOS.setVisibility(View.VISIBLE);
            edit_user_info.setVisibility(View.GONE);
            tabLayout.getTabAt(3).select();
            title.setText("Thêm đường đi mới");
        } else if (id == R.id.nav_info) {
            tablayout_position = 4;
            tabLayout.getTabAt(4).select();
            btn_SOS.setVisibility(View.GONE);
            edit_user_info.setVisibility(View.VISIBLE);
            title.setText("Thông tin tài xế");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }
}
