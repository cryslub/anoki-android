package com.anoki1.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.anoki1.etc.MessageActivity;
import com.anoki1.pojo.Alarm;
import com.anoki1.pojo.Search;
import com.anoki1.prayer.PrayerDetailActivity;
import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Message;
import com.anoki1.pojo.Prayer;
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

    public static void getMessageList(Context context, final CallBack callback){
        Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
        final DBManager dbManager = new DBManager(context, "Anoki.db", null, 1);

        Util.backGroundRest("etc/message", "POST", new Search(), listType, new CallBack<ArrayList<Message>>() {
            @Override
            public void success(ArrayList<Message> result) {
                if(result !=null) {
                    for (Message message : result) {
                        dbManager.insertMessage(message);
                    }
                }

                callback.success(null);

            }
        });


    }



    public static void getAlarmList(Context context, final CallBack callback){
        Type listType = new TypeToken<ArrayList<Alarm>>() {}.getType();
        final DBManager dbManager = new DBManager(context, "Anoki.db", null, 1);

        Util.backGroundRest("etc/alarm", "POST", new Search(), listType, new CallBack<ArrayList<Alarm>>() {
                @Override
                public void success(ArrayList<Alarm> result) {
                    if(result !=null) {
                        for (Alarm alarm : result) {
                            dbManager.insertAlarm(alarm);
                        }
                    }

                    callback.success(null);


                }
        });

    }




    public static void getRequestList(Context context, final CallBack callback){
        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();
        final DBManager dbManager = new DBManager(context);

        Util.backGroundRest("prayer/request", "POST", new Search(), listType, new CallBack<ArrayList<Prayer>>() {
            @Override
            public void success(ArrayList<Prayer> result) {
                if(result !=null) {
                    for (Prayer prayer : result) {
                        if(dbManager.getOneRequest(prayer.id) == null) {
                            dbManager.insertRequest(prayer);
                        }
                    }
                }

                callback.success(null);


            }
        });


    }


}
