package com.anoki1.prayer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.EditText;

import com.anoki1.R;
import com.anoki1.common.DoneState;
import com.anoki1.common.Util;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.fragment.PrayerImageFragment;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Reply;
import com.anoki1.pojo.Response;


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



}
