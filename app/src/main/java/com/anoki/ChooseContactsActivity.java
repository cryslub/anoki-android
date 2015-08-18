package com.anoki;

import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.ContactManage;
import com.anoki.common.DoneState;
import com.anoki.common.FriendViewHolder;
import com.anoki.common.Global;
import com.anoki.common.Initial;
import com.anoki.common.RestService;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class ChooseContactsActivity extends WriteActivityBase {

    private Map<Integer,Friend> selectionMap = new HashMap<Integer,Friend>();
    private Map<String,Integer> contactSelectionMap = new HashMap<String,Integer>();

    private TabHost myTabHost;

    private  ContentResolver contentResolver;

    private Prayer prayer ;

    FriendsAdapter friendsAdapter;
    ContactsAdapter contactsAdapter;

    SelectedAdapter selectedAdapter;

    private Dialog billingDialog;


    @Bind(R.id.search_key_contact)
    EditText searchKeyContact;


    @Nullable @OnTextChanged(R.id.search_key_contact)
    void searchContact(){
        contactsAdapter.setFilter(searchKeyContact.getText().toString());
    }


    protected void setFilter(){
        friendsAdapter.setFilter(searchKey.getText().toString());
    }

    public static final int EX_DIALOG = 100;
    public static final int CHARGE_DIALOG = 110;


    public static final String[] PHOTO_BITMAP_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Photo.PHOTO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contacts);

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

        int height = Util.dpToPixel(getApplicationContext(), 40);

        ((ImageView)((LinearLayout)myTabHost.getTabWidget().getChildTabViewAt(0)).getChildAt(0)).setMaxHeight(height);
        ((ImageView)((LinearLayout)myTabHost.getTabWidget().getChildTabViewAt(1)).getChildAt(0)).setMaxHeight(height);
        myTabHost.getTabWidget().getChildAt(0).getLayoutParams().height = height;
        myTabHost.getTabWidget().getChildAt(1).getLayoutParams().height = height;

        TextView tv = (TextView) myTabHost.getTabWidget().getChildAt(0).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextSize(18);

        tv = (TextView) myTabHost.getTabWidget().getChildAt(1).findViewById(android.R.id.title); //Unselected Tabs
        tv.setTextSize(18);
    }


    private void setFriendList(){

        List<Friend> friendList = RestService.getFriendList();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        friendsAdapter = new FriendsAdapter(friendList);
        // 4. set adapter
        recyclerView.setAdapter(friendsAdapter);


        ListView listView = (ListView) findViewById(R.id.selected_list);
        selectedAdapter = new SelectedAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Friend>());
        // 4. set adapter
        listView.setAdapter(selectedAdapter);

    }

    private void setContactList(){

        List <Object> contactList = getContactList();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.contact_list);
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 3. create an adapter
        contactsAdapter = new ContactsAdapter(contactList);
        // 4. set adapter
        recyclerView.setAdapter(contactsAdapter);
    }

    private List <Object> getContactList(){


        List<Friend> addList= ContactManage.getUnfriendContact(getContentResolver(), getApplicationContext());


        System.out.println("add list size - " + addList.size());

        List <Object> contactList = new ArrayList < Object> ();

        int initialIndex = -1;

        for(Friend friend : addList){


            char first = friend.name.charAt(0);
            if(Initial.isHangul(first)) {
                int index =Initial.getInitialSound(first);
                if(index >initialIndex){
                    contactList.add(Initial.INITIAL_SOUND[index]+"");
                    initialIndex = index;
                }
            }

            contactList.add(friend);
        }
        System.out.println("contactList size - " + contactList.size());

        return contactList;
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


    @Override
    protected void confirm() {
        if(selectionMap.size() + contactSelectionMap.size() > Global.FREE_FRIENDS_COUNT){
            int total = selectionMap.size() + contactSelectionMap.size();
            final int ex = total -Global.FREE_FRIENDS_COUNT;


            showDialog(EX_DIALOG);

        }else {
            RestService.makePrayer(prayer);
            succeed();

        }
    }

    public void done(MenuItem item){

        prayer.friends = new ArrayList<Integer>();
        prayer.friends.addAll(selectionMap.keySet());

        prayer.phone = new ArrayList<String>();
        prayer.phone.addAll(contactSelectionMap.keySet());


        super.done(item);
    }


    public Dialog onCreateDialog(int id) {

        int total = selectionMap.size() + contactSelectionMap.size();
        final int ex = total -Global.FREE_FRIENDS_COUNT;


        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.layout_billing_dialog);

        WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
        lp.dimAmount = 0.7f;

        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        TextView text = (TextView)dialog.findViewById(R.id.text);
        TextView yes = (TextView)dialog.findViewById(R.id.yes);
        TextView no = (TextView)dialog.findViewById(R.id.no);
        TextView exText = (TextView)dialog.findViewById(R.id.ex);
        TextView amount = (TextView)dialog.findViewById(R.id.amount);

        exText.setText(ex+"명");
        amount.setText((ex * 10) + "원");


        switch (id){
            case EX_DIALOG:
                text.setText("함께 기도하는 친구가 10명초과로 비용이 발생합니다");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        bill();
                    }
                });

                break;
            case CHARGE_DIALOG:
                text.setText("충전된 금액이 부족합니다. 결재를 진행하시겠습니까?");
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        charge();
                    }
                });

                break;
        }


        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @OnClick(R.id.yes)
    void bill(){

        int total = selectionMap.size() + contactSelectionMap.size();
        final int ex = total -Global.FREE_FRIENDS_COUNT;


        if(ex*10 > Global.me.dalant){

            showDialog(CHARGE_DIALOG);
        }else {

            billing();
        }

    }

    private void charge(){
        Intent intent = new Intent(ChooseContactsActivity.this, ChargeActivity.class);
        intent.putExtra("prayer", prayer);
        startActivityForResult(intent, Global.PAY);
    }


    private void billing(){
        Intent intent = new Intent(ChooseContactsActivity.this, BillingActivity.class);

        intent.putExtra("prayer", prayer);
        startActivityForResult(intent, Global.PAY);
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


    private class FriendsAdapter extends RecyclerView.Adapter<FriendViewHolder> {
        protected List<Friend> visibleObjects;
        protected List<Friend> allObjects;

        public FriendsAdapter(List<Friend> itemsData)
        {

            this.allObjects = itemsData;
            this.visibleObjects = itemsData;
        }

        public FriendsAdapter() {

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

        protected char getQueryCharacter(String queryText){
            char queryCharacter = ' ';
            if(queryText.length()>0){
                char c = queryText.charAt(0);
                if(Initial.isInitialSound(queryCharacter)){
                    queryCharacter = c;
                }
            }

            return queryCharacter;
        }

        public void setFilter(String queryText) {

            char queryCharacter = getQueryCharacter(queryText);

            visibleObjects = new ArrayList<Friend>();
            boolean isInitial = false;
            for (Friend item: allObjects) {
                if(checkFilter(queryText,queryCharacter,item))
                    visibleObjects.add(item);


            }
            notifyDataSetChanged();
        }


        protected boolean checkFilter(String queryText,char queryCharacter,Friend item){
            if(queryCharacter == ' '){

                if (item.name.contains(queryText) || item.phone.contains(queryText)) {
                    return true;
                }

            }else {
                char c = item.name.charAt(0);
                if(queryCharacter == Initial.INITIAL_SOUND[Initial.getInitialSound(c)]){
                    return true;
                }
            }

            return false;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_friend_row, null);

            // create ViewHolder

            FriendViewHolder viewHolder = new FriendViewHolder(itemLayoutView);
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(FriendViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Friend friend = visibleObjects.get(position);

            viewHolder.bind(friend);

            viewHolder.choose.setOnClickListener(new CheckBox.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectionMap.get(friend.friend) == null) {
                        selectionMap.put(friend.friend, friend);
                    } else {
                        selectionMap.remove(friend.friend);
                    }

                    selectedAdapter.updateList(new ArrayList<Friend>(selectionMap.values()));

                    doneStateCheck();
                }

            });

        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {
///            System.out.println(visibleObjects.size());
            if(visibleObjects !=null)
               return visibleObjects.size();
            else return  0;
        }

    }



    private class ContactsAdapter extends FriendsAdapter {

        protected List<Object> visibleObjects;
        protected List<Object> allObjects;

        private final int SECTION = 1;
        private final int CONTACT = 2;

        public ContactsAdapter(List<Object> itemsData) {
            super();

            this.allObjects = itemsData;
            this.visibleObjects = itemsData;
        }


        public void flushFilter(){
            visibleObjects=new ArrayList<>();
            visibleObjects.addAll(allObjects);
            notifyDataSetChanged();
        }

        public void setFilter(String queryText) {


            char queryCharacter = getQueryCharacter(queryText);


            visibleObjects = new ArrayList<>();
            for (Object item: allObjects) {
                if(item instanceof Friend) {
                    Friend friend = (Friend) item;

                    if(checkFilter(queryText,queryCharacter,friend))
                        visibleObjects.add(item);


                }
            }
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            // Just as an example, return 0 or 2 depending on position
            // Note that unlike in ListView adapters, types don't have to be contiguous

            final Object obj = visibleObjects.get(position);


            if(obj instanceof  Friend){
                return CONTACT;
            }else{
                return SECTION;
            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public FriendViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            switch (viewType){
                case CONTACT: {
                    View itemLayoutView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_contact_row, null);

                    // create ViewHolder

                    FriendViewHolder viewHolder = new FriendViewHolder(itemLayoutView);
                    return viewHolder;
                }
                case SECTION:
                    View itemLayoutView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.layout_contact_section_row, null);

                    // create ViewHolder

                    FriendViewHolder viewHolder = new FriendViewHolder(itemLayoutView);
                    return viewHolder;

            }

            return null;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final FriendViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Object obj = visibleObjects.get(position);

            if(obj instanceof  Friend){
                final Friend friend = (Friend) obj;

                viewHolder.name.setText(friend.name);

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
                    viewHolder.picture.setImageDrawable(getResources().getDrawable(R.drawable.ic_person_black_36dp));
                    viewHolder.picture.setAlpha(.5f);
                }
            }else if(obj instanceof  String){
                String header = (String) obj;
                viewHolder.name.setText(header);
            }


        }

        public int getItemCount() {
///            System.out.println(visibleObjects.size());
            if(visibleObjects !=null)
                return visibleObjects.size();
            else return  0;
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


    private class SelectedAdapter extends ArrayAdapter<Friend> {
        protected List<Friend> visibleObjects;
        protected List<Friend> allObjects;

        private final Context context;

        public SelectedAdapter(Context context, int resource, List<Friend> objects) {
            super(context, resource, objects);
            this.allObjects = objects;
            this.visibleObjects = objects;
            this.context = context;

        }


        public void updateList(List<Friend> itemsData){
            clear();
            addAll(itemsData);
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

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.layout_selected_friend_row, parent, false);


            TextView name = (TextView) rowView.findViewById(R.id.name);
            ImageView picture = (ImageView) rowView.findViewById(R.id.picture);


            final Friend friend = visibleObjects.get(position);

            Util.setPicture(friend.picture,picture);
            name.setText(visibleObjects.get(position).name);


            return rowView;
        }

    }


}
