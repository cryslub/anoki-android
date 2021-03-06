package com.anoki1.common;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.pojo.Friend;

import butterknife.Bind;

/**
 * Created by Administrator on 2015-08-11.
 */

public class FriendViewHolder extends ViewHolderBase<Friend> {


    public View view;

    @Nullable @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Nullable @Bind(R.id.choose)
    public CheckBox choose;
    @Nullable @Bind(R.id.block)
    public ImageView block;


    @Nullable
    @Bind(R.id.phone) public TextView phone;


    public FriendViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Friend friend) {

        if(picture !=null)
           Util.setPicture(friend.picture, picture);
        name.setText(friend.name);
    }

}
