package com.anoki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Response;
import com.google.android.gms.games.Notifications;

public class ChangeNameActivity extends WriteActivityBase implements EditTextFragment.OnFragmentInteractionListener{

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);

        EditTextFragment editTextFragment = (EditTextFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        View view=editTextFragment.getView();

        editText = (EditText) view.findViewById(R.id.calc_txt_Prise);

        editText.setText(Global.me.name);
    }

    public void done(MenuItem item) {
        switch (doneState) {
            case CLEAR:
                onBackPressed();
                break;
            case DONE: {
                Global.me.name =editText.getText().toString();

                Util.rest("user","PUT",Global.me,Response.class);

                succeed();
            }
            break;
        }
    }

    @Override
    public void textChanged(Editable s) {
        TextView  tv = (TextView)findViewById(R.id.text_length);
        tv.setText(s.length()+"");

        doneStateCheck();
    }

    public void doneStateCheck(){
        if(doneMenu == null) return;

        TextView  tv = (TextView)findViewById(R.id.text_length);

        if("0".equals(tv.getText().toString())){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }
}
