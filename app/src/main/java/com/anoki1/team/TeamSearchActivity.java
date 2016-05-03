package com.anoki1.team;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anoki1.R;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.SubActivityBase;
import com.anoki1.common.TeamViewHolder;
import com.anoki1.common.Util;
import com.anoki1.pojo.Search;
import com.anoki1.pojo.Team;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TeamSearchActivity extends SubActivityBase {

    @Bind(R.id.other_list)
    RecyclerView otherRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_search);

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        setFilter(key);


        getSupportActionBar().setTitle(key);

    }


    protected void setFilter(String key){

        Search search = new Search();
        search.searchKey = key;

        if(search.searchKey.length()>0) {

            List<Team> teamList = Util.rest("team/search", "POST", search, new TypeToken<ArrayList<Team>>() {
            }.getType());

            GeneralRecyclerViewAdapter<Team, TeamViewHolder> responseAdapter = new GeneralRecyclerViewAdapter<Team, TeamViewHolder>(teamList, R.layout.layout_team_row, TeamViewHolder.class);
            LinearLayoutManager layoutManager = setRecyclerView(otherRecyclerView, responseAdapter);
        }


    }

}
