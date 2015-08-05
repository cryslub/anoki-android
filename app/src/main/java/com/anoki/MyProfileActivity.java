package com.anoki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.CallBack;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.User;


public class MyProfileActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        User me = Global.me;
        ImageView profileImage = (ImageView) findViewById(R.id.profile_image);
        Util.setPicture(Global.me.picture+"",profileImage,getResources().getDrawable(R.drawable.ic_person_black_48dp));


        setText(R.id.phone, Util.makePhoneNumber(me.country,me.phone));

        setText(R.id.name, me.name);
        setText(R.id.state, me.text);
        setText(R.id.account, me.account);


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


    public void changeImage(View view){
        // Log.v("setName","changeImage");

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Global.PHOTO);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case Global.PHOTO:
                    String pictureId = Util.upload(data.getData(), getContentResolver(), new CallBack() {
                        @Override
                        public void success(String id) {
                            ImageButton button = (ImageButton) findViewById(R.id.profile_image);
                            Bitmap bmp = Util.fetchImage(id);
                            button.setImageBitmap(bmp);
                            button.setAlpha(1.0f);
                        }
                    });

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


    public void changeName(View view){
        Intent intent = new Intent(MyProfileActivity.this, ChangeNameActivity.class);
        startActivityForResult(intent, Global.NAME);
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
