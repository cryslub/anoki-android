package com.anoki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.CallBack;
import com.anoki.common.CircleImageView;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.User;


public class MyProfileActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        setProfile(Global.me);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_profile, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                succeed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Global.PROFILE_PHOTO:
                    String pictureId =  setProfilePicture(data);
                    Global.me.picture = Integer.parseInt(pictureId);
                    Util.rest("user", "PUT", Global.me, User.class);
                    break;
                case Global.NAME:
                    reload();
                    break;

                case Global.STATE:
                    reload();
                    break;
            }
        }
    }


    public void changeState(View view){
        Intent intent = new Intent(MyProfileActivity.this, ChangeStateActivity.class);
        startActivityForResult(intent, Global.STATE);

    }

    public void viewAccount(View view){
        Intent intent = new Intent(MyProfileActivity.this, AccountActivity.class);
        startActivity(intent);

    }
}
