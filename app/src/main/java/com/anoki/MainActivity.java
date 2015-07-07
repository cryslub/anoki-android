package com.anoki;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anoki.Singleton.Global;
import com.anoki.Singleton.Util;
import com.anoki.pojo.Account;
import com.anoki.pojo.Phone;
import com.anoki.pojo.Response;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Phone phone = new Phone();
        phone.number = "01012345678";
        phone.country = "82";
        phone.device="12345678";

        //서버에 인증요청
        Response response = Util.rest("auth/log", "POST", phone, Response.class);

        if("0".equals(response.result)){
            Global.apiKey = response.apiKey;
        }

        Intent intent = new Intent(this, RecentActivity.class);
        startActivity(intent);
    }

    private void init(){

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        //sqlite의 로그인 정보 확인
        Account account = getAccount(dbManager);

        if(account == null){
            //로그인 정보가 없으면 로그인 화면으로
            
        }else {

            //로그인 정보가 있으면 로그인
            //SQLITE의 암호 확인
            //암호가 있으면 암호 화면으로
            //암호가 없으면 최근 화면으로
        }
    }


    private Account getAccount(DBManager dbManager) {
        SQLiteDatabase db = dbManager.getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select EMAIL,PASS from ACCOUNT", null);
        if(cursor.moveToNext()) {

            Account account = new Account();
            account.email = cursor.getString(0);
            account.pass = cursor.getString(1);

            return account;
        }else{
            return null;
        }
    }
}
