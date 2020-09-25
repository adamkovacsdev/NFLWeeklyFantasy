package xyz.adamkovacs.nflweeklyfantasy.Classes;

import android.widget.Switch;

import androidx.annotation.NonNull;

import java.util.Date;

public class Match {

    int weekNumber;
    String homeTeam, awayTeam;
    Date matchStart;
    Switch switchForMatch;
    int homeScore, awayScore;
    boolean homeSelected, awaySelected;

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
        return "Week"+weekNumber+", team: "+homeTeam+", awayTeam: "+awayTeam+", match start: "+matchStart;
    }
}
