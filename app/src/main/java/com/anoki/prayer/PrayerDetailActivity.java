package com.anoki.prayer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.fragment.PrayerImageFragment;
import com.anoki.R;
import com.anoki.fragment.ReplyFragment;
import com.anoki.common.CallBack;
import com.anoki.common.Common;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Media;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Reply;
import com.anoki.pojo.Response;
import com.anoki.pojo.Search;

import org.apmem.tools.layouts.FlowLayout;

import butterknife.Bind;

public class PrayerDetailActivity extends SubActivityBase implements PrayerImageFragment.OnFragmentInteractionListener, ReplyFragment.OnFragmentInteractionListener {

    Prayer prayer;
    int prayerId;
    String pictureId;

    @Bind(R.id.complete) ImageView complete;
    @Bind(R.id.response) ImageView response;
    @Bind(R.id.scrap) ImageView scrap;
    @Bind(R.id.pray) ImageView pray;

    @Bind(R.id.media) LinearLayout mediaList;

    @Bind(R.id.reply_text) EditText replyText;
    @Bind(R.id.scope) TextView scope;



    boolean reply;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prayer_detail);

        Intent intent = getIntent();
        prayerId = (Integer) intent.getIntExtra("prayerId",-1);


       // load();
        reply = intent.getBooleanExtra("reply", false);

        position = intent.getIntExtra("position",-1);

      //  setMediaList();
    }




    private void refresh(){

        load();
    }

    public void load(){
        LinearLayout replyList = (LinearLayout) findViewById(R.id.reply_list);
        replyList.removeAllViews();

        final Search search = new Search();
        search.searchId = prayerId;

        prayer = Util.rest("prayer/detail", "POST", search, Prayer.class);

        setText(R.id.pray_count, "기도 " + prayer.prayCount);
        setText(R.id.reply_count, "댓글 " + prayer.replyCount);
        setText(R.id.response_count, "응답 " + prayer.responseCount);



        if(prayer.userId != Global.me.id){
  //          LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.button_container);

            response.setVisibility(View.GONE);
//            buttonContainer.removeView(response);

            complete.setVisibility(View.GONE);

            ImageButton popup = (ImageButton) findViewById(R.id.popup);

            popup.setVisibility(View.VISIBLE);

//            buttonContainer.removeView(complete);

        }

        if(prayer.scrapd != null || prayer.userId == Global.me.id){
       //     LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.button_container);

            scrap.setVisibility(View.GONE);
        //    buttonContainer.removeView(scrap);
        }

        if(prayer.reply != null && prayer.reply.size() > 0) {

            int i = 2000;

            for (Reply reply : prayer.reply) {

                addReply(reply, i++);
            }
        }

        if(prayer.media != null) {
            mediaList.removeAllViews();
            int i =0;
            for (Media media : prayer.media) {
                addMedia(media,i++);
            }
        }



        prayer.apiKey = Global.apiKey;

        ImageView picture = (ImageView) findViewById(R.id.picture);
        Util.setPicture(prayer.userPicture, picture);

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
        Util.setPicture(Global.me.picture + "", myPicture);


        if(prayer.checkPrayable()){
            pray.setImageResource(R.drawable.btn_pray_mint);
        }else{
            pray.setImageResource(R.drawable.btn_pray_gray);
        }

        if("Y".equals(prayer.completed)){
            complete.setVisibility(View.GONE);
        }

        if(reply){

            showReplyContainer(null);
            replyText.requestFocus();
        }

        if(position>=0){
            position = -1;
        }

        scope.setText(Common.pubKeyMap.get(prayer.pub));

    }



    private void addMedia(final Media media,int id){



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

        Util.setMediaView(itemView, media);

        if(id == position){
            itemView.requestFocus();
        }

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
        load();
    }

    public void showReplyContainer(View view){


        LinearLayout showButton = (LinearLayout) findViewById(R.id.show_reply);
        showButton.setVisibility(View.GONE);

        LinearLayout container = (LinearLayout) findViewById(R.id.reply_container);
        container.setVisibility(View.VISIBLE);

    }


    public void friendFunction(View view){
        if (prayer.userId == Global.me.id) {
            showMyPopupMenu(prayer,view);
        }else{
            showPopupMenu(prayer,view);
        }
    }

    public void responseList(View view){
        Intent intent = new Intent(PrayerDetailActivity.this, ResponseListActivity.class);
        intent.putExtra("prayer",prayer);
        startActivity(intent);
    }


    public void photo(View view){

        Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoLibraryIntent.setType("image/*");
        startActivityForResult(photoLibraryIntent, Global.PHOTO);

    }

    public void done(View view){
        EditText replyText = (EditText) findViewById(R.id.reply_text);
        if(replyText.length() > 0) {
            CheckBox pub = (CheckBox) findViewById(R.id.pub);

            Reply reply = new Reply();
            reply.apiKey = Global.apiKey;
            reply.text = replyText.getText().toString();
            reply.prayer = prayerId;
            reply.type = "S";
            reply.pub = pub.isChecked() ? "N" : "Y";
            reply.picture = pictureId;


            Util.rest("prayer/reply", "POST", reply, Prayer.class);

            replyText.setText("");
            pub.setChecked(false);
            ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);
            flowLayout.removeAllViews();

            refresh();
        }

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

    @Override
    public void responseList(Reply reply) {
        Intent intent = new Intent(PrayerDetailActivity.this, ResponseListActivity.class);
        intent.putExtra("prayer",prayer);
        intent.putExtra("reply",reply);
        startActivity(intent);
    }

    @Override
    public Prayer getPrayer() {
        return prayer;
    }
}
