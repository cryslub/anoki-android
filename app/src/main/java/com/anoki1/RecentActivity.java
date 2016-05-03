package com.anoki1;

import android.app.TabActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki1.common.CallBack;
import com.anoki1.common.DBManager;
import com.anoki1.common.Util;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Search;
import com.google.gson.reflect.TypeToken;
import com.readystatesoftware.viewbadger.BadgeView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class RecentActivity extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        setTabHost();
        friendBadge();

        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ContactTask performBackgroundTask = new ContactTask(getContentResolver(),getApplicationContext());
                            // PerformBackgroundTask this class is the class that extends AsynchTask
                            performBackgroundTask.execute();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
//        timer.schedule(doAsynchronousTask, 0, 600000); //execute in every 50000 ms



        //    mTabHost.addTab(mTabHost.newTabSpec("나").setIndicator("나").setContent(new Intent(this , MeTabActivity.class )));
//        mTabHost.setCurrentTab(0);


/*
        ActionBar actionBar = getSupportActionBar();

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
//        actionBar.setDisplayShowTitleEnabled(false);

        ActionBar.Tab tab1 = actionBar.newTab()
                .setText("최근")
                .setTabListener(new TabListener<RecentFragment>(
                        this, "최근", RecentFragment.class));
        actionBar.addTab(tab1);

        ActionBar.Tab tab2 = actionBar.newTab()
                .setText("나")
                .setTabListener(new TabListener<MeFragment>(
                        this, "나", MeFragment.class));
        actionBar.addTab(tab2);

        ActionBar.Tab tab3 = actionBar.newTab()
                .setText("친구")
                .setTabListener(new TabListener<FriendsFragment>(
                        this, "친구", FriendsFragment.class));
        actionBar.addTab(tab3);

        ActionBar.Tab tab4 = actionBar.newTab()
                .setText("그룹")
                .setTabListener(new TabListener<GroupFragment>(
                        this, "그룹", GroupFragment.class));
        actionBar.addTab(tab4);

        ActionBar.Tab tab5 = actionBar.newTab()
                .setText("더보기")
                .setTabListener(new TabListener<MoreFragment>(
                        this, "더보기", MoreFragment.class));
        actionBar.addTab(tab5);
*/

    }


    private static class ContactTask extends AsyncTask<String, Void, String> {


        private ImageView view;
        private Bitmap bmp;
        ContentResolver contentResolver;
        Context applicationContext;

        public ContactTask(ContentResolver contentResolver, Context applicationContext) {
            this.contentResolver = contentResolver;
            this.applicationContext = applicationContext;
        }


        @Override
        protected String doInBackground(String... params) {
           // ContactManage.checkContact(contentResolver,applicationContext);
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }

    private void friendBadge(){

        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();
        final DBManager dbManager = new DBManager(this);

        Util.backGroundRest("prayer/request", "POST", new Search(), listType, new CallBack<ArrayList<Prayer>>() {
            @Override
            public void success(ArrayList<Prayer> result) {
                if(result !=null) {
                    for (Prayer prayer : result) {
                        dbManager.insertRequest(prayer);
                    }
                }

                int count = dbManager.getNewRequestCount();

                if(count > 0) {

                    final TabHost mTabHost = getTabHost();

                    TextView t = (TextView) mTabHost.getTabWidget().getChildAt(2).findViewById(android.R.id.title); //Unselected Tabs
                    t.setPadding(0, 0, 70, 0);
                    BadgeView badge1 = new BadgeView(RecentActivity.this, t);
                    badge1.setText(count+"");
                    badge1.setTextSize(11);
                    badge1.setBadgeBackgroundColor(getApplicationContext().getResources().getColor(R.color.cinnabar));
                    // badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    badge1.setTextColor(Color.WHITE);

                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) badge1.getLayoutParams();
                    p.setMargins(30, 0, 0, 30);
                    badge1.setLayoutParams(p);

                    badge1.toggle();
                }
            }
        });

    }

    private void setTabHost(){

        final TabHost mTabHost = getTabHost();

        ImageView iv = new ImageView(this);
        iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        mTabHost.addTab(mTabHost.newTabSpec("최근").setIndicator("최근").setContent(new Intent(this, RecentTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("나").setIndicator("나").setContent(new Intent(this, MeTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("친구").setIndicator("친구").setContent(new Intent(this, FriendTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("그룹").setIndicator("그룹").setContent(new Intent(this, GroupTabActivity.class)));
        mTabHost.addTab(mTabHost.newTabSpec("더보기").setIndicator("더보기").setContent(new Intent(this, MoreTabActivity.class)));




        for(int index = 0 ;index < mTabHost.getTabWidget().getChildCount() ; index++) {



            mTabHost.getTabWidget().getChildAt(index).setBackgroundColor(getResources().getColor(R.color.baltic_sea));
            mTabHost.getTabWidget().getChildAt(index).setPadding(0, 0, 0, 0);
            TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(index).findViewById(android.R.id.title); //Unselected Tabs



            if(tv != null) {
                tv.setTextColor(Color.parseColor("#aaaaaa"));
                tv.setTextSize(18);
            }
        }



        TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextColor(Color.WHITE);


        mTabHost.getTabWidget().setDividerDrawable(null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                for (int index = 0; index < mTabHost.getTabWidget().getChildCount(); index++) {
                    TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(index).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#aaaaaa"));
                }


                TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).findViewById(android.R.id.title); //Unselected Tabs
                tv.setTextColor(Color.WHITE);
            }
        });

    }


}
