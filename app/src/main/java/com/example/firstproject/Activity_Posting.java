package com.example.firstproject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.example.firstproject.databinding.ActivityPostingBinding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firstproject.object.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Activity_Posting extends AppCompatActivity {
    private ActivityPostingBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;

    ImageView back_btn_reg_view;ImageView 이미지_view;
    TextView 완료_view, 제목_view, 주소_view, 상세주소_view, 가격_view, 내용_view;
    Button 주소검색_view;

    Gson gson;String 게시글,login_id;

    private String 이미지, 제목, 주소, 상세주소, 가격 ,내용, 판매상태, 식별번호, id, 식별부호_시간;
    private boolean 관심여부 ;
    private double lat,log ;

    Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_posting);

        initializeView();
        initializeProperty();


        /**   버튼    **/
        //뒤로 가기 버튼 [왼쪽 상단]
        back_btn_reg_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //완료 (게시하기) -> 홈으로 내용 전달
        //및 수정 (수정하기) -> 해당 주소의 데이터 값 변경 (replace)
        완료_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                제목 = 제목_view.getText().toString();
                주소 = 주소_view.getText().toString();
                상세주소 = 상세주소_view.getText().toString();
                가격 = 가격_view.getText().toString();
                내용 = 내용_view.getText().toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    식별부호_시간 = CurrentTime();
                }
                //완료
                if(완료_view.getText()!="수정"){
                    if ( uri == null ) Toast.makeText(Activity_Posting.this, "이미지를 추가 해주세요.", Toast.LENGTH_SHORT).show();
                    else if ( 제목.equals("")|| 제목.equals(" ") ) Toast.makeText(Activity_Posting.this, "제목을 추가 해주세요.", Toast.LENGTH_SHORT).show();
                    else if ( 주소.equals("")|| 주소.equals(" ")  ) Toast.makeText(Activity_Posting.this, "주소를 추가 해주세요.", Toast.LENGTH_SHORT).show();
                    else if ( 가격.equals("")|| 가격.equals(" ")  ) Toast.makeText(Activity_Posting.this, "가격을 추가 해주세요.", Toast.LENGTH_SHORT).show();
                    else {
                        //shared
                        Post(식별부호_시간, uri.toString(),제목,주소,상세주소,가격,내용);
                        finish();}}
                //수정
                else{
                    //이미지
                    String 게시물 = 게시글_s.getString(식별번호,"게시물 없음");
                    if(!게시물.equals("게시물 없음")){
                        Post post = gson.fromJson(게시물,Post.class);
                        이미지 = post.get이미지();
                    }
                    // uri 없으면 shared 에서 이미지 가져옴
                    String img;
                    if (uri == null) img= 이미지;
                    else img = uri.toString();
                    //shared
                    Post(식별번호, img, 제목, 주소, 상세주소, 가격, 내용);
                    finish();
                }

            }
        });
        주소검색_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Posting.this , Activity_Map.class);
                mMapLauncher.launch(intent);
            }
        });
        //이미지 받아오기
        이미지_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                imgLauncher.launch(intent);
            }
        });
    }

    /** initial **/
    private void initializeView() {
        /**   binding   **/
        back_btn_reg_view = binding.backBtnTrader;
        완료_view = binding.postReg;
        이미지_view = binding.imageplusReg;
        제목_view = binding.titleReg;
        주소_view = binding.locationReg;
        상세주소_view = binding.buildingReg;
        가격_view = binding.priceReg;
        내용_view = binding.contentsReg;
        주소검색_view = binding.findLocationBtn;
    }
    private void initializeProperty() {
        /**   SharedPreferences   **/
        로그인_s = getSharedPreferences("LoginUser", MODE_PRIVATE);
        게시글_s = getSharedPreferences("post", MODE_PRIVATE);
        게시글_e = 게시글_s.edit(); //Editor
        /**   json   **/
        gson = new GsonBuilder().create();
        /**   Intent   **/
        //수정시 받을 주소 데이터 ( fr. Adapter_ListSell )
        Intent 수정인텐트 = getIntent();
        식별번호 = 수정인텐트.getStringExtra("식별부호");
        //post 저장한 회원 id 값
        login_id = 로그인_s.getString("로그인회원","로그인 정보 없음");
        //객체 만들기
        게시글 = 게시글_s.getString(식별번호,"게시물 데이터 없음");
        if (!게시글.equals("게시물 데이터 없음")){
            Post post = gson.fromJson(게시글,Post.class);
            /** 초기화 **/
            이미지 = MainActivity.getRealPathFromURI(this,Uri.parse(post.get이미지()));
            제목 = post.get제목();
            주소 = post.get주소();
            상세주소 = post.get상세주소();
            가격 = post.get가격();
            내용 = post.get내용();
            /** 초기화 **/
            Glide.with(Activity_Posting.this).load(이미지).into(이미지_view);
            제목_view.setText(제목);
            주소_view.setText(주소);
            상세주소_view.setText(상세주소);
            가격_view.setText(가격);
            내용_view.setText(내용);
            완료_view.setText("수정");
        }
    }

    public void Post(String 식별부호, String set이미지 , String set제목, String set주소, String set상세주소, String set가격, String set내용 ) {

        if (게시글_s.contains(식별부호)){ // 식별부호 있으면 수정
            게시글 = 게시글_s.getString(식별부호, "게시물 없음");
            if (!게시글.equals("게시물 없음")) {
                Post post = gson.fromJson(게시글, Post.class);
                post.set이미지(set이미지);
                post.set제목(set제목);
                post.set주소(set주소);
                post.set상세주소(set상세주소);
                if (lat!=0.0 && log!=0.0){
                    post.setLat(lat);
                    post.setLog(log);
                }
                post.set가격(set가격);
                post.set내용(set내용);
                게시글 = gson.toJson(post, Post.class);
                게시글_e.putString(식별부호, 게시글).apply();
            }
        }else { // 없으면 저장
            Post post = new Post(set이미지, set제목, set주소, set상세주소, lat, log, set가격, set내용, 식별부호, login_id, "", false, true,false, new ArrayList());
            게시글 = gson.toJson(post, Post.class);
            게시글_e.putString(식별부호, 게시글).apply();
        }
    }

    /** registerForActivityResult **/
    ActivityResultLauncher<Intent> mMapLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        String location_name = intent.getStringExtra("location_name");
                        lat = intent.getDoubleExtra("lat",0);
                        log = intent.getDoubleExtra("log",0);
                        주소_view.setText(location_name);
                    }
                }
            });
    ActivityResultLauncher<Intent> imgLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent intent = result.getData();
                        uri = intent.getData();
                        Glide.with(Activity_Posting.this)
                                .load(uri)
                                .into(이미지_view);
                    }
                }
            });


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String CurrentTime (){
        // 현재 시간
        LocalTime now = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        String formatedNow = now.format(formatter);
        return formatedNow;
    }

}