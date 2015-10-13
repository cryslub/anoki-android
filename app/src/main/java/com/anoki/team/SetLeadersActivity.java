package com.anoki.team;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.common.DoneState;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.RestService;
import com.anoki.common.SelectedAdapter;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Invite;
import com.anoki.pojo.Member;
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


class SetLeaderViewHolder extends ViewHolderBase<Member> {

    public View view;

    @Nullable
    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;

    Member member;
    Activity activity;

    public SetLeaderViewHolder(View itemView) {
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


    public void attach(Activity activity){
        this.activity = activity;
    }

    @OnClick(R.id.choose)
    public void select(){
        ((SetLeadersActivity) activity).select(member);
    }

}


public class SetLeadersActivity extends WriteActivityBase {


    private Map<Integer,Friend> selectionMap = new HashMap<Integer,Friend>();


    SelectedAdapter selectedAdapter;

    Team team;

    @Bind(R.id.member_list)
    RecyclerView memberList;

    GeneralRecyclerViewAdapter<Member,SetLeaderViewHolder> memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_leaders);


        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");

        setMemberList();
    }



    private void setMemberList(){

        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId = team.id;
        search.searchKey = "J";
        search.searchType = "setLeader";

        List<Member> memberList = rest("team/members","POST",search,listType);


        memberAdapter = new GeneralRecyclerViewAdapter<Member,SetLeaderViewHolder>(memberList,R.layout.layout_set_leader_row,SetLeaderViewHolder.class,this);

        setRecyclerView(this.memberList, memberAdapter);

        GridView listView = (GridView) findViewById(R.id.selected_list);
        selectedAdapter = new SelectedAdapter(this, android.R.layout.simple_list_item_1, new ArrayList<Friend>());
        // 4. set adapter
        listView.setAdapter(selectedAdapter);

    }

    public void select(Member member){

        Friend friend = new Friend();
        friend.friend = member.user;
        friend.name = member.name;
        friend.picture = member.picture;

        if (selectionMap.get(friend.friend) == null) {
            selectionMap.put(friend.friend, friend);
        } else {
            selectionMap.remove(friend.friend);
        }

        selectedAdapter.updateList(new ArrayList<Friend>(selectionMap.values()));

        doneStateCheck();

    }



    public void doneStateCheck(){
        if(selectionMap.size() == 0 ) {
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }

    protected void confirm(){

        Invite invite = new Invite();
        invite.id = team.id;
        invite.phone = new ArrayList<Friend>(selectionMap.values());

        rest("team/set/leaders","POST",invite);

        succeed();
    }
}
