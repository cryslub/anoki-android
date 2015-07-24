package com.anoki;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseFriendsActivity extends SubActivityBase {

    private Map<Integer,Integer> selectionMap = new HashMap<Integer,Integer>();
    private Map<String,Integer> contactSelectionMap = new HashMap<String,Integer>();

    private TabHost myTabHost;

    private  ContentResolver contentResolver;

    private Prayer prayer ;

    private static final String[] PHOTO_BITMAP_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Photo.PHOTO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friends);

        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");

        setTab();


        setFriendList();
        setContactList();

    }



    private void setTab(){
        myTabHost =(TabHost) findViewById(R.id.tabHost); // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup(); // Adding tabs // tab1 settings
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("친구").setContent(R.id.friend));
        myTabHost.addTab(myTabHost.newTabSpec("tab_creation").setIndicator("초대하기").setContent(R.id.contact));

    }


    private void setFriendList(){
        Type listType = new TypeToken<ArrayList<Friend>>() {}.getType();


        final Search search = new Search();
        search.apiKey = Global.apiKey;
        search.searchKey = "A";

        List<Friend> friendList = Util.rest("friend/list", "POST", search, listType);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final FriendsAdapter friendsAdapter = new FriendsAdapter(friendList);
        // 4. set adapter
        recyclerView.setAdapter(friendsAdapter);

        final EditText searchFriend = (EditText) findViewById(R.id.search_friend);
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                friendsAdapter.setFilter(searchFriend.getText().toString());
            }
        });

    }

    private void setContactList(){

        List <Friend> contactList = new ArrayList < Friend> ();

        contentResolver  = getContentResolver();
        Cursor cur = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if (Integer.parseInt(cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        Friend friend = new Friend();
                        friend.phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        friend.name = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        friend.picture = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_ID)) + "";

                        contactList.add(friend);
                    }
                    pCur.close();
                }
            }
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contact_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        final ContactsAdapter friendsAdapter = new ContactsAdapter(contactList);
        // 4. set adapter
        recyclerView.setAdapter(friendsAdapter);

        final EditText searchFriend = (EditText) findViewById(R.id.search_contact);
        searchFriend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                friendsAdapter.setFilter(searchFriend.getText().toString());
            }
        });
    }


    public void doneStateCheck(){
        if(selectionMap.size() == 0 && contactSelectionMap.size() == 0 ) {
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }


    public void done(MenuItem item){

        prayer.friends = new ArrayList<Integer>();
        prayer.friends.addAll(selectionMap.keySet());

        prayer.phone = new ArrayList<String>();
        prayer.phone.addAll(contactSelectionMap.keySet());


        switch (doneState){
            case CLEAR:
                onBackPressed();
                break;
            case DONE:
            {
                if(selectionMap.size() + contactSelectionMap.size() > Global.FREE_FRIENDS_COUNT){
                    int total = selectionMap.size() + contactSelectionMap.size();
                    final int ex = total -Global.FREE_FRIENDS_COUNT;
                    new AlertDialog.Builder(this)
                            .setIcon(R.drawable.ic_info_black_24dp)
                            .setTitle("결제")
                            .setMessage("함께 기도하는 친구가 10명초과로 비용이 발생합니다.\r\n (아노키는 수익이 아닌 스팸방지를 위해 과금을 합니다.) \r\n초과친구 "+ex+"명/ 결제금액 "+ex*100+"원 결제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    bill(ex);
                                }

                            })
                            .setNegativeButton("아니오", null)
                            .show();
                }else {

                    Intent intent = Util.inviteIntent(prayer);
                    startActivityForResult(intent, Global.SMS);

                }
            }
            break;

        }
    }


    private  void bill(int ex){

        if(ex*100 > Global.me.dalant){

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_info_black_24dp)
                    .setTitle("결제")
                    .setMessage("충전된 금액이 부족합니다. 결재를 진행하시겠습니까?\r\n (아노키는 수익이 아닌 스팸방지를 위해 과금을 합니다.) \r\n초과친구 "+ex+"명/ 결제금액 "+ex*100+"원 결제하시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ChooseFriendsActivity.this, ChargeActivity.class);
                            intent.putExtra("prayer", prayer);
                            startActivityForResult(intent, Global.PAY);
                        }

                    })
                    .setNegativeButton("아니오", null)
                    .show();
        }else {

            Intent intent = new Intent(ChooseFriendsActivity.this, BillingActivity.class);
            intent.putExtra("prayer", prayer);
            startActivityForResult(intent, Global.PAY);

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case Global.SMS:

                    RestService.makePrayer(prayer);
                    break;
            }
            succeed();
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView name;
        CheckBox choose;
        TextView phone;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);
            name = (TextView) itemLayoutView.findViewById(R.id.name);
            choose = (CheckBox) itemLayoutView.findViewById(R.id.choose);
            phone = (TextView) itemLayoutView.findViewById(R.id.phone);
        }
    }

    private class FriendsAdapter extends RecyclerView.Adapter<ViewHolder> {
        protected List<Friend> visibleObjects;
        protected List<Friend> allObjects;

        public FriendsAdapter(List<Friend> itemsData)
        {

            this.allObjects = itemsData;
            this.visibleObjects = itemsData;
        }

        public void updateList(List<Friend> itemsData){
            this.allObjects = itemsData;
            flushFilter();
        }

        public void flushFilter(){
            visibleObjects=new ArrayList<>();
            visibleObjects.addAll(allObjects);
            notifyDataSetChanged();
        }

        public void setFilter(String queryText) {

            visibleObjects = new ArrayList<>();
            for (Friend item: allObjects) {
                if(item.name.contains(queryText) || item.phone.contains(queryText))
                    visibleObjects.add(item);
            }
            notifyDataSetChanged();
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_friend_row, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Friend friend = visibleObjects.get(position);


            Util.setPicture(friend.picture,viewHolder.picture,getDrawable(R.drawable.ic_person_black_36dp));


            viewHolder.name.setText(visibleObjects.get(position).name);

            viewHolder.choose.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectionMap.get(friend.friend)==null){
                        selectionMap.put(friend.friend,1);
                    }else{
                        selectionMap.remove(friend.friend);
                    }
                    doneStateCheck();
                }

            });

        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {
            System.out.println(visibleObjects.size());
            return visibleObjects.size();
        }

    }



    private class ContactsAdapter extends FriendsAdapter {

        public ContactsAdapter(List<Friend> itemsData) {
            super(itemsData);
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_contact_row, null);

            // create ViewHolder

            ViewHolder viewHolder = new ViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Friend friend = visibleObjects.get(position);

            viewHolder.name.setText(visibleObjects.get(position).name);

            viewHolder.choose.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (contactSelectionMap.get(friend.phone) == null) {
                        contactSelectionMap.put(friend.phone, 1);
                    } else {
                        contactSelectionMap.remove(friend.phone);
                    }
                    doneStateCheck();
                }

            });


            viewHolder.phone.setText(friend.phone);

            System.out.println(friend.picture);
            if(!"null".equals(friend.picture) &&!"0".equals(friend.picture)) {
                Bitmap bmp = fetchThumbnail(Integer.parseInt(friend.picture));
                viewHolder.picture.setImageBitmap(bmp);
                viewHolder.picture.setAlpha(1.0f);
            }else{
                viewHolder.picture.setImageDrawable(getDrawable(R.drawable.ic_person_black_36dp));
                viewHolder.picture.setAlpha(.5f);
            }
        }



    }

    final Bitmap fetchThumbnail(final int thumbnailId) {

        final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, thumbnailId);
        final Cursor cursor = contentResolver.query(uri, PHOTO_BITMAP_PROJECTION, null, null, null);

        try {
            Bitmap thumbnail = null;
            if (cursor.moveToFirst()) {
                final byte[] thumbnailBytes = cursor.getBlob(0);
                if (thumbnailBytes != null) {
                    thumbnail = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
                }
            }
            return thumbnail;
        }
        finally {
            cursor.close();
        }

    }


}
