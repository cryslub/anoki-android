package com.anoki1;

import android.app.Activity;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.os.Handler;
import android.os.StrictMode;
import android.os.Bundle;

import com.anoki1.common.CallBack;
import com.anoki1.common.Global;
import com.anoki1.common.DBManager;
import com.anoki1.common.RestService;
import com.anoki1.common.Util;
import com.anoki1.pojo.Account;
import com.anoki1.pojo.Response;
import com.anoki1.user.LoginActivity;

import java.io.File;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Get the Default External Cache Directory
        File httpCacheDir = getExternalCacheDir();

        // Cache Size of 5MB
        long httpCacheSize = 5 * 1024 * 1024;

        try {
            // Install the custom Cache Implementation
            HttpResponseCache.install(httpCacheDir, httpCacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }



        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);






    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {


                final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);
                String pass = dbManager.getPass();
                if(pass == null) {
                    init();
                //              init2();

                }else{
                    Intent intent = new Intent(MainActivity.this, CheckPassActivity.class);
                    intent.putExtra("type","start");
                    startActivity(intent);
                }
            }
        }, 2000);
    }

    private void init2(){

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        //sqlite의 로그인 정보 확인
        Account account = dbManager.getAccount();

//        if(account == null){
            //로그인 정보가 없으면 로그인 화면으로
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
 /*      }else {

            //SQLITE의 암호 확인
            //암호가 있으면 암호 화면으로

            //암호가 없으면 최근 화면으로
            Response response = RestService.log(account.email, account.pass);
            if("0".equals(response.result)){




                Intent intent = new Intent(this, RecentActivity.class);
                startActivity(intent);
            }

        }*/
    }

    private void init(){

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        //sqlite의 로그인 정보 확인
        Account account = dbManager.getAccount();

        if(account == null){
        //로그인 정보가 없으면 로그인 화면으로
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
       }else {

            //SQLITE의 암호 확인
            //암호가 있으면 암호 화면으로

            //암호가 없으면 최근 화면으로
            RestService.log(account.email, account.pass, new CallBack<Response>() {
                @Override
                public void success(Response response) {
                    if("0".equals(response.result)){

                        if(Global.me.regid == null){
                            Util.setRegId(Global.me, getApplicationContext());
                        }

                        Intent intent = new Intent(MainActivity.this, RecentActivity.class);
                        startActivity(intent);
                    }

                }
            });
        }
    }
}
