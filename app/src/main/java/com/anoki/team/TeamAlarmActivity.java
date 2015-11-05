package com.anoki.team;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.RadioButton;

import com.anoki.R;
import com.anoki.common.DBManager;
import com.anoki.common.SubActivityBase;

import butterknife.Bind;

public class TeamAlarmActivity extends SubActivityBase {


    @Bind(R.id.high)
    RadioButton high;

    @Bind(R.id.normal)
    RadioButton normal;

    @Bind(R.id.low)
    RadioButton low;

    @Bind(R.id.none)
    RadioButton none;

    private int teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_alarm);


        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);


        final DBManager dbManager = new DBManager(getApplicationContext());
        int level = dbManager.getTeamAlarm(teamId);
        if(level == -1){
            level = 1;
        }

        switch (level){
            case 3:
                onRadioButtonClicked(high);
                break;
            case 2:
                onRadioButtonClicked(normal);
                break;
            case 1:
                onRadioButtonClicked(low);
                break;
            case 0:
                onRadioButtonClicked(none);
                break;

        }

    }

    protected void afterRadio(){
        update(null);
    }



    public void update(View view){
        int level = 1;
        switch (selected){
            case R.id.high:
                level = 3;
                break;
            case R.id.normal:
                level = 2;
                break;
            case R.id.low:
                level = 1;
                break;
            case R.id.none:
                level = 0;
                break;
        }

        final DBManager dbManager = new DBManager(getApplicationContext());
        dbManager.setTeamAlarm(teamId, level);

    }
}
