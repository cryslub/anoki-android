package com.anoki1.common;

import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Search;
import com.anoki1.pojo.Team;
import com.anoki1.pojo.User;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joon on 2015-07-12.
 */
public class RestService {
    public static void makePrayer(Prayer prayer,final CallBack <Response> callback){

        prayer.apiKey =  Global.apiKey;
        prayer.userName = Global.me.name;
        if(Global.me == null) return;

        Util.backGroundRest("prayer", "POST", prayer, Response.class, new CallBack<Response>() {
            @Override
            public void success(Response result) {
                callback.success(result);
            }
        });

    }
    public static boolean pray(Prayer prayer,final CallBack <Response> callback){


        if(prayer.checkPrayable()){
            prayer.apiKey = Global.apiKey;
            Util.backGroundRest("prayer/pray", "POST",prayer, Response.class, new CallBack<Response>() {
                @Override
                public void success(Response result) {
                    callback.success(result);
                }
            });
            return true;
        }


        return false;
    }


    public static void log(String account, String pass,final CallBack<Response> callback){

        final User user = new User();
        user.account = account;
        user.pass = pass;

        //서버에 인증요청
        Util.backGroundRest("user/log", "POST", user, Response.class, new CallBack<Response>() {
            @Override
            public void success(Response response) {

                if(response != null && "0".equals(response.result)){

                    Global.apiKey = response.apiKey;

                    user.id = response.id;
                    Global.me = Util.rest("user/detail", "POST", user, User.class);
                    Global.me.apiKey = Global.apiKey;

                }

                callback.success(response);
            }
        });

    }


    public static void getFriendList(final CallBack <ArrayList<Friend>> callback){

        getFriendList("A",callback);
    }


    public static void getFriendList(String searchKey, final CallBack <ArrayList<Friend>> callback){
        final Type listType = new TypeToken<ArrayList<Friend>>() {}.getType();

        final Search search = new Search();
        search.apiKey = Global.apiKey;
        search.searchKey = searchKey;


        Util.backGroundRest("friend/list", "POST", search, listType, new CallBack<ArrayList<Friend>>() {
            @Override
            public void success(ArrayList<Friend> result) {

                callback.success(result);

            }
        });

    }

    public static void makeTeam(Team team, final CallBack<Team> callback) {
        team.apiKey =  Global.apiKey;

        Util.backGroundRest("team", "POST", team, Team.class, new CallBack<Team>() {
            @Override
            public void success(Team result) {
                callback.success(result);
            }
        });
    }

    public static void charge(int dalant,final CallBack<Response> callback ){

        User user = new User();
        user.dalant = dalant;
        Util.backGroundRest("user/charge", "POST", user, Response.class, new CallBack<Response>() {
            @Override
            public void success(Response result) {
                Global.reloadMe();
                callback.success(result);
            }
        });



    }
}
