package com.anoki.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.anoki.R;
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
