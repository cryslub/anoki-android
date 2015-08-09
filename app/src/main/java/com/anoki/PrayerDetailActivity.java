package com.anoki;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.anoki.common.CallBack;
import com.anoki.common.Global;
import com.anoki.common.MediaPagerAdapter;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Media;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Reply;
import com.anoki.pojo.Response;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import org.apmem.tools.layouts.FlowLayout;
import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrayerDetailActivity extends SubActivityBase implements PrayerImageFragment.OnFragmentInteractionListener {

    Prayer prayer;
    int prayerId;
    String pictureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_detail);

        Intent intent = getIntent();
        prayerId = (Integer) intent.getIntExtra("prayerId",-1);


        load();
        prayer.apiKey = Global.apiKey;

        ImageView picture = (ImageView) findViewById(R.id.picture);
        Util.setPicture(prayer.userPicture, picture, getResources().getDrawable(R.drawable.ic_person_black_36dp));

        setText(R.id.name, prayer.userName);
        setText(R.id.text,prayer.back+"\r\n\r\n"+prayer.text);
        setText(R.id.date, prayer.time);


        if(prayer.responseCount == 0){
            LinearLayout bar = (LinearLayout)findViewById(R.id.response_bar);
 //           LinearLayout container = (LinearLayout)findViewById(R.id.container);
//            container.removeView(bar);
            bar.setVisibility(View.GONE);
        }else{
            TextView response = (TextView) findViewById(R.id.response_count);
            response.setText(prayer.responseCount+"건");
        }

        ImageView myPicture = (ImageView) findViewById(R.id.my_picture);
        Util.setPicture(Global.me.picture + "", myPicture, getResources().getDrawable(R.drawable.ic_person_black_24dp));

        ImageView photo = (ImageView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoLibraryIntent.setType("image/*");
                startActivityForResult(photoLibraryIntent, Global.PHOTO);


            }
        });

        ImageView done = (ImageView) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText replyText = (EditText) findViewById(R.id.reply_text);
                if(replyText.length() > 0){
                    CheckBox pub = (CheckBox) findViewById(R.id.pub);

                    Reply reply = new Reply();
                    reply.apiKey = Global.apiKey;
                    reply.text = replyText.getText().toString();
                    reply.prayer = prayerId;
                    reply.type = "S";
                    reply.pub = pub.isChecked()?"N":"Y";
                    reply.picture = pictureId;


                    Util.rest("prayer/reply", "POST", reply, Prayer.class);

                    replyText.setText("");
                    pub.setChecked(false);
                    ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);
                    flowLayout.removeAllViews();

                    refresh();
                }
            }
        });


        if(intent.getBooleanExtra("reply", false)){

            showReplyContainer(null);

        }

      //  setMediaList();
    }



    private void setMediaList(){

        ViewPager media = (ViewPager) findViewById(R.id.media);
        if(prayer.media==null|| prayer.media.size() == 0){
            media.setVisibility(View.GONE);
        }else{
            MediaPagerAdapter mCustomPagerAdapter = new MediaPagerAdapter(PrayerDetailActivity.this,prayer.media);
            media.setAdapter(mCustomPagerAdapter);
        }


    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                succeed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void refresh(){
        LinearLayout replyList = (LinearLayout) findViewById(R.id.reply_list);
        replyList.removeAllViews();

        load();
    }

    private void load(){
        final Search search = new Search();
        search.searchId = prayerId;

        prayer = Util.rest("prayer/detail", "POST", search, Prayer.class);

        setText(R.id.pray_count, "기도 " + prayer.prayCount);
        setText(R.id.reply_count, "댓글 " + prayer.replyCount);


        if(prayer.userId != Global.me.id){
  //          LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.button_container);

            TextView response = (TextView) findViewById(R.id.response);
            response.setVisibility(View.GONE);
//            buttonContainer.removeView(response);

            TextView complete = (TextView) findViewById(R.id.complete);
            complete.setVisibility(View.GONE);

            ImageButton popup = (ImageButton) findViewById(R.id.popup);

            popup.setVisibility(View.VISIBLE);

//            buttonContainer.removeView(complete);

        }

        if(prayer.scrapd != null || prayer.userId == Global.me.id){
       //     LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.button_container);
            TextView scrap = (TextView) findViewById(R.id.scrap);
            scrap.setVisibility(View.GONE);
        //    buttonContainer.removeView(scrap);
        }

        if(prayer.reply.size() > 0) {
            LinearLayout replyList = (LinearLayout) findViewById(R.id.reply_list);
            int i = 2000;

            for (Reply reply : prayer.reply) {

                addReply(reply, i++);
            }
        }

        if(prayer.media != null) {
            for (Media media : prayer.media) {
                addMedia(media);
            }
        }

        if(Integer.parseInt(prayer.replyCount) == 0){
            LinearLayout showReply = (LinearLayout) findViewById(R.id.show_reply);
            showReply.setVisibility(View.GONE);
        }

    }



    private void addMedia(final Media media){
        LinearLayout mediaList = (LinearLayout) findViewById(R.id.media);


        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View itemView = mLayoutInflater.inflate(R.layout.layout_prayer_image, mediaList);
        View itemView  = getLayoutInflater().inflate(R.layout.layout_prayer_image, null);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.zoom(media, PrayerDetailActivity.this);
            }
        });

        mediaList.addView(itemView);

        Util.setMediaView(itemView,media);



    }

    private void addReply(Reply reply,int index){
        LinearLayout replyList = (LinearLayout) findViewById(R.id.reply_list);

        LinearLayout rowLayout = new LinearLayout(PrayerDetailActivity.this);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        rowLayout.setId(index);

// add rowLayout to the root layout somewhere here

        ReplyFragment replyFragment = new ReplyFragment();
        replyFragment.setReply(reply);

//                    imageFragment.setUri(mUrls[i]);
        fragTransaction.add(rowLayout.getId(), replyFragment, "fragment" + index);
        fragTransaction.commit();

        replyList.addView(rowLayout);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prayer_detail, menu);
        return true;
    }

    public void profile(View view){
        myProfile(prayer);
    }

    public void pray(View view){

        if(RestService.pray(prayer)) refresh();

    }

    public void scrap(View view){
        Util.rest("prayer/scrap", "POST", prayer, Prayer.class);
        refresh();

    }

    public void response(View view){
        Intent intent = new Intent(PrayerDetailActivity.this, ResponseActivity.class);
        intent.putExtra("prayer", prayer);
        startActivityForResult(intent, Global.RESPONSE);
    }

    public void complete(View view){

        Prayer p = new Prayer();
        p.id = prayer.id;
        Util.rest("prayer/complete", "POST", p, Response.class);
    }

    public void showReplyContainer(View view){


        LinearLayout showButton = (LinearLayout) findViewById(R.id.show_reply);
        showButton.setVisibility(View.GONE);

        LinearLayout container = (LinearLayout) findViewById(R.id.reply_container);
        container.setVisibility(View.VISIBLE);

    }


    public void friendFunction(View view){
        showPopupMenu(prayer, view);
    }

    public void responseList(View view){
        Intent intent = new Intent(PrayerDetailActivity.this, ResponseListActivity.class);
        intent.putExtra("prayer",prayer);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.PHOTO:
                    Util.upload(data.getData(), getContentResolver(), new CallBack() {
                        @Override
                        public void success(String id) {
                            //media list 에 추가

                            pictureId = id;

                            ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);

                            ImageView imageView = new ImageView(PrayerDetailActivity.this);
                            Bitmap bmp = Util.fetchImage(id);
                            imageView.setImageBitmap(bmp);


                            int size = Util.dpToPixel(getApplicationContext(), 80);
                            int margin = Util.dpToPixel(getApplicationContext(), 5);
//                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
//                    layoutParams.setMargins(margin, margin, margin, margin);

//                            imageView.setLayoutParams(layoutParams);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                            //                  flowLayout.addView(imageView, layoutParams);




                            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size,size);
                            layoutParams.setMargins(margin, margin, margin, margin);

                            LinearLayout rowLayout = new LinearLayout(PrayerDetailActivity.this);

                            FragmentManager fragMan = getFragmentManager();
                            FragmentTransaction fragTransaction = fragMan.beginTransaction();

                            rowLayout.setId(Integer.parseInt(id));

// add rowLayout to the root layout somewhere here

                            PrayerImageFragment imageFragment = new PrayerImageFragment();
                            imageFragment.setBmp(bmp);
//                    imageFragment.setUri(mUrls[i]);
                            fragTransaction.add(rowLayout.getId(), imageFragment, "fragment" + id);
                            fragTransaction.commit();

                            flowLayout.removeAllViews();
                            flowLayout.addView(rowLayout,layoutParams);

                        }
                    });
                    break;

                case Global.RESPONSE:
                    reload();
                    break;
            }
        }
    }



    @Override
    public void onDeleteFragment(String id) {
        pictureId = null;
    }
}
