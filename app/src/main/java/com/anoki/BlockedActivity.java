package com.anoki;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anoki.common.Global;
import com.anoki.common.FriendViewHolder;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SimpleFriendViewHolder;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.etc.MessageActivity;
import com.anoki.fragment.SearchFragment;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class BlockedViewHolder extends FriendViewHolder {



    protected Friend friend;

    @OnClick(R.id.release)
    void release(){

        friend.apiKey = Global.apiKey;
        friend.state ="A";
        Util.rest("friend", "PUT", friend);

        RecyclerView recyclerView = (RecyclerView) view.getParent();
        GeneralRecyclerViewAdapter<Friend,BlockedViewHolder> recyclerviewAdapter
                = (GeneralRecyclerViewAdapter<Friend,BlockedViewHolder>) recyclerView.getAdapter();
        recyclerviewAdapter.updateList(RestService.getFriendList("B"));
    }


    @Override
    public void bind(Friend friend) {
        super.bind(friend);

        this.friend = friend;

    }

    public BlockedViewHolder(View itemView) {
        super(itemView);
    }


}


public class BlockedActivity extends SubActivityBase implements SearchFragment.OnFragmentInteractionListener{

    @Bind(R.id.friend_list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked);


        List<Friend> friendList = RestService.getFriendList("B");


        GeneralRecyclerViewAdapter<Friend,BlockedViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Friend,BlockedViewHolder> (friendList,R.layout.layout_blocked_row,BlockedViewHolder.class);
        LinearLayoutManager layoutManager = setRecyclerView(recyclerView,responseAdapter);
    }

    @Override
    public void onSearch(String key) {

    }
}
