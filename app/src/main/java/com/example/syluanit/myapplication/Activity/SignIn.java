package com.example.syluanit.myapplication.Activity;

import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.syluanit.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    FirebaseAuth mAuth;
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
        mAuth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        Animation animationRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
        rotate.startAnimation(animationRotate);
    }

    private void userLogin(){
        String email = username.getText().toString();
        String passwordLogin = password.getText().toString();
        if (email.equals("") || passwordLogin.equals("")){
            Toast.makeText(this, "Vui lòng nhập đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(email, passwordLogin)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(SignIn.this, Home.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

//    protected FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
//
//    @Override
//    public void signIn(String email, String password, final DataCallback<FirebaseUser> callback) {
//        mFirebaseAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        getResponse(task, callback);
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        callback.onGetDataFailed(e.getMessage());
//                    }
//                });
//    }
}
