package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Reply;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrayerDetailActivity extends SubActivityBase {

    Prayer prayer;
    int prayerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_detail);

        Intent intent = getIntent();
        prayerId = (Integer) intent.getIntExtra("prayerId",-1);


        refresh();
        prayer.apiKey = Global.apiKey;

        ImageView picture = (ImageView) findViewById(R.id.picture);
        Util.setPicture(prayer.userPicture,picture, getResources().getDrawable(R.drawable.ic_person_black_36dp));

        setText(R.id.name, prayer.userName);
        setText(R.id.text,prayer.back+"\r\n\r\n"+prayer.text);
        setText(R.id.date,prayer.time);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.reply_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter

        final ReplyAdapter friendsAdapter = new ReplyAdapter(prayer.reply);
        // 4. set adapter
        recyclerView.setAdapter(friendsAdapter);


        if(prayer.responseCount == 0){
            LinearLayout bar = (LinearLayout)findViewById(R.id.response_bar);
            LinearLayout container = (LinearLayout)findViewById(R.id.container);
            container.removeView(bar);
        }else{
            TextView response = (TextView) findViewById(R.id.response);
            response.setText("기도응답이 "+prayer.responseCount+"건 있습니다.");
        }

        ImageView myPicture = (ImageView) findViewById(R.id.my_picture);
        Util.setPicture(Global.me.picture + "", myPicture, getResources().getDrawable(R.drawable.ic_person_black_24dp));

        ImageView photo = (ImageView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(photoPickerIntent, Global.PHOTO);
            }
        });

        ImageView done = (ImageView) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String replyText = ((EditText) findViewById(R.id.reply_text)).getText().toString();
                if(replyText.length() > 0){
                    CheckBox pub = (CheckBox) findViewById(R.id.pub);

                    Reply reply = new Reply();
                    reply.apiKey = Global.apiKey;
                    reply.text = replyText;
                    reply.prayer = prayerId;
                    reply.type = "S";
                    reply.pub = pub.isChecked()?"N":"Y";


                    Util.rest("prayer/reply", "POST", reply, Prayer.class);
                }
            }
        });


    }


    private void refresh(){
        final Search search = new Search();
        search.searchId = prayerId;

        prayer = Util.rest("prayer/detail", "POST", search, Prayer.class);

        setText(R.id.pray_count,"기도 "+ prayer.prayCount);
        setText(R.id.reply_count,"댓글 "+ prayer.replyCount);

        if(prayer.scrapd != null){
            LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.button_container);
            TextView scrap = (TextView) findViewById(R.id.scrap);
            buttonContainer.removeView(scrap);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prayer_detail, menu);
        return true;
    }


    public void pray(View view){



        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

        try {
            Calendar date = Calendar.getInstance();
            date.setTime(format.parse(prayer.lastPrayed));
            Calendar now = Calendar.getInstance();

            double diff = now.getTimeInMillis() - date.getTimeInMillis();
            if(diff > 60*60*1000){


                Util.rest("prayer/pray", "POST",prayer, Prayer.class);
                refresh();
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void scrap(View view){
        Util.rest("prayer/scrap", "POST",prayer, Prayer.class);
        refresh();

    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView name;
        TextView text;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            text = (TextView) itemLayoutView.findViewById(R.id.text);
        }
    }


    private class ReplyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<Reply> itemsData;

        public ReplyAdapter(List<Reply> itemsData) {
            this.itemsData = itemsData;
        }

        public void updateList(List<Reply> itemsData){
            this.itemsData = itemsData;
            notifyDataSetChanged();
        }
        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_recent_row, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Reply reply = itemsData.get(position);

            Util.setPicture(reply.picture,viewHolder.picture,getDrawable(R.drawable.ic_person_black_36dp));


            viewHolder.name.setText(reply.name);
            viewHolder.text.setText(reply.text);

        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {

            return itemsData.size();
        }

    }

}
