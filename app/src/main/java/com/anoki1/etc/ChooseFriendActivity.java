package com.anoki1.etc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.RestService;
import com.anoki1.common.SimpleFriendViewHolder;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.Friend;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class ChooseFriendActivity extends SubActivityBase {

    @Bind(R.id.friend_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);


       RestService.getFriendList(new CallBack<ArrayList<Friend>>() {
            @Override
            public void success(ArrayList<Friend> friendList) {

                GeneralRecyclerViewAdapter<Friend, SimpleFriendViewHolder> responseAdapter = new GeneralRecyclerViewAdapter<Friend, SimpleFriendViewHolder>(friendList, R.layout.layout_simple_friend_row, SimpleFriendViewHolder.class);
                LinearLayoutManager layoutManager = setRecyclerView(recyclerView, responseAdapter);

            }
        });


    }





}
