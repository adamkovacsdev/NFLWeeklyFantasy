package xyz.adamkovacs.nflweeklyfantasy.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

import xyz.adamkovacs.nflweeklyfantasy.R;

public class MainActivity extends AppCompatActivity {

    TextView tv_welcometext, tv_weeklypoints, tv_loggedinuser, tv_logout;
    String username;
    int weeklypoints;
    ImageView iv_matches,iv_weeklypickem,iv_leaderboard,iv_profile,iv_logout;
    Button btn_weekly, btn_leaderboard;
    String mUsername;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ChildEventListener childEventListener;
    FirebaseAuth myFirebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    private static final int RC_SIGN_IN = 123;
    String welcome_msg;
    String uid;
    int value;

    //TODO: Make changes to the UI. On the top welcome the user as it is.
    //TODO: Below add imageview (have to be created in photoshop) as wide as the parent view.
    //Scores
    //Weekly Pick'Em
    //Leaderboard
    //Profile
    //Logout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername = "anonymous";
        tv_welcometext = findViewById(R.id.tv_main_welcometext);

        iv_matches = findViewById(R.id.iv_iv_main_matches);
        iv_weeklypickem = findViewById(R.id.iv_main_weeklypickem);
        iv_leaderboard = findViewById(R.id.iv_main_leaderboards);
        iv_profile = findViewById(R.id.iv_main_profile);
        iv_logout = findViewById(R.id.iv_main_logout);


        firebaseDatabase = FirebaseDatabase.getInstance();
        myFirebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //if the user is signed in
                    uid = user.getUid();
                    firebaseDatabase.getReference().child("users").child(user.getUid()).child("username").setValue(user.getDisplayName());
                    firebaseDatabase.getReference().child("users").child(user.getUid()).child("weeklyPoints").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.getValue(Integer.class) != null){
                                value = snapshot.getValue(Integer.class);
                            } else {
                                value = 0;
                            }

                            if(weeklypoints < value){
                                weeklypoints = value;
                            }
                            Log.i("value","Value is: "+value);
                            Log.i("value","WeeklyPoints is: "+weeklypoints);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            weeklypoints = 0;
                        }
                    });
                    onSignedIn(user.getDisplayName());
                } else {
                    //if the user is signed out
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            new AuthUI.IdpConfig.GoogleBuilder().build(),
                                            new AuthUI.IdpConfig.EmailBuilder().build()))
                                    .setLogo(R.drawable.nfllogo)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                Toast.makeText(this,"Signed in!",Toast.LENGTH_SHORT).show();
            } else {
                if ( resultCode == RESULT_CANCELED){
                    Toast.makeText(this,"Sign in canceled!",Toast.LENGTH_LONG).show();
                    return;
                }

                if( response == null){
                    Toast.makeText(this,"Sign in canceled!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if( response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    Toast.makeText(this,"No internet connection!",Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this,"An unexpected error occured!",Toast.LENGTH_LONG).show();
                Log.e("MainActivity","Sign-in error: "+response.getError());
            }
        }
    }

    //Attaching FirebaseAuth listener
    @Override
    protected void onResume() {
        super.onResume();
        myFirebaseAuth.addAuthStateListener(authStateListener);

    }

    //Detaching FirebaseAuth listener
    @Override
    protected void onPause() {
        super.onPause();
        if(authStateListener != null){
            myFirebaseAuth.removeAuthStateListener(authStateListener);
        }
        detachDatabaseReadListener();
    }


    private void onSignedIn(String username){
        mUsername = username;
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this);
                onSignedOut();
            }
        });

        iv_matches.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), WeeklyMatchesActivity.class);
                i.putExtra("uid",uid);
                i.putExtra("weeklyPoints",weeklypoints);
                if(i.resolveActivity(getPackageManager()) != null)
                    startActivity(i);
            }
        });

       iv_leaderboard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(getApplicationContext(), LeaderboardActivity.class);
               if(i.resolveActivity(getPackageManager()) != null)
                   startActivity(i);
           }
       });

        attachDatabaseReadListener();
    }

    private void onSignedOut(){
        mUsername = "anonymous";
        weeklypoints = 0;
        Toast.makeText(MainActivity.this,"Logged out!",Toast.LENGTH_SHORT).show();
        detachDatabaseReadListener();
    }

    private void attachDatabaseReadListener(){
        if(childEventListener == null){
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    // Add code here
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            };
        }
    }

    private void detachDatabaseReadListener(){
        if(childEventListener != null){
            //Remove childEventListener from databaseReference with databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

}
