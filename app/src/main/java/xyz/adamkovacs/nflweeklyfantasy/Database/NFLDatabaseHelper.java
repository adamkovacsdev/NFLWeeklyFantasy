package xyz.adamkovacs.nflweeklyfantasy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import xyz.adamkovacs.nflweeklyfantasy.Database.UserContract.UserEntry;

public class NFLDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NFLDatabase";
    private static final int DATABASE_VERSION = 1;


    // Creating user table
    private static final String CREATE_TABLE_USERS="CREATE TABLE "+ UserEntry.TABLE_NAME + " ("
            +UserEntry.COLUMN_USERNAME+" TEXT PRIMARY KEY, "
            +UserEntry.COLUMN_EMAIL+" TEXT, "
            +UserEntry.COLUMN_PASSWORD+" TEXT, "
            +UserEntry.COLUMN_WEEKLY_SCORE+" INTEGER);";


    public NFLDatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + UserEntry.TABLE_NAME +"'");
    }

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


    public int getUserWeeklyPoints(String username){
        String selectQuery = "SELECT "+ UserContract.UserEntry.COLUMN_WEEKLY_SCORE+" FROM "+ UserContract.UserEntry.TABLE_NAME + " WHERE "
                + UserContract.UserEntry.COLUMN_USERNAME +"='"+username+"';";
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