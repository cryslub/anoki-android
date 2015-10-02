package com.anoki;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anoki.common.GeneralRecyclerViewAdapter;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Search;
import com.anoki.pojo.Team;
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
    }


    protected void setFilter(){

        Search search = new Search();
        search.searchKey = searchKey.getText().toString();

        if(search.searchKey.length()>0) {

            List<Team> teamList = Util.rest("team/search", "POST", search, new TypeToken<ArrayList<Team>>() {
            }.getType());

            GeneralRecyclerViewAdapter<Team, TeamViewHolder> responseAdapter = new GeneralRecyclerViewAdapter<Team, TeamViewHolder>(teamList, R.layout.layout_team_row, TeamViewHolder.class);
            LinearLayoutManager layoutManager = setRecyclerView(otherRecyclerView, responseAdapter);
        }


    }

}
