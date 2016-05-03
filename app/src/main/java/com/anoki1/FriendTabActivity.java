package com.anoki1;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki1.common.CallBack;
import com.anoki1.common.Common;
import com.anoki1.common.DBManager;
import com.anoki1.common.Global;
import com.anoki1.common.PrayerAdapter;
import com.anoki1.common.TabActivityBase;
import com.anoki1.common.Task;
import com.anoki1.common.Util;
import com.anoki1.fragment.SearchFragment;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Search;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.viewbadger.BadgeView;

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

        Util.styleTab(getApplicationContext(), myTabHost);

        friendBadge();
    }

    private void friendBadge(){


        Common.getRequestList(getApplicationContext(), new CallBack() {
            @Override
            public void success(Object result) {
                final DBManager dbManager = new DBManager(getApplicationContext());

                int count = dbManager.getNewRequestCount();

                if(count > 0) {

                    TabHost mTabHost=(TabHost) findViewById(R.id.tabHost);

                    TextView t = (TextView) mTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title); //Unselected Tabs
                    t.setPadding(0, 0, 70, 0);
                    BadgeView badge1 = new BadgeView(FriendTabActivity.this, t);
                    badge1.setText(count + "");
                    badge1.setTextSize(11);
                    badge1.setBadgeBackgroundColor(getApplicationContext().getResources().getColor(R.color.cinnabar));
                    badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    badge1.setTextColor(Color.WHITE);

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) badge1.getLayoutParams();
                    p.setMargins(30, 0, 0, 30);
                    badge1.setLayoutParams(p);

                    badge1.toggle();
                }
            }
        });


    }

    protected void setFilter(){
        scrapedAdapter.setFilter(searchKey.getText().toString());
    }



    public void load(){
        setScrapedList();
        setRequestList();

    }

    private void setScrapedList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.apiKey = Global.apiKey;

        Util.backGroundRest("prayer/scraped", "POST", search, listType, new CallBack<List<Prayer>>() {
            @Override
            public void success(List<Prayer> prayerList) {
                scrapedAdapter =  setPrayerView(R.id.scraped_list, prayerList);

            }
        });




    }



    private void setRequestList(){

        Common.getRequestList(getApplicationContext(), new CallBack() {
            @Override
            public void success(Object result) {
                final List<Prayer> prayerList = new ArrayList<Prayer>();


                final DBManager dbManager = new DBManager(getApplicationContext());
                List<Prayer> list = dbManager.getRequest();

                Search search = new Search();
                for (final Prayer p : list) {
                    search.searchId = p.id;

                    backGroundRest("prayer/detail", "POST", search, Prayer.class, new CallBack<Prayer>() {
                        @Override
                        public void success(Prayer prayer) {
                            if(prayer != null) {
                                prayer.checked = p.checked;
                                prayerList.add(prayer);

                                setPrayerView(R.id.request_list, prayerList);

                            }

                        }
                    });

                }



                dbManager.checkRequest();
            }
        });



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
