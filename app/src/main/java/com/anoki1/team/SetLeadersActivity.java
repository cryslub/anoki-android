package com.anoki1.team;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.DoneState;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.MemberViewHolderBase;
import com.anoki1.common.SelectedAdapter;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.pojo.Friend;
import com.anoki1.pojo.Invite;
import com.anoki1.pojo.Member;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Search;
import com.anoki1.pojo.Team;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;


class SetLeaderViewHolder extends MemberViewHolderBase {

    public SetLeaderViewHolder(View itemView) {
        super(itemView);
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

        backGroundRest("team/members", "POST", search, listType, new CallBack<List<Member>>() {
            @Override
            public void success(List<Member> memberList) {
                memberAdapter = new GeneralRecyclerViewAdapter<Member, SetLeaderViewHolder>(memberList, R.layout.layout_set_leader_row, SetLeaderViewHolder.class, SetLeadersActivity.this);

                setRecyclerView(SetLeadersActivity.this.memberList, memberAdapter);

                GridView listView = (GridView) findViewById(R.id.selected_list);
                selectedAdapter = new SelectedAdapter(SetLeadersActivity.this, android.R.layout.simple_list_item_1, new ArrayList<Friend>());
                // 4. set adapter
                listView.setAdapter(selectedAdapter);
            }
        });




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

//        rest("team/set/leaders","POST",invite);
        backGroundRest("team/set/leaders", "POST", invite, Response.class, new CallBack<Response>() {

            @Override
            public void success(Response response) {
            }
        });


        succeed();
    }
}
