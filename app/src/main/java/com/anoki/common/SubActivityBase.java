package com.anoki.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Reply;
import com.anoki.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2015-07-10.
 */
public class SubActivityBase extends ActivityBase {

    protected MenuItem doneMenu;
    protected DoneState doneState = DoneState.CLEAR;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_none, menu);
        return true;
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    protected void succeed(){
        setResult(RESULT_OK, null);
        finish();
    }

    protected void setText(int id,String text){
        TextView name = (TextView) findViewById(id);
        name.setText(text);
    }

    protected void reload() {
        finish();
        startActivity(getIntent());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            succeed();
        }
    }

    protected void setProfile(User user){
        ImageView profileImage = (ImageView) findViewById(R.id.profile_image);
        Util.setPicture(user.picture+"",profileImage,getResources().getDrawable(R.drawable.profile_large));


        setText(R.id.phone, Util.makePhoneNumber(user.country,user.phone));

        setText(R.id.name, user.name);
        setText(R.id.state, user.text);
        setText(R.id.account, user.account);


    }


    protected LinearLayoutManager  setRecyclerView  (RecyclerView recyclerView,RecyclerView.Adapter adapter){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return layoutManager;

    }
}
