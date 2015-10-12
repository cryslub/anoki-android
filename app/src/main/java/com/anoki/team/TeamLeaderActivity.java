package com.anoki.team;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Member;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
import com.anoki.team.TeamAuthActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;


class LeaderViewHolder extends ViewHolderBase<Member> {


    public View view;

    @Nullable
    @Bind(R.id.picture)
    public ImageView picture;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.role)
    public TextView role;




    @Nullable
    @Bind(R.id.phone) public TextView phone;

    Member member;

    public LeaderViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Member member) {

        if(picture !=null)
            Util.setPicture(member.picture, picture);
        name.setText(member.name);

        this.member = member;

        switch (member.role){
            case "3":
                role.setText("책임청지기");
                break;
            case "2":
                role.setText("청지기");
                break;

        }
    }


}


public class TeamLeaderActivity extends SubActivityBase {


    Team team;



    @Bind(R.id.leader_list)
    RecyclerView recyclerView;

    GeneralRecyclerViewAdapter<Member,LeaderViewHolder> leaderAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_leader);

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");
    }

    public void load(){
        setLeaderList();
    }


    private void setLeaderList(){

        Type listType = new TypeToken<ArrayList<Member>>() {}.getType();

        Search search = new Search();
        search.searchId = team.id;
        search.searchKey = "J";

        List<Member> leaderList = rest("team/leaders", "POST", search, listType);


        leaderAdapter = new GeneralRecyclerViewAdapter<Member,LeaderViewHolder>(leaderList,R.layout.layout_leader_row,LeaderViewHolder.class);

        setRecyclerView(this.recyclerView, leaderAdapter);

    }

    @OnClick(R.id.leader)
    void leader(){
        Intent intent = new Intent(this, SetLeadersActivity.class);
        intent.putExtra("team",team);
        startActivity(intent);
    }

    @OnClick(R.id.auth)
    void auth(){
        Intent intent = new Intent(this, TeamAuthActivity.class);
        intent.putExtra("team",team);
        startActivity(intent);
    }

}
