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
      /*  usersList = new ArrayList<>();
        usersList.add(new User("justluck4","emailcim","pw",100));
        usersList.add(new User("justluck1","emailcim","pw",400));
        usersList.add(new User("justluck3","emailcim","pw",200));
        usersList.add(new User("justluck2","emailcim","pw",350));*/


        leaderboardAdapter = new LeaderboardAdapter(this,R.layout.leaderboard_item,usersList);
        listView.setAdapter(leaderboardAdapter);



    }
}
