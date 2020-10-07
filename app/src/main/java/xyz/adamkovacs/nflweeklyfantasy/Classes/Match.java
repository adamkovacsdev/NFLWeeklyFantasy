package xyz.adamkovacs.nflweeklyfantasy.Classes;

import android.widget.Switch;

import androidx.annotation.NonNull;

import java.util.Date;

public class Match {

    private int weekNumber;
    private String homeTeam,awayTeam,username;
    private String matchStart;
    private int homeScore,awayScore,homeSelectedint,awaySelectedInt;
    private String homeSelected,awaySelected;
    private String week;
    private Date startDate;
    private String startDateStr;
    private boolean isOver;


    public Match(int weekNumber, String homeTeam, int homeScore, String homeSelected, String awayTeam,
                 int awayScore, String awaySelected, String matchStart){
        this.weekNumber=weekNumber;
        this.homeTeam=homeTeam;
        this.homeScore=homeScore;
        this.homeSelected=homeSelected;
        this.awayTeam=awayTeam;
        this.awayScore=awayScore;
        this.awaySelected=awaySelected;
        this.matchStart=matchStart;
    }

    public Match(int weekNumber, String homeTeam, int homeScore, String homeSelected, String awayTeam,
                 int awayScore, String awaySelected){
        this.weekNumber=weekNumber;
        this.homeTeam=homeTeam;
        this.homeScore=homeScore;
        this.homeSelected=homeSelected;
        this.awayTeam=awayTeam;
        this.awayScore=awayScore;
        this.awaySelected=awaySelected;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public Match(String startDate, int weekNumber, String homeTeam, int homeScore, String awayTeam,
                 int awayScore, boolean isOver){
        this.startDateStr=startDate;
        this.weekNumber=weekNumber;
        this.homeTeam=homeTeam;
        this.homeScore=homeScore;
        this.awayTeam=awayTeam;
        this.awayScore=awayScore;
        this.isOver=isOver;
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

    public String getMatchStart() {
        return matchStart;
    }

    public void setMatchStart(String matchStart) {
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

    public String getIsHomeSelected() {
        return homeSelected;
    }

    public void setIsHomeSelected(String homeSelected) {
        this.homeSelected = homeSelected;
    }

    public String getIsAwaySelected() {
        return awaySelected;
    }

    public void setIsAwaySelected(String awaySelected) {
        this.awaySelected = awaySelected;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    @NonNull
    @Override
    public String toString() {
        return "Week"+weekNumber+", hometeam: "+homeTeam+", homescore: "+homeScore+",homeselected: "+homeSelected+", awayTeam: "+awayTeam+", awayscore: "+awayScore+", awayselected: "+awaySelected+",user: ";
    }
}
