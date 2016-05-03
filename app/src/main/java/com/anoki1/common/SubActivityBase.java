package com.anoki1.common;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.fragment.SearchFragment;
import com.anoki1.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2015-07-10.
 */
public class SubActivityBase extends ActivityBase implements SearchFragment.OnFragmentInteractionListener{


    protected int selected;

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

    }

    protected void setProfile(User user){
        ImageView profileImage = (ImageView) findViewById(R.id.profile_image);
        Util.setPicture(user.picture+"",profileImage);


        setText(R.id.phone, Util.makePhoneNumber(user.country,user.phone));

        setText(R.id.name, user.name);
        setText(R.id.state, user.text);
        setText(R.id.account, user.account);


    }


    public void onRadioButtonClicked(View view){

        selected = view.getId();


        setRadioButton(selected);


    }

    protected void setRadioButton(int selected){
        LinearLayout container = (LinearLayout) findViewById(R.id.container);

        List<View> viewList = Util.getAllChildren(container);
        for(View v : viewList){
            if(v instanceof RadioButton){
                RadioButton radioButton = (RadioButton) v;
                radioButton.setChecked(false);
            }

            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setTypeface(Typeface.DEFAULT);
            }
        }

        RadioButton view = (RadioButton) findViewById(selected);
        view.setChecked(true);

        ViewGroup row = (ViewGroup) view.getParent();
        List<View> list  = Util.getAllChildren(row);
        for(View v : list) {
            if (v instanceof TextView) {
                TextView textView = (TextView) v;
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }
        }

        afterRadio();
    }


    protected void afterRadio(){

    }

    @Override
    public void onSearch(String key) {

    }
}
