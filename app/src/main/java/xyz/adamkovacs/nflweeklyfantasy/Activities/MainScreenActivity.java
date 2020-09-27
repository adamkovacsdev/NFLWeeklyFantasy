package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class MainScreenActivity extends AppCompatActivity {

    TextView tv_welcometext, tv_weeklypoints, tv_loggedinuser, tv_logout;
    String username;
    int weeklypoints;
    NFLDatabaseHelper dbHelper;
    Button btn_weekly, btn_leaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        tv_welcometext = findViewById(R.id.tv_mainscreen_welcometext);
        tv_weeklypoints = findViewById(R.id.tv_mainscreen_weeklypoints);
        tv_loggedinuser = findViewById(R.id.tv_mainscreen_loggedinuser);
        tv_logout = findViewById(R.id.tv_mainscreen_logout);
        btn_weekly = findViewById(R.id.btn_mainscreen_weekly);
        btn_leaderboard = findViewById(R.id.btn_mainscreen_leaderboard);
        dbHelper = new NFLDatabaseHelper(this);


        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        tv_loggedinuser.setText(username);
        tv_welcometext.append(username + "!");
        weeklypoints = dbHelper.getUserWeeklyPoints(username);


        tv_weeklypoints.setText(Integer.toString(weeklypoints));

        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                if(i.resolveActivity(getPackageManager()) != null)
                    startActivity(i);
            }
        });

        btn_weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WeeklyPickEmActivity.class);
                i.putExtra("Username", username);
                if(i.resolveActivity(getPackageManager()) != null)
                    startActivity(i);
            }
        });


        btn_leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LeaderboardActivity.class);
                if(i.resolveActivity(getPackageManager()) != null)
                    startActivity(i);
            }
        });
    }
}
