package com.example.firstproject.object;

import java.util.ArrayList;
import java.util.HashMap;

public class Signup_member {
    String name ,e_mail,phone_num,password,thumnail;
    ArrayList<String> 관심목록_arr , 구매목록_arr;
    ArrayList<HashMap> 채팅목록_arr;
    public Signup_member(String name, String e_mail, String phone_num, String password, String thumnail, ArrayList<String> 관심목록_arr, ArrayList<String> 구매목록_arr, ArrayList<HashMap> 채팅목록_arr) {
        this.name = name;
        this.e_mail = e_mail;
        this.phone_num = phone_num;
        this.password = password;
        this.thumnail = thumnail;
        this.관심목록_arr = 관심목록_arr;
        this.구매목록_arr = 구매목록_arr;
        this.채팅목록_arr = 채팅목록_arr;
    }

    public ArrayList<String> get구매목록_arr() {
        return 구매목록_arr;
    }

    public void set구매목록_arr(ArrayList<String> 구매목록_arr) {
        this.구매목록_arr = 구매목록_arr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getE_mail() {
        return e_mail;
    }

    public void setE_mail(String e_mail) {
        this.e_mail = e_mail;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public ArrayList<String> get관심목록_arr() {
        return 관심목록_arr;
    }

    public void set관심목록_arr(ArrayList<String> 관심목록_arr) {
        this.관심목록_arr = 관심목록_arr;
    }

    public ArrayList<HashMap> get채팅목록_arr() {
        return 채팅목록_arr;
    }

    public void set채팅목록_arr(ArrayList<HashMap> 채팅목록_arr) {
        this.채팅목록_arr = 채팅목록_arr;
    }
}