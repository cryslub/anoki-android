package com.anoki.common;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import com.anoki.MyProfileActivity;
import com.anoki.R;
import com.anoki.UserPrayerListActivity;
import com.anoki.UserProfileActivity;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.anoki.pojo.User;
import com.google.gson.reflect.TypeToken;

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
        prayerAdapter.setFilter(searchKey.getText().toString());
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart(){
        super.onStart();

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
                    case R.id.message:
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

    protected User setPrayerListPage(int id) {
        return setPrayerListPage(id,false);
    }

    protected User setPrayerListPage(int id,boolean editable){

        User user = new User();

        user.id = id;

        user = Util.rest("user/detail", "POST", user, User.class);

        ImageView profileImage = (ImageView) findViewById(R.id.picture);
        Util.setPicture(user.picture + "", profileImage, getResources().getDrawable(R.drawable.ic_person_white_48dp));



        TextView name = (TextView) findViewById(R.id.name);
        name.setText(user.name);



        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();


        final Search search = new Search();
        search.searchId = user.id;

        List<Prayer> prayerList = Util.rest("user/prayer", "POST", search, listType);

        setPrayerView(R.id.prayer_list,prayerList,editable);

        return user;
    }

    protected  PrayerAdapter setPrayerView(int id,List<Prayer> prayerList) {
        return  setPrayerView(id,prayerList,false);
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


    protected  void load(){

    }
}
