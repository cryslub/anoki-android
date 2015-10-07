package com.anoki;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.User;
import com.anoki.user.BoxActivity;
import com.anoki.user.MyProfileActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MeTabActivity extends TabActivityBase implements PrayerAdapter.OnPrayListener{

    @Bind(R.id.edit)
    Button edit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_tab);

        ButterKnife.bind(this);
    }


    @Override
    protected void onStart() {
        super.onStart();

        load();
    }

    public void load(){
        User user = setPrayerListPage(Global.me.id,true);
        if(user.text != null) {
            TextView text = (TextView) findViewById(R.id.text);
            text.setText(user.text);
        }
    }
    public void profile(View view){
        Intent intent = new Intent(MeTabActivity.this, MyProfileActivity.class);
        startActivityForResult(intent,Global.PROFILE);

    }


    public void box(View view){
        Intent intent = new Intent(MeTabActivity.this, BoxActivity.class);
        startActivity(intent);
    }


    public void edit(View view){

        if(prayerAdapter.editable) {
            prayerAdapter.editable = false;
            edit.setText("편집");

            int i  =prayerAdapter.visibleObjects.size();
            for(Prayer prayer : prayerAdapter.visibleObjects){
                prayer.idx = i;
                Util.rest("prayer","PUT",prayer);
                i--;
            }

        }else{
            prayerAdapter.editable = true;
            edit.setText("완료");
        }
    }

    protected void refresh(){
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
}
