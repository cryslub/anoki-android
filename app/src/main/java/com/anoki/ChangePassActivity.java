package com.anoki;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Response;


public class ChangePassActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change_pass, menu);
        return true;
    }

    public void confirm(View view){
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText confirm = (EditText) findViewById(R.id.confirm);

        if(pass.getText().toString().equals(confirm.getText().toString())) {

            Global.me.pass = pass.getText().toString();
            Util.rest("user", "PUT", Global.me, Response.class);

            succeed();
        }
    }
}
