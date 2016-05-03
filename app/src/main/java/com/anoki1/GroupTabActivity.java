package com.anoki1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anoki1.common.CallBack;
import com.anoki1.common.GeneralRecyclerViewAdapter;
import com.anoki1.common.TabActivityBase;
import com.anoki1.common.TeamViewHolder;
import com.anoki1.common.Util;
import com.anoki1.fragment.SearchFragment;
import com.anoki1.pojo.Search;
import com.anoki1.pojo.Team;
import com.anoki1.team.NewTeamActivity;
import com.anoki1.team.TeamSearchActivity;
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

    }


    public void load(){

        super.load();


        setMyList();
    }

    private void setMyList(){

        backGroundRest("user/team", "POST", new Search(), new TypeToken<ArrayList<Team>>() {
        }.getType(), new CallBack<ArrayList<Team>>() {
            @Override
            public void success(ArrayList<Team> teamList) {

                GeneralRecyclerViewAdapter<Team,TeamViewHolder> responseAdapter = new  GeneralRecyclerViewAdapter<Team,TeamViewHolder> (teamList,R.layout.layout_team_row,TeamViewHolder.class);
                LinearLayoutManager layoutManager = setRecyclerView(myRecyclerView,responseAdapter);
            }
        });


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
