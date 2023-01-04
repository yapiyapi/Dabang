package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.firstproject.adpt.Adapter_TraderChoice;
import com.example.firstproject.databinding.ActivityTraderChoiceBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.example.firstproject.object.Data;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Activity_TraderChoice extends AppCompatActivity {
    private ActivityTraderChoiceBinding binding;

    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;
    Gson gson;
    private String 이미지, 제목 , 가격;
    private ArrayList<String> 거래자_arr;
    String 게시물, 식별번호;
    private String 거래자_id;
    private ArrayList 구매목록_arr;

    RecyclerView trader_choice_recy;
    Adapter_TraderChoice 어댑터;
    LinearLayoutManager 레이아웃매니저;

    ImageView post_trader_img, back_btn_trader;
    TextView 완료_view,Title_trader,PriceInMonth_trader ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_trader_choice);

        initializeView();
        initializeProperty();
        data_set();

        /** Adapter **/
        trader_choice_recy = (RecyclerView)this.findViewById(R.id.trader_choice_recy);
        레이아웃매니저 = new LinearLayoutManager(this);
        어댑터 = new Adapter_TraderChoice();
        trader_choice_recy.setLayoutManager(레이아웃매니저);
        trader_choice_recy.setAdapter(어댑터);
        /** button **/
        어댑터.setOnItemClickListener(new Adapter_TraderChoice.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String id) {
                거래자_id = id;
            }
        });
        //완료 버튼 [우측 상단]
        완료_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // signup  구매목록_arr [ 식별번호, 식별번호 ..]
                /** Signup 객체 **/
                String 회원정보 = 회원정보_s.getString(거래자_id,"거래자 없음");
                if(!회원정보.equals("거래자 없음")) {
                    Signup_member signup_member = gson.fromJson(회원정보, Signup_member.class);
                    /** 초기화 **/
                    구매목록_arr = signup_member.get구매목록_arr();
                    구매목록_arr.add(식별번호);
                    회원정보 = gson.toJson(signup_member, Signup_member.class);
                    회원정보_e.putString(거래자_id,회원정보).apply();
                }
                /** Post 객체 **/
                String post = 게시글_s.getString(식별번호,"게시글 없음");
                if(!post.equals("게시글 없음")) {
                    Post post1 = gson.fromJson(post, Post.class);
                    /** 초기화 **/
                    if(거래자_id!=null) post1.set구매자(거래자_id);
                    post = gson.toJson(post1, Post.class);
                    게시글_e.putString(식별번호,post).apply();
                }
                finish();
            }
        });
        //뒤로 가기 버튼 [왼쪽 상단]
        back_btn_trader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /** initial **/
    private void initializeView() {
        /**   binding   **/
        post_trader_img = binding.postTraderImg;
        back_btn_trader = binding.backBtnTrader;
        Title_trader = binding.titleTrader;
        PriceInMonth_trader = binding.priceTrader;
        완료_view = binding.postTrader;
    }
    private void initializeProperty() {
        /**   SharedPreferences   **/
        게시글_s = getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        게시글_e = 게시글_s.edit();
        회원정보_e = 회원정보_s.edit();
        /** gson **/
        gson = new GsonBuilder().create();
        /** Intent **/
        Intent intent = getIntent();
        식별번호 = intent.getStringExtra("식별번호");
        /** Post 객체 **/
        게시물 = 게시글_s.getString(식별번호,"게시물 없음");
        if(!게시물.equals("게시물 없음")) {
            Post post = gson.fromJson(게시물, Post.class);
            /** 초기화 **/
            이미지 = MainActivity.getRealPathFromURI( this,Uri.parse(post.get이미지()));
            제목 = post.get제목();
            가격 = post.get가격();
            거래자_arr = post.get거래자();

            Glide.with(this).load(이미지).into(post_trader_img);
            Title_trader.setText(제목);
            PriceInMonth_trader.setText(가격);
        }
    }

    /** data_set **/
    private void data_set() {
        ArrayList<String> 거래자_list = Data.거래자_list;
        거래자_list.clear();
        /** 거래자_arr 초기화 **/ // [ id, id ..]
        for( String i : 거래자_arr)거래자_list.add(i);
    }

}