package com.anoki.team;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.RequiresPermission;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.fragment.SearchFragment;
import com.anoki.common.PrayerAdapter;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Member;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
import com.anoki.prayer.WriteActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class TeamDetailActivity extends SubActivityBase implements SearchFragment.OnFragmentInteractionListener,PrayerAdapter.OnPrayListener{


    @Bind(R.id.picture)
    ImageView picture;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.text)
    TextView text;

    @Bind(R.id.join)
    TextView join;

    @Bind(R.id.wait)
    TextView wait;

    @Bind(R.id.search)
    FrameLayout search;

    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.details)
    LinearLayout details;


    @Bind(R.id.members)
    TextView members;


    @Bind(R.id.leaders)
    TextView leaders;


    Team team;

    int teamId;

    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);

        Intent intent = getIntent();
        teamId = intent.getIntExtra("teamId", -1);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_main, menu);
        this.menu = menu;

        MenuItem write = menu.findItem(R.id.action_write);
        if("J".equals(team.joined)){
            write.setVisible(true);
        }else{
            write.setVisible(false);
        }

        return true;
    }

    public void write(MenuItem menuItem){
        Intent intent = new Intent(this, WriteActivity.class);
        intent.putExtra("teamId",team.id);
        intent.putExtra("type","team");
        startActivity(intent);
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

        getSupportActionBar().setTitle(team.name);

        if(!"J".equals(team.joined ) && "N".equals(team.scope)){
            text.setVisibility(View.VISIBLE);

            this.search.setVisibility(View.GONE);
            container.setVisibility(View.GONE);
        }else{
            text.setVisibility(View.GONE);

            this.search.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);

        }


        if(team.joined == null){
            join.setVisibility(View.VISIBLE);
        }else{
            join.setVisibility(View.GONE);
        }


        if("R".equals(team.joined)){
            wait.setVisibility(View.VISIBLE);
        }else{
            wait.setVisibility(View.GONE);
        }



        Type listType = new TypeToken<ArrayList<Prayer>>() {}.getType();

        List<Prayer> prayerList = Util.rest("team/prayer", "POST", search, listType);

        setPrayerView(R.id.prayer_list, prayerList, false);
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

        rest("team/member/join", "POST", member);
        load();
       // reload();
    }

    @Override
    public void onSearch(String key) {

    }

    @Override
    public void onPray() {

    }

    @Override
    public boolean showPicture() {
        return false;
    }

    @Override
    public void onScrap() {

    }

    public void settings(View view){
        Intent intent = new Intent(TeamDetailActivity.this, TeamSettingsActivity.class);
        intent.putExtra("teamId",team.id);
        startActivityForResult(intent,100);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK) {

            String leave = data.getStringExtra("leave");
            if(leave != null){
                finish();
            }
        }
    }
}
