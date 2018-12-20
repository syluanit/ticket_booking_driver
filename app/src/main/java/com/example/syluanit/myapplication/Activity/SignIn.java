package com.example.syluanit.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.syluanit.myapplication.R;
import com.example.syluanit.myapplication.Service.Database;
import com.google.android.gms.common.api.Response;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

//    String url = "http://192.168.43.218/busmanager/public/dangnhapDriverAndroid";
    String url;
    FirebaseAuth mAuth;
    TextView   forgetPassword;
    Button login;
    ImageView rotate;
    EditText username, password;
    Database database;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        username = (EditText) findViewById(R.id.username_login);
        password = (EditText) findViewById(R.id.password_login);
        forgetPassword = (TextView) findViewById(R.id.forgetpassword_login);

        login = (Button) findViewById(R.id.btn_login);
        rotate = (ImageView) findViewById(R.id.rotate);
        mAuth = FirebaseAuth.getInstance();
        database = new Database(this, "driver.sqlite", null, 1);

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
        } else if (!detect_email(email)) {
            Toast.makeText(SignIn.this, "Vui lòng kiểm tra địng dạng email!", Toast.LENGTH_LONG).show();
        }else if (passwordLogin.length() < 6 || passwordLogin.length() > 30) {
            Toast.makeText(SignIn.this, "Vui lòng nhập mật khẩu từ 6 đến 30 ký tự!", Toast.LENGTH_SHORT).show();
        }
        else {
            //kiểm tra đăng nhập trên MySql trước
            String ip = getResources().getString(R.string.ip);
            String address = getResources().getString(R.string.address);
            url = ip + address + "/dangnhapDriverAndroid";
            sendUserData(url);

            dialog = new Dialog(SignIn.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.progressdialog);
            ImageView iv_rotate = (ImageView) dialog.findViewById(R.id.iv_rotate);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Animation animationRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_not_reverse);
            iv_rotate.startAnimation(animationRotate);
            dialog.show();
        }
    }

    private void sendUserData(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("AAA", "onResponse: yeahyeah" + response);

                try {
                    final JSONObject jsonObject = new JSONObject(response);
                    String res = jsonObject.getString("kq");

                    if (res.equals("success")) {
                        try {
                            JSONArray jsonArray = jsonObject.getJSONArray("tt");
                            final JSONObject jsonObject1 = (JSONObject) jsonArray.get(0);
                            // kiểm tra đăng nhập trên irebase
                            mAuth.signInWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            dialog.cancel();
                                            if (task.isSuccessful()) {

                                                Intent intent = new Intent(SignIn.this, Home.class);
                                                startActivity(intent);
                                                finish();

                                                database.queryData("CREATE TABLE IF NOT EXISTS User(Id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                        " userId INTEGER, name VARCHAR(200), doB VARCHAR(200), " +
                                                        "gender VARCHAR(200), address VARCHAR(200), username VARCHAR(200)," +
                                                        "email VARCHAR(200), branch VARCHAR(200), license VARCHAR(200)," +
                                                        "phoneNumber VARCHAR(200), startWorkingDate VARCHAR(200))");

                                                try {
                                                    database.queryData("INSERT INTO User VALUES(null," +
                                                            " '" + jsonObject1.getString("Mã") + "'," +
                                                            " '" + jsonObject1.getString("Họ_Tên") + "', " +
                                                            " '" + jsonObject1.getString("Ngày_sinh") + "', " +
                                                            " '" + jsonObject1.getString("Giới_tính") + "', " +
                                                            " '" + jsonObject1.getString("Địa_chỉ") + "', " +
                                                            " '" + jsonObject1.getString("Username") + "', " +
                                                            " '" + jsonObject1.getString("Email") + "', " +
                                                            " '" + jsonObject1.getString("Chi_nhánh") + "', " +
                                                            " '" + jsonObject1.getString("Bằng_lái") + "', " +
                                                            " '" + jsonObject1.getString("Sđt") + "', " +
                                                            "'" + jsonObject1.getString("Date_Starting") + "')");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            } else {
                                                Toast.makeText(SignIn.this, "Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            //automatically sign up when phirebase doesn't have the account
                            mAuth.createUserWithEmailAndPassword(username.getText().toString(), password.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("BBB", "onComplete:  Success Sign Up");
                                            }
                                            else Log.d("BBB", "onComplete:  phailed Sign Up");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("BBB", "onFailure: " + e.getMessage().toString());
                                        }
                                    });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (res.equals("wrong")) {
                        dialog.cancel();
                        Toast.makeText(SignIn.this, "Vui lòng kiểm tra tài khoản mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Toast.makeText(SignIn.this, "Vui lòng kiểm tra kết nối sau đó thử lại!", Toast.LENGTH_SHORT).show();
                        Log.d("AAA", "onErrorResponse: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("X-CSRF-Token", accessToken);
                params.put("DNDT", username.getText().toString().trim());
                params.put("DNMK", password.getText().toString().trim());
                Log.d("AAA", "getParams: OK!!!");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public Boolean detect_email (String number) {

        if (!number.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$"))
            return false;

        // if not found, return false
        return true;
    }
}
