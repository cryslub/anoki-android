package com.anoki1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.anoki1.common.CallBack;
import com.anoki1.common.MediaPagerAdapter;
import com.anoki1.common.PrayerViewHolderBase;
import com.anoki1.common.RestService;
import com.anoki1.common.TabActivityBase;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Search;
import com.anoki1.common.Global;
import com.anoki1.common.Util;
import com.anoki1.prayer.PrayerDetailActivity;
import com.anoki1.prayer.RequestListActivity;
import com.anoki1.prayer.WriteActivity;
import com.anoki1.user.UserPrayerListActivity;
import com.google.gson.reflect.TypeToken;
import com.makeramen.dragsortadapter.DragSortAdapter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class RecentTabActivity extends TabActivityBase {

    private RecentAdapter recentAdapter;
    private  List<Prayer> recentList = new ArrayList<Prayer>();

    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {


                setRecentList();

            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            recentAdapter.updateList(recentList);
        }

    }


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

        backGroundRest("prayer/recent", "POST", search, listType, new CallBack<List<Prayer>>() {

            @Override
            public void success(List<Prayer> result) {
                recentList = result;
                recentAdapter.updateList(recentList);
            }
        });

        //recentList = Util.rest("prayer/recent", "POST", search, listType);

    }

    public  void load(){
        super.load();
        refresh();
    }



    protected void refresh() {

        setRecentList();
 //       recentAdapter.updateList(recentList);
    }


    protected void showMyPopupMenu(final Prayer prayer,View popup){
        PopupMenu popupMenu = new PopupMenu(this, popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_my_popup, popupMenu.getMenu());

        if(prayer.friends == null || prayer.friends.size()==0 ){
            popupMenu.getMenu().findItem(R.id.request).setVisible(false);
        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit: {
                        Intent intent = new Intent(RecentTabActivity.this, WriteActivity.class);
                        intent.putExtra("prayer", prayer);
                        startActivity(intent);
                    }
                    break;
                    case R.id.delete: {
                        Util.rest("prayer", "DELETE", prayer);
//                        succeed();
                         load();
                    }
                    break;
                    case R.id.request: {
                        Intent intent = new Intent(RecentTabActivity.this, RequestListActivity.class);
                        intent.putExtra("prayer", prayer);
                        startActivity(intent);
                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
    }

    class ViewHolder extends PrayerViewHolderBase {



        LinearLayout profile;
        ImageView picture;
        TextView name;
        TextView date;

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
            super(adapter, itemLayoutView, parentActivity);

            profile = (LinearLayout) itemLayoutView.findViewById(R.id.profile);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            date = (TextView) itemLayoutView.findViewById(R.id.date);

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
//                friendFunction.setVisibility(View.INVISIBLE);
                scrap.setVisibility(View.GONE);
            }else{
                scrap.setVisibility(View.VISIBLE);

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
                        if (prayer.userId == Global.me.id) {
                            myProfile(prayer);
                        }else{
                            Intent intent = new Intent(RecentTabActivity.this, UserPrayerListActivity.class);
                            intent.putExtra("userId", prayer.userId);
                            startActivity(intent);
                        }
                    }
                });


                pray.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RestService.pray(prayer, new CallBack<Response>() {
                            @Override
                            public void success(Response result) {
                                refresh();
                            }
                        });

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

            picture.setImageResource(R.drawable.profile_large);
                Util.setPicture(prayer.userPicture, picture, null);

            System.out.println("userName - " + prayer.userName);

            name.setText(prayer.userName);
            date.setText(prayer.time);


            prayCount.setText("기도 " + prayer.prayCount);
            replyCount.setText("댓글 "+prayer.replyCount);




            setMedia(prayer);


        }

        private void setMedia(Prayer prayer){
            if(prayer.media==null|| prayer.media.size() == 0){

                media.setVisibility(View.GONE);
            }else{
                MediaPagerAdapter mCustomPagerAdapter = new MediaPagerAdapter(RecentTabActivity.this,prayer);
                media.setVisibility(View.VISIBLE);
                media.setAdapter(mCustomPagerAdapter);

            }

        }

        @OnClick({R.id.reply_count,R.id.reply})
        void reply(){
            Intent intent = new Intent(RecentTabActivity.this, PrayerDetailActivity.class);
            intent.putExtra("prayerId", prayer.id);
            intent.putExtra("reply", true);
            startActivityForResult(intent, Global.PRAYER);
        }

        @OnClick(R.id.popup)
        void popup() {
            if (prayer.userId == Global.me.id) {
                showMyPopupMenu(prayer,popup);
            }else{
                showPopupMenu(prayer,popup);
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
            if(itemsData == null) return 0;
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
