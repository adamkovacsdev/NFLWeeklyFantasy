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
import android.widget.RadioGroup;
import android.widget.Spinner;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import xyz.adamkovacs.nflweeklyfantasy.Adapters.WeeklyMatchesAdapter;
import xyz.adamkovacs.nflweeklyfantasy.Classes.Match;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class WeeklyMatchesActivity extends AppCompatActivity {

    Spinner sp_weeks;
    WeeklyMatchesAdapter weeklyAdapter;
    List<Match> matchList;
    ListView listView;
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

    //TODO: Make user selections in other layout, then load it here from the database.
    //Make the weekly scores section call the data from the API every 10-30mins. (have to decide)
    //Set the background color of the listview item green if the match is over and the user selected the winner.
    //Set the background color of the listview item red if the match is over and the user selected the loser.
    //If every match is over, calculate the users weekly score, write it to the database and the score to the user's overall points.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_matches);

        currentWeekTask = new CurrentWeekAsyncTask();
        callCurrentWeekTask();

        listView = findViewById(R.id.weekly_list_view);
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

                weeklyAdapter = new WeeklyMatchesAdapter(view.getContext(),R.layout.weekly_list_item,matchList,uid,selectedWeek);
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

            Collections.sort(matchList, new Comparator<Match>() {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                @Override
                public int compare(Match match, Match t1) {
                    try {
                        return format.parse(match.getStartDateStr()).compareTo(format.parse(t1.getStartDateStr()));
                    } catch (ParseException e){
                        throw new IllegalArgumentException(e);
                    }
                }
            });


            for (Match m: matchList
                 ) {
                String week = "week "+Integer.toString(m.getWeekNumber());
                String matchId = m.getHomeTeam()+"@"+m.getAwayTeam();
                databaseReference = FirebaseDatabase.getInstance().getReference().child("matches").child("weeks").child(week);

                Map<String, Object> newMatch = new HashMap<>();
                Log.i("currdate","Current date: "+m.getStartDate());
                newMatch.put("date",m.getStartDateStr());
                newMatch.put("hometeam",m.getHomeTeam());
                newMatch.put("homescore",m.getHomeScore());
                newMatch.put("homeselected",m.getIsHomeSelected());
                newMatch.put("awayteam",m.getAwayTeam());
                newMatch.put("awayscore",m.getAwayScore());
                newMatch.put("awayselected",m.getIsAwaySelected());
                newMatch.put("isover",m.isOver());

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
                    Log.i("WeeklyMatchesActivity","URL response hiba,kód: "+httpURLConnection.getResponseCode());
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
                    Log.i("WeeklyMatchesActivity","URL response hiba,kód: "+httpURLConnection.getResponseCode());
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
                    String homeTeam,awayteam,homescore,awayscore,startDateStr;
                    boolean isOver;
                    Date startDate;
                    int week;
                    startDateStr = current.getString("Date");
                    week = current.getInt("Week");
                    homeTeam = current.getString("HomeTeam");
                    homescore = current.getString("HomeScore");
                    awayteam=current.getString("AwayTeam");
                    awayscore=current.getString("AwayScore");
                    if(homescore.equals("null"))
                        homescore="0";
                    if(awayscore.equals("null")){
                        awayscore="0";
                    }
                    isOver = current.getBoolean("IsOver");
                    m = new Match(startDateStr,week,homeTeam,Integer.parseInt(homescore),awayteam,Integer.parseInt(awayscore), isOver);
                    matches.add(m);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return matches;
        }
    }
}


