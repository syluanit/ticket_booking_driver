package com.example.syluanit.myapplication.Activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.syluanit.myapplication.R;

public class ChangePasswordForm extends AppCompatActivity {

    EditText password, newPassword, confirmPassword;
    Button accept;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_form);

        back = (ImageView) findViewById(R.id.back_pressed_password);
        accept = (Button) findViewById(R.id.btn_password);
        password = (EditText) findViewById(R.id.old_password);
        newPassword = (EditText) findViewById(R.id.new_password);
        confirmPassword = (EditText) findViewById(R.id.check_new_password);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) ChangePasswordForm.this).onBackPressed();
            }
        });
    }
}
