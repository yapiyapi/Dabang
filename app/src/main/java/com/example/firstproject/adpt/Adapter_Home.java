package com.example.firstproject.adpt;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.MainActivity;
import com.example.firstproject.R;
import com.example.firstproject.object.ADV;
import com.example.firstproject.object.Data;
import com.example.firstproject.object.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

public class Adapter_Home extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    SharedPreferences 게시글_s, 로그인_s;
    Gson gson;
    String 게시물;
    private String 이미지, 제목 , 주소 , 상세주소, 가격;
    private boolean 관심, 판매중;

    ArrayList<String> 전체_data_arr = Data.전체_data_arr;

    private boolean 로그인여부;
    //  --------------------------onclick --------------
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String key);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == 0) {  // 광고
            view = inflater.inflate(R.layout.cell_adv, parent, false);
            return new AdvViewHolder(view);}
        else {          // content
            view = inflater.inflate(R.layout.cell_content, parent, false);
            return new CustomViewHolder(view);}
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        if(holder instanceof AdvViewHolder);
        else if(holder instanceof CustomViewHolder)
        {
            /**   SharedPreferences   **/
            게시글_s = holder.itemView.getContext().getSharedPreferences("post", MODE_PRIVATE);
            로그인_s = holder.itemView.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
            /** gson **/
            gson = new GsonBuilder().create();
            /** 게시물 식별번호 **/
            String 식별번호 = 전체_data_arr.get(position);

            /** Post 객체 **/
            게시물 = 게시글_s.getString(식별번호,"게시물 없음");
            if(!게시물.equals("게시물 없음")) {
                Post post = gson.fromJson(게시물, Post.class);
                /** 초기화 **/
                이미지 = MainActivity.getRealPathFromURI(holder.itemView.getContext(),Uri.parse(post.get이미지()));
                제목 = post.get제목();
                주소 = post.get주소();
                상세주소 = post.get상세주소();
                가격 = post.get가격();
                관심 = post.is관심여부();
                판매중 = post.is판매중();

                Glide.with(holder.itemView).load(이미지).into(((CustomViewHolder) holder).이미지_view);
                ((CustomViewHolder) holder).제목_view.setText(제목);
                ((CustomViewHolder) holder).지역_view.setText(주소);
                ((CustomViewHolder) holder).아파트_view.setText(상세주소);
                ((CustomViewHolder) holder).가격_view.setText(가격);
                // 하트
                로그인여부 = 로그인_s.getAll().isEmpty();
                if (로그인여부!=false)((CustomViewHolder) holder).하트_view.setVisibility(View.GONE);
                else{
                    if (관심) ((CustomViewHolder) holder).하트_view.setImageResource(R.drawable.favorite);
                    else ((CustomViewHolder) holder).하트_view.setImageResource(R.drawable.favorite_empty);
                }
                //거래완료
                if (!판매중)((CustomViewHolder) holder).거래완료_view.setVisibility(View.VISIBLE);
                else((CustomViewHolder) holder).거래완료_view.setVisibility(View.GONE);
            }

            /** 버튼 **/
            //하트 눌릴 떄
            ((CustomViewHolder) holder).하트_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    게시물 = 게시글_s.getString(식별번호,"게시물 없음");
                    if(!게시물.equals("게시물 없음")) {
                        Post post = gson.fromJson(게시물, Post.class);
                        관심 = post.is관심여부();
                    }
                    if (!관심) {                                            // false
                        ((CustomViewHolder) holder).하트_view.setImageResource(R.drawable.favorite);
                        MainActivity.shared_하트상태변경(식별번호,true,holder.itemView.getContext());

                    } else {                                               //true
                        ((CustomViewHolder) holder).하트_view.setImageResource(R.drawable.favorite_empty);
                        MainActivity.shared_하트상태변경(식별번호,false,holder.itemView.getContext());

                    }
                }

            });
        }



    }


    @Override
    public int getItemCount() {
        return 전체_data_arr.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    //    ----------------뷰홀더-------------------
    public class AdvViewHolder extends RecyclerView.ViewHolder{
        ImageView content;
        @RequiresApi(api = Build.VERSION_CODES.P)
        AdvViewHolder(@NonNull View itemView){
            super(itemView);
            content =  itemView.findViewById(R.id.image_slide_img);
            /** 광고 **/
            adv(itemView,content);

        }
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder{

        TextView 제목_view, 지역_view, 아파트_view, 거래완료_view, 가격_view;
        ImageView 이미지_view, 하트_view, 메뉴_view;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            제목_view =(TextView)itemView.findViewById(R.id.title_cell);
            지역_view =(TextView)itemView.findViewById(R.id.location_cell);
            아파트_view =(TextView)itemView.findViewById(R.id.apart_cell);
            거래완료_view =(TextView)itemView.findViewById(R.id.sale_status_txt);
            가격_view =(TextView)itemView.findViewById(R.id.price_cell);
            이미지_view =(ImageView) itemView.findViewById(R.id.cell_imageview);
            하트_view =(ImageView) itemView.findViewById(R.id.favorite_empty);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int posi = getAdapterPosition();
                    /** 게시물 식별번호 **/
                    String 식별번호 = 전체_data_arr.get(posi);
                    if (posi != RecyclerView.NO_POSITION)
                    {
                        if (mListener != null) {
                            mListener.onItemClick(v, 식별번호) ;
                        }
                    }
                }
            });
        }
    }

    // 이미지 슬라이더 구현 메서드
    @RequiresApi(api = Build.VERSION_CODES.P)
    private void adv(View itemView, ImageView content) {

        final Handler 핸들러 = Handler.createAsync(Looper.getMainLooper());
        Thread thread = new Thread() {
            @Override
            public void run() {
                int num = 0;
                while(true){
                    int image = ADV.images.get(num);
                    int finalNum = num;
                    핸들러.post(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(itemView.getContext()).load(image).into(content);
                            itemView.setOnClickListener(new View.OnClickListener() {
                                String site;
                                @Override
                                public void onClick(View v) {
                                    switch (finalNum){
                                        case 0:
                                            site = "https://www.payco.com";
                                            break;
                                        case 1:
                                            site = "https://www.emart.ssg.com";
                                            break;
                                        case 2:
                                            site = "http://www.naver.com";
                                            break;
                                    }
                                    Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(site));
                                    itemView.getContext().startActivity(myIntent);
                                }
                            });
                        }
                    });
                    if (num==ADV.images.size()-1) num = 0;
                    else num++;
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
    }

}
