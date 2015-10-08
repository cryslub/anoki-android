package com.anoki;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.TabActivityBase;
import com.anoki.common.TeamViewHolder;
import com.anoki.common.Util;
import com.anoki.fragment.SearchFragment;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
import com.anoki.team.NewTeamActivity;
import com.anoki.team.TeamSearchActivity;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;



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
        intent.putExtra("type","new");
        startActivity(intent);
    }

    @Override
    public void onSearch(String key) {
        Intent intent = new Intent(GroupTabActivity.this, TeamSearchActivity.class);
        intent.putExtra("key",key);
        startActivity(intent);
    }
}
