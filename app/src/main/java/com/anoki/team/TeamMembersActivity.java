package com.anoki.team;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.common.AnokiDialog;
import com.anoki.common.BillingDialog;
import com.anoki.common.ContactManage;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.Global;
import com.anoki.common.Initial;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.etc.BillingActivity;
import com.anoki.etc.ChargeActivity;
import com.anoki.fragment.SearchFragment;
import com.anoki.pojo.DialogData;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Invite;
import com.anoki.pojo.Member;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;



class MemberViewHolder extends ViewHolderBase<Member> {


    public View view;

    @Nullable @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Nullable @Bind(R.id.choose)
    public CheckBox choose;
    @Nullable @Bind(R.id.block)
    public ImageView block;


    @Nullable
    @Bind(R.id.phone) public TextView phone;

    Member member;

    public MemberViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Member member) {

        if(picture !=null)
            Util.setPicture(member.picture, picture);
        name.setText(member.name);

        this.member = member;
    }

    @Nullable @OnClick(R.id.block)
    public void block(){
        Util.rest("team/member","DELETE",member);

        RecyclerView recyclerView = (RecyclerView) view.getParent();
        GeneralRecyclerViewAdapter<Member,MemberViewHolder> recyclerviewAdapter
                = (GeneralRecyclerViewAdapter<Member,MemberViewHolder>) recyclerView.getAdapter();


        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId = member.team;
        search.searchKey = "J";
        List<Member> memberList = Util.rest("team/members", "POST", search, listType);

        recyclerviewAdapter.updateList(memberList);
    }


    @Nullable @OnClick(R.id.accept)
    public void accept(){
        member.state = "J";
        Util.rest("team/member","PUT",member);

        RecyclerView recyclerView = (RecyclerView) view.getParent();
        GeneralRecyclerViewAdapter<Member,MemberViewHolder> recyclerviewAdapter
                = (GeneralRecyclerViewAdapter<Member,MemberViewHolder>) recyclerView.getAdapter();


        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId = member.team;
        search.searchKey = "R";
        List<Member> memberList = Util.rest("team/members", "POST", search, listType);

        recyclerviewAdapter.updateList(memberList);
    }

}

public class TeamMembersActivity extends SubActivityBase implements SearchFragment.OnFragmentInteractionListener{


    private Map<Integer,Friend> selectionMap = new HashMap<Integer,Friend>();
    private Map<String,Integer> contactSelectionMap = new HashMap<String,Integer>();

    @Bind(R.id.tabHost)
    TabHost myTabHost;

    private ContentResolver contentResolver;

    private Prayer prayer ;
    private String type;
    private int blockId;


    private Dialog billingDialog;


    @Bind(R.id.search_key_contact)
    EditText searchKeyContact;

    Team team;

    int teamId;


    @Bind(R.id.member_list)
    RecyclerView memberList;

    @Bind(R.id.request_list)
    RecyclerView requestList;

    GeneralRecyclerViewAdapter<Member,MemberViewHolder> memberAdapter;
    GeneralRecyclerViewAdapter<Member,MemberViewHolder> requestAdapter;


    @Nullable
    @OnTextChanged(R.id.search_key_contact)
    void searchContact(){
        requestAdapter.setFilter(searchKeyContact.getText().toString());
    }


    protected void setFilter(){
        memberAdapter.setFilter(searchKey.getText().toString());
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
        setContentView(R.layout.activity_team_members);

        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId",-1);


        setTab(myTabHost, new String[]{"그룹원", "가입요청"}, new int[]{R.id.member, R.id.request});
        setMemberList();
        setRequestList();


    }


    private void setMemberList(){

        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId = teamId;
        search.searchKey = "J";

        List<Member> memberList = rest("team/members","POST",search,listType);


        memberAdapter = new  GeneralRecyclerViewAdapter<Member,MemberViewHolder> (memberList,R.layout.layout_member_row,MemberViewHolder.class);

        setRecyclerView(this.memberList, memberAdapter);

    }


    private void setRequestList(){

        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId = teamId;
        search.searchKey = "R";
        List<Member> memberList = rest("team/members","POST",search,listType);

        requestAdapter = new  GeneralRecyclerViewAdapter<Member,MemberViewHolder> (memberList,R.layout.layout_request_row,MemberViewHolder.class);

        setRecyclerView(this.requestList, requestAdapter);

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



    private Invite makeInvite(){
        Invite invite = new Invite();

        for(String contact : contactSelectionMap.keySet()){
            Friend friend = new Friend();
            friend.phone = contact;
            invite.phone.add(friend);
        }
        return invite;
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
        Intent intent = new Intent(this, ChargeActivity.class);
        intent.putExtra("prayer", prayer);
        startActivityForResult(intent, Global.PAY);
    }

    private void block(){
        Friend friend = new Friend();
        friend.id = blockId;
        friend.state="B";
        Util.rest("friend", "PUT", friend, Response.class);

        load();
    }

    private void billing(){
        Intent intent = new Intent(this, BillingActivity.class);

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

    @Override
    public void onSearch(String key) {

    }



}
