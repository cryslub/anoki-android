package com.anoki.common;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anoki.pojo.Account;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;
import com.anoki.pojo.Phone;
import com.anoki.pojo.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015-07-03.
 */
public class DBManager  extends SQLiteOpenHelper {


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE ACCOUNT(EMAIL TEXT, PASS TEXT,PHONE TEXT, COUNTRY TEXT);");
        db.execSQL("CREATE TABLE CONTACT(_id INTEGER PRIMARY KEY AUTOINCREMENT, COUNTRY TEXT, PHONE TEXT);");
        db.execSQL("CREATE TABLE TEAM_ALARM(_id INTEGER PRIMARY KEY AUTOINCREMENT,TEAM INTEGER, ONOFF INTEGER, LEVEL INTEGER);");
        db.execSQL("CREATE TABLE ALARM(ONOFF INTEGER, LEVEL INTEGER, PREVIEW INTEGER, SOUND INTEGER, VIBE INTEGER);");
        db.execSQL("CREATE TABLE PASS(PASS TEXT);");
        db.execSQL("CREATE TABLE MESSAGE(_id INTEGER PRIMARY KEY AUTOINCREMENT,USER INTEGER,SENDER TEXT, MESSAGE TEXT,PICTURE INTEGER,USER_PICTURE INTEGER,CHECKED INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Account getAccount() {

        SQLiteDatabase db = getReadableDatabase();

        String str = "";

        Cursor cursor = db.rawQuery("SELECT EMAIL,PASS FROM ACCOUNT", null);
        Account ret = null;
        if(cursor.moveToNext()) {

            Account account = new Account();
            account.email = cursor.getString(0);
            account.pass = cursor.getString(1);


            ret = account;
        }

        cursor.close();
        db.close();
        return ret;

    }


    public String getPass() {

        SQLiteDatabase db = getReadableDatabase();

        String str = "";

        Cursor cursor = db.rawQuery("SELECT PASS FROM PASS", null);
        String ret = null;
        if(cursor.moveToNext()) {

            ret = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return ret;

    }

    public void setAccount(String email, String pass){
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("INSERT INTO ACCOUNT (EMAIL,PASS) VALUES('" + email + "','" + pass + "')");

        db.close();
    }

    public Map<String,String> getContactMap() {
        SQLiteDatabase db = getReadableDatabase();

        Map<String,String> contactMap  = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT PHONE FROM CONTACT", null);
        while (cursor.moveToNext()) {
            contactMap.put(cursor.getString(0), cursor.getString(0));
        }

        cursor.close();
        db.close();
        return contactMap;
    }

    public void insertContactInfo(Friend friend){
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("INSERT INTO CONTACT (PHONE) VALUES('" + friend.phone + "')");
        db.close();
    }

    public void insertMessage(Message message){
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("INSERT INTO MESSAGE (USER,SENDER,MESSAGE,PICTURE,USER_PICTURE,CHECKED) VALUES(" + message.user + ",'" + message.sender + "','" + message.text + "'," + message.picture + "," + message.userPicture + "," + message.checked + ")");
        db.close();
    }


    public List<Message> getMessage() {
        SQLiteDatabase db = getReadableDatabase();

        List<Message> list = new ArrayList<Message>();
        Cursor cursor = db.rawQuery("SELECT USER,SENDER,MESSAGE,PICTURE,USER_PICTURE,CHECKED,_id FROM MESSAGE", null);
        while (cursor.moveToNext()) {
            Message message = new Message();
            message.user = cursor.getInt(0);
            message.sender = cursor.getString(1);
            message.text = cursor.getString(2);
            message.picture = cursor.getInt(3)+"";
            message.userPicture = cursor.getInt(4)+"";
            message.checked = cursor.getInt(5);
            message.id = cursor.getInt(6);

            list.add(message);
        }

        cursor.close();
        db.close();
        return list;
    }

    public void updateMessage(Message message){

        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("UPDATE MESSAGE SET CHECKED = " + message.checked + " WHERE _id=" + message.id);
        db.close();
    }

    public void deleteMessage(Message message){

        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("DELETE FROM MESSAGE  WHERE _id="+message.id);
        db.close();
    }

}
