package com.anoki1.user;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.anoki1.R;
import com.anoki1.common.Global;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.pojo.Response;

import butterknife.OnClick;


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

    @OnClick(R.id.ok)
    public void ok(View view){
        EditText pass = (EditText) findViewById(R.id.pass);
        EditText confirm = (EditText) findViewById(R.id.confirm);

        if(pass.getText().toString().equals(confirm.getText().toString())) {

            Global.me.pass = pass.getText().toString();
            Util.rest("user", "PUT", Global.me, Response.class);

            succeed();
        }
    }
}
