package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import xyz.adamkovacs.nflweeklyfantasy.Adapters.WeeklyPickEmAdapter;
import xyz.adamkovacs.nflweeklyfantasy.Classes.Match;
import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class WeeklyPickEmActivity extends AppCompatActivity {

    Spinner sp_weeks;
    WeeklyPickEmAdapter weeklyAdapter;
    List<Match> matchList;
    ListView listView;
    NFLDatabaseHelper dbHelper;
    String selectedWeek;
    String api_key="?key=";  // ADD YOUR OWN API KEY HERE!
    String season="2020REG";
    String week_current_url="https://api.sportsdata.io/v3/nfl/scores/json/CurrentWeek"+api_key;
    String week_current;
    String matches_url_base="https://api.sportsdata.io/v3/nfl/scores/json/ScoresByWeek/";
    String matches_url;
    RadioGroup rg_selection;
    CurrentWeekAsyncTask currentWeekTask;
    CurrentWeekMatchesAsyncTask currentMatchesTask;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid;
    int weeklyPoints;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_pick_em);

        currentWeekTask = new CurrentWeekAsyncTask();
        callCurrentWeekTask();

        dbHelper = new NFLDatabaseHelper(this);
        listView = findViewById(R.id.weekly_list_view);
        rg_selection = findViewById(R.id.rg_selector);
        firebaseDatabase = FirebaseDatabase.getInstance();


        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        weeklyPoints = intent.getIntExtra("weeklyPoints",0);

        weeklyPoints +=100;

        firebaseDatabase.getReference().child("users").child(uid).child("weeklyPoints").setValue(weeklyPoints);


        callCurrentWeekMatchesTask();

        sp_weeks = findViewById(R.id.sp_weekly);
        int currentweek=0;
        String[] weeks =new String[] { "Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6", "Week 7", "Week 8", "Week 9", "Week 10", "Week 11", "Week 12", "Week 13", "Week 14", "Week 15", "Week 16", "Week 17"};
        for(int i=0; i < weeks.length;i++){
            if(weeks[i].equals("Week "+week_current)){
                currentweek = i;
            }
        }
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_weeks.setAdapter(adapter);
        sp_weeks.setSelection(currentweek);
        sp_weeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                matchList.clear();
                if(weeklyAdapter!= null){
                    weeklyAdapter.notifyDataSetChanged();
                }

                selectedWeek =(String) parent.getItemAtPosition(position);
                week_current = Integer.toString(position+1);
                callCurrentWeekMatchesTask();

                weeklyAdapter = new WeeklyPickEmAdapter(view.getContext(),R.layout.weekly_list_item,matchList,uid,selectedWeek);
                listView.setAdapter(weeklyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    void callCurrentWeekTask(){
        try{
            week_current = currentWeekTask.execute(week_current_url).get();
        } catch (ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    void callCurrentWeekMatchesTask(){
        matches_url = matches_url_base+season+"/"+week_current+api_key;
        currentMatchesTask = new CurrentWeekMatchesAsyncTask();
        try{
            matchList = currentMatchesTask.execute(matches_url).get();
            for (Match m: matchList
                 ) {
                String week = "week "+Integer.toString(m.getWeekNumber());
                String matchId = m.getHomeTeam()+"@"+m.getAwayTeam();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("matches").child("weeks").child(week);

                Map<String, Object> newMatch = new HashMap<>();
                newMatch.put("hometeam",m.getHomeTeam());
                newMatch.put("homescore",m.getHomeScore());
                newMatch.put("homeselected",m.getIsHomeSelected());
                newMatch.put("awayteam",m.getAwayTeam());
                newMatch.put("awayscore",m.getAwayScore());
                newMatch.put("awayselected",m.getIsAwaySelected());

                databaseReference.child(matchId).updateChildren(newMatch);
            }

        }catch (ExecutionException e){
            e.printStackTrace();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    private static class CurrentWeekAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            if(strings.length < 1 || strings[0] == null)
                return null;

            URL url = null;
            try {
                url= new URL(strings[0]);
            } catch (Exception e){
                e.printStackTrace();
            }

            String jsonResponse = "";
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try{
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == 200){
                    inputStream = httpURLConnection.getInputStream();
                    StringBuilder stringBuilder = new StringBuilder();
                    if(inputStream != null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String sor = bufferedReader.readLine();
                        while(sor != null){
                            stringBuilder.append(sor);
                            sor = bufferedReader.readLine();
                        }
                    }
                    jsonResponse = stringBuilder.toString();
                } else {
                    Log.i("WeeklyPickEmActivity","URL response hiba,kód: "+httpURLConnection.getResponseCode());
                }
            } catch ( Exception e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return jsonResponse;
        }
    }

    private static class CurrentWeekMatchesAsyncTask extends AsyncTask<String, Void, ArrayList<Match>>{

        @Override
        protected ArrayList<Match> doInBackground(String... strings) {

            ArrayList<Match> matches = new ArrayList<>();

            if(strings.length < 1 || strings[0] == null)
                return null;

            URL url = null;
            try {
                url= new URL(strings[0]);
            } catch (Exception e){
                e.printStackTrace();
            }

            String jsonResponse = "";
            HttpURLConnection httpURLConnection = null;
            InputStream inputStream = null;
            try{
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();

                if(httpURLConnection.getResponseCode() == 200){
                    inputStream = httpURLConnection.getInputStream();
                    StringBuilder stringBuilder = new StringBuilder();
                    if(inputStream != null){
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String sor = bufferedReader.readLine();
                        while(sor != null){
                            stringBuilder.append(sor);
                            sor = bufferedReader.readLine();
                        }
                    }
                    jsonResponse = stringBuilder.toString();
                } else {
                    Log.i("WeeklyPickEmActivity","URL response hiba,kód: "+httpURLConnection.getResponseCode());
                }
            } catch ( Exception e) {
                e.printStackTrace();
            } finally {
                if(httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Match m;
            try{
                JSONArray elementsArray = new JSONArray(jsonResponse);
                for(int i=0; i < elementsArray.length(); i++){
                    JSONObject current = elementsArray.getJSONObject(i);
                    String homeTeam,awayteam,homescore,awayscore;
                    int week;
                    String homeselected,awayselected;
                    week = current.getInt("Week");
                    homeTeam = current.getString("HomeTeam");
                    homescore = current.getString("HomeScore");
                    homeselected = "0";
                    awayteam=current.getString("AwayTeam");
                    awayscore=current.getString("AwayScore");
                    awayselected="0";
                    if(homescore.equals("null"))
                        homescore="0";
                    if(awayscore.equals("null")){
                        awayscore="0";
                    }
                    m = new Match(week,homeTeam,Integer.parseInt(homescore),homeselected,awayteam,Integer.parseInt(awayscore),awayselected);
                    matches.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return matches;
        }
    }
}


