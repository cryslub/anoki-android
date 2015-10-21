package com.anoki.common;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anoki.etc.AlarmListActivity;
import com.anoki.R;
import com.anoki.prayer.WriteActivity;

/**
 * Created by Administrator on 2015-07-08.
 */
public abstract  class TabActivityBase extends ActivityBase {



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent, menu);

        setMessage(menu);
        setAlarm(menu);

        return super.onCreateOptionsMenu(menu);
    }


    private void setMessage(Menu menu){
        MenuItem item = menu.findItem(R.id.action_message);
        MenuItemCompat.setActionView(item, R.layout.layout_message);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);


        Common.getMessageList(getApplicationContext());
        final DBManager dbManager = new DBManager(getApplicationContext());

        int count = dbManager.getNewMessageCount();
        if(count ==0){
            RelativeLayout layout = (RelativeLayout) notifCount.findViewById(R.id.container);
            layout.setVisibility(View.GONE);
        }else {
            TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText(count + "");
        }
    }

    private void setAlarm(Menu menu){
        MenuItem item = menu.findItem(R.id.action_alarm);
        MenuItemCompat.setActionView(item, R.layout.layout_alarm);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);


        Common.getAlarmList(getApplicationContext());
        final DBManager dbManager = new DBManager(getApplicationContext());

        int count = dbManager.getNewAlarmCount();
        if(count ==0){
            RelativeLayout layout = (RelativeLayout) notifCount.findViewById(R.id.container);
            layout.setVisibility(View.GONE);
        }else {
            TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            tv.setText(count + "");
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        setTitle();
    }

    public void alarm(MenuItem item){
        Intent intent = new Intent(this, AlarmListActivity.class);
        startActivity(intent);

    }



    public void write(MenuItem item){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivityForResult(intent, Global.WRITE);
    }




    protected void setText(int id,String text){
        TextView name = (TextView) findViewById(id);
        name.setText(text);
    }

    protected void setTitle(){
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_custom_view_home);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        load();
    }
}
