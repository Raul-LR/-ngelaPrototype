package com.angela_prototype.rlr.angelaprototype.Pojos;

import android.text.format.Time;

/**
 * Created by Raúl on 22/06/2017.
 */

public class Alert {

    private int id;
    private int user_id;
    private String type;
    private String lectures;
    private String problem;
    private String date;
    private String time;

    public Alert (){
        this.id = 0;
        this.user_id = 0;
        this.type = "";
        this.problem = "";
        this.lectures = "";
        this.date = "";
        this.time = "";
    }

    public Alert(String type, String problem, String lectures, int user_id){
        Time now = new Time(Time.getCurrentTimezone());
        this.id = 0;
        this.user_id = user_id;
        this.type = type;
        this.problem = problem;
        this.lectures = lectures;
        this.date = now.monthDay + "/" + now.month + "/" + now.year;
        this.time = now.format("%k:%M:%S");
    }

    public Alert(int id, int user_id, String type, String problem, String lectures, String date, String time){
        this.id = id;
        this.user_id = user_id;
        this.type = type;
        this.problem = problem;
        this.lectures = lectures;
        this.date = date;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getLectures() {
        return lectures;
    }

    public void setLectures(String lectures) {
        this.lectures = lectures;
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

    @Override
    public String toString() {
        return "Alert{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", type='" + type + '\'' +
                ", lectures='" + lectures + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
