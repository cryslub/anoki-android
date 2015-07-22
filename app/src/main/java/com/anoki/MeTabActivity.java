package com.anoki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anoki.common.TabActivityBase;


public class MeTabActivity extends TabActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_tab);
    }


    @Override
    protected void refresh() {

    }
}
