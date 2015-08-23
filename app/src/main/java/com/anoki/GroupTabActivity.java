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


    public TeamViewHolder(View itemView) {
        super(itemView);

        view = itemView;
    }


    @Override
    public void bind(Team team) {

        Util.setPicture(team.picture, picture);
        name.setText(team.name);

    }
}

public class GroupTabActivity extends TabActivityBase {

    @Bind(R.id.tabHost)
    TabHost myTabHost;

    @Bind(R.id.my_list)
    RecyclerView myRecyclerView;

    @Bind(R.id.other_list)
    RecyclerView otherRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_tab);

        setTab(myTabHost, new String[]{"내그룹", "그룹검색"}, new int[]{R.id.my, R.id.other});

        setMyList();

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

}
