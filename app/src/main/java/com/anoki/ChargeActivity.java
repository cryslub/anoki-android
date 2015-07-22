package com.anoki;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.vending.billing.IInAppBillingService;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;
import com.google.android.gms.games.Notifications;

import java.util.List;


public class ChargeActivity extends SubActivityBase {

    IInAppBillingService mService;
    private Prayer prayer;
    private int selected;


    ServiceConnection mServiceConn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mService = IInAppBillingService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charge, menu);
        return true;
    }

    public void onRadioButtonClicked(View view){
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        List<View> viewList = Util.getAllChildren(container);
        for(View v : viewList){
            if(v instanceof RadioButton){
                RadioButton radioButton = (RadioButton) v;
                radioButton.setChecked(false);
            }
        }

        RadioButton radioButton = (RadioButton) view;
        radioButton.setChecked(true);
        selected = view.getId();
    }

    public void charge(View view){


        String productId;
        int dalant=0;

        switch (selected){
            case R.id.two_hundred:
                productId = "two_hundred";
                dalant = 200;
                break;
            case R.id.one_thousand:
                productId = "one_thousand";
                dalant = 1000;
                break;

            case R.id.two_thousand:
                productId = "one_thousand";
                dalant =2000;
                break;
            case R.id.four_thousand:
                productId = "one_thousand";
                dalant = 4000;
                break;
            case R.id.seven_thousand:
                productId = "one_thousand";
                dalant = 7000;
                break;
            case R.id.ten_thousand:
                productId = "one_thousand";
                dalant = 10000;
                break;


        }


        User user = new User();
        user.dalant = dalant;
        Response response = Util.rest("user/charge", "POST", user, Response.class);
        Global.reloadMe();

        int total = prayer.getTotal();
        int ex = total - Global.FREE_FRIENDS_COUNT;

        if(Global.me.dalant < ex*100){

        }else {


            Intent intent = new Intent(ChargeActivity.this, BillingActivity.class);
            intent.putExtra("prayer", prayer);
            startActivityForResult(intent, Global.PAY);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            succeed();
        }
    }
}