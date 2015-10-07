package com.anoki;

import android.os.Bundle;
import android.widget.TabHost;

import com.anoki.common.Global;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.fragment.SearchFragment;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FriendTabActivity extends TabActivityBase implements SearchFragment.OnFragmentInteractionListener, PrayerAdapter.OnPrayListener{


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

    @Override
    public void onSearch(String key) {

    }
}
