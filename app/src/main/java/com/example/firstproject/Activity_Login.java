package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.firstproject.databinding.ActivityLoginBinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Activity_Login extends AppCompatActivity {
    private ActivityLoginBinding binding;
    SharedPreferences 회원정보_s;
    private TextView id_tx, pw_tx;
    private Button 로그인버튼;
    private TextView 회원가입;

    private String id = null;
    private String pw = null;
    Gson gson;String 회원;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        initializeView();
        initializeProperty();

        /** 버튼 **/
        //로그인 버튼
        로그인버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = id_tx.getText().toString();
                pw = pw_tx.getText().toString();
                //객체 만들기
                회원 = 회원정보_s.getString(id,"비회원");

                /** 데이터 추출 **/
                if(!회원.equals("비회원")) {// 아이디 존재
                    Signup_member signup_member = gson.fromJson(회원,Signup_member.class);

                    String 이름 = signup_member.getName(); //이름
                    String 이메일 = signup_member.getE_mail(); //이메일
                    String 전화번호 = signup_member.getPhone_num(); //전화번호
                    String 비밀번호 = signup_member.getPassword(); //비밀번호
                    if(pw.equals(비밀번호)){ //비밀번호까지 모두 같을 때
                        //로그인 유저 data 만들기
                        SharedPreferences 로그인 = getSharedPreferences("LoginUser", MODE_PRIVATE);
                        SharedPreferences.Editor 로그인에디터 = 로그인.edit();
                        로그인에디터.putString("로그인회원", id).apply();
//                        //자동 로그인 로직
//                        if( auto_login.isChecked() ){
//                            SharedPreferences 자동로그인 = getSharedPreferences("autoLoginUser", MODE_PRIVATE);
//                            SharedPreferences.Editor 자동로그인에디터 = 자동로그인.edit();
//                            자동로그인에디터.putString("자동로그인회원", id).apply();
//                        }
                        // 홈화면으로 이동
                        Intent intent = new Intent( view.getContext() , MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "로그인", Toast.LENGTH_SHORT).show();
                    }else Toast.makeText(getApplicationContext(),"입력정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getApplicationContext(),"입력정보를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
        //회원가입 textview
        회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( view.getContext() , Activity_SignUp.class);
                startActivity(intent);
            }
        });
    }

    /** initial **/
    private void initializeView() {
        /** binding **/
        id_tx = binding.textLoginId;
        pw_tx = binding.textLoginPassword;
        로그인버튼 = binding.btnLogin;
        회원가입 = binding.textviewSignup;
    }
    private void initializeProperty() {
        /** shared preference **/
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        //gson 생성
        gson = new GsonBuilder().create();
    }
}