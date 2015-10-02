package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.Common;
import com.anoki.common.DBManager;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;
import com.anoki.pojo.Search;

import butterknife.Bind;

public class MessageDetailActivity extends SubActivityBase {

    @Bind(R.id.picture)
    public ImageView userPicture;
    @Bind(R.id.attached)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.text)
    public TextView text;

    Message message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);

        Intent intent = getIntent();
        message = (Message) intent.getSerializableExtra("message");

        Util.setPicture(message.userPicture, userPicture, null);
        Util.setPicture(message.picture, picture, null);

        if(message.user == Global.me.id) {
            name.setText(message.sender + "님으로 부터");
        }else{
            name.setText(message.sender + "님에게");
        }
        text.setText(message.text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_detail, menu);


        return true;
    }

    public void reply(MenuItem item){
        Common.replyMessage(this, message);
    }

    public void delete(MenuItem item){

        final DBManager dbManager = new DBManager(this, "Anoki.db", null, 1);
        dbManager.deleteMessage(message);

        finish();

    }

    public void inform(MenuItem item){

        Search search = new Search();
        search.searchKey = message.text;
        search.searchId = message.senderId;
        Util.rest("etc/inform","POST",search);

        toast("신고되었습니다.");
    }

    public void block(MenuItem item){

        Friend friend = new Friend();
        friend.friend = message.senderId;
        friend.state = "B";

        rest("friend","PUT",friend);
    }
}
