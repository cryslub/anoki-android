package com.anoki.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.common.Global;
import com.anoki.common.SubActivityBase;
import com.anoki.etc.ChooseContactsActivity;
import com.anoki.pojo.Member;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;

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

    @Bind(R.id.super_auth)
    LinearLayout superAuth;

    @Bind(R.id.auth)
    LinearLayout auth;


    @Bind(R.id.invite)
    LinearLayout invite;


    @Bind(R.id.manage)
    LinearLayout manage;


    @Bind(R.id.title)
    LinearLayout title;



    Team team;



    int teamId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_settings);


        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);
    }


    public void load(){

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

        if(team.role >= team.inviteAuth){
            invite.setVisibility(View.VISIBLE);
        }else{
            invite.setVisibility(View.GONE);
        }

        if(team.role >= team.leaveAuth){
            manage.setVisibility(View.VISIBLE);
        }else{
            manage.setVisibility(View.GONE);
        }


        if(team.role >= team.profileAuth){
            title.setVisibility(View.VISIBLE);
        }else{
            title.setVisibility(View.GONE);
        }


        if(team.role >= 2){
            auth.setVisibility(View.VISIBLE);
        }else{
            auth.setVisibility(View.GONE);
        }


        if(team.role == 3){
            superAuth.setVisibility(View.VISIBLE);
        }else{
            superAuth.setVisibility(View.GONE);
        }


    }

    public void leave(View view){
        Member member = new Member();
        member.user = Global.me.id;
        member.team = team.id;
        rest("team/member", "DELETE", member);

        getIntent().putExtra("leave", "leave");

        succeed(getIntent());
    }

    public void alarm(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamAlarmActivity.class);
        intent.putExtra("teamId",team.id);
        startActivity(intent);
    }

    public void invite(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, ChooseContactsActivity.class);
        intent.putExtra("type", "invite");
        intent.putExtra("team", team);

        startActivity(intent);
    }

    public void members(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamMembersActivity.class);
        intent.putExtra("teamId",team.id);
        startActivity(intent);

    }

    public void title(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, NewTeamActivity.class);
        intent.putExtra("team",team);
        intent.putExtra("type","edit");
        startActivity(intent);

    }

    public void type(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamTypeActivity.class);
        intent.putExtra("team",team);
        intent.putExtra("type","edit");
        startActivity(intent);

    }

    public void limit(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamLimitActivity.class);
        intent.putExtra("team",team);
        intent.putExtra("type","edit");
        startActivity(intent);
    }

    public void delete(View view){
        rest("team", "DELETE", team);

        getIntent().putExtra("leave", "leave");

        succeed(getIntent());
    }

    public void auth(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamAuthActivity.class);
        intent.putExtra("team", team);
        startActivity(intent);
    }

    public void leaders(View view){
        Intent intent = new Intent(TeamSettingsActivity.this, TeamLeaderActivity.class);
        intent.putExtra("team",team);
        startActivity(intent);
    }

    public void charge(View view){
        Intent intent = new Intent(this,TeamBillingActivity.class);
        intent.putExtra("team", team);
        intent.putExtra("type","charge");
        startActivity(intent);
    }

}
