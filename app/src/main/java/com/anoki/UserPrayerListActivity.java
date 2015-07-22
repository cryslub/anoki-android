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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prayer_list);


        Intent intent = getIntent();

        int id = intent.getIntExtra("userId", -1);

        User user = Util.setPrayerListPage(this,id);



        if(user.text != null)
            setText(R.id.text, "상태메시지 " + user.text);
        setText(R.id.phone, "연락처 " +user.phone);

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
        reload();
    }
}
