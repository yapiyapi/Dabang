package com.example.firstproject.adpt;

import static android.content.Context.MODE_PRIVATE;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.Activity_Posting;
import com.example.firstproject.MainActivity;
import com.example.firstproject.R;
import com.example.firstproject.object.Data;
import com.example.firstproject.object.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;


public class Adapter_List_Hide extends RecyclerView.Adapter<Adapter_List_Hide.CustomViewHolder> {
    SharedPreferences 게시글_s, 로그인_s;
    SharedPreferences.Editor 게시글에디터;
    Gson gson;
    String 게시물;String 판매상태;
    private String 이미지, 제목 , 주소 , 상세주소, 가격;
    private boolean 관심,판매중;

    ArrayList 전체_data_arr = Data.전체_data_arr;
    ArrayList<String> 판매중_list = Data.판매중_list;
    ArrayList<String> 판매완료_list = Data.판매완료_list;
    ArrayList<String> 숨김_list = Data.숨김_list;

    //  --------------------------onclick --------------
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public interface OnItemClickListener {
        void onItemClick(View v, String key) ;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View 뷰 = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_content, parent,false);
        return new CustomViewHolder(뷰);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        int pos = position;
        boolean 판매중_reset;
        /**   SharedPreferences   **/
        게시글_s = holder.itemView.getContext().getSharedPreferences("post", MODE_PRIVATE);
        로그인_s = holder.itemView.getContext().getSharedPreferences("LoginUser", MODE_PRIVATE);
        게시글에디터 = 게시글_s.edit();
        /** gson **/
        gson = new GsonBuilder().create();
        /** 게시물 식별번호 **/
        String 식별번호 = 숨김_list.get(position);
        /** Post 객체 **/
        게시물 = 게시글_s.getString(식별번호,"게시물 없음");
        if(!게시물.equals("게시물 없음")) {
            Post post = gson.fromJson(게시물, Post.class);
            /** 초기화 **/
            이미지 = MainActivity.getRealPathFromURI(holder.itemView.getContext(), Uri.parse(post.get이미지()));
            제목 = post.get제목();
            주소 = post.get주소();
            상세주소 = post.get상세주소();
            가격 = post.get가격();
            관심 = post.is관심여부();
            판매중 = post.is판매중();

            Glide.with(holder.itemView).load(이미지).into(holder.이미지_view);
            holder.제목_view.setText(제목);
            holder.지역_view.setText(주소);
            holder.아파트_view.setText(상세주소);
            holder.가격_view.setText(가격);
            //sub menu 보이게
            holder.메뉴_view.setVisibility(View.VISIBLE);
            //하트
            if (관심) holder.하트_view.setImageResource(R.drawable.favorite);
            else holder.하트_view.setImageResource(R.drawable.favorite_empty);
            //거래완료
            if (!판매중) holder.거래완료_view.setVisibility(View.VISIBLE);
            else holder.거래완료_view.setVisibility(View.GONE);
        }
        판매중_reset = 판매중;

        /** 버튼 **/
        //하트 눌릴 떄
        holder.하트_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //관심 초기화
                게시물 = 게시글_s.getString(식별번호,"게시물 없음");
                if(!게시물.equals("게시물 없음")) {
                    Post post = gson.fromJson(게시물, Post.class);
                    관심 = post.is관심여부();
                }
                if (!관심) {                                            // false
                    holder.하트_view.setImageResource(R.drawable.favorite);
                    MainActivity.shared_하트상태변경(식별번호,true,holder.itemView.getContext());

                } else {                                               //true
                    holder.하트_view.setImageResource(R.drawable.favorite_empty);
                    MainActivity.shared_하트상태변경(식별번호,false,holder.itemView.getContext());

                }
            }
        });
        //sub menu 눌릴 때
        holder.메뉴_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
                popupMenu.getMenuInflater().inflate(R.menu.sub_menu_hide, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        //수정
                        if (menuItem.getItemId() == R.id.update_hide) {
                            //Post activity로 이동
                            Intent PostIntent = new Intent(view.getContext(), Activity_Posting.class);
                            PostIntent.putExtra("식별부호", 식별번호);
                            view.getContext().startActivity(PostIntent);
                        }
                        //숨김해제
                        else if (menuItem.getItemId() == R.id.hide_clear) {
                            MainActivity.shared_상태변경(식별번호,"숨김",false,holder.itemView.getContext());
                            전체_data_arr.add(식별번호);
                            숨김_list.remove(식별번호);
                            if (판매중_reset) 판매중_list.add(0,식별번호);
                            else 판매완료_list.add(0,식별번호);
                            //새로고침
                            notifyItemRemoved(pos);
                        }
                        //삭제
                        else if (menuItem.getItemId() == R.id.delete_hide) {
                            //데이터 삭제
                            게시글에디터.remove(식별번호).apply();
                            숨김_list.remove(식별번호);
                            //새로고침
                            notifyItemRemoved(pos);
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    // 작성여부의 true 갯수 만큼 view 만듦
    @Override
    public int getItemCount() {
        return 숨김_list.size();
    }

    //    ----------------뷰홀더-------------------
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
            메뉴_view =(ImageView) itemView.findViewById(R.id.sub_menu);

            /** Click **/
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int posi = getAdapterPosition();
                    /** 게시물 식별번호 **/
                    String 식별번호 = 숨김_list.get(posi);
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

}
