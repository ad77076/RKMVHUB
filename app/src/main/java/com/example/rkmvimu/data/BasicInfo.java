package com.example.rkmvimu.data;

import android.provider.BaseColumns;

public final class BasicInfo {
    private BasicInfo() {}

    public static class EntryColumns implements BaseColumns {
        public static final String TABLE_NAME = "basic_information";
        public static final String COLUMN_NAME_MOBILE = "mobile_number";
        public static final String COLUMN_NAME_NAME = "full_name";
        public static final String COLUMN_NAME_PASSWORD="password";
        public static final String COLUMN_NAME_EMAIL="email";
        public static final String COLUMN_NAME_DEPARTMENT="department";
        public static final String COLUMN_NAME_ROLE="role";
        public static final String COLUMN_NAME_PICTURE="profile_picture";
        public static final String COLUMN_NAME_LOCAL_LOGIN="local_login";

    }
}
