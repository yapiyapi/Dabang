package com.example.firstproject.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firstproject.Activity_Account;
import com.example.firstproject.Activity_ListBuy;
import com.example.firstproject.Activity_ListHart;
import com.example.firstproject.Activity_ListSell;
import com.example.firstproject.Activity_Login;
import com.example.firstproject.MainActivity;
import com.example.firstproject.R;
import com.example.firstproject.databinding.FragmentViewMoreBinding;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_ViewMore extends Fragment {
    private FragmentViewMoreBinding binding;
    SharedPreferences 게시글_s ,회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글_e, 회원정보_e, 로그인_e;

    private String 이름, 이메일, 계정이미지, login_id;
    private boolean 로그인여부;

    ImageView 판매내역, 구매내역, 관심목록;
    CircleImageView 계정이미지_img;
    View 뷰;
    TextView 계정이름, 계정내용;
    Gson gson;String 회원;

    Signup_member signup_member;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_more, container,false);
        View view = binding.getRoot();

        initializeView();
        initializeProperty();


        /** 버튼 **/
        // 더보기 화면에서 계정 view 클릭했을 때 ( 데이터 전송 )
        뷰.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //로그인 했으면 account Activity로 이동
                if (로그인여부!=true){
                    Intent intent = new Intent(getActivity(), Activity_Account.class);
                    startActivity(intent);}
                //로그인 안했으면 login Activity로 이동
                else{
                    Intent intent = new Intent(getActivity(), Activity_Login.class);
                    startActivity(intent);}
            }
        });

        판매내역.setOnClickListener(new View.OnClickListener() {//판매내역
            @Override
            public void onClick(View view) {
                if (로그인여부!=false) Toast.makeText(getActivity(), "로그인 후 사용바랍니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), Activity_ListSell.class);
                    startActivity(intent);
                }
            }
        });
        구매내역.setOnClickListener(new View.OnClickListener() { //구매내역
            @Override
            public void onClick(View view) {
                if (로그인여부!=false) Toast.makeText(getActivity(), "로그인 후 사용바랍니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), Activity_ListBuy.class);
                    startActivity(intent);
                }
            }
        });
        관심목록.setOnClickListener(new View.OnClickListener() {//관심목록 (hart)
            @Override
            public void onClick(View view) {
                if (로그인여부!=false) Toast.makeText(getActivity(), "로그인 후 사용바랍니다.", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(getActivity(), Activity_ListHart.class);
                    startActivity(intent);
                }
            }
        });

        return view;
    }

    private void initializeProperty() {
        /** SharedPreferences **/
        회원정보_s = getActivity().getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        //gson 생성
        gson = new GsonBuilder().create();
        //객체 만들기
        login_id = 로그인_s.getString("로그인회원","비회원");
        회원 = 회원정보_s.getString(login_id,"비회원");
        // data 초기화
        로그인여부 = 로그인_s.getAll().isEmpty();
        if (로그인여부!=true) { // 로그인 정보가 있다면
            if(!회원.equals("비회원")){
                signup_member = gson.fromJson(회원,Signup_member.class);
                이름 = signup_member.getName(); //이름
                이메일 = signup_member.getE_mail(); //이메일
                계정이미지 = MainActivity.getRealPathFromURI(getActivity(), Uri.parse(signup_member.getThumnail()));//썸네일
            }
            계정이름.setText(이름);
            계정내용.setText(이메일);
            if (계정이미지!=null) Glide.with(getActivity()).load(계정이미지).into(계정이미지_img);
        }
    }

    private void initializeView() {
        /** binding **/
        판매내역 = binding.sellImgbtn;
        구매내역 = binding.buyImgbtn;
        관심목록 = binding.hartImgbtn;

        뷰 = binding.view;
        계정이미지_img = binding.imagebtnViewMore;

        계정이름 = binding.titleTxtview;
        계정내용 = binding.contentTxtview;
    }

    @Override
    public void onResume() {
        super.onResume();
        /** SharedPreferences **/
        회원정보_s = getActivity().getSharedPreferences("signup", MODE_PRIVATE);
        로그인_s = getActivity().getSharedPreferences("LoginUser", MODE_PRIVATE);
        //gson 생성
        gson = new GsonBuilder().create();
        //객체 만들기
        login_id = 로그인_s.getString("로그인회원","비회원");
        회원 = 회원정보_s.getString(login_id,"비회원");
        // data 초기화
        로그인여부 = 로그인_s.getAll().isEmpty();
        if (로그인여부!=true) { // 로그인 정보가 있다면
            if(!회원.equals("비회원")){
                signup_member = gson.fromJson(회원,Signup_member.class);
                이름 = signup_member.getName(); //이름
                이메일 = signup_member.getE_mail(); //이메일
                계정이미지 = MainActivity.getRealPathFromURI(getActivity(), Uri.parse(signup_member.getThumnail()));//썸네일
            }
            계정이름.setText(이름);
            계정내용.setText(이메일);
            if (계정이미지==null) ;
            else Glide.with(getActivity()).load(계정이미지).into(계정이미지_img);
        }
    }
}