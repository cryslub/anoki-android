package com.anoki;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.anoki.common.MediaPagerAdapter;
import com.anoki.common.RestService;
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
    protected void refresh() {

        setRecentList();
        recentAdapter.updateList(recentList);
    }

    private class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout profile;
        ImageView picture;
        TextView name;
        TextView date;
        TextView response;

        TextView text;
        ViewPager media;

        TextView pray;
        TextView reply;
        TextView scrap;

        TextView more;

        LinearLayout mediaContainer;

        View itemLayoutView;

        TextView prayCount;
        TextView replyCount;

        LinearLayout buttonContainer;

        LinearLayout friendFunction;

        ImageButton popup = null;
        ImageButton viewProfile;
        ImageButton viewPrayerList;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            profile = (LinearLayout) itemLayoutView.findViewById(R.id.profile);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            response = (TextView) itemLayoutView.findViewById(R.id.response);

            text = (TextView) itemLayoutView.findViewById(R.id.text);
            media = (ViewPager) itemLayoutView.findViewById(R.id.media);

            pray = (TextView) itemLayoutView.findViewById(R.id.pray);
            reply = (TextView) itemLayoutView.findViewById(R.id.reply);
            scrap = (TextView) itemLayoutView.findViewById(R.id.scrap);

            more = (TextView) itemLayoutView.findViewById(R.id.more);


            prayCount = (TextView) itemLayoutView.findViewById(R.id.pray_count);
            replyCount = (TextView) itemLayoutView.findViewById(R.id.reply_count);

            buttonContainer = (LinearLayout) itemLayoutView.findViewById(R.id.button_container);
            friendFunction = (LinearLayout) itemLayoutView.findViewById(R.id.friend_function);


            popup = (ImageButton) itemLayoutView.findViewById(R.id.popup);

            this.itemLayoutView = itemLayoutView;
//            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item_title);
 //           imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }

        public void bind(final Prayer prayer){
            if(prayer.userId == Global.me.id) {
                friendFunction.setVisibility(View.INVISIBLE);
                buttonContainer.removeView(scrap);

            }else {
                friendFunction.setVisibility(View.VISIBLE);


                popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("popup");
                        PopupMenu popupMenu = new PopupMenu(RecentTabActivity.this, popup);
                        //Inflating the Popup using xml file
                        popupMenu.getMenuInflater()
                                .inflate(R.menu.menu_popup, popupMenu.getMenu());


                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.message:
                                        break;
                                    case R.id.profile: {
                                        Intent intent = new Intent(RecentTabActivity.this, UserProfileActivity.class);
                                        intent.putExtra("userId", prayer.userId);
                                        startActivity(intent);
                                    }
                                    break;
                                    case R.id.prayer: {
                                        Intent intent = new Intent(RecentTabActivity.this, UserPrayerListActivity.class);
                                        intent.putExtra("userId", prayer.userId);
                                        startActivity(intent);
                                    }
                                    break;

                                }
                                return true;
                            }
                        });

                        popupMenu.show();
                    }
                });

            }
                if("".equals(prayer.back)){
                    text.setText(prayer.text);
                }else  if("".equals(prayer.text)){
                    text.setText(prayer.back);
                } else {
                    text.setText(prayer.back + "\r\n\r\n" + prayer.text);
                }


                profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println(prayer.userId + "," + Global.me.id);
                        if (prayer.userId == Global.me.id) {
                            Intent intent = new Intent(RecentTabActivity.this, MyProfileActivity.class);
                            startActivityForResult(intent,Global.PROFILE);
                        }
                    }
                });

                more.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RecentTabActivity.this, PrayerDetailActivity.class);
                        intent.putExtra("prayerId", prayer.id);
                        startActivityForResult(intent, Global.PRAYER);
                    }
                });

                pray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(RestService.pray(prayer)) refresh();

                    }
                });


                reply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RecentTabActivity.this, PrayerDetailActivity.class);
                        intent.putExtra("prayerId", prayer.id);
                        intent.putExtra("reply", true);
                        startActivityForResult(intent, Global.PRAYER);
                    }
                });



                if(prayer.scrapd != null){
                    buttonContainer.removeView(scrap);
                }else{
                    scrap.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            prayer.apiKey = Global.apiKey;
                            Util.rest("prayer/scrap", "POST",prayer, Prayer.class);
                            refresh();
                        }
                    });
                }

                Util.setPicture(prayer.userPicture, picture, getDrawable(R.drawable.ic_person_black_48dp));

                name.setText(prayer.userName);
                date.setText(prayer.time);


                prayCount.setText("기도 " + prayer.prayCount);
                replyCount.setText("댓글 "+prayer.replyCount);


            setMedia(prayer);

            if(prayer.responseCount == 0){
                response.setVisibility(View.GONE);
            }

        }

        private void setMedia(Prayer prayer){
            if(prayer.media==null|| prayer.media.size() == 0){
                media.setVisibility(View.GONE);
            }else{
                MediaPagerAdapter mCustomPagerAdapter = new MediaPagerAdapter(getApplicationContext(),prayer.media);
                media.setAdapter(mCustomPagerAdapter);
            }

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

            viewHolder.bind(prayer);


        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {

            return itemsData.size();
        }

    }


}
