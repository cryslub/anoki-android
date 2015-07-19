package com.anoki.common;

import com.anoki.pojo.User;

/**
 * Created by Administrator on 2015-07-07.
 */
public class Global {

    public static final int PHOTO = 100;
    public static final int VIDEO = 200;
    public static final int CAMERA = 300;
    public static final int FRIENDS = 400;
    public static final int PAY = 500;
    public static final int PRAYER = 600;
    public static final int NAME = 700;
    public static final int STATE = 800;
    public static final int PROFILE = 900;


    public static final int FREE_FRIENDS_COUNT  = 1;

    public static String apiKey;

    public static User me;

    public static void reloadMe(){
        Global.me = Util.rest("user/detail", "POST", me, User.class);
        Global.me.apiKey = apiKey;
    }

}
