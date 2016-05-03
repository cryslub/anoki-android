package com.anoki1.user;

import android.os.Bundle;
import android.view.View;

import com.anoki1.R;
import com.anoki1.common.SubActivityBase;

public class ForgotAccountActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_account);
    }



    public void done(View view){
        succeed();
    }
}
