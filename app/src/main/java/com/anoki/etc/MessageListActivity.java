package com.anoki.etc;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.common.Common;
import com.anoki.common.DBManager;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Message;
import com.anoki.pojo.Search;
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

    @Bind(R.id.more)
    public ImageView more;

    @Bind(R.id.container)
    LinearLayout container;

    @OnClick(R.id.container)
    void detail(){

        final DBManager dbManager = new DBManager(view.getContext(), "Anoki.db", null, 1);
        message.checked = 1;
        dbManager.updateMessage(message);

        Intent intent = new Intent(view.getContext(),MessageDetailActivity.class);
        intent.putExtra("message",message);
        view.getContext().startActivity(intent);
    }

    @OnClick(R.id.more)
    void more(){
        PopupMenu popupMenu = new PopupMenu(view.getContext(), more);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_message_popup, popupMenu.getMenu());

        if(message.user != Global.me.id){
//            popupMenu.getMenu().findItem(R.id.reply).setEnabled(false);
            popupMenu.getMenu().findItem(R.id.reply).setVisible(false);
        }
        final DBManager dbManager = new DBManager(view.getContext(), "Anoki.db", null, 1);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.reply: {
                        Common.replyMessage(view.getContext(),message);
                    }
                    break;
                    case R.id.delete: {
                        dbManager.deleteMessage(message);
                        RecyclerView recyclerView = (RecyclerView) view.getParent();
                        GeneralRecyclerViewAdapter<Message,MessageViewHolder> recyclerviewAdapter
                                = (GeneralRecyclerViewAdapter<Message,MessageViewHolder>) recyclerView.getAdapter();
                        recyclerviewAdapter.updateList(dbManager.getMessage());
                      //  view.setVisibility(View.GONE);
                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
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

        if(message.checked == 0){
            container.setBackgroundColor(Color.LTGRAY);

        }else{
            container.setBackgroundColor(Color.WHITE);

        }

        if(message.user == Global.me.id){
            arrow.setImageResource(R.drawable.icn_arrow_left);

        }else{
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



    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void add(MenuItem menuItem){
        Intent intent = new Intent(MessageListActivity.this, ChooseFriendActivity.class);
        startActivity(intent);
    }


    private List<Message> getMessageList(){
        Type listType = new TypeToken<ArrayList<Message>>() {}.getType();
        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        List<Message> list =  Util.rest("etc/message","POST",new Search(), listType);
        if(list !=null) {
            for (Message message : list) {
                dbManager.insertMessage(message);
            }
        }

        return dbManager.getMessage();
    }

    public void load(){

        List<Message> list = getMessageList();

        GeneralRecyclerViewAdapter<Message,MessageViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Message,MessageViewHolder> (list,R.layout.layout_message_row,MessageViewHolder.class);

        setRecyclerView(recyclerView, responseAdapter);

    }

}
