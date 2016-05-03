package com.anoki1.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.anoki1.R;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.pojo.User;

public class UserProfileActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();

        User user = new User();

        user.id = intent.getIntExtra("userId", -1);

        user = Util.rest("user/detail", "POST",user,User.class);

        if(user !=null) {
            setProfile(user);


            getSupportActionBar().setTitle(user.name);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }


}
