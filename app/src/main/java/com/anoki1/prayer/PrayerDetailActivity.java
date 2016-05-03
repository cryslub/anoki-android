package com.anoki1.prayer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.VideoView;

import com.anoki1.common.CallBack;
import com.anoki1.common.Common;
import com.anoki1.common.PictureCallBack;
import com.anoki1.etc.MessageActivity;
import com.anoki1.fragment.PrayerImageFragment;
import com.anoki1.R;
import com.anoki1.fragment.ReplyFragment;
import com.anoki1.common.Global;
import com.anoki1.common.RestService;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.Util;
import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Media;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Reply;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Search;
import com.anoki1.user.MyProfileActivity;
import com.anoki1.user.UserProfileActivity;

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
    @Bind(R.id.reply_list) LinearLayout replyList;

    @Bind(R.id.reply_text) EditText replyText;


    @Bind(R.id.friend_count) TextView friendCount;
    @Bind(R.id.scope) TextView scope;
    @Bind(R.id.scope_image) ImageView scopeImage;

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



    @Override
    public void onWindowFocusChanged(boolean hasFocus){
       // load();

    }

    private void refresh(){

    }

    public void load(){

        final Search search = new Search();
        search.searchId = prayerId;

        Util.backGroundRest("prayer/detail", "POST", search, Prayer.class, new CallBack<Prayer>(){

            @Override
            public void success(Prayer prayer) {

                PrayerDetailActivity.this.prayer = prayer;

                prayer = Util.rest("prayer/detail", "POST", search, Prayer.class);

                if( prayer == null) return;
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

                    int i = 1500;

                    Log.i("add reply","add reply");
                    replyList.removeAllViews();


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
                    TextView response = (TextView) findViewById(R.id.bar_response_count);
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

                switch (prayer.pub){
                    case "P":
                        scope.setText("전체공개");
                        friendCount.setText("All");
                        scopeImage.setImageResource(R.drawable.ic_publish_friend);
                        break;
                    case "F":
                        scope.setText("친구공개");
                        friendCount.setText("+"+prayer.friends.size());
                        scopeImage.setImageResource(R.drawable.ic_publish_friend);
                        break;
                    case "S":
                        scope.setText("나만보기");
                        friendCount.setText("");
                        scopeImage.setImageResource(R.drawable.ic_publish_lock);
                        break;
                }
            }
        });


    }



    private void addMedia(final Media media,int id){


        View itemView  = getLayoutInflater().inflate(R.layout.layout_prayer_image, null);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.zoom(media, PrayerDetailActivity.this);
            }
        });

        mediaList.addView(itemView);

        Util.setMediaView(itemView, media, true, mediaList.getMeasuredWidth());

        if(id == position){
            itemView.requestFocus();
        }

    }

    private void addReply(final Reply reply,int index){
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View itemView = mLayoutInflater.inflate(R.layout.layout_prayer_image, mediaList);
        final View itemView  = getLayoutInflater().inflate(R.layout.fragment_reply, null);

        TextView text = (TextView) itemView.findViewById(R.id.text);
        TextView name = (TextView) itemView.findViewById(R.id.name);
        TextView time = (TextView) itemView.findViewById(R.id.time);

        ImageView picture = (ImageView) itemView.findViewById(R.id.picture);
        ImageView cover = (ImageView) itemView.findViewById(R.id.cover);
        ImageView image = (ImageView) itemView.findViewById(R.id.image);
        final ImageView popup = (ImageView) itemView.findViewById(R.id.popup);

        LinearLayout container = (LinearLayout) itemView.findViewById(R.id.container);


        Util.setPicture(reply.userPicture, picture);




        name.setText(reply.name);
        text.setText(reply.text);
        time.setText(reply.time);

        if (!"R".equals(reply.type)) {
            //LinearLayout con = (LinearLayout) itemLayoutView.findViewById(R.id.container);
            TextView response = (TextView) itemView.findViewById(R.id.response);
            response.setVisibility(View.GONE);
            //con.removeView(response);
            cover.setImageResource(R.drawable.profile_cover_gray_small);
        }

        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReplyPopupMenu(reply, popup, itemView);
            }
        });

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (reply.userId == Global.me.id) {
                    intent = new Intent(PrayerDetailActivity.this, MyProfileActivity.class);
                } else {
                    intent = new Intent(PrayerDetailActivity.this, UserProfileActivity.class);
                    intent.putExtra("userId", reply.userId);
                }
                startActivity(intent);

            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("R".equals(reply.type)) {
                    responseList(reply);
                }

            }
        });


        replyList.addView(itemView);


        if ("null".equals(reply.picture) || reply.picture == null || "0".equals(reply.picture)) {
            image.setVisibility(View.GONE);
        } else {

            new DownloadImageTask(image).execute(reply.picture);

            // int height = (int) ((float) c.getWidth() / (float) bmp.getWidth()) * bmp.getHeight();
            // Log.i("media", c.getWidth() + ", " + bmp.getWidth() + "," + bmp.getHeight());

            //  image.getLayoutParams().height = height;
            // image.requestLayout();
        }





/*        LinearLayout replyList = (LinearLayout) findViewById(R.id.reply_list);

        LinearLayout rowLayout = new LinearLayout(PrayerDetailActivity.this);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        rowLayout.setId(View.generateViewId());

// add rowLayout to the root layout somewhere here

        ReplyFragment replyFragment = new ReplyFragment();
        replyFragment.setReply(reply);

//                    imageFragment.setUri(mUrls[i]);
        fragTransaction.add(rowLayout.getId(), replyFragment, "fragment" + index);
        fragTransaction.commit();

        replyList.addView(rowLayout);*/

    }

    class DownloadImageTask extends AsyncTask<String, Void, String> {


        private ImageView view;
        private Bitmap bmp;

        public DownloadImageTask(ImageView view){
            this.view = view;
        }

        @Override
        protected String doInBackground(String... params) {
            bmp = Util.fetchImage(params[0]);
            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            view.setImageBitmap(bmp);
            view.setAlpha(1.0f);

            if(bmp!=null) {


            }
        }
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

        RestService.pray(prayer, new CallBack<Response>() {
            @Override
            public void success(Response result) {
                refresh();
            }
        });

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

            load();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {



        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.PHOTO:
                    Util.upload(data.getData(), getContentResolver(), new PictureCallBack() {
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
                            imageFragment.setId(id);
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



    protected void showReplyPopupMenu(final Reply reply, ImageView popup,final View itemView){
        PopupMenu popupMenu = new PopupMenu(PrayerDetailActivity.this, popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_reply_popup, popupMenu.getMenu());


        if(reply.userId == Global.me.id){
            popupMenu.getMenu().findItem(R.id.message).setVisible(false);
            popupMenu.getMenu().findItem(R.id.inform).setVisible(false);
        }else {
            if (prayer.userId != Global.me.id ) {
                popupMenu.getMenu().findItem(R.id.delete).setVisible(false);
            }
        }



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.message: {
                        Intent intent = new Intent(PrayerDetailActivity.this, MessageActivity.class);
                        Friend friend = new Friend();
                        friend.name = reply.name;
                        friend.picture = reply.userPicture;
                        friend.friend = reply.userId;
                        intent.putExtra("friend", friend);
                        startActivityForResult(intent, Global.MESSAGE);
                    }
                    break;
                    case R.id.copy: {
                        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) PrayerDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboard.setText(reply.text);
                        } else {
                            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) PrayerDetailActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", reply.text);
                            clipboard.setPrimaryClip(clip);
                        }
                    }
                    break;
                    case R.id.delete: {
                        Util.rest("prayer/reply","DELETE",reply);
                        replyList.removeView(itemView);
                    }
                    break;
                    case R.id.inform: {

                        Search search = new Search();
                        search.searchKey = reply.text;
                        search.searchId = reply.userId;
                        Util.rest("etc/inform","POST",search);

                        Common.toast(PrayerDetailActivity.this, "신고되었습니다.");

                    }
                    break;

                }
                return true;
            }
        });

        popupMenu.show();
    }
}
