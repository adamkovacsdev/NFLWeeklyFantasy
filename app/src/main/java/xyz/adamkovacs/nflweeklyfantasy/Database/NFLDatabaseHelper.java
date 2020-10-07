package xyz.adamkovacs.nflweeklyfantasy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import xyz.adamkovacs.nflweeklyfantasy.Classes.Match;
import xyz.adamkovacs.nflweeklyfantasy.Classes.User;
import xyz.adamkovacs.nflweeklyfantasy.Database.UserContract.UserEntry;
import xyz.adamkovacs.nflweeklyfantasy.Database.WeeklySelectionsContract.WeeklySelectionEntry;

public class NFLDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NFLDatabase";
    private static final int DATABASE_VERSION = 3;


    // Creating user table
    private static final String CREATE_TABLE_USERS="CREATE TABLE "+ UserEntry.TABLE_NAME + " ("
            +UserEntry.COLUMN_USERNAME+" TEXT PRIMARY KEY, "
            +UserEntry.COLUMN_EMAIL+" TEXT, "
            +UserEntry.COLUMN_PASSWORD+" TEXT, "
            +UserEntry.COLUMN_WEEKLY_SCORE+" INTEGER);";

    // Create table for weekly teams picked by user
    private static final String CREATE_TABLE_WEEKLY="CREATE TABLE "+ WeeklySelectionEntry.TABLE_NAME+" ("
            +WeeklySelectionEntry.COLUMN_WEEK+" TEXT, "
            +WeeklySelectionEntry.COLUMN_HOMETEAM+" TEXT, "
            +WeeklySelectionEntry.COLUMN_IS_HOMETEAM_SELECTED+" INTEGER, "
            +WeeklySelectionEntry.COLUMN_AWAYTEAM+" TEXT, "
            +WeeklySelectionEntry.COLUMN_IS_AWAYTEAM_SELECTED+" INTEGER, "
            +WeeklySelectionEntry.COLUMN_USERNAME+" TEXT,"
            +"FOREIGN KEY ("+WeeklySelectionEntry.COLUMN_USERNAME+") REFERENCES "+UserEntry.TABLE_NAME+"("
            +UserEntry.COLUMN_USERNAME+"));";

    public NFLDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_WEEKLY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + UserEntry.TABLE_NAME +"'");
        db.execSQL("DROP TABLE IF EXISTS '" + WeeklySelectionEntry.TABLE_NAME+"'");
    }

    //TODO: UpdateUser method -to update weekly and perfect score
    public void AddUser(String username, String email, String password){
        boolean userAlreadyInDb = isUserInDatabase(username,password);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newUser = new ContentValues();
        newUser.put(UserEntry.COLUMN_USERNAME, username);
        newUser.put(UserEntry.COLUMN_EMAIL, email);
        newUser.put(UserEntry.COLUMN_PASSWORD, password);
        newUser.put(UserEntry.COLUMN_WEEKLY_SCORE,0);
        if(!userAlreadyInDb) {
            db.insertWithOnConflict(UserEntry.TABLE_NAME, null, newUser, SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
        }
    }

    // Add a user selected match
    public void AddWeeklySelected(String username, String week, String hometeam, int isHomeTeam,
                                  String awayteam, int isAwayTeam){
        boolean weeklyAlreadyInDb = isWeeklyUserSelected(username,week,hometeam,awayteam);
        Log.i("database","weeklyAlreadyindb: "+weeklyAlreadyInDb);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newWeekly = new ContentValues();
        newWeekly.put(WeeklySelectionEntry.COLUMN_WEEK, week);
        newWeekly.put(WeeklySelectionEntry.COLUMN_HOMETEAM, hometeam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_IS_HOMETEAM_SELECTED, isHomeTeam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_AWAYTEAM, awayteam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_IS_AWAYTEAM_SELECTED, isAwayTeam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_USERNAME, username);
        if(!weeklyAlreadyInDb) {
            db.insertWithOnConflict(WeeklySelectionEntry.TABLE_NAME, null, newWeekly, SQLiteDatabase.CONFLICT_IGNORE);
            Log.i("database","Adding weekly to db.");
            db.close();
        } else {
            db.update(WeeklySelectionEntry.TABLE_NAME,newWeekly,WeeklySelectionEntry.COLUMN_USERNAME+"='"+username+"' AND "+WeeklySelectionEntry.COLUMN_WEEK+"='"+week+"' AND "+WeeklySelectionEntry.COLUMN_HOMETEAM+"='"+hometeam+"' AND "+WeeklySelectionEntry.COLUMN_AWAYTEAM+"='"+awayteam+"'",null);
            Log.i("database","Updating weekly to db.");
            db.close();
        }
    }


    //Temporary method for debugging purposes
    public String WhatsInDatabase(){
        String selected="";
        String selectQuery ="SELECT * FROM "+WeeklySelectionEntry.TABLE_NAME+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    selected += "Username: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_USERNAME))+" Week: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_WEEK)) +" Hometeam: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_HOMETEAM))+" IsHomeSelected: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_IS_HOMETEAM_SELECTED)) +" Awayteam: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_AWAYTEAM))+" IsAwayTeamSelected: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_IS_AWAYTEAM_SELECTED))+"\r\n";

                }while(cursor.moveToNext());
            }
        }
        db.close();
        return selected;
    }

    // Checks if the user is already in the database
    public boolean isUserInDatabase(String username, String password){
        String selectQuery = "SELECT "+UserEntry.COLUMN_USERNAME+", "+UserEntry.COLUMN_PASSWORD + " FROM "
                + UserEntry.TABLE_NAME+" WHERE "+ UserEntry.COLUMN_USERNAME+"='"+username+"' AND "+UserEntry.COLUMN_PASSWORD+"='"+password+"';";
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        Log.i("test",cursor.toString());
        if(cursor != null){
            if(cursor.moveToFirst()){
                return true;
            } else {
                return false;
            }
        }
        else {
            return false;
        }
    }


    // Checks if the user already selected a winner for a match
    public boolean isWeeklyUserSelected(String username, String week, String hometeam, String awayteam){
        String selectQuery = "SELECT * FROM "+WeeklySelectionEntry.TABLE_NAME+" WHERE "+WeeklySelectionEntry.COLUMN_USERNAME+"='"+username+"'"
                +" AND "+ WeeklySelectionEntry.COLUMN_WEEK+"='"+week+"'" + " AND "+WeeklySelectionEntry.COLUMN_HOMETEAM+"='"+hometeam+"'"
                +" AND "+WeeklySelectionEntry.COLUMN_AWAYTEAM+"='"+awayteam+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.i("database","Selectquery: "+selectQuery);
        if(cursor != null){
            if(cursor.moveToFirst()){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    public int getUserWeeklyPoints(String username){
        String selectQuery = "SELECT "+UserEntry.COLUMN_WEEKLY_SCORE+" FROM "+UserEntry.TABLE_NAME + " WHERE "
                + UserEntry.COLUMN_USERNAME +"='"+username+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        int score = 0;
        if(cursor != null){
            if(cursor.moveToFirst()){
                do {
                    score = cursor.getInt(cursor.getColumnIndex("weekly_score"));
                }while (cursor.moveToNext());
            }
        }
        return score;
    }

    public List<User> getUsers(){
        String selectQuery = "SELECT * FROM " + UserEntry.TABLE_NAME+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<User> users = new ArrayList<>();
        String username="";
        String email="";
        String password="";
        int weeklyScore=0;
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    username = cursor.getString(cursor.getColumnIndex("username"));
                    email = cursor.getString(cursor.getColumnIndex("email"));
                    password = cursor.getString(cursor.getColumnIndex("password"));
                    weeklyScore = cursor.getInt(cursor.getColumnIndex("weekly_score"));
                    users.add(new User(username,email,weeklyScore));
                }while (cursor.moveToNext());
            }
        }

        return users;
    }

    public List<Match> getMatches(){
        String selectQuery = "SELECT * FROM " + WeeklySelectionEntry.TABLE_NAME+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        ArrayList<Match> matches = new ArrayList<>();
        String week;
        String home;
        int homeselect;
        String away;
        int awayselect;
        String username;
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    week = cursor.getString(cursor.getColumnIndex("week"));
                    home = cursor.getString(cursor.getColumnIndex("hometeam"));
                    homeselect = cursor.getInt(cursor.getColumnIndex("is_hometeam_selected"));
                    away = cursor.getString(cursor.getColumnIndex("awayteam"));
                    awayselect = cursor.getInt(cursor.getColumnIndex("is_awayteam_selected"));
                    username = cursor.getString(cursor.getColumnIndex("username"));
                    matches.add(new Match(week,home,homeselect,away,awayselect,username));
                }while (cursor.moveToNext());
            }
        }
        return matches;
    }
}