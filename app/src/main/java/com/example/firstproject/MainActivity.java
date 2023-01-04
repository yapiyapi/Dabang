package com.example.firstproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import com.example.firstproject.databinding.ActivityMainBinding;
import com.example.firstproject.fragment.Fragment_ChatList;
import com.example.firstproject.fragment.Fragment_Home;
import com.example.firstproject.fragment.Fragment_Map;
import com.example.firstproject.fragment.Fragment_ViewMore;
import com.example.firstproject.object.Data;
import com.example.firstproject.object.Post;
import com.example.firstproject.object.Signup_member;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    Fragment_Home home_f;
    Fragment_Map map_f;
    Fragment_ChatList chat_f;
    Fragment_ViewMore 더보기_f;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;
    Gson gson;
    private String 회원, login_id;
    private boolean isLogin;

    TextView 로그인시간;
    ImageButton home_btn, lct_btn, chat_btn, inf_btn;

    ProgressDialog customProgressDialog;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initializeView();
        initializeProperty();

        // Home 화면으로 이동
        MvToFragment(0);
        /** 권한요청 **/
        권한요청();
        /** button **/
        home_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MvToFragment(0);}
        });
        lct_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MvToFragment(1);}
        });
        chat_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MvToFragment(2);}
        });
        inf_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MvToFragment(3);}
        });
    }

    public static void data_set (Context context){ // [ id ,  [ 식별번호 , 판매상태 ] ] 로 만들어 줌
        ArrayList<String> 전체_data_arr = Data.전체_data_arr;
        HashMap<String , HashMap<String, ArrayList>> 전체_data = Data.전체_data;
        ArrayList<String> 판매중_list = Data.판매중_list;
        ArrayList<String> 판매완료_list = Data.판매완료_list;
        ArrayList<String> 숨김_list = Data.숨김_list;
        ArrayList<String> 관심_list = Data.관심_list;
        ArrayList<String> 구매_list = Data.구매_list;
        ArrayList<String> 관심목록, 구매목록;
        Gson gson;
        /**   SharedPreferences   **/
        SharedPreferences 게시글_s = context.getSharedPreferences("post", MODE_PRIVATE);
        SharedPreferences 회원정보_s = context.getSharedPreferences("signup", MODE_PRIVATE);
        SharedPreferences 로그인_s = context.getSharedPreferences("LoginUser", MODE_PRIVATE);
        /** login_id **/
        String login_id = 로그인_s.getString("로그인회원","로그인회원 없음");
        /** gson **/
        gson = new GsonBuilder().create();
        /** 초기화 **/
        구매_list.clear();
        전체_data_arr.clear();
        전체_data_arr.add("http://www.payco.com");
        판매중_list.clear();
        판매완료_list.clear();
        숨김_list.clear();
        관심_list.clear();
        /** Signup_member 초기화 **/ // [ 식별번호, 식별번호 ..]
        String 회원정보 = 회원정보_s.getString(login_id,"회원정보 없음");
        if(!회원정보.equals("회원정보 없음")) {
            Signup_member signup_member = gson.fromJson(회원정보, Signup_member.class);
            /** 관심_list 초기화 **/ // [ 식별번호, 식별번호 ..]
            관심목록 = signup_member.get관심목록_arr();
            for (String i : 관심목록) 관심_list.add(i);
            /** 구매_list 초기화 **/ // [ 식별번호, 식별번호 ..]
            구매목록 = signup_member.get구매목록_arr();
            for (String i : 구매목록) 구매_list.add(i);
        }
        /** Post 초기화 **/
        Iterator iterator = 게시글_s.getAll().keySet().iterator();
        while (iterator.hasNext()) {
            String 식별번호 = (String) iterator.next();                // 식별부호
            String value = 게시글_s.getString(식별번호,"데이터 없음");
            //객체 만들기
            Post post = gson.fromJson(value,Post.class);
            String id = post.get작성자();                             // id

            boolean 판매중 = post.is판매중();                    // 판매상태
            boolean 숨김 = post.is숨김();                    // 숨김상태

            /** 전체_data_arr 초기화 **/ // [ 식별번호, 식별번호 ..]
            if(!숨김==true) 전체_data_arr.add(식별번호);
            /** 전체_data 초기화 **/ // id : 판매중 [] 판매완료 [] 숨김 []
            //전체_data [ id : 판매중 [] 판매완료 [] 숨김 []  ]
            if(!전체_data.containsKey(id)){
                전체_data.put(id,new HashMap<String, ArrayList>(){{
                    put("판매중", new ArrayList());
                    put("판매완료", new ArrayList());
                    put("숨김", new ArrayList());
                }});
            }
            HashMap<String, ArrayList> hash = 전체_data.get(id);
            ArrayList<String> 판매중_arr = hash.get("판매중");
            ArrayList<String> 판매완료_arr = hash.get("판매완료");
            ArrayList<String> 숨김_arr = hash.get("숨김");

            if(숨김==true) {       //숨김
                숨김_arr.add(식별번호);
                hash.put("숨김",숨김_arr);     //hash     완성
            }else{
                if(판매중==true) {    //판매중
                    판매중_arr.add(식별번호);        //식별_arr 완성
                    hash.put("판매중",판매중_arr);     //hash     완성
                }
                else {               //판매완료
                    판매완료_arr.add(식별번호);         //식별_arr 완성
                    hash.put("판매완료",판매완료_arr);     //hash     완성
                }
            }
            전체_data.put(id, hash);    //전체_data 완성


            if (login_id.equals(id)){
                /** Array_판매상태 초기화 **/ // [ 식별번호, 식별번호 ..]
                if(숨김==true) 숨김_list.add(식별번호);     //숨김
                else{
                    if(판매중==true) 판매중_list.add(식별번호);
                    else 판매완료_list.add(식별번호);
                }
            }
        }
//        Log.i("a", String.valueOf(전체_data_arr));
    }
    // signup 의 관심목록_arr[식별부호, bool] / post 의 관심여부 둘다 수정
    public static void shared_하트상태변경(String 식별번호, boolean bool, Context context){
        SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
        SharedPreferences.Editor 회원정보에디터, 게시글에디터;

        Gson gson;String 게시물;
        String 복사될회원정보,복사한회원정보;
        /**   SharedPreferences   **/
        게시글_s = context.getSharedPreferences("post", MODE_PRIVATE);
        회원정보_s = context.getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = context.getSharedPreferences("LoginUser", MODE_PRIVATE);
        회원정보에디터 = 회원정보_s.edit(); //Editor
        게시글에디터 = 게시글_s.edit(); //Editor

        String 로그인회원_id = 로그인_s.getString("로그인회원","로그인 회원 없음");
        /**   json   **/
        gson = new GsonBuilder().create();
        /**   회원정보 객체 만들기   **/
        //[name, e-mail, phone_num, password , thumnail, 관심목록_arr]
        복사될회원정보 = 회원정보_s.getString(로그인회원_id, "회원 없음");
        if(!복사될회원정보.equals("회원 없음")){
            Signup_member signup_member = gson.fromJson(복사될회원정보, Signup_member.class);
            //관심목록_arr [ (식별번호  관심), (식별번호  관심) .. ]
            ArrayList<String> 관심목록_arr = signup_member.get관심목록_arr();
            if (bool == true) 관심목록_arr.add(0,식별번호);
            else 관심목록_arr.remove(식별번호);
            signup_member.set관심목록_arr(관심목록_arr);
            복사한회원정보 = gson.toJson(signup_member, Signup_member.class);
            회원정보에디터.putString(로그인회원_id, 복사한회원정보).apply();
        }

        /**   Post 객체 만들기   **/
        게시물 = 게시글_s.getString(식별번호, "게시물 없음");
        if (!게시물.equals("게시물 없음")){
            Post post = gson.fromJson(게시물, Post.class);
            post.set관심여부(bool);
            게시물 = gson.toJson(post, Post.class);
            게시글에디터.putString(식별번호, 게시물).apply();
        }

    }
    public static void shared_상태변경(String 식별번호 , String 판매상태or숨김상태 ,boolean 상태 ,Context context){
        SharedPreferences 게시글_s;
        SharedPreferences.Editor 게시글에디터;
        Gson gson;String 게시물;
        /**   SharedPreferences   **/
        게시글_s = context.getSharedPreferences("post", MODE_PRIVATE);
        게시글에디터 = 게시글_s.edit();
        /**   json   **/
        gson = new GsonBuilder().create();
        /**   Post 객체 만들기   **/
        게시물 = 게시글_s.getString(식별번호, "게시물 없음");
        if (!게시물.equals("게시물 없음")) {
            Post post = gson.fromJson(게시물, Post.class);
            if(판매상태or숨김상태.equals("판매")) post.set판매중(상태);
            else if(판매상태or숨김상태.equals("숨김")) post.set숨김(상태);
            게시물 = gson.toJson(post, Post.class);
            게시글에디터.putString(식별번호, 게시물).apply();
        }
    }

    /** initial **/
    private void initializeView() {
        /** binding **/
        로그인시간 = binding.textviewTime;
        home_btn= binding.homeBtn;
        lct_btn= binding.locationBtn;
        chat_btn= binding.chatBtn;
        inf_btn= binding.informationBtn;
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void initializeProperty() {
        /** SharedPreferences **/
        회원정보_s = getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getSharedPreferences("LoginUser", MODE_PRIVATE);
        로그인_e = 로그인_s.edit();
        login_id = 로그인_s.getString("로그인회원","로그인 데이터 없음");
        if (login_id.equals("로그인 데이터 없음")) isLogin = false;
        else isLogin = true;
        /** gson **/
        gson = new GsonBuilder().create();
        /** Fragment **/
        home_f = new Fragment_Home();
        더보기_f = new Fragment_ViewMore();

        /** 로그인 시간 및 이름 **/
        if (!login_id.equals("로그인 데이터 없음")) {
            // 객체 만들기
            회원 = 회원정보_s.getString(login_id, "비회원");
            if(!회원.equals("비회원")){
                Signup_member signup_member = gson.fromJson(회원, Signup_member.class);
                String 이름 = signup_member.getName();
                /** time **/
//                로그인시간.setVisibility(View.VISIBLE); // textview 보이게 설정
//                Time_Setting(로그인시간,로그인_e ); //쓰레드
            }
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void Time_Setting(TextView 로그인시간 ,SharedPreferences.Editor 로그인정보_s) {
        final Handler 핸들러 = Handler.createAsync(Looper.getMainLooper());

        Thread thread = new Thread() {
            int time = 600; // 10분
            String time_s ;
            @Override
            public void run() {
                while(time>0){
                    time--;
                    핸들러.post(new Runnable() {
                        @Override
                        public void run() {
                            time_s = String.format("%d:%d",time/60,time%60);
                            로그인시간.setText(time_s);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                로그인정보_s.clear().apply();
                로그인시간.setVisibility(View.INVISIBLE);
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "로그인 시간이 만료되었습니다.\n다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        };
        thread.start();
    }
    /** button **/
    @RequiresApi(api = Build.VERSION_CODES.P)
    public void MvToFragment(int a){
        switch (a){
            case 0: // home
                home_f = new Fragment_Home();
                FragmentTransaction transaction_1 = getSupportFragmentManager().beginTransaction();
                transaction_1.replace(R.id.frameLayout,home_f).addToBackStack(null).detach(home_f).attach(home_f).commit();
                break;
            case 1: // map
                if(!isLogin) Toast.makeText(this, "로그인 후 사용해주세요", Toast.LENGTH_SHORT).show();
                else{
                    loading();
                    map_f = new Fragment_Map();
                    FragmentTransaction transaction_2 = getSupportFragmentManager().beginTransaction();
                    transaction_2.replace(R.id.frameLayout, map_f).addToBackStack(null).detach(map_f).attach(map_f).commit();
                }
                break;
            case 2: // chat
                if(!isLogin) Toast.makeText(this, "로그인 후 사용해주세요", Toast.LENGTH_SHORT).show();
                else {
                    chat_f = new Fragment_ChatList();
                    FragmentTransaction transaction_3 = getSupportFragmentManager().beginTransaction();
                    transaction_3.replace(R.id.frameLayout, chat_f).addToBackStack(null).detach(chat_f).attach(chat_f).commit();
                }
                break;
            case 3: // viewMore
                더보기_f = new Fragment_ViewMore();
                FragmentTransaction transaction_4 = getSupportFragmentManager().beginTransaction();
                transaction_4.replace(R.id.frameLayout, 더보기_f).addToBackStack(null).detach(더보기_f).attach(더보기_f).commit();
                break;

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void loading() {
        final Handler 핸들러 = Handler.createAsync(Looper.getMainLooper());

        Thread thread = new Thread() {
            int time = 600; // 10분
            String time_s ;
            @Override
            public void run() {

                핸들러.post(new Runnable() {
                    @Override
                    public void run() {
                        //로딩창 객체 생성
                        customProgressDialog = new ProgressDialog(MainActivity.this);
                        //로딩창을 투명하게
                        customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                        customProgressDialog.show();
                    }
                });
                try {
                    Thread.sleep(1500);
                    customProgressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
    /** 외부 저장소 권한 **/
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void 권한요청(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }
    }
    public static String getRealPathFromURI(final Context context, final Uri uri) {

        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {

            // MediaProvider
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }
}