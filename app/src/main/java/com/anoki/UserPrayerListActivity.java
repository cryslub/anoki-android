package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.anoki.pojo.Search;
import com.anoki.pojo.User;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserPrayerListActivity extends SubActivityBase implements PrayerAdapter.OnPrayListener{

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prayer_list);


        Intent intent = getIntent();

        id = intent.getIntExtra("userId", -1);

    }


    @Override
    protected void onStart() {
        super.onStart();

        load();
    }

    private void load(){
        User user = Util.setPrayerListPage(this,id);



        if(user.text != null)
            setText(R.id.text, "상태메시지 " + user.text);
        // setText(R.id.phone, "연락처 " +user.phone);

        getSupportActionBar().setTitle(user.name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }


    @Override
    public void onPray() {
        load();
    }

    @Override
    public boolean showPicture() {
        return false;
    }

    @Override
    public void onScrap() {
        load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK) {
            load();
        }
    }
}
