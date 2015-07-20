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

public class UserPrayerListActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prayer_list);

        Intent intent = getIntent();

        User user = new User();

        user.id = intent.getIntExtra("userId", -1);

        user = Util.rest("user/detail", "POST", user, User.class);

        ImageView profileImage = (ImageView) findViewById(R.id.picture);
        Util.setPicture(user.picture+"",profileImage,getResources().getDrawable(R.drawable.ic_person_black_48dp));



        setText(R.id.name, user.name);
        if(user.text != null)
            setText(R.id.text, "상태메시지 " + user.text);
        setText(R.id.phone, "연락처 " +user.phone);

        getSupportActionBar().setTitle(user.name);

        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchId = user.id;

        List<Prayer> prayerList = Util.rest("user/prayer", "POST", search, listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.prayer_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final PrayerAdapter prayerAdapter = new PrayerAdapter(prayerList);
        // 4. set adapter
        recyclerView.setAdapter(prayerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView date;

        TextView prayCount;
        TextView replyCount;

        Button pray;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text = (TextView) itemLayoutView.findViewById(R.id.text);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            prayCount = (TextView) itemLayoutView.findViewById(R.id.pray_count);
            replyCount = (TextView) itemLayoutView.findViewById(R.id.reply_count);
            pray = (Button) itemLayoutView.findViewById(R.id.pray);

        }

        public void bind(final Prayer prayer){
            text.setText(prayer.text);
            date.setText(prayer.time);
            prayCount.setText(prayer.prayCount);
            replyCount.setText(prayer.replyCount);

            pray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(RestService.pray(prayer)) reload();
                }
            });

        }
    }

    private class PrayerAdapter extends RecyclerView.Adapter<ViewHolder> {
        protected List<Prayer> visibleObjects;
        protected List<Prayer> allObjects;

        public PrayerAdapter(List<Prayer> itemsData)
        {

            this.allObjects = itemsData;
            this.visibleObjects = itemsData;
        }

        public void updateList(List<Prayer> itemsData){
            this.allObjects = itemsData;
            flushFilter();
        }

        public void flushFilter(){
            visibleObjects=new ArrayList<>();
            visibleObjects.addAll(allObjects);
            notifyDataSetChanged();
        }


        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_prayer_row, null);

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
            System.out.println(visibleObjects.size());
            return visibleObjects.size();
        }

    }

}
