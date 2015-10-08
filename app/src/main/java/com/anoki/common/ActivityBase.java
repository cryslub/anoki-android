package com.anoki.common;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.anoki.etc.MessageActivity;
import com.anoki.etc.MessageListActivity;
import com.anoki.user.MyProfileActivity;
import com.anoki.R;
import com.anoki.prayer.RequestListActivity;
import com.anoki.user.UserPrayerListActivity;
import com.anoki.user.UserProfileActivity;
import com.anoki.prayer.WriteActivity;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.anoki.pojo.User;
import com.google.gson.reflect.TypeToken;
import com.nullwire.trace.ExceptionHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

/**
 * Created by joon on 2015-08-09.
 */
public class ActivityBase extends ActionBarActivity {

    protected PrayerAdapter prayerAdapter;

    @Nullable @Bind(R.id.search_key)
    protected EditText searchKey;


    @Nullable @OnTextChanged(R.id.search_key)
    void search(){
        setFilter();
    }

    protected void setFilter(){
        if(prayerAdapter != null && searchKey != null)
            prayerAdapter.setFilter(searchKey.getText().toString());
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExceptionHandler.register(this, "http://anoki.co.kr/anoki/error.jsp");
    }


    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);
    }



    @Override
    protected void onResume() {
        super.onResume();

        load();
    }

    protected void myProfile(Prayer prayer){
        if (prayer.userId == Global.me.id) {
            Intent intent = new Intent(this, MyProfileActivity.class);
            startActivityForResult(intent,Global.PROFILE);
        }
    }


    protected void showPopupMenu(final Prayer prayer,View popup){
        PopupMenu popupMenu = new PopupMenu(this, popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_popup, popupMenu.getMenu());


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.message: {
                        Intent intent = new Intent(ActivityBase.this, MessageActivity.class);
                        Friend friend = new Friend();
                        friend.name = prayer.userName;
                        friend.picture = prayer.userPicture;
                        friend.friend = prayer.userId;
                        intent.putExtra("friend", friend);
                        startActivityForResult(intent, Global.MESSAGE);
                    }
                    break;
                    case R.id.profile: {
                        Intent intent = new Intent(ActivityBase.this, UserProfileActivity.class);
                        intent.putExtra("userId", prayer.userId);
                        startActivity(intent);
                    }
                    break;
                    case R.id.prayer: {
                        Intent intent = new Intent(ActivityBase.this, UserPrayerListActivity.class);
                        intent.putExtra("userId", prayer.userId);
                        startActivity(intent);
                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
    }


    protected void showMyPopupMenu(final Prayer prayer,View popup){
        PopupMenu popupMenu = new PopupMenu(this, popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_my_popup, popupMenu.getMenu());

        if(prayer.friends == null || prayer.friends.size()==0 ){
            popupMenu.getMenu().findItem(R.id.request).setVisible(false);
        }


        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit: {
                        Intent intent = new Intent(ActivityBase.this, WriteActivity.class);
                        intent.putExtra("prayer", prayer);
                        startActivity(intent);
                    }
                    break;
                    case R.id.delete: {
                        Util.rest("prayer", "DELETE", prayer);
                        load();
                    }
                    break;
                    case R.id.request: {
                        Intent intent = new Intent(ActivityBase.this, RequestListActivity.class);
                        intent.putExtra("prayer", prayer);
                        startActivity(intent);
                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
    }

    protected User setPrayerListPage(int id) {
        return setPrayerListPage(id,false);
    }

    protected User setPrayerListPage(int id,boolean editable){

        User user = new User();

        user.id = id;

        user = Util.rest("user/detail", "POST", user, User.class);

        ImageView profileImage = (ImageView) findViewById(R.id.picture);
        Util.setPicture(user.picture + "", profileImage);



        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user.name);



        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchId = user.id;

        List<Prayer> prayerList = Util.rest("user/prayer", "POST", search, listType);

        setPrayerView(R.id.prayer_list, prayerList, editable);

        return user;
    }

    protected  PrayerAdapter setPrayerView(int id,List<Prayer> prayerList) {
        return  setPrayerView(id, prayerList, false);
    }

    protected  PrayerAdapter setPrayerView(int id,List<Prayer> prayerList,boolean editable){

        RecyclerView recyclerView = (RecyclerView) findViewById(id);


        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        prayerAdapter = new PrayerAdapter(recyclerView,prayerList,this);
        // 4. set adapter
        recyclerView.setAdapter(prayerAdapter);


        return prayerAdapter;

    }


    protected void succeed(){
        setResult(RESULT_OK, null);
        finish();
    }

    protected void succeed(Intent intent){
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case Global.MESSAGE:
                    message(null);
                    break;
                case Global.PROFILE_PHOTO:
                    setProfilePicture(data);
                    break;
                default:
                    succeed();
            }

        }
    }


    public String setProfilePicture( Intent data){
        return Util.upload(data.getData(), getContentResolver(), new CallBack() {
            @Override
            public void success(String id) {
                CircleImageView button = (CircleImageView) findViewById(R.id.profile_image);
                Bitmap bmp = Util.fetchImage(id);
                button.setImageBitmap(bmp);
                button.setAlpha(1.0f);
            }
        });


    }

    public void message(MenuItem item){
        Intent intent = new Intent(this, MessageListActivity.class);
        startActivity(intent);

    }

    protected  void load(){

    }


    protected void setTab(TabHost mTabHost,String[] titles,int[] ids){


        mTabHost.setup(); // Adding tabs // tab1 settings

        int i=0;
        for(String title : titles){
            mTabHost.addTab(mTabHost.newTabSpec("tab_creation").setIndicator(title).setContent(ids[i++]));
        }

        Util.styleTab(getApplicationContext(), mTabHost);


    }


    protected LinearLayoutManager  setRecyclerView  (RecyclerView recyclerView,RecyclerView.Adapter adapter){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return layoutManager;

    }


    public void changeImage(View view){
        // Log.v("setName","changeImage");

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, Global.PROFILE_PHOTO);

    }

    protected void toast(String text){
        Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    protected <T> T rest(String path, String method, Object in, Class<T> classOfT) {
        return Util.rest(path,method,in,classOfT);
    }

    protected <T> T rest(String path, String method, Object in, Type classOfT) {
        return Util.rest(path,method,in,classOfT);
    }

    protected String rest(String path, String method, Object in) {
        return Util.rest(path,method,in);
    }

    public  void setPicture(String picture, ImageView view) {
        Util.setPicture(picture, view, null);
    }
}
