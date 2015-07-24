package com.anoki.common;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.anoki.AlarmListActivity;
import com.anoki.MessageListActivity;
import com.anoki.R;
import com.anoki.RecentActivity;
import com.anoki.WriteActivity;

/**
 * Created by Administrator on 2015-07-08.
 */
public abstract  class TabActivityBase extends AppCompatActivity {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent, menu);
        return true;
    }

    public void alarm(MenuItem item){
        Intent intent = new Intent(this, AlarmListActivity.class);
        startActivity(intent);

    }

    public void message(MenuItem item){
        Intent intent = new Intent(this, MessageListActivity.class);
        startActivity(intent);

    }

    public void write(MenuItem item){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivityForResult(intent,Global.WRITE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //refresh
            refresh();
        }
    }

    abstract  protected void refresh();

}
