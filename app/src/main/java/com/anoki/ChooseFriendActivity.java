package com.anoki;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.FriendViewHolder;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Reply;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class SimpleFriendViewHolder extends FriendViewHolder{

    Friend friend;


    public SimpleFriendViewHolder(View itemView) {
        super(itemView);
    }


    public void bind(Friend friend){
        super.bind(friend);

        this.friend = friend;
        this.choose.setVisibility(View.GONE);

    }


    @OnClick(R.id.container)
    public void next(){
        Intent intent = new Intent(view.getContext(),MessageActivity.class);
        intent.putExtra("friend",friend);
        ((Activity)view.getContext()).startActivityForResult(intent, Global.MESSAGE);
    }
}

public class ChooseFriendActivity extends SubActivityBase {

    @Bind(R.id.friend_list) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend);


        List<Friend> friendList = RestService.getFriendList();

        GeneralRecyclerViewAdapter<Friend,SimpleFriendViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Friend,SimpleFriendViewHolder> (friendList,R.layout.layout_friend_row,SimpleFriendViewHolder.class);
        LinearLayoutManager layoutManager = setRecyclerView(recyclerView,responseAdapter);


    }





}
