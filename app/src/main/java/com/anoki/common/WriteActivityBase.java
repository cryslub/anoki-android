package com.anoki.common;

import android.view.Menu;

import com.anoki.R;

/**
 * Created by joon on 2015-07-25.
 */
public class WriteActivityBase extends SubActivityBase {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);

        doneMenu = menu.findItem(R.id.action_done);

        return true;
    }
}