package com.example.recyclerdb;

public class TaskModel {

    private String id;
    private String name;
    private String date;
    private String time;
    private String detail;

    public TaskModel(String id, String name, String date, String time, String detail) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
}