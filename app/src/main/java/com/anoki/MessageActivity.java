package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.Util;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class MessageActivity extends WriteActivityBase {

    @Bind(R.id.text)
    EditText text;

    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;

    Friend friend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        Intent intent = getIntent();
        friend = (Friend) intent.getSerializableExtra("friend");

        Util.setPicture(friend.picture, picture, getDrawable(R.drawable.ic_person_black_36dp));
        name.setText(friend.name);

    }



    @OnTextChanged(R.id.text)
    public void doneStateCheck(){
        if(text.getText().toString().length() > 0){
            setDonState(DoneState.DONE);
        }else{
            setDonState(DoneState.CLEAR);
        }
    }


    @Override
    protected void confirm() {
        Message message = new Message();
        message.text = text.getText().toString();
        message.user = friend.friend;
        message.picture = pictureId;
        Util.rest("etc/send/message","POST",message);
        succeed();
    }

}
