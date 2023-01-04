package com.example.firstproject.adpt;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.MainActivity;
import com.example.firstproject.R;
import com.example.firstproject.object.Data;
import com.example.firstproject.object.Signup_member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Adapter_TraderChoice extends RecyclerView.Adapter<Adapter_TraderChoice.CustomViewHolder> {

    SharedPreferences 회원정보_s, 로그인_s;
    SharedPreferences.Editor 게시글에디터;
    Gson gson;
    String 회원정보;
    private int lastCheckedPosition = -1;
    private String 이미지, name ;

    ArrayList<String> 거래자_list = Data.거래자_list;
    // ----------- ---------------    -------------//
    private OnItemClickListener mListener = null ;
    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String id) ;
    }
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View 뷰 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_trader, parent,false);
        return new CustomViewHolder(뷰);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        /**   SharedPreferences   **/
        회원정보_s = holder.itemView.getContext().getSharedPreferences("signup", MODE_PRIVATE);
        게시글에디터 = 회원정보_s.edit(); //Editor
        /** gson **/
        gson = new GsonBuilder().create();
        /** 거래자_id **/
        String 거래자_id = 거래자_list.get(position);
        /** Signup_member 객체 **/
        회원정보 = 회원정보_s.getString(거래자_id,"거래자 없음");
        if(!회원정보.equals("거래자 없음")) {
            Signup_member signupMember = gson.fromJson(회원정보, Signup_member.class);
            /** 초기화 **/
            이미지 = MainActivity.getRealPathFromURI(holder.itemView.getContext(), Uri.parse(signupMember.getThumnail()));
            name = signupMember.getName();
            if (이미지==null) ;
            else Glide.with(holder.itemView).load(이미지).into(holder.거래자_imageview);
            holder.거래자_textview.setText(name);
            //radioButton 초기화
            holder.radioButton.setChecked(position == lastCheckedPosition);
        }


    }

    // 작성여부의 true 갯수 만큼 view 만듦
    @Override
    public int getItemCount() {
        return 거래자_list.size();
    }

    //    ----------------뷰홀더-------------------
    public class CustomViewHolder extends RecyclerView.ViewHolder{

        ImageView 거래자_imageview;
        TextView 거래자_textview;
        RadioButton radioButton;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            거래자_imageview =(ImageView)itemView.findViewById(R.id.cell_trader_imageview);
            거래자_textview =(TextView)itemView.findViewById(R.id.cell_trader_textview);
            radioButton =(RadioButton)itemView.findViewById(R.id.cell_trader_radioButton);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 라디오 버튼 하나만 선택
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);
                    // 라디오 버튼 위치 전달
                    int posi = getAdapterPosition();
                    /** 거래자_ id **/
                    String 거래자_id = 거래자_list.get(posi);
                    if (posi != RecyclerView.NO_POSITION)
                    {
                        if (mListener != null) {
                            mListener.onItemClick(v, 거래자_id) ;
                        }
                    }

                }
            });
        }
    }

}
