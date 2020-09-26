package xyz.adamkovacs.nflweeklyfantasy.Classes;

import java.util.ArrayList;

public class User {

    String username;
    String password;
    String email;
    boolean isLoggedIn;
    int weekly_score;
    String placement;

    public User(String username,String email, String password, int weekly_score){
        this.username=username;
        this.email=email;
        this.password=password;
        this.weekly_score=weekly_score;
    }
    public User(String username,String email, String password, int weekly_score, String placement){
        this.username=username;
        this.email=email;
        this.password=password;
        this.weekly_score=weekly_score;
        this.placement=placement;
    }

    public User(String username, String password){
        this.username=username;
        this.password=password;
    }


    //Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
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
}
