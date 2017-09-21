package com.example.yui.mailbox.dao;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper{

    private Context mContext;

    private String maillist, user;

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        this(context, name, factory, version, null);
    }

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 创建book表
        user = "create table user(" +
                "id integer primary key autoincrement, " +
                "mailAddress text, " +
                "password text," +
                "userName text," +
                "lastNumber text," +
                "startNumber text," +
                "token int)";
        sqLiteDatabase.execSQL(user);
        // 创建category表
        maillist = "create table maillist(" +
                "id integer primary key autoincrement, " +
                "messageId text, " +
                "senderName text, " +
                "senderAddress text, " +
                "receiverAddress text, " +
                "mailContent text, " +
                "subject text, " +
                "sentDate text, " +
                "receivedDate text, " +
                "hasFile integer, " +
                "token integer, " +
                "type integer, " +
                "isNew integer)";
        sqLiteDatabase.execSQL(maillist);
        // 提示数据库创建完毕
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE user;");
        sqLiteDatabase.execSQL("DROP TABLE maillist;");

        onCreate(sqLiteDatabase);
    }
}
