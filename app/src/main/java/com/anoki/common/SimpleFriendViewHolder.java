package com.anoki.common;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.anoki.R;
import com.anoki.etc.MessageActivity;
import com.anoki.pojo.Friend;

import butterknife.OnClick;

/**
 * Created by Administrator on 2015-10-06.
 */

public class SimpleFriendViewHolder extends FriendViewHolder{

    protected Friend friend;


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
