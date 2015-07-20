package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.User;

public class UserProfileActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        User user = new User();

        user.id = intent.getIntExtra("userId", -1);

        user = Util.rest("user/detail", "POST",user,User.class);

        ImageView profileImage = (ImageView) findViewById(R.id.profile_image);
        Util.setPicture(user.picture+"",profileImage,getResources().getDrawable(R.drawable.ic_person_black_48dp));


        setText(R.id.phone, user.phone);

        setText(R.id.name, user.name);
        setText(R.id.state, user.text);
        setText(R.id.account, user.account);

        getSupportActionBar().setTitle(user.name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }


}
