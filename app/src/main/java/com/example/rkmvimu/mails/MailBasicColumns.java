package com.example.rkmvimu.mails;

import android.provider.BaseColumns;

public final class MailBasicColumns {
    private MailBasicColumns() {}

    public static class EntryColumns implements BaseColumns
    {
        public static final String TABLE_NAME = "mail_list";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_SENDER = "sender";
        public static final String COLUMN_NAME_MEANT = "meant";
        public static final String COLUMN_NAME_SUBJECT = "subject";
        public static final String COLUMN_NAME_MAIL = "mail_body";
        public static final String COLUMN_NAME_READ = "read_unread";
        public static final String COLUMN_NAME_SENDER_NUM="sender_num";
    }
}
