package com.anoki;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;


public class RecentActivity extends AppCompatActivity {

    public static class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        /** Constructor used each time a new tab is created.
         * @param activity  The host Activity, used to instantiate the fragment
         * @param tag  The identifier tag for the fragment
         * @param clz  The fragment's Class, used to instantiate the fragment
         */
        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }

    /* The following are each of the ActionBar.TabListener callbacks */

        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            // Check if the fragment is already initialized
            if (mFragment == null) {
                // If not, instantiate and add it to the activity
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                // If it exists, simply attach it in order to show it
                ft.attach(mFragment);
            }
        }

        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mFragment != null) {
                // Detach the fragment, because another one is being attached
                ft.detach(mFragment);
            }
        }

        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            // User selected the already selected tab. Usually do nothing.
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);


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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void alarm(MenuItem item){
        Intent intent = new Intent(this, AlaramListActivity.class);
        startActivity(intent);
    }

    public void message(MenuItem item){
        Intent intent = new Intent(this, MessageListActivity.class);
        startActivity(intent);

    }

    public void write(MenuItem item){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);

    }
}
