package com.anoki.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.anoki.etc.MessageActivity;
import com.anoki.pojo.Alarm;
import com.anoki.pojo.Search;
import com.anoki.prayer.PrayerDetailActivity;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;
import com.anoki.pojo.Prayer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public static  List<Message> getMessageList(Context context){
        Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
        final DBManager dbManager = new DBManager(context, "Anoki.db", null, 1);

        List<Message> list =  Util.rest("etc/message","POST",new Search(), listType);
        if(list !=null) {
            for (Message message : list) {
                dbManager.insertMessage(message);
            }
        }

        return dbManager.getMessage();
    }



    public static List<Alarm> getAlarmList(Context context){
        Type listType = new TypeToken<ArrayList<Alarm>>() {}.getType();
        final DBManager dbManager = new DBManager(context, "Anoki.db", null, 1);

        List<Alarm> list =  Util.rest("etc/alarm","POST",new Search(), listType);
        if(list !=null) {
            for (Alarm alarm : list) {
                dbManager.insertAlarm(alarm);
            }
        }

        return dbManager.getAlarm();
    }


    public static List<Alarm> getRequestList(Context context){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();
        final DBManager dbManager = new DBManager(context);

        List<Prayer> list =  Util.rest("prayer/request","POST",new Search(), listType);
        if(list !=null) {
            for (Prayer prayer : list) {
                dbManager.insertRequest(prayer);
            }
        }

        return dbManager.getAlarm();
    }


}
