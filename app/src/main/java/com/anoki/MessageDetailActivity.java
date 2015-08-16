package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Message;

import butterknife.Bind;

public class MessageDetailActivity extends SubActivityBase {

    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.text)
    public TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        Intent intent = getIntent();
        Message message = (Message) intent.getSerializableExtra("message");

        Util.setPicture(message.userPicture, picture, null);
        if(message.user == Global.me.id) {
            name.setText(message.sender + "님으로 부터");
        }else{
            name.setText(message.sender + "님에게");
        }
        text.setText(message.text);
    }


}
