package com.anoki;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.BinderThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Member;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;

import butterknife.Bind;
import butterknife.OnClick;

public class TeamDetailActivity extends SubActivityBase {


    @Bind(R.id.picture)
    ImageView picture;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.text)
    TextView text;

    @Bind(R.id.join)
    TextView join;


    Team team;

    int teamId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);




    }


    protected void load(){

        Search search = new Search();
        search.searchId = teamId;

        team = rest("team/detail","POST",search,Team.class);

        setPicture(team.picture, picture);
        name.setText(team.name);
        text.setText(team.text);

        getSupportActionBar().setTitle(team.name);

        if(team.joined == null){
            join.setVisibility(View.VISIBLE);
        }else{
            join.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.join)
    void join(){
        Member member = new Member();
        member.team = team.id;

        if("Y".equals(team.joinAck )){
            member.state ="R";
        }else{
            member.state ="J";
        }
        rest("team/member/join","POST",member);
        load();
    }

}
