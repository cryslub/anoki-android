package com.anoki1.etc;

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
import com.anoki1.R;
import com.anoki1.common.Global;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.inappbilling.util.IabHelper;
import com.anoki1.inappbilling.util.IabResult;
import com.anoki1.inappbilling.util.Inventory;
import com.anoki1.inappbilling.util.Purchase;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.User;

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

        dalant=1000;

        switch (selected){
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
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArQJhZUZluGu46CKwsRtzUtPPnPYtXN7ACtCETIE1hZU8XwShxvdWruWOEt2X3tJPYAXLlUquolnqi1EvI3A1/ZEwvVIvmviUGO76XOo8dO02xGko6OcInKYUBbD5q53pK6Fcd4Ma9jWGRuhlxnDWA8pb/iafvYKgHuaDonsXAiWT+fNhv2fu61Nb6h8HmkwZyQD3i219YZaROLAWtMrAMwKFobgL8K8RQSdMvn8AVAy/GNv6n47v1R/wVLsakJetTkMi2+V1uyrN+gRjwe2EdRk0ZnmuGu/Vkguna1cdihav7IAalqoLhWx7mEI30cdI/LsQ3hu104SyjU+bhpdZTwIDAQAB";

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
