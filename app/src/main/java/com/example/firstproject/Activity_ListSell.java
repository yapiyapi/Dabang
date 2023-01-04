package com.example.firstproject;

import static com.example.firstproject.MainActivity.data_set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.firstproject.adpt.Adapter_List_Hide;
import com.example.firstproject.adpt.Adapter_List_Sell;
import com.example.firstproject.adpt.Adapter_List_SoldOut;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.firstproject.databinding.ActivityListSellBinding;
import com.google.gson.Gson;

public class Activity_ListSell extends AppCompatActivity {
    private ActivityListSellBinding binding;

    private RecyclerView sell_recy, sold_out_recy, hide_recy;
    private Button sell_btn, sold_out_btn, hide_btn;
    private View back_btn_sell_list;
    //어댑터 변수
    Adapter_List_Sell 어댑터_판매중;
    Adapter_List_SoldOut 어댑터_판매완료;
    Adapter_List_Hide 어댑터_숨김;
    LinearLayoutManager 레이아웃매니저;

    Gson gson;
    //셰어드
    SharedPreferences 게시글_s;SharedPreferences.Editor 게시글에디터;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_sell);

        initializeView();

        /** Adapter **/
        //어댑터 및 레이아웃매니저 생성
        레이아웃매니저 = new LinearLayoutManager(this);
        어댑터_판매중 = new Adapter_List_Sell();
        sell_recy.setLayoutManager(레이아웃매니저);
        sell_recy.setAdapter(어댑터_판매중);
        /** button **/
        // 리사이클러뷰 onclick시 화면전환
        어댑터_판매중.setOnItemClickListener(new Adapter_List_Sell.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String key) {
                Intent intent = new Intent( Activity_ListSell.this , Activity_PostToChat.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        }) ;
        sell_btn.setOnClickListener(new View.OnClickListener() {  //판매중
            @Override
            public void onClick(View view) {
                //상단바 글씨 크기
                Frame_Setting(0);
                //프레임 Visible 및 어댑터 연결
                Vis(0,sell_recy,sold_out_recy,hide_recy);
                //어댑터 연결
                레이아웃매니저 = new LinearLayoutManager(view.getContext());
                어댑터_판매중 = new Adapter_List_Sell();
                sell_recy.setLayoutManager(레이아웃매니저);
                sell_recy.setAdapter(어댑터_판매중);
                /** --------버튼 설정 ---------**/
                // 리사이클러뷰 onclick시 화면전환
                어댑터_판매중.setOnItemClickListener(new Adapter_List_Sell.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, String key) {
                        Intent intent = new Intent( Activity_ListSell.this , Activity_PostToChat.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                }) ;
            }
        });
        sold_out_btn.setOnClickListener(new View.OnClickListener() {  //판매완료
            @Override
            public void onClick(View view) {
                //상단바 글씨 크기
                Frame_Setting(1);
                //프레임 Visible 및 어댑터 연결
                Vis(1,sell_recy,sold_out_recy,hide_recy);
                //어댑터 연결
                레이아웃매니저 = new LinearLayoutManager(view.getContext());
                어댑터_판매완료 = new Adapter_List_SoldOut();
                sold_out_recy.setLayoutManager(레이아웃매니저);
                sold_out_recy.setAdapter(어댑터_판매완료);
                /** --------버튼 설정 ---------**/
                // 리사이클러뷰 onclick시 화면전환
                어댑터_판매완료.setOnItemClickListener(new Adapter_List_SoldOut.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, String key) {
                        Intent intent = new Intent( Activity_ListSell.this , Activity_PostToChat.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                }) ;
            }
        });
        hide_btn.setOnClickListener(new View.OnClickListener() {  //숨김
            @Override
            public void onClick(View view) {
                //상단바 글씨 크기
                Frame_Setting(2);
                //프레임 Visible 및 어댑터 연결
                Vis(2,sell_recy,sold_out_recy,hide_recy);
                //어댑터 연결
                레이아웃매니저 = new LinearLayoutManager(view.getContext());
                어댑터_숨김 = new Adapter_List_Hide();
                hide_recy.setLayoutManager(레이아웃매니저);
                hide_recy.setAdapter(어댑터_숨김);
                /** --------버튼 설정 ---------**/
                // 리사이클러뷰 onclick시 화면전환
                어댑터_숨김.setOnItemClickListener(new Adapter_List_Hide.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, String key) {
                        Intent intent = new Intent( Activity_ListSell.this , Activity_PostToChat.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                }) ;
            }
        });
        //뒤로 가기 버튼 [왼쪽 상단]
        back_btn_sell_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /** initial **/
    private void initializeView() {
        /** binding **/
        //각각 Recyclerview 화면
        sell_recy = binding.sellRecy;
        sold_out_recy = binding.soldOutRecy;
        hide_recy = binding.hideRecy;
        //상단 바 버튼
        sell_btn = binding.sellBtn;
        sold_out_btn = binding.soldOutBtn;
        hide_btn = binding.hideBtn;
        //뒤로가기 버튼
        back_btn_sell_list = binding.backBtnSellList;
    }

    /** method **/
    //상단바 글씨 크기
    public void Frame_Setting(int status){
        final int big_size = 16;
        final int small_size = 14;

        switch (status){
            case 0:
                sell_btn.setTextSize(big_size); sell_btn.setTypeface(null, Typeface.BOLD);
                sold_out_btn.setTextSize(small_size); sold_out_btn.setTypeface(null, Typeface.NORMAL);
                hide_btn.setTextSize(small_size); hide_btn.setTypeface(null, Typeface.NORMAL);
                break;
            case 1:
                sell_btn.setTextSize(small_size); sell_btn.setTypeface(null, Typeface.NORMAL);
                sold_out_btn.setTextSize(big_size); sold_out_btn.setTypeface(null, Typeface.BOLD);
                hide_btn.setTextSize(small_size); hide_btn.setTypeface(null, Typeface.NORMAL);
                break;
            case 2:
                sell_btn.setTextSize(small_size); sell_btn.setTypeface(null, Typeface.NORMAL);
                sold_out_btn.setTextSize(small_size); sold_out_btn.setTypeface(null, Typeface.NORMAL);
                hide_btn.setTextSize(big_size); hide_btn.setTypeface(null, Typeface.BOLD);
                break;
        }
    }
    //프레임 Visible
    public void Vis(int status, RecyclerView sell_recy, RecyclerView sold_out_recy,RecyclerView hide_recy){
        switch (status){
            case 0:
                //프레임 Visible
                sell_recy.setVisibility(View.VISIBLE);
                sold_out_recy.setVisibility(View.INVISIBLE);
                hide_recy.setVisibility(View.INVISIBLE);
                break;
            case 1:
                //프레임 Visible
                sell_recy.setVisibility(View.INVISIBLE);
                sold_out_recy.setVisibility(View.VISIBLE);
                hide_recy.setVisibility(View.INVISIBLE);
                break;
            case 2:
                //프레임 Visible
                sell_recy.setVisibility(View.INVISIBLE);
                sold_out_recy.setVisibility(View.INVISIBLE);
                hide_recy.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        dataBinding(getApplicationContext());
        data_set(getApplicationContext());
    }
}