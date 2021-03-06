package com.anoki1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.anoki1.common.Global;
import com.anoki1.common.TabActivityBase;
import com.anoki1.common.Util;
import com.anoki1.etc.NoticeActivity;
import com.anoki1.etc.SettingsActivity;
import com.anoki1.pojo.User;
import com.anoki1.etc.ChooseContactsActivity;
import com.anoki1.user.BoxActivity;
import com.anoki1.user.DalantActivity;
import com.anoki1.user.MyProfileActivity;

public class MoreTabActivity extends TabActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_tab);

        setUserInfo(Global.me);
    }


    @Override
    protected void onStart() {
        super.onStart();

        setUserInfo(Global.me);
    }

    protected void setUserInfo(User user){

        if(user == null) return;

        ImageView profileImage = (ImageView) findViewById(R.id.picture);
        Util.setPicture(user.picture + "", profileImage);


        setText(R.id.name, user.name);
        setText(R.id.text, user.text);
        setText(R.id.account, user.account);

    }



    public void profile(View view){
        Intent intent = new Intent(MoreTabActivity.this, MyProfileActivity.class);
        startActivityForResult(intent, Global.PROFILE);
    }

    public void box(View view){
        Intent intent = new Intent(MoreTabActivity.this, BoxActivity.class);
        startActivity(intent);
    }

    public void notice(View view){
        Intent intent = new Intent(MoreTabActivity.this, NoticeActivity.class);
        startActivity(intent);
    }

    public void dalant(View view){
        Intent intent = new Intent(MoreTabActivity.this, DalantActivity.class);
        startActivity(intent);
    }

    public void friend(View view){
        Intent intent = new Intent(MoreTabActivity.this, ChooseContactsActivity.class);
        intent.putExtra("type","info");
        startActivity(intent);

    }

    public void suggest(View view){

    }

    public void settings(View view){
        Intent intent = new Intent(MoreTabActivity.this, SettingsActivity.class);
           startActivity(intent);
    }
}
