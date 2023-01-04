package com.example.firstproject.object;

public class Message {
    private String id;
    private String text;
    private String 판매자;
    private int viewType;


    public Message() {
    }

    public Message(String id, String text, String 판매자,int viewType) {
        this.id = id;
        this.text = text;
        this.판매자 = 판매자;
        this.viewType = viewType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String get판매자() {
        return 판매자;
    }

    public void set판매자(String 판매자) {
        this.판매자 = 판매자;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

