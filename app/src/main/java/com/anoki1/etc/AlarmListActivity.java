package com.anoki1.etc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.DBManager;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.common.ViewHolderBase;
import com.anoki1.pojo.Alarm;
import com.anoki1.pojo.Search;
import com.anoki1.prayer.PrayerDetailActivity;
import com.anoki1.team.TeamDetailActivity;
import com.anoki1.user.UserProfileActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class AlarmViewHolder extends ViewHolderBase<Alarm> {


    public View view;

    Alarm alarm;

    @Bind(R.id.picture)
    public ImageView picture;

    @Bind(R.id.text)
    public TextView text;

    @Bind(R.id.date)
    public TextView date;

    @Bind(R.id.scrap)
    public ImageView scrap;

    @OnClick(R.id.container)
    void detail(){
        Intent intent = null;
        switch (alarm.type){
            case"F":
            {
                intent = new Intent(view.getContext(), UserProfileActivity.class);
                intent.putExtra("userId",alarm.gId);
                break;
            }
            case"R":
            {
                intent = new Intent(view.getContext(), PrayerDetailActivity.class);
                intent.putExtra("prayerId",alarm.gId);
                intent.putExtra("reply",true);
                break;
            }
            case"Q":
            {
                intent = new Intent(view.getContext(), PrayerDetailActivity.class);
                intent.putExtra("prayerId",alarm.gId);

                break;
            }
            case"S":
            {
                intent = new Intent(view.getContext(), PrayerDetailActivity.class);
                intent.putExtra("prayerId",alarm.gId);
                intent.putExtra("reply",true);
                break;
            }
            case"G":
            {
                intent = new Intent(view.getContext(), TeamDetailActivity.class);
                intent.putExtra("teamId",alarm.gId);
                break;
            }

        }

        view.getContext().startActivity(intent);


    }

    @OnClick(R.id.scrap)
    void scrap(){

    }


    public AlarmViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Alarm alarm) {

        this.alarm = alarm;

        Util.setPicture(alarm.picture+"",picture);


        this.text.setText(Util.alarmText(alarm.type,alarm.name1,alarm.name2));

        if("Q".equals(alarm.type)){
            scrap.setVisibility(View.VISIBLE);
        }else{
            scrap.setVisibility(View.GONE);
        }

    }
}

public class AlarmListActivity extends SubActivityBase {

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);
    }


    private void getAlarmList(){
        Type listType = new TypeToken<ArrayList<Alarm>>() {}.getType();
        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        backGroundRest("etc/alarm", "POST", new Search(), listType, new CallBack<List<Alarm>>() {
            @Override
            public void success(List<Alarm> list) {
                if(list !=null) {
                    for (Alarm alarm : list) {
                        dbManager.insertAlarm(alarm);
                    }

                    List<Alarm> alarmList= dbManager.getAlarm();
                    GeneralRecyclerViewAdapter<Alarm,AlarmViewHolder> responseAdapter
                            = new  GeneralRecyclerViewAdapter<Alarm,AlarmViewHolder> (alarmList,R.layout.layout_alarm_row,AlarmViewHolder.class);

                    setRecyclerView(recyclerView, responseAdapter);

                    final DBManager dbManager = new DBManager(getApplicationContext());
                    dbManager.checkAlarm();;
                }



            }
        });

    }


    public void load(){

       getAlarmList();



    }


}
