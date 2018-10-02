package com.example.syluanit.myapplication.Activity;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.syluanit.myapplication.R;

public class SignIn extends AppCompatActivity {

    TextView  signUp, forgetPassword;
    Button login;
    ImageView rotate;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);
        forgetPassword = (TextView) findViewById(R.id.forgetpassword_login);
        signUp = (TextView) findViewById(R.id.signUp);
        login = (Button) findViewById(R.id.btn_login);
        rotate = (ImageView) findViewById(R.id.rotate);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent(SignIn.this, Home.class);
                startActivity(intent);
            }
        });

        Animation animationRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        rotate.startAnimation(animationRotate);
    }
}
