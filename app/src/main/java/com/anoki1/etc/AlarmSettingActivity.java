package com.anoki1.etc;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.anoki1.R;
import com.anoki1.common.DBManager;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.AlarmSetting;

import butterknife.Bind;

public class AlarmSettingActivity extends SubActivityBase {

    AlarmSetting setting;

    @Bind(R.id.high)
    RadioButton high;

    @Bind(R.id.normal)
    RadioButton normal;

    @Bind(R.id.low)
    RadioButton low;

    @Bind(R.id.none)
    RadioButton none;

    @Bind(R.id.preview)
    CheckBox preview;

    @Bind(R.id.sound)
    CheckBox sound;

    @Bind(R.id.vibe)
    CheckBox vibe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        final DBManager dbManager = new DBManager(getApplicationContext());
        setting = dbManager.getAlarmSetting();


        preview.setChecked(setting.preview == 1);
        sound.setChecked(setting.sound == 1);
        vibe.setChecked(setting.vibe == 1);


        switch (setting.level){
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
        switch (selected){
            case R.id.high:
                setting.level = 3;
                break;
            case R.id.normal:
                setting.level = 2;
                break;
            case R.id.low:
                setting.level = 1;
                break;
            case R.id.none:
                setting.level = 0;
                break;
        }

        setting.preview = preview.isChecked()?1:0;
        setting.sound = sound.isChecked()?1:0;
        setting.vibe = vibe.isChecked()?1:0;

        final DBManager dbManager = new DBManager(getApplicationContext());
        dbManager.updateAlarmSetting(setting);

    }

}
