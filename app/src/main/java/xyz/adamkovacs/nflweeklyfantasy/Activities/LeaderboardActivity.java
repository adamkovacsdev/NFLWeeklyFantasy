package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import xyz.adamkovacs.nflweeklyfantasy.Adapters.LeaderboardAdapter;
import xyz.adamkovacs.nflweeklyfantasy.Classes.User;
import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class LeaderboardActivity extends AppCompatActivity {

    NFLDatabaseHelper dbHelper;
    List<User> usersList;
    ListView listView;
    LeaderboardAdapter leaderboardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        dbHelper = new NFLDatabaseHelper(this);
        listView = findViewById(R.id.leaderboard_list_view);
        usersList = dbHelper.getUsers();

        leaderboardAdapter = new LeaderboardAdapter(this,R.layout.leaderboard_item,usersList);
        listView.setAdapter(leaderboardAdapter);

    }
}
