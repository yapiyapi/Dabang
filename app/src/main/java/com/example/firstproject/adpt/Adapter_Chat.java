package com.example.firstproject.adpt;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.firstproject.R;
import com.example.firstproject.object.Code;
import com.example.firstproject.object.Message;

import java.util.ArrayList;
import java.util.Iterator;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adapter_Chat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Message> myDataList = null;

    private String login_id;
    private String 이미지;

    public Adapter_Chat(ArrayList<Message> dataList , String Login_id, String 이미지)
    {
        myDataList = dataList;
        login_id = Login_id;
        this.이미지 = 이미지;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(viewType == Code.ViewType.MY_CONTENT)
        {
            view = inflater.inflate(R.layout.cell_message_send, parent, false);
            return new MyViewHolder(view);
        }
        else
        {
            view = inflater.inflate(R.layout.cell_message_receive, parent, false);
            return new OpponentViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof MyViewHolder)
        {
            ((MyViewHolder) holder).message1.setText(myDataList.get(position).getText());
        }
        else if(holder instanceof OpponentViewHolder)
        {
            ((OpponentViewHolder) holder).message2.setText(myDataList.get(position).getText());
            if(이미지!=null) Glide.with(holder.itemView).load(이미지).into(((OpponentViewHolder) holder).thumnail);
        }
    }

    @Override
    public int getItemCount() {
        return myDataList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return myDataList.get(position).getViewType();
    }
    /** ViewHolder **/
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView message1;
        @RequiresApi(api = Build.VERSION_CODES.P)
        MyViewHolder(@NonNull View itemView){
            super(itemView);
            message1 =  itemView.findViewById(R.id.message_send);


        }
    }

    public class OpponentViewHolder extends RecyclerView.ViewHolder{

        TextView message2;
        CircleImageView thumnail;
        public OpponentViewHolder(@NonNull View itemView) {
            super(itemView);
            message2 =  itemView.findViewById(R.id.message_receive);
            thumnail =  itemView.findViewById(R.id.thumnail_receive);
        }
    }

    public void addChat(Message message){
        myDataList.add(message);
        notifyItemInserted(myDataList.size()-1);

        // viewType 설정 ( login_member : 0 , opponent : 1 )
        if(myDataList.size()!=0){
            if (login_id.equals(message.get판매자())){
                switch (message.getViewType()){
                    case 0:
                        message.setViewType(Code.ViewType.OPPONENT_CONTENT);
                        break;
                    case 1:
                        message.setViewType(Code.ViewType.MY_CONTENT);
                        break;
                }
            }
        }


        Iterator iterator = myDataList.iterator();
        int a = 0;
        while (iterator.hasNext()){
            Message key = (Message) iterator.next();
            Log.i("a",a+"  "+key.getViewType());
            a++;
        }
    }

}
