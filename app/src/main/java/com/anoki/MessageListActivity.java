package com.anoki;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaExtractor;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Message;
import com.anoki.pojo.Search;
import com.google.android.gms.games.Notifications;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class MessageViewHolder extends ViewHolderBase<Message> {


    public View view;

    Message message;

    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.text)
    public TextView text;

    @Bind(R.id.date)
    public TextView date;

    @Bind(R.id.arrow)
    public ImageView arrow;


    @Bind(R.id.container)
    LinearLayout container;

    @OnClick(R.id.container)
    void detail(){
        Intent intent = new Intent(view.getContext(),MessageDetailActivity.class);
        intent.putExtra("message",message);
        view.getContext().startActivity(intent);
    }


    public MessageViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Message message) {

        this.message = message;

        Util.setPicture(message.userPicture, picture);
        name.setText(message.sender);
        date.setText(message.time);
        text.setText(message.text);

        if(message.user == Global.me.id){
            container.setBackgroundColor(Color.LTGRAY);
            arrow.setImageResource(R.drawable.icn_arrow_left);

        }else{
            container.setBackgroundColor(Color.WHITE);
            arrow.setImageResource(R.drawable.icn_arrow_right);

        }

    }
}


public class MessageListActivity extends SubActivityBase {

    @Bind(R.id.list)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message_list, menu);
        return true;
    }


    public void add(MenuItem menuItem){
        Intent intent = new Intent(MessageListActivity.this, ChooseFriendActivity.class);
        startActivity(intent);
    }


    private List<Message> getMessageList(){
        Type listType = new TypeToken<ArrayList<Message>>() {}.getType();

        return Util.rest("etc/message","POST",new Search(), listType);
    }

    protected void load(){

        List<Message> list = getMessageList();

        GeneralRecyclerViewAdapter<Message,MessageViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Message,MessageViewHolder> (list,R.layout.layout_message_row,MessageViewHolder.class);
        setRecyclerView(recyclerView, responseAdapter);

    }
}
