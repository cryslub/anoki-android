package com.anoki;

import android.os.Bundle;
import android.app.Activity;

import com.anoki.common.SubActivityBase;

public class TeamSettingsActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_settings);
    }

}
