package com.anoki;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.StrictMode;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.anoki.common.ContactManage;
import com.anoki.common.RestService;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Invite;
import com.anoki.pojo.Phone;
import com.anoki.pojo.Search;
import com.anoki.pojo.User;
import com.anoki.common.Global;
import com.anoki.common.Util;
import com.anoki.pojo.Account;
import com.anoki.pojo.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init();
    }

    private void init(){

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        //sqlite의 로그인 정보 확인
        Account account = dbManager.getAccount();

//        if(account == null){
            //로그인 정보가 없으면 로그인 화면으로
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
     /*   }else {

            //SQLITE의 암호 확인
            //암호가 있으면 암호 화면으로

            //암호가 없으면 최근 화면으로
            Response response = RestService.log(account.email, account.pass);
            if("0".equals(response.result)){


                ContactManage.checkContact(getContentResolver(),getApplicationContext());

                Intent intent = new Intent(this, RecentActivity.class);
                startActivity(intent);
            }

        }*/
    }


}
