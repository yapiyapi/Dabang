package com.example.firstproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.firstproject.databinding.ActivityAccountBinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Account extends AppCompatActivity {
    private ActivityAccountBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;
    private String 썸네일, 아이디, 이름, 이메일, 전화번호, 비밀번호;
    private String login_id;

    TextView 아이디텍스트_view,닉네임텍스트_view,이메일텍스트_view,전화번호텍스트_view,로그아웃_view;
    EditText 아이디에딧텍스트_view,닉네임에딧텍스트_view,이메일에딧텍스트_view,전화번호에딧텍스트_view;
    Button 아이디수정_view,닉네임수정_view,이메일수정_view,전화번호수정_view;
    ImageView 백버튼;
    CircleImageView 아이콘;

    Gson gson;String 회원;
    Signup_member signup_member;
    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_account);


        initializeView();
        initializeProperty();

        /** 버튼 **/
        백버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        아이콘.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                launcher.launch(intent);
            }
        });

        //닉네임 수정 버튼 로직
        닉네임수정_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button("닉네임",닉네임텍스트_view,닉네임에딧텍스트_view,닉네임수정_view);
            }
        });
        //이메일 수정 버튼 로직
        이메일수정_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button("이메일",이메일텍스트_view,이메일에딧텍스트_view,이메일수정_view);
            }
        });
        //전화번호 수정 버튼 로직
        전화번호수정_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button("전화번호", 전화번호텍스트_view, 전화번호에딧텍스트_view, 전화번호수정_view);
            }
        });
        //아이디 수정 버튼 로직
        아이디수정_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button("아이디",아이디텍스트_view,아이디에딧텍스트_view,아이디수정_view);
            }
        });
        //로그아웃
        로그아웃_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                로그인_e.clear().apply();

                Intent intent = new Intent(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    /** initial **/
    private void initializeView(){
        /** binding **/
        아이디수정_view = binding.idChangeBtn;
        닉네임수정_view = binding.nameChangeBtn;
        이메일수정_view = binding.emailChangeBtn;
        전화번호수정_view = binding.phNumChangeBtn;
        아이디텍스트_view = binding.textviewId;
        닉네임텍스트_view = binding.textviewName;
        이메일텍스트_view = binding.textviewEmail;
        전화번호텍스트_view = binding.textviewPhoneNum;
        아이디에딧텍스트_view = binding.edittextId;
        닉네임에딧텍스트_view = binding.edittextName;
        이메일에딧텍스트_view = binding.edittextEmail;
        전화번호에딧텍스트_view = binding.edittextPhoneNum;

        로그아웃_view = binding.textviewLogout;

        백버튼 = binding.backBtnAccount;
        아이콘 = binding.iconAccount;
    }
    private void initializeProperty() {
        /** SharedPreferences **/
        게시글_s = getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getSharedPreferences("LoginUser", MODE_PRIVATE);
        게시글_e = 게시글_s.edit();
        회원정보_e = 회원정보_s.edit();
        로그인_e = 로그인_s.edit();
        /** gson **/
        gson = new GsonBuilder().create();
        //객체 만들기
        login_id = 로그인_s.getString("로그인회원","비회원");
        회원 = 회원정보_s.getString(login_id,"비회원");//json
        if(!회원.equals("비회원")){
            Signup_member signup_member = gson.fromJson(회원,Signup_member.class); // 회원정보 객체

            썸네일 = MainActivity.getRealPathFromURI(this,Uri.parse(signup_member.getThumnail()));
            이름 = signup_member.getName();
            이메일 = signup_member.getE_mail();
            전화번호 = signup_member.getPhone_num();
            비밀번호 = signup_member.getPassword();
            /** 초기화 **/
            if (썸네일!=null) Glide.with(this).load(썸네일).into(아이콘);
            아이디텍스트_view.setText(login_id);
            닉네임텍스트_view.setText(이름);
            이메일텍스트_view.setText(이메일);
            전화번호텍스트_view.setText(전화번호);
        }

    }
    private void initializeIcon() {
        회원 = 회원정보_s.getString(login_id,"비회원");//json
        if(!회원.equals("회원정보 없음")) {
            signup_member = gson.fromJson(회원, Signup_member.class); // 회원정보 객체
            signup_member.setThumnail(String.valueOf(uri));
        }
        회원 = gson.toJson(signup_member, Signup_member.class);
        회원정보_e.putString(login_id,회원).apply();
    }

    /** button **/
    private void button(String 수정 ,TextView text , EditText editText , Button bt){
        if(bt.getText().toString().equals("수정")){// 수정하고 싶을 때
            수정(text,editText,bt);
        }
        else{ // 수정 다하고 확인
            확인(text,editText,bt);
            shared_수정(수정,editText);
        }
    }

    /** 수정 및 확인 로직 **/
    public void 수정( TextView text , EditText editText , Button bt){
        String 수정값 = text.getText().toString();
        editText.setText(수정값);
        text.setVisibility(View.INVISIBLE);
        editText.setVisibility(View.VISIBLE);
        bt.setText("확인");
    }
    public void 확인( TextView text , EditText editText , Button bt){
        String 수정값 = editText.getText().toString();
        text.setText(수정값);
        text.setVisibility(View.VISIBLE);
        editText.setVisibility(View.INVISIBLE);
        bt.setText("수정");
    }
    public void shared_수정(String 수정,EditText editText){

        회원 = 회원정보_s.getString(login_id,"비회원");
        if(!회원.equals("비회원")){
            signup_member = gson.fromJson(회원,Signup_member.class);

            switch (수정){
                case "닉네임":
                    signup_member.setName(editText.getText().toString());
                    break;
                case "이메일":
                    signup_member.setE_mail(editText.getText().toString());
                    break;
                case "전화번호":
                    signup_member.setPhone_num(editText.getText().toString());
                    break;
                case "아이디":
                    아이디 = editText.getText().toString();
                    회원정보_e.remove(login_id).apply(); //삭제
                    회원정보_e.putString(아이디,회원).apply();

                    로그인_e.putString("로그인회원",아이디).apply();
                    break;
                case "썸네일":
                    signup_member.setThumnail(uri.toString());
                    break;
            }
            if(!수정.equals("아이디")){
                회원 = gson.toJson(signup_member, Signup_member.class);
                회원정보_e.putString(login_id,회원).apply();
            }
        }
    }
    /** launcher **/
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        uri = intent.getData();
                        Glide.with(Activity_Account.this)
                                .load(uri)
                                .into(아이콘);
                        shared_수정("썸네일",null);
                        initializeIcon();
                    }
                }
            });
}