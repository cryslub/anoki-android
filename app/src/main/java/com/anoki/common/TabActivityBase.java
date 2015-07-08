package com.anoki.common;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anoki.R;

/**
 * Created by Administrator on 2015-07-08.
 */
public class TabActivityBase extends AppCompatActivity{
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent, menu);
        return true;
    }

    public void alarm(MenuItem item){

    }

    public void message(MenuItem item){

    }

    public void write(MenuItem item){

    }

}
