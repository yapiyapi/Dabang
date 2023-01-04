package com.example.firstproject.object;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Data {
    // adapter 에서 getItemCount 에  shared 사용 불가
    // adapter 에서 getItemCount, notifyItemRemoved

    //   [44 555 22 233 000 111]
    public static ArrayList 전체_data_arr = new ArrayList();
    //   id   판매중 :  [44 555]
    //        판매완료 :  [22 233]
    //        숨김 :    [000 111]
    public static HashMap<String , HashMap<String, ArrayList> > 전체_data = new HashMap<String, HashMap<String, ArrayList> >();
    //                   [  식별번호 ]
    public static ArrayList<String> 판매중_list = new ArrayList<String>();
    public static ArrayList<String> 판매완료_list = new ArrayList<String>();
    public static ArrayList<String> 숨김_list = new ArrayList<String>();
    public static ArrayList<String> 구매_list = new ArrayList<String>();

    public static ArrayList<String> 관심_list = new ArrayList<String>();

    public static ArrayList<String> 거래자_list = new ArrayList<String>();



}
