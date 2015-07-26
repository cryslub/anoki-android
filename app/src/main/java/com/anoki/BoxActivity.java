package com.anoki;

import android.support.v7.app.AppCompatActivity;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BoxActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box);

        setTab();
        setMeList();
        setFriendList();
    }


    private void setTab(){
        TabHost myTabHost =(TabHost) findViewById(R.id.tabHost); // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup(); // Adding tabs // tab1 settings
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("나").setContent(R.id.me));
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("친구").setContent(R.id.friend));

    }


    private void setMeList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchId = Global.me.id;
        search.searchKey = "Y";

        List<Prayer> prayerList = Util.rest("user/prayer", "POST", search, listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.completed_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final BoxAdapter boxAdapter = new BoxAdapter(prayerList);
        // 4. set adapter
        recyclerView.setAdapter(boxAdapter);

    }

    private void setFriendList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchKey = "Y";

        List<Prayer> prayerList = Util.rest("prayer/scraped", "POST", search, listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.scraped_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final BoxAdapter boxAdapter = new BoxAdapter(prayerList);
        // 4. set adapter
        recyclerView.setAdapter(boxAdapter);

    }


    public void showPeriod(View view){
        LinearLayout period = (LinearLayout) findViewById(R.id.period);
        if(period.isShown()){
            period.setVisibility(View.GONE);
        }else {
            period.setVisibility(View.VISIBLE);
        }
    }


    public void showFriendPeriod(View view){
        LinearLayout period = (LinearLayout) findViewById(R.id.friend_period);
        if(period.isShown()){
            period.setVisibility(View.GONE);
        }else {
            period.setVisibility(View.VISIBLE);
        }

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text = (TextView) itemLayoutView.findViewById(R.id.text);

        }

        public void bind(Prayer prayer) {
            text.setText(prayer.text);
        }
    }

    private class BoxAdapter extends RecyclerView.Adapter<ViewHolder> {
        protected List<Prayer> visibleObjects;
        protected List<Prayer> allObjects;

        public BoxAdapter(List<Prayer> itemsData)
        {

            this.allObjects = itemsData;
            this.visibleObjects = itemsData;
        }

        public void updateList(List<Prayer> itemsData){
            this.allObjects = itemsData;
        }

        public void flushFilter(){
            visibleObjects=new ArrayList<>();
            visibleObjects.addAll(allObjects);
            notifyDataSetChanged();
        }

        public void setFilter(String queryText) {


        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_box_row, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Prayer prayer = visibleObjects.get(position);

            viewHolder.bind(prayer);

            

        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return visibleObjects.size();
        }

    }


}
