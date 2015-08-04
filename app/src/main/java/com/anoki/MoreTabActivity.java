package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.anoki.common.Global;
import com.anoki.common.TabActivityBase;

public class MoreTabActivity extends TabActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tab);
    }



    @Override
    protected void refresh() {

    }

    public void profile(View view){
        Intent intent = new Intent(MoreTabActivity.this, MyProfileActivity.class);
        startActivityForResult(intent, Global.PROFILE);
    }

    public void box(View view){
        Intent intent = new Intent(MoreTabActivity.this, BoxActivity.class);
        startActivity(intent);
    }
}
