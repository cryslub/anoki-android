package com.anoki;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.anoki.pojo.Account;
import com.anoki.pojo.Phone;
import com.anoki.pojo.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015-07-03.
 */
public class DBManager  extends SQLiteOpenHelper {

    SQLiteDatabase db;

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        db = getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ACCOUNT(EMAIL TEXT, PASS TEXT,PHONE TEXT, COUNTRY TEXT);");
        db.execSQL("CREATE TABLE CONTACT(_id INTEGER PRIMARY KEY AUTOINCREMENT, COUNTRY TEXT, PHONE TEXT);");
        db.execSQL("CREATE TABLE TEAM_ALARM(_id INTEGER PRIMARY KEY AUTOINCREMENT,TEAM INTEGER, ONOFF INTEGER, LEVEL INTEGER);");
        db.execSQL("CREATE TABLE ALARM(ONOFF INTEGER, LEVEL INTEGER, PREVIEW INTEGER, SOUND INTEGER, VIBE INTEGER);");
        db.execSQL("CREATE TABLE PASS(PASS TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Account getAccount() {
        String str = "";

        Cursor cursor = db.rawQuery("SELECT EMAIL,PASS FROM ACCOUNT", null);
        if(cursor.moveToNext()) {

            Account account = new Account();
            account.email = cursor.getString(0);
            account.pass = cursor.getString(1);

            return account;
        }else{
            return null;
        }
    }

    public void setAccount(String email, String pass){
        db.execSQL("INSERT INTO ACCOUNT (EMAIL,PASS) VALUES('"+email+"','"+pass+"')");
    }

    public Map<String,String> getContactMap() {

        Map<String,String> contactMap  = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT PHONE FROM CONTACT", null);
        while (cursor.moveToNext()) {
            contactMap.put(cursor.getString(0), cursor.getString(0));
        }

        return contactMap;
    }

    public void insertContactInfo(Phone phone){
        db.execSQL("INSERT INTO CONTACT (PHONE) VALUES('"+phone.number+"')");
    }
}
