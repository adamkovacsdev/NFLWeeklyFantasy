package xyz.adamkovacs.nflweeklyfantasy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import xyz.adamkovacs.nflweeklyfantasy.Database.UserContract.UserEntry;
import xyz.adamkovacs.nflweeklyfantasy.Database.WeeklySelectionsContract.WeeklySelectionEntry;

public class NFLDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NFLDatabase";
    private static final int DATABASE_VERSION = 2;


    // Creating user table
    private static final String CREATE_TABLE_USERS="CREATE TABLE "+ UserEntry.TABLE_NAME + " ("
            +UserEntry.COLUMN_USERNAME+" TEXT PRIMARY KEY, "
            +UserEntry.COLUMN_EMAIL+" TEXT, "
            +UserEntry.COLUMN_PASSWORD+" TEXT, "
            +UserEntry.COLUMN_WEEKLY_SCORE+" INTEGER);";

    // Create table for weekly teams picked by user
    private static final String CREATE_TABLE_WEEKLY="CREATE TABLE "+ WeeklySelectionEntry.TABLE_NAME+" ("
            +WeeklySelectionEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
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
        ContentValues newWeekly = new ContentValues();
        newWeekly.put(WeeklySelectionEntry.COLUMN_USERNAME, username);
        newWeekly.put(WeeklySelectionEntry.COLUMN_WEEK, week);
        newWeekly.put(WeeklySelectionEntry.COLUMN_HOMETEAM, hometeam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_IS_HOMETEAM_SELECTED, isHomeTeam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_AWAYTEAM, awayteam);
        newWeekly.put(WeeklySelectionEntry.COLUMN_IS_AWAYTEAM_SELECTED, isAwayTeam);
        SQLiteDatabase db = this.getWritableDatabase();
        if(!weeklyAlreadyInDb) {
            db.insertWithOnConflict(WeeklySelectionEntry.TABLE_NAME, null, newWeekly, SQLiteDatabase.CONFLICT_IGNORE);
            db.close();
        } else {
            db.update(WeeklySelectionEntry.TABLE_NAME,newWeekly,WeeklySelectionEntry.COLUMN_USERNAME+"='"+username+"' AND "+WeeklySelectionEntry.COLUMN_WEEK+"='"+week+"' AND "+WeeklySelectionEntry.COLUMN_HOMETEAM+"='"+hometeam+"' AND "+WeeklySelectionEntry.COLUMN_AWAYTEAM+"='"+awayteam+"'",null);
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
                    selected += "Username: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_USERNAME))+" Week: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_WEEK)) +" Hometeam: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_HOMETEAM))+" IsHomeSelected: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_IS_HOMETEAM_SELECTED)) +" Awayteam: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_AWAYTEAM))+" IsAwayTeamSelected: "+ cursor.getString(cursor.getColumnIndex(WeeklySelectionEntry.COLUMN_IS_AWAYTEAM_SELECTED));

                }while(cursor.moveToNext());
            }
        }
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
}