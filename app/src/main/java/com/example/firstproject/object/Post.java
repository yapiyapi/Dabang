package com.example.firstproject.object;

import java.util.ArrayList;

public class Post {
    String 이미지,제목,주소,상세주소,가격,내용, 식별부호, 작성자 , 구매자;
    boolean 관심여부,판매중,숨김;
    double lat,log;
    ArrayList 거래자;

    public Post(String 이미지, String 제목, String 주소, String 상세주소, double lat, double log, String 가격, String 내용, String 식별부호, String 작성자, String 구매자, boolean 관심여부, boolean 판매중, boolean 숨김, ArrayList 거래자) {
        this.이미지 = 이미지;
        this.제목 = 제목;
        this.주소 = 주소;
        this.lat = lat;
        this.log = log;
        this.상세주소 = 상세주소;
        this.가격 = 가격;
        this.내용 = 내용;
        this.식별부호 = 식별부호;
        this.작성자 = 작성자;
        this.구매자 = 구매자;
        this.관심여부 = 관심여부;
        this.판매중 = 판매중;
        this.숨김 = 숨김;
        this.거래자 = 거래자;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
    }

    public String get작성자() {
        return 작성자;
    }

    public void set작성자(String 작성자) {
        this.작성자 = 작성자;
    }

    public String get구매자() {
        return 구매자;
    }

    public void set구매자(String 구매자) {
        this.구매자 = 구매자;
    }

    public ArrayList get거래자() {
        return 거래자;
    }

    public void set거래자(ArrayList 거래자) {
        this.거래자 = 거래자;
    }

    public String get이미지() {
        return 이미지;
    }

    public void set이미지(String 이미지) {
        this.이미지 = 이미지;
    }

    public String get제목() {
        return 제목;
    }

    public void set제목(String 제목) {
        this.제목 = 제목;
    }

    public String get주소() {
        return 주소;
    }

    public void set주소(String 주소) {
        this.주소 = 주소;
    }

    public String get상세주소() {
        return 상세주소;
    }

    public void set상세주소(String 상세주소) {
        this.상세주소 = 상세주소;
    }

    public String get가격() {
        return 가격;
    }

    public void set가격(String 가격) {
        this.가격 = 가격;
    }

    public String get내용() {
        return 내용;
    }

    public void set내용(String 내용) {
        this.내용 = 내용;
    }

    public String get식별부호() {
        return 식별부호;
    }

    public void set식별부호(String 식별부호) {
        this.식별부호 = 식별부호;
    }

    public boolean is관심여부() {
        return 관심여부;
    }

    public void set관심여부(boolean 관심여부) {
        this.관심여부 = 관심여부;
    }

    public boolean is판매중() {
        return 판매중;
    }

    public void set판매중(boolean 판매중) {
        this.판매중 = 판매중;
    }

    public boolean is숨김() {
        return 숨김;
    }

    public void set숨김(boolean 숨김) {
        this.숨김 = 숨김;
    }
}
