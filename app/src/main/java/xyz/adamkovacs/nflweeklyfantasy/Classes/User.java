package xyz.adamkovacs.nflweeklyfantasy.Classes;

import java.util.ArrayList;

public class User implements Comparable {

    String username;
    String email;
    int weekly_score;
    String placement;

    public User(String username,String email, int weekly_score){
        this.username=username;
        this.email=email;
        this.weekly_score=weekly_score;
    }
    public User(String username,String email, int weekly_score, String placement){
        this.username=username;
        this.email=email;
        this.weekly_score=weekly_score;
        this.placement=placement;
    }

    public User(String username, int weekly_score){
        this.username=username;
        this.weekly_score=weekly_score;
    }


    //Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getWeekly_score() {
        return weekly_score;
    }

    public void setWeekly_score(int weekly_score) {
        this.weekly_score = weekly_score;
    }

    public String getPlacement() {
        return placement;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", weekly_score=" + weekly_score +
                '}';
    }

    @Override
    public int compareTo(Object user) {
        int points = ((User) user).getWeekly_score();
        return points-this.getWeekly_score();
    }
}

