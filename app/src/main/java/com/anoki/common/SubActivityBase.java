package com.anoki.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anoki.R;

/**
 * Created by Administrator on 2015-07-10.
 */
public class SubActivityBase extends ActionBarActivity {

    protected MenuItem doneMenu;
    protected DoneState doneState = DoneState.CLEAR;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);

        doneMenu = menu.findItem(R.id.action_done);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void succeed(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        onBackPressed();
    }
}
