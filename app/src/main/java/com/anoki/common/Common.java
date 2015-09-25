package com.anoki.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.anoki.MessageActivity;
import com.anoki.MyProfileActivity;
import com.anoki.PrayerDetailActivity;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;
import com.anoki.pojo.Prayer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by joon on 2015-08-09.
 */
public class Common {

    public static Map<String,String> pubKeyMap = new HashMap<String,String>(){{
        put("P","전체공개");
        put("F","친구선택");
        put("S","나만보기");
    }};

    public static void showPrayerDetail(Activity activity,Prayer prayer){
        Intent intent = new Intent(activity, PrayerDetailActivity.class);
        intent.putExtra("prayerId", prayer.id);
        activity.startActivityForResult(intent, Global.PRAYER);
    }

    public static void replyMessage(Context context, Message message){
        Intent intent = new Intent(context, MessageActivity.class);
        Friend friend = new Friend();
        friend.name =  message.sender;
        friend.picture = message.userPicture;
        friend.friend = message.user;
        intent.putExtra("friend", friend);
        context.startActivity(intent);
    }

    public static void toast(Context context, String text){
        Toast toast = Toast.makeText(context,text, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
