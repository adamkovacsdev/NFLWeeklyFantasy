package xyz.adamkovacs.nflweeklyfantasy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import xyz.adamkovacs.nflweeklyfantasy.Classes.Match;
import xyz.adamkovacs.nflweeklyfantasy.Database.NFLDatabaseHelper;
import xyz.adamkovacs.nflweeklyfantasy.R;

public class WeeklyPickEmAdapter extends ArrayAdapter<Match> {

    int resourceLayout;
    Context context;
    NFLDatabaseHelper dbHelper;
    String username, selectedWeek;
    int homeSelected, awaySelected;
    RadioButton rb_homeSelected, rb_awaySelected;
    RadioGroup rg_selector;
    ImageView homeLogo, awayLogo;

    public WeeklyPickEmAdapter(Context context, int resource, List<Match> matches, String username, String selectedWeek) {
        super(context, resource, matches);
        this.context=context;
        this.resourceLayout=resource;
        this.username=username;
        this.selectedWeek=selectedWeek;
        Log.i("teszt","Adapter constructor");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //TODO: the selector is still bugged, multiple rbuttons being selected by changing only one. find a fix
        convertView = null; // without this line all the radio buttons would change if you change any of them

        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceLayout, null);
            dbHelper = new NFLDatabaseHelper(context);
        }

        final Match currentMatch = getItem(position);

        if(currentMatch !=null){
            final String homeTeam = currentMatch.getHomeTeam();
            final String awayTeam = currentMatch.getAwayTeam();


            homeLogo = convertView.findViewById(R.id.iv_weekly_hometeam);
            TextView homeName = convertView.findViewById(R.id.tv_weekly_hometeam_nameshort);
            homeName.setText(homeTeam);
            TextView homeScore = convertView.findViewById(R.id.tv_weekly_hometeam_score);
            String scoreStr = Integer.toString(currentMatch.getHomeScore());
            homeScore.setText(scoreStr);

            TextView awayScore = convertView.findViewById(R.id.tv_weekly_awayteam_score);
            String scoreStr1 = Integer.toString(currentMatch.getAwayScore());
            awayScore.setText(scoreStr1);
            TextView awayName = convertView.findViewById(R.id.tv_weekly_awayteam_nameshort);
            awayName.setText(awayTeam);
            awayLogo = convertView.findViewById(R.id.iv_weekly_awayteam);

            setTeamLogos(homeTeam,awayTeam);

            rb_homeSelected = convertView.findViewById(R.id.rb_home_team_selected);
            rb_awaySelected = convertView.findViewById(R.id.rb_away_team_selected);

            rg_selector = convertView.findViewById(R.id.rg_selector);

            if(currentMatch.isHomeSelected()){
                rb_homeSelected.setChecked(true);
                rb_awaySelected.setChecked(false);
            } else if(currentMatch.isAwaySelected()){
                rb_homeSelected.setChecked(false);
                rb_awaySelected.setChecked(true);
            }

            rg_selector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.rb_home_team_selected){
                        rb_homeSelected.setChecked(true);
                        rb_awaySelected.setChecked(false);
                        currentMatch.setHomeSelected(true);
                        currentMatch.setAwaySelected(false);
                        Log.i("teszt1","Hometeam: "+currentMatch.getHomeTeam()+" (selected: "+currentMatch.isHomeSelected()+" -should be: true)"+", awayteam: "+currentMatch.getAwayTeam()+" (selected: "+currentMatch.isAwaySelected()+" -should be false)\r\n");
                        awaySelected = 0; // false
                        homeSelected = 1;
                        dbHelper.AddWeeklySelected(username,selectedWeek,homeTeam,homeSelected,awayTeam,awaySelected);
                        String adatb = dbHelper.WhatsInDatabase();
                        Log.i("teszt",adatb);
                        Toast.makeText(group.getContext(), "Selected team: " + homeTeam, Toast.LENGTH_SHORT).show();

                    } else if (checkedId == R.id.rb_away_team_selected){
                        rb_awaySelected.setChecked(true);
                        rb_homeSelected.setChecked(false);
                        currentMatch.setAwaySelected(true);
                        currentMatch.setHomeSelected(false);
                        Log.i("teszt1","Hometeam: "+currentMatch.getHomeTeam()+" (selected: "+currentMatch.isHomeSelected()+" -should be: false)"+", awayteam: "+currentMatch.getAwayTeam()+" (selected: "+currentMatch.isAwaySelected()+" -should be true)\r\n");
                        awaySelected = 1; // true
                        homeSelected = 0; // false
                        dbHelper.AddWeeklySelected(username, selectedWeek, homeTeam, homeSelected, awayTeam, awaySelected);
                        String adatb = dbHelper.WhatsInDatabase();
                        Log.i("teszt",adatb+"\r\n");
                        Toast.makeText(group.getContext(), "Selected team: " + awayTeam, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        return convertView;
    }

    void setTeamLogos(String homeTeam, String awayTeam){
        if(homeTeam.equals("KC")){
            homeLogo.setImageResource(R.drawable.kc);
        } else if(homeTeam.equals("BAL")){
            homeLogo.setImageResource(R.drawable.bal);
        } else if(homeTeam.equals("GB")){
            homeLogo.setImageResource(R.drawable.gb);
        } else if(homeTeam.equals("NO")){
            homeLogo.setImageResource(R.drawable.no);
        } else if(homeTeam.equals("SF")){
            homeLogo.setImageResource(R.drawable.sf);
        } else if(homeTeam.equals("SEA")){
            homeLogo.setImageResource(R.drawable.sea);
        } else if(homeTeam.equals("ARI")){
            homeLogo.setImageResource(R.drawable.ari);
        } else if(homeTeam.equals("ATL")){
            homeLogo.setImageResource(R.drawable.atl);
        } else if(homeTeam.equals("BUF")){
            homeLogo.setImageResource(R.drawable.buf);
        } else if(homeTeam.equals("CAR")){
            homeLogo.setImageResource(R.drawable.car);
        } else if(homeTeam.equals("CHI")){
            homeLogo.setImageResource(R.drawable.chi);
        } else if(homeTeam.equals("CIN")){
            homeLogo.setImageResource(R.drawable.cin);
        } else if(homeTeam.equals("CLE")){
            homeLogo.setImageResource(R.drawable.cle);
        } else if(homeTeam.equals("DAL")){
            homeLogo.setImageResource(R.drawable.dal);
        } else if(homeTeam.equals("DEN")){
            homeLogo.setImageResource(R.drawable.den);
        } else if(homeTeam.equals("DET")){
            homeLogo.setImageResource(R.drawable.det);
        } else if(homeTeam.equals("HOU")){
            homeLogo.setImageResource(R.drawable.hou);
        } else if(homeTeam.equals("IND")){
            homeLogo.setImageResource(R.drawable.ind);
        } else if(homeTeam.equals("JAX")){
            homeLogo.setImageResource(R.drawable.jax);
        } else if(homeTeam.equals("LAC")){
            homeLogo.setImageResource(R.drawable.lac);
        } else if(homeTeam.equals("LAR")){
            homeLogo.setImageResource(R.drawable.lar);
        } else if(homeTeam.equals("LV")){
            homeLogo.setImageResource(R.drawable.lv);
        } else if(homeTeam.equals("MIA")){
            homeLogo.setImageResource(R.drawable.mia);
        } else if(homeTeam.equals("MIN")){
            homeLogo.setImageResource(R.drawable.min);
        } else if(homeTeam.equals("NYG")){
            homeLogo.setImageResource(R.drawable.nyg);
        } else if(homeTeam.equals("NYJ")){
            homeLogo.setImageResource(R.drawable.nyj);
        } else if(homeTeam.equals("PHI")){
            homeLogo.setImageResource(R.drawable.phi);
        } else if(homeTeam.equals("PIT")){
            homeLogo.setImageResource(R.drawable.pit);
        } else if(homeTeam.equals("TB")){
            homeLogo.setImageResource(R.drawable.tb);
        } else if(homeTeam.equals("TEN")){
            homeLogo.setImageResource(R.drawable.ten);
        } else if(homeTeam.equals("WAS")){
            homeLogo.setImageResource(R.drawable.was);
        } else if(homeTeam.equals("NE")){
            homeLogo.setImageResource(R.drawable.ne);
        }

        if(awayTeam.equals("KC")){
            awayLogo.setImageResource(R.drawable.kc);
        } else if(awayTeam.equals("BAL")){
            awayLogo.setImageResource(R.drawable.bal);
        } else if(awayTeam.equals("GB")){
            awayLogo.setImageResource(R.drawable.gb);
        } else if(awayTeam.equals("NO")){
            awayLogo.setImageResource(R.drawable.no);
        } else if(awayTeam.equals("SF")){
            awayLogo.setImageResource(R.drawable.sf);
        } else if(awayTeam.equals("SEA")){
            awayLogo.setImageResource(R.drawable.sea);
        } else if(awayTeam.equals("ARI")){
            awayLogo.setImageResource(R.drawable.ari);
        } else if(awayTeam.equals("ATL")){
            awayLogo.setImageResource(R.drawable.atl);
        } else if(awayTeam.equals("BUF")){
            awayLogo.setImageResource(R.drawable.buf);
        } else if(awayTeam.equals("CAR")){
            awayLogo.setImageResource(R.drawable.car);
        } else if(awayTeam.equals("CHI")){
            awayLogo.setImageResource(R.drawable.chi);
        } else if(awayTeam.equals("CIN")){
            awayLogo.setImageResource(R.drawable.cin);
        } else if(awayTeam.equals("CLE")){
            awayLogo.setImageResource(R.drawable.cle);
        } else if(awayTeam.equals("DAL")){
            awayLogo.setImageResource(R.drawable.dal);
        } else if(awayTeam.equals("DEN")){
            awayLogo.setImageResource(R.drawable.den);
        } else if(awayTeam.equals("DET")){
            awayLogo.setImageResource(R.drawable.det);
        } else if(awayTeam.equals("HOU")){
            awayLogo.setImageResource(R.drawable.hou);
        } else if(awayTeam.equals("IND")){
            awayLogo.setImageResource(R.drawable.ind);
        } else if(awayTeam.equals("JAX")){
            awayLogo.setImageResource(R.drawable.jax);
        } else if(awayTeam.equals("LAC")){
            awayLogo.setImageResource(R.drawable.lac);
        } else if(awayTeam.equals("LAR")){
            awayLogo.setImageResource(R.drawable.lar);
        } else if(awayTeam.equals("LV")){
            awayLogo.setImageResource(R.drawable.lv);
        } else if(awayTeam.equals("MIA")){
            awayLogo.setImageResource(R.drawable.mia);
        } else if(awayTeam.equals("MIN")){
            awayLogo.setImageResource(R.drawable.min);
        } else if(awayTeam.equals("NYG")){
            awayLogo.setImageResource(R.drawable.nyg);
        } else if(awayTeam.equals("NYJ")){
            awayLogo.setImageResource(R.drawable.nyj);
        } else if(awayTeam.equals("PHI")){
            awayLogo.setImageResource(R.drawable.phi);
        } else if(awayTeam.equals("PIT")){
            awayLogo.setImageResource(R.drawable.pit);
        } else if(awayTeam.equals("TB")){
            awayLogo.setImageResource(R.drawable.tb);
        } else if(awayTeam.equals("TEN")){
            awayLogo.setImageResource(R.drawable.ten);
        } else if(awayTeam.equals("WAS")){
            awayLogo.setImageResource(R.drawable.was);
        } else if(awayTeam.equals("NE")){
            awayLogo.setImageResource(R.drawable.ne);
        }
    }
}
