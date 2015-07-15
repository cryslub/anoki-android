package com.anoki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseFriendsActivity extends SubActivityBase {

    private Map<Integer,Integer> selectionMap = new HashMap<Integer,Integer>();
    private Map<Integer,Integer> contactSelectionMap = new HashMap<Integer,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friends);


        Type listType = new TypeToken<List<Friend>>() {}.getType();


        Search search = new Search();
        search.apiKey = Global.apiKey;
        search.searchKey = "A";

        List<Friend> friendList = Util.rest("friend/list","POST",search,listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        FriendsAdapter friendsAdapter = new FriendsAdapter(friendList);
        // 4. set adapter
        recyclerView.setAdapter(friendsAdapter);
    }




    public void doneStateCheck(){
        if(selectionMap.size() == 0 && contactSelectionMap.size() == 0 ) {
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }


    public void done(MenuItem item){

        switch (doneState){
            case CLEAR:
                onBackPressed();
                break;
            case DONE:
            {
                onBackPressed();
            }
            break;

        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView name;
        CheckBox choose;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            choose = (CheckBox) itemLayoutView.findViewById(R.id.choose);
        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Friend> itemsData;

        public FriendsAdapter(List<Friend> itemsData) {
            this.itemsData = itemsData;
        }

        public void updateList(List<Friend> itemsData){
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

            final Friend friend = itemsData.get(position);

            if(!"null".equals(friend.picture) && friend.picture!=null) {
                Bitmap bmp = Util.fetchImage(friend.picture);
                viewHolder.picture.setImageBitmap(bmp);
                viewHolder.picture.setAlpha(1.0f);
            }


            viewHolder.name.setText(itemsData.get(position).name);

            viewHolder.choose.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectionMap.get(friend.friend)==null){
                        selectionMap.put(friend.friend,1);
                    }else{
                        selectionMap.remove(friend.friend);
                    }
                    doneStateCheck();
                }

            });

        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {

            return itemsData.size();
        }

    }


}
