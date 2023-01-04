package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.firstproject.databinding.ActivitySignUpBinding;

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

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_SignUp extends AppCompatActivity {
    private ActivitySignUpBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;
    private TextView name_view,email_view,phNum_view,id_view,pw_view,pwConf_view;

    ArrayList<String> 관심목록_arr,구매목록_arr;
    ArrayList<HashMap> chat_arr;
    private String 이름, 이메일, 전화번호, 아이디, 비밀번호, 비밀번호확인 ;
    private String 계정이미지="null" ;
    private Button 회원가입버튼;
    Gson gson;
    String 회원;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        initializeView();
        initializeProperty();

        /** 버튼 **/
        회원가입버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                이름 = name_view.getText().toString();
                이메일 = email_view.getText().toString();
                전화번호 = phNum_view.getText().toString();
                아이디 = id_view.getText().toString();
                비밀번호 = pw_view.getText().toString();
                비밀번호확인 = pwConf_view.getText().toString();
                //객체 생성
                Signup_member signupMember = new Signup_member(이름,이메일,전화번호,비밀번호,계정이미지,관심목록_arr,구매목록_arr,chat_arr);
                //json 객체
                회원 = gson.toJson(signupMember, Signup_member.class);

                // 아이디 중복확인
                if (회원정보_s.getAll().containsKey(아이디)) Toast.makeText(Activity_SignUp.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                else{ // 비밀번호 확인 로직
                    if (비밀번호.equals(비밀번호확인)){
                        // 회원정보 저장
                        회원정보_e.putString(아이디,회원).apply();
                        Intent intent = new Intent( view.getContext() , Activity_Login.class);
                        startActivity(intent);
                    }else Toast.makeText(Activity_SignUp.this, "비밀번호 확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /** initial **/
    private void initializeView() {
        /** binding **/
        name_view = binding.editName;
        email_view = binding.editEmail;
        phNum_view = binding.editPhoneNum;
        id_view = binding.editId;
        pw_view = binding.editPassword;
        pwConf_view = binding.editPasswordConfirm;

        회원가입버튼 = binding.btnSignup;
    }
    private void initializeProperty() {
        관심목록_arr = new ArrayList<>();
        구매목록_arr = new ArrayList<>();
        chat_arr = new ArrayList<>();
        /** 데이터 추출 **/
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        회원정보_e = 회원정보_s.edit();
        //gson 생성
        gson = new GsonBuilder().create();
    }
}