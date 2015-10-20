package com.anoki.etc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anoki.R;
import com.anoki.common.FriendViewHolder;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SimpleFriendViewHolder;
import com.anoki.common.SubActivityBase;
import com.anoki.pojo.Friend;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


public class ChooseFriendActivity extends SubActivityBase {

    @Bind(R.id.friend_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);


        List<Friend> friendList = RestService.getFriendList();

        GeneralRecyclerViewAdapter<Friend,SimpleFriendViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Friend,SimpleFriendViewHolder> (friendList,R.layout.layout_simple_friend_row,SimpleFriendViewHolder.class);
        LinearLayoutManager layoutManager = setRecyclerView(recyclerView,responseAdapter);


    }





}
