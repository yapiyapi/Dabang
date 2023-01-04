package com.example.firstproject;

import static com.example.firstproject.MainActivity.data_set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.firstproject.adpt.Adapter_ListHart;
import com.example.firstproject.databinding.ActivityListHartBinding;
import com.example.firstproject.object.Data;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import java.util.ArrayList;

public class Activity_ListHart extends AppCompatActivity {
    private ActivityListHartBinding binding;
    RecyclerView hart_recy;
    Adapter_ListHart 어댑터;
    LinearLayoutManager 레이아웃매니저;

    ArrayList<String> 관심_list = Data.관심_list;
    View back_btn_att_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_list_hart);

        initializeView();

        레이아웃매니저 = new LinearLayoutManager(this);
        어댑터 = new Adapter_ListHart();
        hart_recy.setLayoutManager(레이아웃매니저);
        hart_recy.setAdapter(어댑터);
        /** button **/
        //리사이클러뷰 화면전환
        어댑터.setOnItemClickListener(new Adapter_ListHart.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String key) {
                Intent intent = new Intent( Activity_ListHart.this , Activity_PostToChat.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        }) ;

        //뒤로 가기 버튼 [왼쪽 상단]
        back_btn_att_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    /** initial **/
    private void initializeView() {
        /** binding **/
        hart_recy = binding.attRecy;
        back_btn_att_list= binding.backBtnAttList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        data_set(getApplicationContext());
    }
}