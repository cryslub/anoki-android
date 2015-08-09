package com.anoki;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.vending.billing.IInAppBillingService;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Prayer;



public class BillingActivity extends SubActivityBase {

    private Prayer prayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");
        int total = prayer.friends.size() + prayer.phone.size();
        int ex = total - Global.FREE_FRIENDS_COUNT;

        TextView totalText = (TextView)findViewById(R.id.total);
        totalText.setText(total + "명");

        TextView exText = (TextView)findViewById(R.id.ex);
        exText.setText(ex + "명");


        TextView sum = (TextView)findViewById(R.id.sum);
        sum.setText("총 "+ex+"명 ("+ex+"명 X "+Global.DALANT_PER_PERSON+" 달란트)");

        TextView price = (TextView)findViewById(R.id.price);
        price.setText( (ex*Global.DALANT_PER_PERSON)+ "달란트");

    }


    public void pay(View view){
        int total = prayer.friends.size() + prayer.phone.size();
        int ex = total - Global.FREE_FRIENDS_COUNT;

        prayer.dalant = ex *Global.DALANT_PER_PERSON;

//        Intent intent = Util.inviteIntent(prayer);
//        startActivityForResult(intent, Global.SMS);

    }


}
