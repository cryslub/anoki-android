package com.anoki1.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.Global;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Search;

import butterknife.OnClick;

public class AccountActivity extends SubActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        TextView account = (TextView) findViewById(R.id.account);
        account.setText(Global.me.account);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @OnClick(R.id.changePass)
    public void changePass(View view){

        Search search = new Search();

        EditText pass = (EditText) findViewById(R.id.pass);
        search.searchKey = pass.getText().toString();

        Response response = Util.rest("auth/pass","POST",search,Response.class);
        if("0".equals(response.result)) {

            Intent intent = new Intent(AccountActivity.this, ChangePassActivity.class);
            startActivity(intent);
        }
    }

    @OnClick(R.id.restore)
    public void restore(View view){
        Search search = new Search();
        Util.rest("auth/restore","POST",search,Response.class);

        Intent intent = new Intent(AccountActivity.this, RestoreActivity.class);
        startActivity(intent);
    }

}
