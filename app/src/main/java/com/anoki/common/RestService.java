package com.anoki.common;

import android.content.Intent;

import com.anoki.RecentActivity;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.anoki.pojo.Search;
import com.anoki.pojo.User;
import com.google.android.gms.games.Notifications;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by joon on 2015-07-12.
 */
public class RestService {
    public static void makePrayer(Prayer prayer){

        prayer.apiKey =  Global.apiKey;
        Response response = Util.rest("prayer", "POST", prayer, Response.class);

    }
    public static boolean pray(Prayer prayer){


        if(prayer.checkPrayable()){
            prayer.apiKey = Global.apiKey;
            Util.rest("prayer/pray", "POST",prayer, Response.class);
            return true;
        }


        return false;
    }


    public static Response log(String account, String pass){

        User user = new User();
        user.account = account;
        user.pass = pass;

        //서버에 인증요청
        Response response = Util.rest("user/log", "POST", user, Response.class);

        if(response != null && "0".equals(response.result)){

            Global.apiKey = response.apiKey;

            user.id = response.id;
            Global.me = Util.rest("user/detail", "POST", user, User.class);
            Global.me.apiKey = Global.apiKey;

        }

        return response;

    }


    public static List<Friend> getFriendList(){
        Type listType = new TypeToken<ArrayList<Friend>>() {}.getType();

        final Search search = new Search();
        search.apiKey = Global.apiKey;
        search.searchKey = "A";

        return  Util.rest("friend/list", "POST", search, listType);

    }
}
