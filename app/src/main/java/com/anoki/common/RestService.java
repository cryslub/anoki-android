package com.anoki.common;

import android.content.Intent;

import com.anoki.RecentActivity;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by joon on 2015-07-12.
 */
public class RestService {
    public static void makePrayer(Prayer prayer){

        prayer.apiKey =  Global.apiKey;
        Response response = Util.rest("prayer", "POST", prayer, Response.class);

    }
    public static boolean pray(Prayer prayer){


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

        try {
            Calendar date = Calendar.getInstance();
            date.setTime(format.parse(prayer.lastPrayed));
            Calendar now = Calendar.getInstance();

            double diff = now.getTimeInMillis() - date.getTimeInMillis();
            if(diff > 60*60*1000){

                Prayer p = new Prayer();

                p.id = prayer.id;
                Util.rest("prayer/pray", "POST",p, Response.class);
                return true;
            }

        } catch (ParseException e) {
            e.printStackTrace();
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

}
