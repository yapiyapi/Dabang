package com.example.firstproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firstproject.adpt.Adapter_Chat;
import com.example.firstproject.adpt.Adapter_List_Buy;
import com.example.firstproject.databinding.ActivityChatBinding;
import com.example.firstproject.databinding.ActivityListBuyBinding;
import com.example.firstproject.object.Code;
import com.example.firstproject.object.Message;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Activity_chat extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ArrayList<Message> dataList;

    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;
    Gson gson;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private RecyclerView chat_recy;
    private ImageView post_btn,chatImg_img;
    private EditText post_Edit;
    private TextView seller_txt ,chatTitle_txt ,chatPrice_txt,chatCompleted_txt;

    private ArrayList<HashMap> chat_arr;
    private ArrayList<String> 거래자_arr;
    private String chat_txt ,login_id,opp_id,식별번호,작성자,이미지,
            member,게시글,content_img,content_title,content_price
            ,name,opp_name;
    private boolean 판매중;
    //어댑터 변수
    Adapter_Chat 어댑터;
    LinearLayoutManager 레이아웃매니저;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);

        initializeView();
        initializeProperty();
        Firebase();
        Log.i("a", "dataList.size()  "+String.valueOf(dataList.size()));
        /** Adapter **/
        //어댑터 및 레이아웃매니저 생성
        레이아웃매니저 = new LinearLayoutManager(this);
        어댑터 = new Adapter_Chat(dataList, login_id,이미지);
        chat_recy.setLayoutManager(레이아웃매니저);
        chat_recy.setAdapter(어댑터);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Write a message to the database
                Message msg = new Message();
                msg.setText(post_Edit.getText().toString());
                if (login_id.equals(작성자)) msg.setViewType(1);
                msg.set판매자(작성자);
                // Push to the database
                myRef.push().setValue(msg);

                post_Edit.setText("");
            }
        });

    }


    /** initial **/
    private void initializeView() {
        chat_recy = binding.chatRecy;
        post_Edit = binding.postEdit;
        post_btn = binding.postBtn;
        seller_txt = binding.sellerChat;
        chatImg_img = binding.chatImg;
        chatTitle_txt = binding.chatTitle;
        chatPrice_txt = binding.chatPrice;
        chatCompleted_txt = binding.chatCompleted;
    }
    private void initializeProperty() {
        dataList = new ArrayList<>();
        /**   SharedPreferences   **/
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getSharedPreferences("LoginUser", MODE_PRIVATE);
        게시글_s = getSharedPreferences("post", MODE_PRIVATE);
        게시글_e = 게시글_s.edit(); //Editor
        회원정보_e = 회원정보_s.edit(); //Editor
        /**   json   **/
        gson = new GsonBuilder().create();
        /**   Intent   **/
        Intent intent = getIntent();
        opp_id = intent.getStringExtra("opp_id");
        식별번호 = intent.getStringExtra("식별번호");
        //post 저장한 회원 id 값
        login_id = 로그인_s.getString("로그인회원","로그인 정보 없음");
        //객체 만들기
        게시글 = 게시글_s.getString(식별번호,"게시글 데이터 없음");
        if (!게시글.equals("게시글 데이터 없음")) {
            Post post = gson.fromJson(게시글, Post.class);
            /** 초기화 **/
            content_img = MainActivity.getRealPathFromURI(this, Uri.parse(post.get이미지()));
            content_title = post.get제목();
            content_price = post.get가격();
            작성자=post.get작성자();
            판매중=post.is판매중();
        }
        member = 회원정보_s.getString(작성자, "회원 없음");
        if (!member.equals("회원 없음")) {
            Signup_member signup_member = gson.fromJson(member, Signup_member.class);
            name = signup_member.getName();
        }
        member = 회원정보_s.getString(opp_id, "회원 없음");
        if (!member.equals("회원 없음")) {
            Signup_member signup_member = gson.fromJson(member, Signup_member.class);
            이미지 = MainActivity.getRealPathFromURI(this,Uri.parse(signup_member.getThumnail()));
            opp_name = signup_member.getName();
        }

        Glide.with(this).load(content_img).into(chatImg_img);
        if(login_id.equals(작성자)) seller_txt.setText(opp_name);
        else seller_txt.setText(name);
        chatTitle_txt.setText(content_title);
        chatPrice_txt.setText(content_price);
        if (!판매중) chatCompleted_txt.setVisibility(View.VISIBLE);

    }
    private void Firebase() {
        String format;
        database = FirebaseDatabase.getInstance();
        if (opp_id.equals(작성자)) format = String.format("%s,%s",login_id,작성자);
        else format = String.format("%s,%s",opp_id,작성자);
        myRef = database.getReference(식별번호).child(format);


        if(작성자.equals(opp_id)) opp_id = login_id;
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message ms = snapshot.getValue(Message.class); //자동으로 데이터 갯수만큼 읽어옴
                ((Adapter_Chat)어댑터).addChat(ms);
                // chat_arr 만들기 ( login_id )
                sharedArrAdd(opp_id, 작성자,식별번호);
                // chat_arr 만들기 ( 판매자 )
                sharedArrAdd(작성자, opp_id,식별번호);
                // 거래자 list 만들기
                sharedArrAdd(식별번호, opp_id);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot){}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName){}
            @Override
            public void onCancelled(@NonNull DatabaseError error){}
        });
    }

    public void sharedArrAdd(String id, String Add_id, String 식별번호){

        member = 회원정보_s.getString(id, "회원 없음");
        if (!member.equals("회원 없음")) {
            Signup_member signup_member = gson.fromJson(member, Signup_member.class);
            chat_arr = signup_member.get채팅목록_arr();
            HashMap<String, Object> map = new HashMap<>();
            map.put(Add_id, 식별번호);
            if (!chat_arr.contains(map)) chat_arr.add(map);
            signup_member.set채팅목록_arr(chat_arr);
            member = gson.toJson(signup_member, Signup_member.class);
            회원정보_e.putString(id, member).apply();
        }
    }
    public void sharedArrAdd(String 식별번호, String Add_id){
        게시글 = 게시글_s.getString(식별번호, "게시글 없음");
        if (!게시글.equals("게시글 없음")) {
            Post post = gson.fromJson(게시글, Post.class);
            거래자_arr = post.get거래자();
            // 거래자 id 가 없으면 추가
            if (!거래자_arr.contains(Add_id))거래자_arr.add(Add_id);
            post.set거래자(거래자_arr);
            게시글 = gson.toJson(post, Post.class);
            게시글_e.putString(식별번호, 게시글).apply();
        }
    }
}