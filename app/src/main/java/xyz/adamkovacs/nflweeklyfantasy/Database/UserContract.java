package xyz.adamkovacs.nflweeklyfantasy.Database;

import android.provider.BaseColumns;

public class UserContract {

    private UserContract() {}

    public static final class UserEntry implements BaseColumns {

        public final static String TABLE_NAME = "users";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USERNAME="username";
        public final static String COLUMN_EMAIL="email";
        public final static String COLUMN_PASSWORD="password";
        public final static String COLUMN_WEEKLY_SCORE="weekly_score";

    }
}
