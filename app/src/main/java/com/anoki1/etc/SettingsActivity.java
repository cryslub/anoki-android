package com.anoki1.etc;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anoki1.CheckPassActivity;
import com.anoki1.R;
import com.anoki1.common.AnokiDialog;
import com.anoki1.common.CallBack;
import com.anoki1.common.DBManager;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.common.Global;
import com.anoki1.pojo.DialogData;
import com.anoki1.pojo.Response;
import com.anoki1.user.AccountActivity;
import com.anoki1.user.LoginActivity;
import com.anoki1.user.PassActivity;

import butterknife.Bind;

public class SettingsActivity extends SubActivityBase {


    private static final int LEAVE_DIALOG = 100;

    @Bind(R.id.version)
    TextView version;

    @Bind(R.id.lock)
    TextView lock;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version.setText( pInfo.versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    public void load(){

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        String pass = dbManager.getPass();
        if(pass == null) {
            lock.setText("꺼짐");
        }else{
            lock.setText("켜짐");
        }
    }

    public void version(View view){
        Intent intent = new Intent(SettingsActivity.this, VersionActivity.class);
        startActivity(intent);
    }

    public void account(View view){
        Intent intent = new Intent(SettingsActivity.this, AccountActivity.class);
        startActivity(intent);
    }

    public void pass(View view){
        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);
        String pass = dbManager.getPass();

        if(pass == null) {
            Intent intent = new Intent(SettingsActivity.this, PassActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SettingsActivity.this, CheckPassActivity.class);
            intent.putExtra("type","pass");
            startActivity(intent);
        }
    }

    public void blocked(View view){
        Intent intent = new Intent(SettingsActivity.this, BlockedActivity.class);
        startActivity(intent);
    }

    public void alarm(View view){
        Intent intent = new Intent(SettingsActivity.this, AlarmSettingActivity.class);
        startActivity(intent);

    }

    public void groupAlarm(View view){

    }

    public void help(View view){

    }

    public void support(View view){

    }

    public void leave(View view){
        showDialog(LEAVE_DIALOG);
    }



    public Dialog onCreateDialog(int id) {

        DialogData data = new DialogData();
        data.title="탈퇴";
        data.text = "탈퇴하시겠습니까?" ;
        data.onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        };


        return new AnokiDialog(this,data);

    }

    void deleteUser(){


        backGroundRest("user", "DELETE", Global.me, Response.class, new CallBack<Response>() {
            @Override
            public void success(Response result) {
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finishAffinity();
            }
        });


    }
}
