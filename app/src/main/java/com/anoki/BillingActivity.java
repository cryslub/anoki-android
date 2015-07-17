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
import com.anoki.common.SubActivityBase;
import com.anoki.pojo.Prayer;



public class BillingActivity extends SubActivityBase {

    IInAppBillingService mService;


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
        setContentView(R.layout.activity_billing);

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

        Intent intent = getIntent();
        Prayer prayer = (Prayer) intent.getSerializableExtra("prayer");
        int total = prayer.friends.size() + prayer.phone.size();
        int ex = total - Global.FREE_FRIENDS_COUNT;

        TextView totalText = (TextView)findViewById(R.id.total);
        totalText.setText(total + "명");

        TextView exText = (TextView)findViewById(R.id.ex);
        exText.setText(ex + "명");


        TextView sum = (TextView)findViewById(R.id.sum);
        sum.setText("총 "+ex+"명 ("+ex+"명 X 100 원)");

        TextView price = (TextView)findViewById(R.id.price);
        price.setText( (ex*100)+ "원");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_billing, menu);
        return true;
    }

    public void pay(View view){
        succeed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }
    }
}
