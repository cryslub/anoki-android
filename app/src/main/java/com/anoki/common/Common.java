package com.anoki.common;

import android.app.Activity;
import android.content.Intent;

import com.anoki.MyProfileActivity;
import com.anoki.PrayerDetailActivity;
import com.anoki.pojo.Prayer;

/**
 * Created by joon on 2015-08-09.
 */
public class Common {

    public static void showPrayerDetail(Activity activity,Prayer prayer){
        Intent intent = new Intent(activity, PrayerDetailActivity.class);
        intent.putExtra("prayerId", prayer.id);
        activity.startActivityForResult(intent, Global.PRAYER);
    }


}
