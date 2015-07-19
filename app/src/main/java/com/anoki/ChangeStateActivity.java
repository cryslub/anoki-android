package com.anoki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anoki.common.SubActivityBase;

public class ChangeStateActivity extends SubActivityBase {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
    }

    public void done(MenuItem item){

    }
}
