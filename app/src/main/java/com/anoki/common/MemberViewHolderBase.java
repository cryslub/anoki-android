package com.anoki.common;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Member;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015-10-20.
 */

public class MemberViewHolderBase extends ViewHolderBase<Member> {

    public View view;

    @Nullable
    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;

    protected Member member;

    public MemberViewHolderBase(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Member member) {

        if(picture !=null)
            Util.setPicture(member.picture, picture);
        name.setText(member.name);

        this.member = member;
    }



}