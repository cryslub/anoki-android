package com.anoki;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2015-07-03.
 */
public class DBManager  extends SQLiteOpenHelper {


    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ACCOUNT(EMAIL TEXT, PASS TEXT);");
        db.execSQL("CREATE TABLE CONTACT(_id INTEGER PRIMARY KEY AUTOINCREMENT, COUNTRY TEXT, NUMBER TEXT);");
        db.execSQL("CREATE TABLE TEAM_ALARM(_id INTEGER PRIMARY KEY AUTOINCREMENT,TEAM INTEGER, ON INTEGER, LEVEL INTEGER);");
        db.execSQL("CREATE TABLE ALARM(ON INTEGER, LEVEL INTEGER, PREVIEW INTEGER, SOUND INTEGER, VIBE INTEGER);");
        db.execSQL("CREATE TABLE PASS(PASS TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
