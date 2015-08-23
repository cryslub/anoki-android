package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Team;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class TeamTypeActivity extends WriteActivityBase {

    private Team team;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.auth)
    CheckBox auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_type);

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");

        name.setText(team.name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);

        doneMenu = menu.findItem(R.id.action_done);


        doneMenu.setIcon(R.drawable.ic_arrow_forward_white_24dp);
        doneState = DoneState.NEXT;


        return true;
    }

    @Override
    protected void next(){

        switch (selected){
            case R.id.private_team:
                team.scope ="S";
                break;
            case R.id.name_opened:
                team.scope ="N";
                break;
            case R.id.public_team:
                team.scope ="P";
                break;

        }

        team.inviteAuth = auth.isChecked()?"Y":"N";

        Intent intent = new Intent(TeamTypeActivity.this, TeamLimitActivity.class);
        intent.putExtra("team",team);
        startActivity(intent);
    }
}
