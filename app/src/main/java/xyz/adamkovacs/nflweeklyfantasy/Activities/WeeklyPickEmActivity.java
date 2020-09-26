package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.adamkovacs.nflweeklyfantasy.Adapters.WeeklyPickEmAdapter;
import xyz.adamkovacs.nflweeklyfantasy.Classes.Match;
import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class WeeklyPickEmActivity extends AppCompatActivity {

    Spinner sp_weeks;
    WeeklyPickEmAdapter weeklyAdapter;
    List<Match> matchList, sortedMatchList;
    ListView listView;
    RadioButton rb_homeSelected, rb_awaySelected;
    NFLDatabaseHelper dbHelper;
    String username = "justluck";
    String selectedWeek;
    int homeSelected, awaySelected;
    String api_key="";  // ADD YOUR OWN API KEY HERE!
    String season="2020REG";
    String week_current_url="https://api.sportsdata.io/v3/nfl/scores/json/CurrentWeek"+api_key;
    String week_current="";
    String matches_url="https://api.sportsdata.io/v3/nfl/scores/json/ScoresByWeek/"+season+"/"+week_current+api_key;
    RadioGroup rg_selection;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_pick_em);


        dbHelper = new NFLDatabaseHelper(this);
        listView = findViewById(R.id.weekly_list_view);
        rg_selection = findViewById(R.id.rg_selector);

        matchList = new ArrayList<Match>();
        sortedMatchList = new ArrayList<>();

        matchList.add(new Match(3,"MIA",31,false,"JAX",13,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"CHI",0,false,"ATL",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"LAR",0,false,"BUF",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"WAS",0,false,"CLE",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"TEN",0,false,"MIN",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"LV",0,false,"NE",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"SF",0,false,"NYG",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"CIN",0,false,"PHI",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"HOU",0,false,"PIT",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"NYJ",0,false,"IND",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"CAR",0,false,"LAC",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"DET",0,false,"ARI",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"TB",0,false,"DEN",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"DAL",0,false,"SEA",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"GB",0,false,"NO",0,false,Calendar.getInstance().getTime()));
        matchList.add(new Match(3,"KC",0,false,"BAL",0,false,Calendar.getInstance().getTime()));


        sp_weeks = findViewById(R.id.sp_weekly);

        String[] weeks =new String[] { "Week 1", "Week 2", "Week 3", "Week 4", "Week 5", "Week 6", "Week 7", "Week 8", "Week 9", "Week 10", "Week 11", "Week 12", "Week 13", "Week 14", "Week 15", "Week 16", "Week 17"};
        ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, weeks);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_weeks.setAdapter(adapter);

        sp_weeks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sortedMatchList.clear();

                if(weeklyAdapter!= null){
                    weeklyAdapter.notifyDataSetChanged();
                }

                selectedWeek =(String) parent.getItemAtPosition(position);

                for (Match m: matchList
                ) {
                    String weekNew = "Week "+m.getWeekNumber();
                    if(weekNew.equals(selectedWeek) && !sortedMatchList.contains(m)){
                        sortedMatchList.add(m);
                    }
                }
                weeklyAdapter = new WeeklyPickEmAdapter(view.getContext(),R.layout.weekly_list_item,sortedMatchList,username,selectedWeek);
                listView.setAdapter(weeklyAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
