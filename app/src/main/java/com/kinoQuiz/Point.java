package com.kinoQuiz;

public class Point {

    private String user;
    private String score;
    private String time;

    public Point() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Point(String user, String score, String time) {
        this.user = user;
        this.score = score;
        this.time = time;
    }
}
