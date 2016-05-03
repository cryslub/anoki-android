package com.anoki1.user;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.anoki1.R;
import com.anoki1.common.Global;
import com.anoki1.common.PictureCallBack;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.pojo.User;


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
                    String pictureId = Util.upload(data.getData(), getContentResolver(), new PictureCallBack() {
                        @Override
                        public void success(String id) {
                            ImageView button = (ImageView) findViewById(R.id.profile_image);
                            Bitmap bmp = Util.fetchImage(id);

                            button.setImageBitmap(bmp);
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
