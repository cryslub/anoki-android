package com.anoki;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;


public class ForgotActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
    }


    public void account(View view){
        Intent intent = new Intent(ForgotActivity.this, InputPhoneNumberActivity.class);
        startActivityForResult(intent, Global.AUTH);
    }

    public void pass(View view){
        Intent intent = new Intent(ForgotActivity.this, ForgotPassActivity.class);
        startActivityForResult(intent, Global.PASS);
    }
}
