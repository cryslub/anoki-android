package com.anoki;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.etc.ChooseContactsActivity;
import com.anoki.pojo.Member;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
import com.anoki.team.NewTeamActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TeamSettingsActivity extends SubActivityBase {

    @Bind(R.id.picture)
    ImageView picture;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.text)
    TextView text;

    @Bind(R.id.members)
    TextView members;


    @Bind(R.id.leaders)
    TextView leaders;

    @Bind(R.id.remain)
    TextView remain;


    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.remain_container)
    LinearLayout remainContainer;

    Team team;



    int teamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_settings);


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

        members.setText(team.memberCount+"명");
        leaders.setText(team.leader);
        remain.setText(team.remain + "일");


        getSupportActionBar().setTitle(team.name);

        if(team.role == 1){
            container.setVisibility(View.GONE);
        }else{
            container.setVisibility(View.VISIBLE);
        }

        if(team.role == 1 || team.dalant == 0){
            remainContainer.setVisibility(View.GONE);
        }else{
            remainContainer.setVisibility(View.VISIBLE);
        }


    }

    public void leave(View view){
        Member member = new Member();
        member.user = Global.me.id;
        member.team = team.id;
        rest("team/member","DELETE",member);

        getIntent().putExtra("leave", "leave");

        succeed(getIntent());
    }

    public void invite(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, ChooseContactsActivity.class);
        intent.putExtra("type","invite");
        intent.putExtra("team",team);

        startActivity(intent);
    }

    public void members(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamMembersActivity.class);
        intent.putExtra("teamId",team.id);
        startActivity(intent);

    }

    public void title(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, NewTeamActivity.class);
        intent.putExtra("teamId",team.id);
        intent.putExtra("type","edit");
        startActivity(intent);

    }

    public void type(View view){

    }
    public void limit(View view){

    }
    public void delete(View view){

    }
    public void auth(View view){

    }

    public void leaders(View view){

    }



}
