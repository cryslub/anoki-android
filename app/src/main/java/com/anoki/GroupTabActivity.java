package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.RestService;
import com.anoki.common.TabActivityBase;
import com.anoki.common.Util;
import com.anoki.common.ViewHolderBase;
import com.anoki.pojo.Dalant;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
import com.google.gson.reflect.TypeToken;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;


class TeamViewHolder extends ViewHolderBase<Team> {


    public View view;

    @Bind(R.id.name)
    public TextView name;

    @Bind(R.id.picture)
    public ImageView picture;

    Team team;

    public TeamViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Team team) {

        Util.setPicture(team.picture, picture);
        name.setText(team.name);
        this.team = team;

    }

    @OnClick(R.id.container)
    public void detail(){
        Intent intent = new Intent(view.getContext(),TeamDetailActivity.class);
        intent.putExtra("teamId",team.id);
        view.getContext().startActivity(intent);
    }
}

public class GroupTabActivity extends TabActivityBase implements  SearchFragment.OnFragmentInteractionListener{


    @Bind(R.id.my_list)
    RecyclerView myRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tab);

        setMyList();

    }



    private void setMyList(){

        List<Team> teamList = Util.rest("user/team", "POST", new Search(),  new TypeToken<ArrayList<Team>>() {}.getType());

        GeneralRecyclerViewAdapter<Team,TeamViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Team,TeamViewHolder> (teamList,R.layout.layout_team_row,TeamViewHolder.class);
        LinearLayoutManager layoutManager = setRecyclerView(myRecyclerView,responseAdapter);
    }

    @OnClick(R.id.newTeam)
    void newTeam(){
        Intent intent = new Intent(GroupTabActivity.this,NewTeamActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSearch(String key) {
        Intent intent = new Intent(GroupTabActivity.this, TeamSearchActivity.class);
        intent.putExtra("key",key);
        startActivity(intent);
    }
}
