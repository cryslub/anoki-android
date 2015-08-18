package com.anoki.common;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Friend;

import butterknife.Bind;

/**
 * Created by Administrator on 2015-08-11.
 */

public class FriendViewHolder extends ViewHolderBase<Friend> {


    public View view;

    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.choose)
    public CheckBox choose;

    @Nullable
    @Bind(R.id.phone) public TextView phone;


    public FriendViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Friend friend) {

        Util.setPicture(friend.picture, picture);
        name.setText(friend.name);
    }
}
