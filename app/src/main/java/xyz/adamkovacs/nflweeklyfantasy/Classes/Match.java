package xyz.adamkovacs.nflweeklyfantasy.Classes;

import android.widget.Switch;

import androidx.annotation.NonNull;

import java.util.Date;

public class Match {

    private int weekNumber;
    private String homeTeam,awayTeam,username;
    private Date matchStart;
    private int homeScore,awayScore,homeSelectedint,awaySelectedInt;
    private boolean homeSelected,awaySelected;
    private String week;

    public Match(int weekNumber, String homeTeam, int homeScore, boolean homeSelected, String awayTeam,
                 int awayScore, boolean awaySelected, Date matchStart){
        this.weekNumber=weekNumber;
        this.homeTeam=homeTeam;
        this.homeScore=homeScore;
        this.homeSelected=homeSelected;
        this.awayTeam=awayTeam;
        this.awayScore=awayScore;
        this.awaySelected=awaySelected;
        this.matchStart=matchStart;
    }

    public Match(int weekNumber, String homeTeam, int homeScore, boolean homeSelected, String awayTeam,
                 int awayScore, boolean awaySelected){
        this.weekNumber=weekNumber;
        this.homeTeam=homeTeam;
        this.homeScore=homeScore;
        this.homeSelected=homeSelected;
        this.awayTeam=awayTeam;
        this.awayScore=awayScore;
        this.awaySelected=awaySelected;
    }

    public Match(String week, String homeTeam,int homeSelectedint, String awayTeam, int awaySelectedInt, String username){
        this.week=week;
        this.homeTeam=homeTeam;
        this.homeSelectedint=homeSelectedint;
        this.awayTeam=awayTeam;
        this.awaySelectedInt=awaySelectedInt;
        this.username=username;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public Date getMatchStart() {
        return matchStart;
    }

    public void setMatchStart(Date matchStart) {
        this.matchStart = matchStart;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getAwayScore() {
        return awayScore;
    }

    public void setAwayScore(int awayScore) {
        this.awayScore = awayScore;
    }

    public boolean isHomeSelected() {
        return homeSelected;
    }

    public void setHomeSelected(boolean homeSelected) {
        this.homeSelected = homeSelected;
    }

    public boolean isAwaySelected() {
        return awaySelected;
    }

    public void setAwaySelected(boolean awaySelected) {
        this.awaySelected = awaySelected;
    }

    @NonNull
    @Override
    public String toString() {
        return "Week"+weekNumber+", hometeam: "+homeTeam+", homescore: "+homeScore+",homeselected: "+homeSelected+", awayTeam: "+awayTeam+", awayscore: "+awayScore+", awayselected: "+awaySelected+",user: ";
    }
}
