package xyz.adamkovacs.nflweeklyfantasy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import xyz.adamkovacs.nflweeklyfantasy.Classes.Match;
import xyz.adamkovacs.nflweeklyfantasy.Classes.User;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class LeaderboardAdapter extends ArrayAdapter<User> {

    int resourceLayout;
    Context context;
    List<User> users;

    public LeaderboardAdapter(@NonNull Context context, int resource, List<User> users) {
        super(context, resource,users);
        this.context=context;
        this.resourceLayout=resource;
        this.users=users;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceLayout, null);
        }

         User currentUser = getItem(position);

        if(currentUser !=null){

            TextView tv_placement = convertView.findViewById(R.id.tv_leaderboard_placement);
            tv_placement.setText(currentUser.getPlacement());
            TextView tv_username = convertView.findViewById(R.id.tv_leaderboard_username);
            tv_username.setText(currentUser.getUsername());
            TextView tv_score = convertView.findViewById(R.id.tv_leaderboard_score);
            String userScore = Integer.toString(currentUser.getWeekly_score());
            tv_score.setText(userScore);

            ImageView iv_placement = convertView.findViewById(R.id.iv_leaderboard_placement);
            if(position == 0){
                iv_placement.setImageResource(R.drawable.firstplace);
            } else if ( position == 1){
                iv_placement.setImageResource(R.drawable.secondplace);
            } else if (position == 2){
                iv_placement.setImageResource(R.drawable.thirdplace);
            } else {
                iv_placement.setImageResource(R.drawable.notintop3);
            }
        }

        return convertView;
    }
}
