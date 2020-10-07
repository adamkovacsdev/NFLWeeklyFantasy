package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import xyz.adamkovacs.nflweeklyfantasy.Adapters.LeaderboardAdapter;
import xyz.adamkovacs.nflweeklyfantasy.Classes.User;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class LeaderboardActivity extends AppCompatActivity {

    ArrayList<User> usersList;
    ListView listView;
    LeaderboardAdapter leaderboardAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //TODO: Load the users from the Firebase Database by ther weeklyPoints DESC and load the list to the adapter.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        listView = findViewById(R.id.leaderboard_list_view);
        usersList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query topScoredUsers = databaseReference.child("users");
        topScoredUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    HashMap<String, Object> dataMap = (HashMap) snapshot.getValue();

                    for (String key : dataMap.keySet()) {
                        Object data = dataMap.get(key);
                        try {
                            if(leaderboardAdapter!=null){
                                leaderboardAdapter.notifyDataSetChanged();
                            }
                            HashMap<String, Object> userData = (HashMap) data;
                            String username = userData.get("username").toString();
                            String temp = userData.get("weeklyPoints").toString();
                            int weeklyPoints = Integer.parseInt(temp);
                            User user = new User(username,weeklyPoints);
                            usersList.add(user);

                            Collections.sort(usersList);


                            leaderboardAdapter = new LeaderboardAdapter(LeaderboardActivity.this,R.layout.leaderboard_item,usersList);
                            listView.setAdapter(leaderboardAdapter);
                        } catch (ClassCastException e) {

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
