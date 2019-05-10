package com.pm.wd.sl.college.projectsantaclaus.Objects;

public class Message {
    int id;
    String sendr_uid;
    String recvr_uid;
    String msg;
    String url;
    String date;
    String time;
    int original_size;
    int compressed_size;
    User sender;

    public Message(int id, String sendr_uid, String recvr_uid, String msg, String url, String date, String time, int originalSize, int compressedSize, User sender) {
        this.id = id;
        this.sendr_uid = sendr_uid;
        this.recvr_uid = recvr_uid;
        this.msg = msg;
        this.url = url;
        this.date = date;
        this.time = time;
        this.original_size = originalSize;
        this.compressed_size = compressedSize;
        this.sender = sender;
    }

    public String getDate() {
        return date;
    }

    public User getSender() {
        return sender;
    }


    public Message setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Message setTime(String time) {
        this.time = time;
        return this;
    }


    public int getId() {
        return id;
    }

    public Message setId(int id) {
        this.id = id;
        return this;
    }

    public String getSendrUid() {
        return sendr_uid;
    }

    public Message setSendrUid(String sendr_uid) {
        this.sendr_uid = sendr_uid;
        return this;
    }

    public String getRecvrUid() {
        return recvr_uid;
    }

    public Message setRecvrUid(String recvr_uid) {
        this.recvr_uid = recvr_uid;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Message setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Message setUrl(String url) {
        this.url = url;
        return this;
    }

    public int getOriginalSize() {
        return original_size;
    }

    public Message setOriginalSize(int originalSize) {
        this.original_size = originalSize;
        return this;
    }

    public int getCompressedSize() {
        return compressed_size;
    }

    public Message setCompressedSize(int compressedSize) {
        this.compressed_size = compressedSize;
        return this;
    }
}
