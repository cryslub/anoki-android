package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Response;
import com.anoki.pojo.Search;

import butterknife.Bind;
import butterknife.OnClick;

public class ForgotPassActivity extends SubActivityBase implements EditTextFragment.OnFragmentInteractionListener{

    @Bind(R.id.calc_txt_Prise)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
    }


    @OnClick(R.id.restore)
    public void restore(View view){
        Search search = new Search();
        search.searchKey = editText.getText().toString();

        Response response = Util.rest("auth/restore", "POST", search, Response.class);

        if("0".equals(response.result)) {
            Intent intent = new Intent(ForgotPassActivity.this, RestoreActivity.class);
            startActivityForResult(intent, Global.RESTORE);
        }
    }


    @Override
    public void textChanged(Editable s) {

    }
}
