package com.anoki.etc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.android.vending.billing.IInAppBillingService;
import com.anoki.R;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.inappbilling.util.IabHelper;
import com.anoki.inappbilling.util.IabResult;
import com.anoki.inappbilling.util.Inventory;
import com.anoki.inappbilling.util.Purchase;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;

import butterknife.OnClick;


public class ChargeActivity extends SubActivityBase {

    IInAppBillingService mService;
    private Prayer prayer;

    private String caller;

    private static final String TAG =
            "com.anoki.inappbilling";

    IabHelper mHelper;
    String productId;
    int dalant;

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            else if (purchase.getSku().equals(productId)) {
                consumeItem();
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(productId),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        User user = new User();
                        user.dalant = dalant;
                        Response response = Util.rest("user/charge", "POST", user, Response.class);
                        Global.reloadMe();

                        if("DalantActivity".equals(caller)){
                            succeed();
                        }else {

                            int total = prayer.getTotal();
                            int ex = total - Global.FREE_FRIENDS_COUNT;

                            if (Global.me.dalant < ex * 10) {

                            } else {


                                Intent intent = new Intent(ChargeActivity.this, BillingActivity.class);
                                intent.putExtra("prayer", prayer);
                                startActivityForResult(intent, Global.PAY);
                            }
                        }
                    } else {
                        // handle error
                    }
                }
            };


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
        caller = intent.getStringExtra("caller");

        setupBilling();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_charge, menu);
        return true;
    }


    @OnClick(R.id.charge)
    public void charge(View view){

        dalant=200;

        switch (selected){
            case R.id.two_hundred:
                dalant = 200;
                break;
            case R.id.one_thousand:
                dalant = 1000;
                break;

            case R.id.two_thousand:
                dalant =2000;
                break;
            case R.id.four_thousand:
                dalant = 4000;
                break;
            case R.id.seven_thousand:
                dalant = 7000;
                break;
            case R.id.ten_thousand:
                dalant = 10000;
                break;


        }

        productId = Util.getProductId(dalant);

        mHelper.launchPurchaseFlow(this, productId, 10001,
                mPurchaseFinishedListener, "mypurchasetoken");


    }

    private void setupBilling(){
        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAneFzOD1XpPSDJV1Mu7DARiZDcWi4jk9l97EqPA/NES8rER6wFvqZ/5h3qOfOcHtBAiLFSvnh5OSfq63Xq5POw1vpvjchTAPIVmsF6URfEr7Fo+Ypx1Uhx5ZEfrzwXD65Ryc12cCa1I39Hj26W1XWWbvynBsUv7RMRd6zTo/JT+rLa3NE35bShGW9/0Kqsa6wTj3wpZhPpogv22uPUSwYTH1tN0I5eBP5aTIQ0crVyJuVBQJHUKz20fFIGo3WJJqLn9GAPVM25t5EOvAmdxsL2df5Pk0Tpi485yGCD8igwWgdatdfbG8QAYgm4pJaN7vTjS8ci2Zio3YJNBA69VbqWQIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                           public void onIabSetupFinished(IabResult result)
                           {
                               if (!result.isSuccess()) {
                                   Log.d(TAG, "In-app Billing setup failed: " +
                                           result);
                               } else {
                                   Log.d(TAG, "In-app Billing is set up OK");
                               }
                           }
                       });


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            unbindService(mServiceConn);
        }

        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
