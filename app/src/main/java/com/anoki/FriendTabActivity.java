package com.anoki;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.RestService;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnTextChanged;


public class FriendTabActivity extends TabActivityBase implements PrayerAdapter.OnPrayListener{


    protected  PrayerAdapter scrapedAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_tab);


        TabHost myTabHost=(TabHost) findViewById(R.id.tabHost); // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup(); // Adding tabs // tab1 settings
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("친구기도").setContent(R.id.scraped));
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("기도요청").setContent(R.id.request));

        Util.styleTab(getApplicationContext(),myTabHost);


    }


    protected void setFilter(){
        scrapedAdapter.setFilter(searchKey.getText().toString());
    }



    protected void load(){
        setScrapedList();
        setRequestList();

    }

    private void setScrapedList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.apiKey = Global.apiKey;

        List<Prayer> prayerList = Util.rest("prayer/scraped", "POST", search, listType);

        scrapedAdapter =  setPrayerView(R.id.scraped_list, prayerList);



    }



    private void setRequestList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.apiKey = Global.apiKey;

        List<Prayer> prayerList = Util.rest("prayer/request", "POST", search, listType);

        setPrayerView(R.id.request_list, prayerList);


    }

    protected void refresh() {

    }


    @Override
    public void onPray() {
        load();
    }

    @Override
    public boolean showPicture() {
        return true;
    }

    @Override
    public void onScrap() {
        load();
    }
}
