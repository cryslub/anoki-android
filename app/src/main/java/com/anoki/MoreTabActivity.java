package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.anoki.common.Global;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.User;

public class MoreTabActivity extends TabActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tab);

        setUserInfo(Global.me);
    }

    protected void setUserInfo(User user){
        ImageView profileImage = (ImageView) findViewById(R.id.picture);
        Util.setPicture(user.picture + "", profileImage, getResources().getDrawable(R.drawable.ic_person_black_48dp));


        setText(R.id.name, user.name);
        setText(R.id.text, user.text);
        setText(R.id.account, user.account);

    }

    @Override
    protected void refresh() {

    }

    public void profile(View view){
        Intent intent = new Intent(MoreTabActivity.this, MyProfileActivity.class);
        startActivityForResult(intent, Global.PROFILE);
    }

    public void box(View view){
        Intent intent = new Intent(MoreTabActivity.this, BoxActivity.class);
        startActivity(intent);
    }
}
