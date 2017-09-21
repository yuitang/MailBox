package com.example.yui.mailbox.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.yui.mailbox.bean.MailBean;
import com.example.yui.mailbox.bean.UserBean;

import java.util.ArrayList;
import java.util.List;


public class DBUtils {

    // 数据库版本号
    private static final int DATABASE_VERSION = 5;
    // 数据库名
    private static final String DATABASE_NAME = "MailDB.db";

    private static DBOpenHelper mDBOpenHelper;
    private static SQLiteDatabase mSqLiteDatabase;

    private DBUtils(){}

    public static int queryTokenByMessageId(int messageId){
        Cursor cursor = mSqLiteDatabase.query("maillist", null, "messageId=?",
                new String[]{messageId + ""}, null, null, null);
        cursor.moveToNext();
        int token = cursor.getInt(cursor.getColumnIndex("token"));
        return token;
    }

    public static String getUserName(){
        Cursor cursor = mSqLiteDatabase.query("user", null, "token=?",
                new String[]{"1"}, null, null, null);
        cursor.moveToNext();
        String userName = cursor.getString(cursor.getColumnIndex("userName"));
        return userName;
    }

    public static void prepare(Context context){
        mDBOpenHelper = new DBOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        // 以可写入的方式获取数据库
        mSqLiteDatabase = mDBOpenHelper.getWritableDatabase();
    }

    public static void insertDateByMailBean(MailBean bean){
        if (!hasMailOrNot(bean.getMessageId() + "")){
            ContentValues values = new ContentValues();
            values.put("messageId", bean.getMessageId());
            values.put("senderName", bean.getName());
            values.put("senderAddress", bean.getSender());
            values.put("receiverAddress", bean.getReceiver());
            values.put("hasFile", bean.getHasFile());
            values.put("subject", bean.getSubject());
            values.put("receivedDate", bean.getReceivedData());
            values.put("sentDate", bean.getSentDate());
            values.put("isNew", bean.getNew());
            values.put("type", bean.getMailType());
            values.put("token", 0);
            insertDate("maillist", values);
            values.clear();
        }
    }

    //插入
    public static void insertDate(String tableName, ContentValues values){

        mSqLiteDatabase.insert(tableName, null, values);
    }

    private static boolean hasMailOrNot(String id){
        Cursor cursor = mSqLiteDatabase.query("maillist", null, "messageId=?",
                new String[]{id}, null, null, null);
        if (cursor.moveToNext()){
            return true;
        }
        return false;
    }

    public static boolean hasAccountOrNot(String account){
        Cursor cursor = mSqLiteDatabase.query("user", null, "mailAddress=?",
                new String[]{account}, null, null, null);
        if (cursor.moveToNext()){
            return true;
        }
        return false;
    }

    public static int getUserLastPosition(){
        Cursor cursor = mSqLiteDatabase.query("user", null, "token=?",
                new String[]{"1"}, null, null, null);

        int number = 0;
        while (cursor.moveToNext()){
            number = cursor.getInt(cursor.getColumnIndex("lastNumber"));
        }

        return number;
    }

    public static UserBean getUserByToken(){
        Cursor cursor = mSqLiteDatabase.query("user", null, "token=?",
                new String[]{"1"}, null, null, null);
        while (cursor.moveToNext()){
            UserBean bean = new UserBean();

            bean.setMailAddress(cursor.getString(cursor.getColumnIndex("mailAddress")));
            bean.setPassword(cursor.getString(cursor.getColumnIndex("password")));

            return bean;
        }

        return null;
    }

    public static MailBean queryMaiByMessageId(int messageId){
        Cursor cursor = mSqLiteDatabase.query("maillist", null, "messageId=?",
                new String[]{messageId + ""}, null, null, null);

        cursor.moveToNext();
        // 遍历Cursor对象，取出数据。
        MailBean bean = new MailBean();

        bean.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
        bean.setName(cursor.getString(cursor.getColumnIndex("senderName")));
        bean.setSender(cursor.getString(cursor.getColumnIndex("senderAddress")));
        bean.setReceiver(cursor.getString(cursor.getColumnIndex("receiverAddress")));
        bean.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
        bean.setReceivedData(cursor.getString(cursor.getColumnIndex("receivedDate")));
        bean.setSentDate(cursor.getString(cursor.getColumnIndex("sentDate")));
        bean.setMailType(cursor.getInt(cursor.getColumnIndex("type")));
        bean.setMailContext(cursor.getString(cursor.getColumnIndex("mailContent")));

        cursor.close();
        return bean;
    }

    //遍历maillist表
    public static List<MailBean> queryMailList(){

        List<MailBean> list = new ArrayList<>();

        Cursor cursor = mSqLiteDatabase.query("maillist", null, null, null, null, null, "messageId desc");
        // 遍历Cursor对象，取出数据。
        while (cursor.moveToNext()) {
            MailBean bean = new MailBean();

            bean.setMessageId(cursor.getString(cursor.getColumnIndex("messageId")));
            bean.setName(cursor.getString(cursor.getColumnIndex("senderName")));
            bean.setSender(cursor.getString(cursor.getColumnIndex("senderAddress")));
            bean.setReceiver(cursor.getString(cursor.getColumnIndex("receiverAddress")));
            bean.setSubject(cursor.getString(cursor.getColumnIndex("subject")));
            bean.setReceivedData(cursor.getString(cursor.getColumnIndex("receivedDate")));
            bean.setSentDate(cursor.getString(cursor.getColumnIndex("sentDate")));
            bean.setHasFile(cursor.getInt(cursor.getColumnIndex("hasFile")));
            bean.setNew(cursor.getInt(cursor.getColumnIndex("isNew")));

            list.add(bean);
        }
        // 记得关闭资源
        cursor.close();
        return list;
    }
    //遍历user表
    public static List<UserBean> queryUser(){
        List<UserBean> list = new ArrayList<>();

        Cursor cursor = mSqLiteDatabase.query("user", null, null, null, null, null, null);
        // 遍历Cursor对象，取出数据。
        while (cursor.moveToNext()) {
            UserBean bean = new UserBean();
            bean.setMailAddress(cursor.getString(cursor.getColumnIndex("mailAddress")));
            bean.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            list.add(bean);
        }
        // 记得关闭资源
        cursor.close();
        return list;
    }
    //更新
    public static void update(String tableName, String key, String value, ContentValues values){
        // 更新数据
        mSqLiteDatabase.update(tableName, values,
                key + " = ?", new String[]{value});
    }
    //删除
    public static void delete(String tableName, String key, String value){
        mSqLiteDatabase.delete(tableName, key + " = ?", new String[]{value});
    }
}
