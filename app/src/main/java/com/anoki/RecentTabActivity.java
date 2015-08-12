package com.anoki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.Common;
import com.anoki.common.MediaPagerAdapter;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.PrayerViewHolderBase;
import com.anoki.common.RestService;
import com.anoki.common.TabActivityBase;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.anoki.common.Global;
import com.anoki.common.Util;
import com.google.gson.reflect.TypeToken;
import com.makeramen.dragsortadapter.DragSortAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class RecentTabActivity extends TabActivityBase {

    private RecentAdapter recentAdapter;
    private  List<Prayer> recentList = new ArrayList<Prayer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_tab);



        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);



        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        recentAdapter = new RecentAdapter(recyclerView,recentList,this);
        // 4. set adapter
        recyclerView.setAdapter(recentAdapter);


    }


    @Override
    protected void onStart() {
        super.onStart();

        refresh();
    }

    private void setRecentList(){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();

        Search search = new Search();
        search.apiKey = Global.apiKey;
        search.page = 0;
        search.size = 100;

        recentList = Util.rest("prayer/recent", "POST", search, listType);

    }





    protected void refresh() {

        setRecentList();
        recentAdapter.updateList(recentList);
    }

    class ViewHolder extends PrayerViewHolderBase {

        LinearLayout profile;
        ImageView picture;
        TextView name;
        TextView date;
        TextView response;

        TextView text;
        ViewPager media;

        @Bind(R.id.reply) ImageView reply;


        LinearLayout mediaContainer;

        View itemLayoutView;

        TextView prayCount;
        TextView replyCount;

        LinearLayout buttonContainer;

        LinearLayout friendFunction;

        ImageButton popup = null;
        ImageButton viewProfile;
        ImageButton viewPrayerList;

        public ViewHolder(DragSortAdapter adapter, View itemLayoutView,Activity parentActivity) {
            super(adapter,itemLayoutView,parentActivity);

            profile = (LinearLayout) itemLayoutView.findViewById(R.id.profile);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            response = (TextView) itemLayoutView.findViewById(R.id.response);

            text = (TextView) itemLayoutView.findViewById(R.id.text);
            media = (ViewPager) itemLayoutView.findViewById(R.id.media);




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
            super.bind(prayer);

            if(prayer.userId == Global.me.id) {
                friendFunction.setVisibility(View.INVISIBLE);
                buttonContainer.removeView(scrap);

            }else {
                friendFunction.setVisibility(View.VISIBLE);


                popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       showPopupMenu(prayer,popup);
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
                        myProfile(prayer);
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

            System.out.println("userName - " + prayer.userName);

            name.setText(prayer.userName);
                date.setText(prayer.time);


                prayCount.setText("기도 " + prayer.prayCount);
                replyCount.setText("댓글 "+prayer.replyCount);


            setMedia(prayer);

            if(prayer.responseCount == 0){
                response.setVisibility(View.GONE);
            }else{
                response.setVisibility(View.VISIBLE);
            }

        }

        private void setMedia(Prayer prayer){
            if(prayer.media==null|| prayer.media.size() == 0){

                media.setVisibility(View.GONE);
            }else{
                MediaPagerAdapter mCustomPagerAdapter = new MediaPagerAdapter(RecentTabActivity.this,prayer.media);
                media.setVisibility(View.VISIBLE);
                media.setAdapter(mCustomPagerAdapter);

            }

        }
    }


    private class RecentAdapter extends DragSortAdapter<RecentTabActivity.ViewHolder> {

        private List<Prayer> itemsData;
        private Activity parentActivity;

        public RecentAdapter(RecyclerView recyclerView,List<Prayer> itemsData,Activity parentActivity) {
            super(recyclerView);
            this.itemsData = itemsData;
            this.parentActivity = parentActivity;
        }

        public void updateList(List<Prayer> itemsData){
            this.itemsData = itemsData;
            notifyDataSetChanged();
        }
        // Create new views (invoked by the layout manager)
        @Override
        public RecentTabActivity.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recent_row, null);

            // create ViewHolder

            RecentTabActivity.ViewHolder viewHolder = new RecentTabActivity.ViewHolder(this,itemLayoutView,parentActivity);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(RecentTabActivity.ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Prayer prayer = itemsData.get(position);

            System.out.println("position - " + position);

            viewHolder.bind(prayer);


        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {

            return itemsData.size();
        }

        @Override
        public int getPositionForId(long l) {

            return 0;
        }

        @Override
        public boolean move(int i, int i1) {

            return false;
        }



        @Override
        public long getItemId(int position) {

            return itemsData.get(position).id;
        }
    }


}
