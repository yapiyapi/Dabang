package com.example.firstproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.firstproject.adpt.Adapter_List_Buy;
import com.example.firstproject.databinding.ActivityListBuyBinding;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Activity_ListBuy extends AppCompatActivity {
    private ActivityListBuyBinding binding;
    private RecyclerView buy_recy;
    private View back_btn_buy_list;

    //어댑터 변수
    Adapter_List_Buy 어댑터;
    LinearLayoutManager 레이아웃매니저;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_buy);

        initializeView();

        /** Adapter **/
        //어댑터 및 레이아웃매니저 생성
        레이아웃매니저 = new LinearLayoutManager(this);
        어댑터 = new Adapter_List_Buy();
        buy_recy.setLayoutManager(레이아웃매니저);
        buy_recy.setAdapter(어댑터);
        /** button **/
        // 리사이클러뷰 onclick시 화면전환
        어댑터.setOnItemClickListener(new Adapter_List_Buy.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String key) {
                Intent intent = new Intent( Activity_ListBuy.this , Activity_PostToChat.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        }) ;
        //뒤로 가기 버튼 [왼쪽 상단]
        back_btn_buy_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /** initial **/
    private void initializeView() {
        /** binding **/
        buy_recy = binding.buyRecy;
        back_btn_buy_list = binding.backBtnBuyList;
    }

}