package com.example.firstproject;

import static com.example.firstproject.R.drawable.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;

import com.bumptech.glide.Glide;
import com.example.firstproject.databinding.ActivityPostToChatBinding;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_PostToChat extends AppCompatActivity {
    private ActivityPostToChatBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;

    ImageView 이미지뷰_view, 하트_view;
    CircleImageView 판매자이미지_view;
    TextView 판매자text_view, 제목_view, 상세주소_view, 주소_view, 가격_view, 내용_view,거래완료_view;
    Button 채팅하기_view;
    Gson gson;String 게시글, 회원정보;
    private String login_id,식별번호,작성자;
    private String 이미지, 제목 , 주소 , 상세주소, 가격, 내용;
    private boolean 관심여부,로그인여부,판매중 ;

    private String 썸네일, 판매자_이름, 전화번호;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_post_to_chat);

        initializeView();
        initializeProperty();

        /** 버튼 **/
        하트_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button(0);}
        });
        채팅하기_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button(1);}
        });
    }
    /** initial **/
    private void initializeView(){
        /** binding **/
        판매자이미지_view = binding.sellerImgCallInquiry;
        판매자text_view = binding.sellerCallInquiry;
        이미지뷰_view = binding.imageviewCall;
        제목_view = binding.titleCallInquiry;
        주소_view = binding.locationCallInquiry;
        상세주소_view = binding.buildingCallInquiry;
        가격_view = binding.purchaseCallInquiry;
        내용_view = binding.contentCallInquiry;
        거래완료_view = binding.transactionCompletedCallInquiry;
        채팅하기_view = binding.chatInquiry;
        하트_view = binding.favoriteEmptyCallinquiry;
    }
    @SuppressLint("ResourceAsColor")
    private void initializeProperty() {
        /** SharedPreferences **/
        게시글_s = getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getSharedPreferences("LoginUser", MODE_PRIVATE);
        회원정보_e = 회원정보_s.edit(); //Editor
        게시글_e = 게시글_s.edit(); //Editor
        /** gson **/
        gson = new GsonBuilder().create();
        /** Intent **/
        Intent intent = getIntent();
        식별번호 =  intent.getStringExtra("key"); // 식별번호
        /** 초기화 **/
        login_id = 로그인_s.getString("로그인회원","로그인회원 없음");
        //객체 만들기
        게시글 = 게시글_s.getString(식별번호,"Callinquiry 데이터 없음");
        if(!게시글.equals("Callinquiry 데이터 없음")){
            Post post = gson.fromJson(게시글,Post.class);

            이미지 = MainActivity.getRealPathFromURI(this,Uri.parse(post.get이미지()));
            제목 = post.get제목();
            주소 = post.get주소();
            상세주소 = post.get상세주소();
            가격 = post.get가격();
            내용 = post.get내용();
            관심여부 = post.is관심여부();
            판매중 = post.is판매중();
            작성자 = post.get작성자();
            //하트 초기화
            로그인여부 = !로그인_s.getAll().isEmpty();
            if (로그인여부==false) {
                하트_view.setVisibility(View.GONE);
                채팅하기_view.setBackgroundResource(R.drawable.round_button_chat_dark);
                채팅하기_view.setEnabled(false);
            }
            else{
                if (관심여부) 하트_view.setImageResource(favorite);
                else 하트_view.setImageResource(favorite_empty);
            }
            //거래완료
            if (!판매중) { //거래완료
                거래완료_view.setVisibility(View.VISIBLE);
                채팅하기_view.setBackgroundResource(R.drawable.round_button_chat_dark);
                채팅하기_view.setEnabled(false);
            }
            else 거래완료_view.setVisibility(View.GONE); //판매중
            //채팅하기 btn
            if(login_id.equals(작성자)) 채팅하기_view.setVisibility(View.GONE); // 자신의 게시물이면 채팅버튼 없애기
        }

        //회원정보 ( 썸네일, 판매자 이름 , 전화번호 )
        회원정보 = 회원정보_s.getString(작성자,"회원정보 없음");
        if(!회원정보.equals("회원정보 없음")){
            Signup_member signup_member = gson.fromJson(회원정보,Signup_member.class); // 회원정보 객체

            썸네일 = MainActivity.getRealPathFromURI(this,Uri.parse(signup_member.getThumnail()));
            판매자_이름 = signup_member.getName();
            전화번호 = signup_member.getPhone_num();
            /** 초기화 **/
            if (썸네일!=null) Glide.with(this).load(썸네일).into(판매자이미지_view);
            Glide.with(this).load(이미지).into(이미지뷰_view);
            판매자text_view.setText(판매자_이름);
            제목_view.setText(제목);
            주소_view.setText(주소);
            상세주소_view.setText(상세주소);
            가격_view.setText(가격);
            내용_view.setText(내용);
        }
    }
    /** button **/
    public void button(int btn){
        switch (btn){
            case 0:
                /** 하트 **/
                게시글 = 게시글_s.getString(식별번호,"Callinquiry 데이터 없음");
                if(!게시글.equals("Callinquiry 데이터 없음")) {
                    Post post = gson.fromJson(게시글, Post.class);
                    관심여부 = post.is관심여부();
                }
                if(!관심여부){                                            // false
                    하트_view.setImageResource(favorite);
                    MainActivity.shared_하트상태변경(식별번호,true,getApplicationContext());
                }
                else{                                               //true
                    하트_view.setImageResource(favorite_empty);
                    MainActivity.shared_하트상태변경(식별번호,false,getApplicationContext());
                }
                break;
            case 1:
                /** 채팅하기 **/
                // 판매중이면 채팅하기
                if (판매중){
                    Intent intent = new Intent(Activity_PostToChat.this, Activity_chat.class);
                    intent.putExtra("식별번호",식별번호);
                    intent.putExtra("opp_id",login_id);
                    startActivity(intent);
                }
                break;
        }


    }


    public void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }





}