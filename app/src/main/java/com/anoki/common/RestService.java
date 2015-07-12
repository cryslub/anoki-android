package com.anoki.common;

import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;

/**
 * Created by joon on 2015-07-12.
 */
public class RestService {
    public static void makePrayer(Prayer prayer){

        prayer.apiKey =  Global.apiKey;
        Response response = Util.rest("prayer", "POST", prayer, Response.class);

    }
}
