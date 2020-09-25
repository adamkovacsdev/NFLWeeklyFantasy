package xyz.adamkovacs.nflweeklyfantasy.Database;

import android.provider.BaseColumns;

public class WeeklySelectionsContract  {

    private WeeklySelectionsContract() {
    }

    public static final class WeeklySelectionEntry implements BaseColumns {

        public final static String TABLE_NAME = "weekly_user";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USERNAME = "username";
        public final static String COLUMN_WEEK = "week";
        public final static String COLUMN_HOMETEAM="hometeam";
        public final static String COLUMN_IS_HOMETEAM_SELECTED="is_hometeam_selected";
        public final static String COLUMN_AWAYTEAM="awayteam";
        public final static String COLUMN_IS_AWAYTEAM_SELECTED="is_awayteam_selected";
    }
}
