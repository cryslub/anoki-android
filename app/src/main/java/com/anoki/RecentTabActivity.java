package com.anoki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.TabActivityBase;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.anoki.common.Global;
import com.anoki.common.Util;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecentTabActivity extends TabActivityBase {

    private RecentAdapter recentAdapter;
    private  List<Prayer> recentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_tab);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        setRecentList();


        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        recentAdapter = new RecentAdapter(recentList);
        // 4. set adapter
        recyclerView.setAdapter(recentAdapter);


    }

    private void setRecentList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();

        Search search = new Search();
        search.apiKey = Global.apiKey;
        search.page = 0;
        search.size = 100;

        recentList = Util.rest("prayer/recent", "POST", search, listType);

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

    @Override
    protected void refresh() {
        System.out.print("refresh");
        setRecentList();
        recentAdapter.updateList(recentList);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout profile;
        ImageView picture;
        TextView name;
        TextView date;
        TextView text;
        ViewPager media;

        TextView pray;
        TextView reply;
        TextView scrap;

        TextView more;

        LinearLayout media_container;

        View itemLayoutView;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            profile = (LinearLayout) itemLayoutView.findViewById(R.id.profile);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            text = (TextView) itemLayoutView.findViewById(R.id.text);
            media = (ViewPager) itemLayoutView.findViewById(R.id.media);

            pray = (TextView) itemLayoutView.findViewById(R.id.pray);
            reply = (TextView) itemLayoutView.findViewById(R.id.reply);
            scrap = (TextView) itemLayoutView.findViewById(R.id.scrap);

            more = (TextView) itemLayoutView.findViewById(R.id.more);

            media_container = (LinearLayout) itemLayoutView.findViewById(R.id.media_container);

            this.itemLayoutView = itemLayoutView;
//            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
 //           imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }


    private class RecentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Prayer> itemsData;

        public RecentAdapter(List<Prayer> itemsData) {
            this.itemsData = itemsData;
        }

        public void updateList(List<Prayer> itemsData){
            this.itemsData = itemsData;
            notifyDataSetChanged();
        }
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recent_row, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Prayer prayer = itemsData.get(position);

            viewHolder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecentTabActivity.this, UserProfileActivity.class);
                    intent.putExtra("userId",prayer.userId);
                    startActivity(intent);
                }
            });

            viewHolder.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(RecentTabActivity.this, PrayerDetailActivity.class);
                    intent.putExtra("prayerId",prayer.id);
                    startActivity(intent);

                }
            });

            viewHolder.pray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            viewHolder.reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


            viewHolder.scrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



            if(!"null".equals(prayer.userPicture) && prayer.userPicture!=null) {
                Bitmap bmp = Util.fetchImage(prayer.userPicture);
                viewHolder.picture.setImageBitmap(bmp);
            }

            viewHolder.name.setText(itemsData.get(position).userName);
            viewHolder.date.setText(itemsData.get(position).time);
            viewHolder.text.setText(itemsData.get(position).back+ "\r\n\r\n"+prayer.text);

            if(prayer.media.size() == 0){
                viewHolder.media_container.setVisibility(View.INVISIBLE);
                viewHolder.media_container.removeView(viewHolder.media);
//                viewHolder.media_container.setLayoutParams(new LinearLayout.LayoutParams(0,0));
//                ((ViewManager)viewHolder.media.getParent()).removeView(viewHolder.media);
            }
 //           viewHolder.imgViewIcon.setImageResource(itemsData[position].getImageUrl());


        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {

            return itemsData.size();
        }

    }

}
