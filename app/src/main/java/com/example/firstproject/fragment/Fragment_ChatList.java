package com.example.firstproject.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.firstproject.Activity_chat;
import com.example.firstproject.R;
import com.example.firstproject.adpt.Adapter_ChatList;
import com.example.firstproject.databinding.FragmentChatlistBinding;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;


public class Fragment_ChatList extends Fragment {
    private FragmentChatlistBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;

    RecyclerView 리싸이클러뷰;
    Adapter_ChatList 어댑터;LinearLayoutManager 레이아웃매니저;

    Gson gson;String member,login_id;
    private ArrayList<HashMap> chat_list;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chatlist, container,false);
        View view = binding.getRoot();

        initializeView();
        initializeProperty();

        /** 어댑터 연결 **/
        레이아웃매니저 = new LinearLayoutManager(getActivity());
        어댑터 = new Adapter_ChatList(chat_list);
        리싸이클러뷰.setLayoutManager(레이아웃매니저);
        리싸이클러뷰.setAdapter(어댑터);

        //리사이클러뷰 화면전환
        어댑터.setOnItemClickListener(new Adapter_ChatList.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String chat_id, String 식별번호) {
                Intent intent = new Intent(getActivity(), Activity_chat.class);
                intent.putExtra("식별번호",식별번호);
                intent.putExtra("opp_id",chat_id);
                startActivity(intent);
            }
        }) ;

        return view;
    }

    private void initializeView() {
        /** binding **/
        리싸이클러뷰 = binding.chatListRecy;
    }

    private void initializeProperty() {
        /** SharedPreferences **/
        게시글_s = getActivity().getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = getActivity().getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        /** gson **/
        gson = new GsonBuilder().create();
        /** 초기화 **/
        login_id = 로그인_s.getString("로그인회원","로그인회원 없음");
        if(!login_id.equals("로그인회원 없음")) {
            /** signup_member 객체 **/
            member = 회원정보_s.getString(login_id,"회원 없음");
            if(!member.equals("회원 없음")) {
                Signup_member signup_member = gson.fromJson(member, Signup_member.class);
                /** 초기화 **/
                chat_list = signup_member.get채팅목록_arr();

            }
        }else  chat_list = new ArrayList<>() ;


    }

}