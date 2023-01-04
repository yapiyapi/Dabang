package com.example.firstproject.adpt;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.Activity_PostToChat;
import com.example.firstproject.Activity_chat;
import com.example.firstproject.MainActivity;
import com.example.firstproject.R;
import com.example.firstproject.object.ADV;
import com.example.firstproject.object.Data;
import com.example.firstproject.object.Message;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_ChatList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<HashMap> myChatList = null;
    FirebaseDatabase database;
    DatabaseReference myRef;

    SharedPreferences 게시글_s, 회원정보_s,로그인_s;
    Gson gson;String chat_id;
    String 식별번호,게시글,member,login_id,key;
    Query query;
    private String name,썸네일, 작성자,이미지;
    private boolean 판매중;

    public Adapter_ChatList(ArrayList chat_list) {
        myChatList = chat_list;
    }

    //  --------------------------onclick --------------
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String chat_id ,String key);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View 뷰 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_chatlist, parent,false);
        return new CustomViewHolder(뷰);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /**   SharedPreferences   **/
        게시글_s = holder.itemView.getContext().getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = holder.itemView.getContext().getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = holder.itemView.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
        /** gson **/
        gson = new GsonBuilder().create();
        //객체 만들기
        login_id = 로그인_s.getString("로그인회원","비회원");
        /** Signup_member 객체 **/ // 작성자 썸네일, 작성자_id
        Iterator<String> key = myChatList.get(position).keySet().iterator();
        chat_id = key.next();
        식별번호 = (String) myChatList.get(position).get(chat_id);
        member = 회원정보_s.getString(chat_id,"chatList 없음");
        if(!member.equals("chatList 없음")) {
            Signup_member signupMember = gson.fromJson(member, Signup_member.class);
            /** 초기화 **/
            썸네일 = MainActivity.getRealPathFromURI(holder.itemView.getContext(),Uri.parse(signupMember.getThumnail()));
            name = signupMember.getName();
            if(썸네일!=null) Glide.with(holder.itemView).load(썸네일).into(((CustomViewHolder) holder).썸네일_view);
            ((CustomViewHolder)holder).판매자_view.setText(name);
        }
        /** Post 객체 **/ // 게시글 이미지
        게시글 = 게시글_s.getString(식별번호,"게시글 없음");
        if(!게시글.equals("게시글 없음")) {
            Post post = gson.fromJson(게시글, Post.class);
            /** 초기화 **/
            이미지 = MainActivity.getRealPathFromURI(holder.itemView.getContext(), Uri.parse(post.get이미지()));
            작성자 = post.get작성자();
            판매중 = post.is판매중();
            if(이미지!=null) Glide.with(holder.itemView).load(이미지).into(((CustomViewHolder) holder).이미지_view);
        }
        /** 내용 **/
        Firebase(((CustomViewHolder)holder).내용_view);
    }

    @Override
    public int getItemCount() {
        return myChatList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //    ----------------뷰홀더-------------------
    public class CustomViewHolder extends RecyclerView.ViewHolder{
        CircleImageView 썸네일_view;
        ImageView 이미지_view;
        TextView 판매자_view, 내용_view;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            썸네일_view =(CircleImageView) itemView.findViewById(R.id.chatlist_thumnail);
            판매자_view =(TextView)itemView.findViewById(R.id.chatlist_seller);
            내용_view =(TextView)itemView.findViewById(R.id.chatlist_content);
            이미지_view =(ImageView)itemView.findViewById(R.id.chatlist_imageview);

            /** Click **/
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int posi = getAdapterPosition();
                    /** 게시물 식별번호 **/
                    Iterator<String> key = myChatList.get(posi).keySet().iterator();
                    chat_id = key.next();
                    식별번호 = (String) myChatList.get(posi).get(chat_id);
                    if (posi != RecyclerView.NO_POSITION)
                    {
                        if (mListener != null) {
                            mListener.onItemClick(v, chat_id, 식별번호) ;
                        }
                    }
                }
            });
        }
    }

    private void Firebase(TextView a) {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(식별번호);

        if (login_id.equals(작성자)) key = String.format("%s,%s",chat_id,작성자);  // [ login_id , 작성자 ]
        else key = String.format("%s,%s",login_id,작성자);  // [ login_id , 작성자 ]
        myRef = myRef.child(key);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message ms = snapshot.getValue(Message.class); //자동으로 데이터 갯수만큼 읽어옴
                a.setText(ms.getText());
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
}
