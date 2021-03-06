package com.anoki1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anoki1.common.CallBack;
import com.anoki1.common.Global;
import com.anoki1.common.PrayerAdapter;
import com.anoki1.common.TabActivityBase;
import com.anoki1.common.Util;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.User;
import com.anoki1.user.BoxActivity;
import com.anoki1.user.MyProfileActivity;

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

        super.load();


        setPrayerListPage(Global.me.id, true, new CallBack<User>() {
            @Override
            public void success(User user) {
                if(user.text != null) {
                    TextView text = (TextView) findViewById(R.id.text);
                    text.setText(user.text);
                }

            }
        });
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

            int i  =prayerAdapter.visibleObjects.size();
            for(Prayer prayer : prayerAdapter.visibleObjects){
                prayer.idx = i;
                Util.rest("prayer","PUT",prayer);
                i--;
            }


            for(Prayer prayer: prayerAdapter.allObjects){
                prayer.edit = false;
            }

            prayerAdapter.updateList(prayerAdapter.allObjects);
            prayerAdapter.editable = false;
            edit.setText("편집");



            load();
        }else{
            for(Prayer prayer: prayerAdapter.allObjects){
                prayer.edit = true;
            }

            prayerAdapter.updateList(prayerAdapter.allObjects);

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
