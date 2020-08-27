package com.example.recyclerdb;

public class TaskModel {

    private int id;
    private String name;
    private String date;
    private String time;
    private String detail;
    private byte[] image;

    public TaskModel(int id, String name, String date, String time, String detail, byte[] image) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.detail = detail;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
