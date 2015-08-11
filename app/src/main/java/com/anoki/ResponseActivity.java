package com.anoki;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.anoki.common.CallBack;
import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Reply;
import com.anoki.pojo.Response;

import org.apmem.tools.layouts.FlowLayout;


public class ResponseActivity extends WriteActivityBase implements PrayerImageFragment.OnFragmentInteractionListener {

    private Prayer prayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doneStateCheck();
            }
        };

        EditText text = (EditText) findViewById(R.id.text);
        text.addTextChangedListener(textWatcher);
    }


    public void doneStateCheck(){
        EditText text = (EditText) findViewById(R.id.text);

        if(text.getText().length() == 0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }

    @Override
    protected void confirm() {
        EditText text = (EditText) findViewById(R.id.text);
        CheckBox pub = (CheckBox) findViewById(R.id.pub);

        Reply reply = new Reply();
        reply.prayer = prayer.id;
        reply.type = "R";
        reply.text = text.getText().toString();
        reply.pub = pub.isChecked()?"Y" : "N";
        reply.picture = pictureId;

        Util.rest("prayer/reply","POST",reply,Response.class);

        succeed();
    }



    @Override
    public void onDeleteFragment(String id) {
        pictureId = null;
    }
}
