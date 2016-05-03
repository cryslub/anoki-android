package com.anoki1.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.PrayerAdapter;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.User;

public class UserPrayerListActivity extends SubActivityBase implements PrayerAdapter.OnPrayListener{

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_prayer_list);


        Intent intent = getIntent();

        id = intent.getIntExtra("userId", -1);

    }



    public void load(){
       setPrayerListPage(id, new CallBack<User>() {
            @Override
            public void success(User user) {
                if(user.text != null)
                    setText(R.id.text, "상태메시지 " + user.text);
                // setText(R.id.phone, "연락처 " +user.phone);

                getSupportActionBar().setTitle(user.name);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }


    @Override
    public void onPray() {
        load();
    }

    @Override
    public boolean showPicture() {
        return false;
    }

    @Override
    public void onScrap() {
        load();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK) {
            load();
        }
    }
}
