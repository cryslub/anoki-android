package com.anoki1.common;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.pojo.Team;
import com.anoki1.team.TeamDetailActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015-10-06.
 */

public class TeamViewHolder extends ViewHolderBase<Team> {


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

