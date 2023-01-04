package com.example.firstproject.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.example.firstproject.MainActivity.data_set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.firstproject.Activity_PostToChat;
import com.example.firstproject.Activity_Posting;
import com.example.firstproject.R;
import com.example.firstproject.adpt.Adapter_Home;
import com.example.firstproject.databinding.FragmentHomeBinding;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;


public class Fragment_Home extends Fragment {
    private FragmentHomeBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;

    CircleImageView 플러스버튼;
    TextView name_home;
    RecyclerView 리싸이클러뷰;
    Adapter_Home 어댑터;LinearLayoutManager 레이아웃매니저;
    private boolean 로그인여부;
    Parcelable thread;

    Gson gson;String 회원,login_id,닉네임;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container,false);
        View view = binding.getRoot();

        initializeView();
        initializeProperty();


////        초기화
//        게시글_e.clear().apply();
//        회원정보_e.clear().apply(ㅂ);
//        로그인_e.clear().apply();

//        Iterator iterator = 회원정보_s.getAll().keySet().iterator();
//        while (iterator.hasNext()){
//            String key = (String) iterator.next();
//            /** Signup_member 객체 **/
//            String signup = 회원정보_s.getString(key,"회원 없음");
//            if(!signup.equals("회원 없음")) {
//                Signup_member member = gson.fromJson(signup, Signup_member.class);
//                member.set관심목록_arr(new ArrayList<>());
//                member.set구매목록_arr(new ArrayList<>());
//                member.set채팅목록_arr(new ArrayList<>());
//                signup = gson.toJson(member, Signup_member.class);
//                회원정보_e.putString(key,signup).apply();
//            }
//        }


        /** 어댑터 연결 **/
        리싸이클러뷰 = (RecyclerView) view.findViewById(R.id.recy);
        레이아웃매니저 = new LinearLayoutManager(getActivity());
        어댑터 = new Adapter_Home();
        리싸이클러뷰.setLayoutManager(레이아웃매니저);
        리싸이클러뷰.setAdapter(어댑터);

        //리사이클러뷰 화면전환
        어댑터.setOnItemClickListener(new Adapter_Home.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String key) {
                Intent intent = new Intent(getActivity(), Activity_PostToChat.class);
                intent.putExtra("key", key);
                startActivity(intent);
            }
        }) ;

        /** 버튼 **/
        //게시물 추가 [오른쪽 아래]
        플러스버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (로그인여부==false) Toast.makeText(getActivity(), "로그인 후 사용바랍니다.", Toast.LENGTH_SHORT).show();
                else{
                    Intent intent = new Intent(getActivity(), Activity_Posting.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void initializeView() {
        /** binding **/
        플러스버튼 = binding.plusBtnHome;
        name_home = binding.nameHome;
    }

    private void initializeProperty() {
        /** SharedPreferences **/
        게시글_s = getActivity().getSharedPreferences("post", MODE_PRIVATE);
        게시글_e = 게시글_s.edit(); //Editor
        회원정보_s = getActivity().getSharedPreferences("signup", MODE_PRIVATE);
        회원정보_e = 회원정보_s.edit(); //Editor
        로그인_s = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        로그인_e = 로그인_s.edit(); //Editor
        /** gson **/
        gson = new GsonBuilder().create();
        login_id = 로그인_s.getString("로그인회원","로그인 정보 없음");
        회원 = 회원정보_s.getString(login_id,"회원 없음");
        if (!회원.equals("회원 없음")) {
            Signup_member signup_member = gson.fromJson(회원, Signup_member.class);
            닉네임 = signup_member.getName();
        }
        /** 플러스버튼 **/
        로그인여부 = !로그인_s.getAll().isEmpty();
        if(!로그인여부) 플러스버튼.setVisibility(View.GONE);
        else {
            name_home.setVisibility(View.VISIBLE);
            name_home.setText(닉네임);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        data_set(getActivity());
        관심_binding();
    }
    // 회원정보에 저장된 관심목록을 게시물에 관심여부에 반영
    public void 관심_binding(){ // signup 의 관심목록_arr[식별부호] -> post 의 관심여부
        Gson gson;

        String 회원, 아이디, 복사될게시글 , 복사한게시글;

        SharedPreferences 게시글_s,회원정보_s, 로그인_s;
        /** SharedPreferences **/
        게시글_s = getActivity().getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = getActivity().getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        SharedPreferences.Editor 게시글에디터 = 게시글_s.edit();
        /** gson **/
        gson = new GsonBuilder().create();
        /** post 모든 값 false 초기화 **/
        Iterator iterator = 게시글_s.getAll().keySet().iterator();
        while(iterator.hasNext()){
            String 식별번호 = (String) iterator.next();
            String 게시글 = (String) 게시글_s.getString(식별번호,"게시글 없음");
            //객체 만들기
            Post post = gson.fromJson(게시글,Post.class);
            post.set관심여부(false);  // 관심 여부 모두 false
            게시글 = gson.toJson(post, Post.class);
            게시글에디터.putString(식별번호,게시글).apply();
        }
        /** 회원정보[관심_arr] -> true 복사 **/
        아이디 = 로그인_s.getString("로그인회원","정보 없음");
        회원 = 회원정보_s.getString(아이디,"비회원");
        if(!회원.equals("비회원")){
            //[name, e-mail, phone_num, password , thumnail, 관심목록_arr]
            Signup_member signup_member = gson.fromJson(회원,Signup_member.class);
            /** 회원 관심목록 **/ // [식별부호, false] , [식별부호, false] ..
            ArrayList<String> 관심목록_arr = signup_member.get관심목록_arr();
            /** 복사 **/
            for(String 식별번호 : 관심목록_arr) { //for문을 통한 전체출력
                복사될게시글 = 게시글_s.getString(식별번호,"게시글 없음"); //[014223]
                if(!복사될게시글.equals("게시글 없음")){
                    Post post = gson.fromJson(복사될게시글,Post.class);
                    post.set관심여부(true); // 관심 여부 true
                    복사한게시글 = gson.toJson(post, Post.class);
                    게시글에디터.putString(식별번호,복사한게시글).apply();
                }
            }
        }
    }
}