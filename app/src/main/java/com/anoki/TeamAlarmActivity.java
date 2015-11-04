package com.anoki;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import com.anoki.common.SubActivityBase;

public class TeamAlarmActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_alarm);
    }


    public void update(View view){

    }
}
