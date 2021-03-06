package com.anoki1.etc;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentUris;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.Toast;

import com.anoki1.R;
import com.anoki1.common.AnokiDialog;
import com.anoki1.common.BillingDialog;
import com.anoki1.common.CallBack;
import com.anoki1.common.ContactManage;
import com.anoki1.common.DoneState;
import com.anoki1.common.FriendViewHolder;
import com.anoki1.common.Global;
import com.anoki1.common.Initial;
import com.anoki1.common.RestService;
import com.anoki1.common.SelectedAdapter;
import com.anoki1.common.Util;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.fragment.SearchFragment;
import com.anoki1.pojo.Team;
import com.anoki1.team.TeamDetailActivity;
import com.anoki1.user.UserProfileActivity;
import com.anoki1.pojo.DialogData;
import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Invite;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class ChooseContactsActivity extends WriteActivityBase implements SearchFragment.OnFragmentInteractionListener{

    private Map<Integer,Friend> selectionMap = new HashMap<Integer,Friend>();
    private Map<String,Integer> contactSelectionMap = new HashMap<String,Integer>();

    @Bind(R.id.tabHost)
    TabHost myTabHost;

    private  ContentResolver contentResolver;

    private Prayer prayer ;
    private String type;
    private int blockId;

    FriendsAdapter friendsAdapter;
    ContactsAdapter contactsAdapter;

    SelectedAdapter selectedAdapter;

    private Dialog billingDialog;


    @Bind(R.id.search_key_contact)
    EditText searchKeyContact;

    Team team;


    @Nullable @OnTextChanged(R.id.search_key_contact)
    void searchContact(){
        contactsAdapter.setFilter(searchKeyContact.getText().toString());
    }


    protected void setFilter(){
        if(friendsAdapter != null) {
            friendsAdapter.setFilter(searchKey.getText().toString());
        }
    }

    public static final int EX_DIALOG = 100;
    public static final int CHARGE_DIALOG = 110;
    public static final int BLOCK_DIALOG = 120;


    public static final String[] PHOTO_BITMAP_PROJECTION = new String[] {
            ContactsContract.CommonDataKinds.Photo.PHOTO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_contacts);

        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");
        type = intent.getStringExtra("type");


        setTab(myTabHost, new String[]{"친구", "초대하기"}, new int[]{R.id.friend, R.id.contact});
        setFriendList();
        setContactList();


        if("info".equals(type)) {
            getSupportActionBar().setTitle("친구정보");
        }else if("team".equals(type) || "invite".equals(type)){


            getSupportActionBar().setTitle("그룹원 초대");
            team = (Team) intent.getSerializableExtra("team");
            doneState = DoneState.DONE;


        }
    }





    private void setFriendList(){

        RestService.getFriendList(new CallBack<ArrayList<Friend>>() {
            @Override
            public void success(ArrayList<Friend> friendList) {
                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.friend_list);
                // 2. set layoutManger
                recyclerView.setLayoutManager(new LinearLayoutManager(ChooseContactsActivity.this));
                // 3. create an adapter
                friendsAdapter = new FriendsAdapter(friendList);
                // 4. set adapter
                recyclerView.setAdapter(friendsAdapter);


                GridView listView = (GridView) findViewById(R.id.selected_list);
                selectedAdapter = new SelectedAdapter(ChooseContactsActivity.this, android.R.layout.simple_list_item_1, new ArrayList<Friend>());
                // 4. set adapter
                listView.setAdapter(selectedAdapter);

            }
        });


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
        if(selectionMap.size() == 0 && contactSelectionMap.size() == 0 && !"team".equals(type)) {
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }


    private Invite makeInvite(){
        Invite invite = new Invite();

        for(String contact : contactSelectionMap.keySet()){
            Friend friend = new Friend();
            friend.phone = contact;
            invite.phone.add(friend);
        }
        return invite;
    }

    @Override
    protected void confirm() {
        if("info".equals(type)){
            backGroundRest("friend", "POST", makeInvite(), Invite.class, new CallBack<Invite>() {
                @Override
                public void success(Invite result) {

                }
            });
        }else if("team".equals(type) ){

            Invite invite = makeInvite();
            invite.team = team.id;
            invite.phone.addAll(selectionMap.values());

            backGroundRest("team/invite", "POST", invite, Invite.class, new CallBack<Invite>() {
                @Override
                public void success(Invite result) {
                    Intent intent = new Intent(ChooseContactsActivity.this, TeamDetailActivity.class);
                    intent.putExtra("teamId", team.id);
                    startActivity(intent);

                }
            });


        }else if("invite".equals(type) ){

            Invite invite = makeInvite();
            invite.team = team.id;
            invite.friends = new ArrayList<Integer> ();
            for(Friend friend: selectionMap.values()){
                invite.friends.add(friend.friend);
            }
//            invite.phone.addAll(selectionMap.values());

            backGroundRest("team/invite", "POST", invite, Invite.class, new CallBack<Invite>() {
                @Override
                public void success(Invite result) {
                    succeed();

                }
            });



        } else {

            prayer.friends = new ArrayList<Friend>();
            for(Integer id : selectionMap.keySet()){
                Friend friend = new Friend();
                friend.friend = id;
                prayer.friends.add(friend);
            }

            prayer.phone = new ArrayList<String>();
            prayer.phone.addAll(contactSelectionMap.keySet());


            if (selectionMap.size() + contactSelectionMap.size() > Global.FREE_FRIENDS_COUNT) {

                int total = selectionMap.size() + contactSelectionMap.size();
                final int ex = total - Global.FREE_FRIENDS_COUNT;

                showDialog(EX_DIALOG);

            } else {
                RestService.makePrayer(prayer, new CallBack<Response>() {
                    @Override
                    public void success(Response result) {
                        succeed();

                    }
                });

            }
        }

        if(contactSelectionMap.size() > 0){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "초대하였습니다.", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void done(MenuItem item){



        super.done(item);
    }


    public Dialog onCreateDialog(int id) {

        int total = selectionMap.size() + contactSelectionMap.size();
        final int ex = total -Global.FREE_FRIENDS_COUNT;


        Dialog dialog = null;
        DialogData data = new DialogData();


        switch (id){
            case EX_DIALOG: {

                data.ex = ex;
                data.text = "함께 기도하는 친구가 10명초과로 비용이 발생합니다";

                data.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bill();
                    }
                };


                dialog = new BillingDialog(this,data);

            }
                break;
            case CHARGE_DIALOG:
                data.title = "결제";
                data.text = "충전된 금액이 부족합니다. 결제를 진행하시겠습니까?";


                data.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        charge();
                    }
                };


                dialog = new AnokiDialog(this,data);

                break;
            case BLOCK_DIALOG:
                data.title="차단";
                data.text = "차단하시겠습니까? 차단하면 차단한 친구가 보내는 기도요청을 받을 수 없으며, 친구목록에서 삭제됩니다.\n" +
                        "(차단여부는 상대방이 알수 없습니다.)";
                data.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        block();
                    }
                };


                dialog = new AnokiDialog(this,data);

                break;
        }



        return dialog;
    }

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

    private void block(){
        Friend friend = new Friend();
        friend.id = blockId;
        friend.state="B";
        Util.backGroundRest("friend", "PUT", friend, Response.class, new CallBack<Response>() {
            @Override
            public void success(Response result) {
                load();

            }
        });
 //       Util.rest("friend", "PUT", friend, Response.class);

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

                    RestService.makePrayer(prayer, new CallBack<Response>() {
                        @Override
                        public void success(Response result) {
                            succeed();

                        }
                    });
                    break;
                default:
                    succeed();
            }
        }else{
            succeed();

        }
    }

    @Override
    public void onSearch(String key) {

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
        public void onBindViewHolder(final FriendViewHolder viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final Friend friend = visibleObjects.get(position);

            viewHolder.bind(friend);

            if("info".equals(type)){

                viewHolder.block.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        blockId = friend.id;
                        showDialog(BLOCK_DIALOG);
                    }
                });
                viewHolder.view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(viewHolder.view.getContext(), UserProfileActivity.class);
                        intent.putExtra("userId", friend.friend);
                        viewHolder.view.getContext().startActivity(intent);
                    }
                });
                viewHolder.choose.setVisibility(View.GONE);
                viewHolder.block.setVisibility(View.VISIBLE);

            }else {
                viewHolder.choose.setVisibility(View.VISIBLE);
                viewHolder.block.setVisibility(View.GONE);


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
                    if(bmp != null) {
                        viewHolder.picture.setImageBitmap(bmp);
//                        viewHolder.picture.setAlpha(1.0f);
                    }
                }else{
                    viewHolder.picture.setImageDrawable(getResources().getDrawable(R.drawable.profile_large));
 //                   viewHolder.picture.setAlpha(.5f);
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
        if(contentResolver != null) {
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
            } finally {
                cursor.close();
            }
        }
        return null;
    }



}
