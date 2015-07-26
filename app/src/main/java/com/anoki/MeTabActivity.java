package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.User;

import org.w3c.dom.Text;


public class MeTabActivity extends TabActivityBase implements PrayerAdapter.OnPrayListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_tab);


    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("onStart");

        load();
    }

    public void load(){
        User user = Util.setPrayerListPage(this, Global.me.id);
        if(user.text != null) {
            TextView text = (TextView) findViewById(R.id.text);
            text.setText(user.text);
        }
    }
    public void profile(View view){
        Intent intent = new Intent(MeTabActivity.this, MyProfileActivity.class);
        startActivityForResult(intent,Global.PROFILE);

    }


    public void box(View view){
        Intent intent = new Intent(MeTabActivity.this, BoxActivity.class);
        startActivity(intent);
    }


    public void edit(View view){

    }

    protected void refresh(){
    }

    @Override
    public void onPray() {
        load();
    }

    @Override
    public boolean showPicture() {
        return false;
    }
}
