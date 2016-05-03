package com.anoki1.team;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.DoneState;
import com.anoki1.common.Global;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Team;

import butterknife.Bind;

public class TeamTypeActivity extends WriteActivityBase {

    private Team team;

    @Bind(R.id.name)
    TextView name;

    @Bind(R.id.auth)
    CheckBox auth;

    @Bind(R.id.private_team)
    RadioButton privateTeam;

    @Bind(R.id.name_opened)
    RadioButton nameOpened;

    @Bind(R.id.public_team)
    RadioButton publicTeam;


    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_type);

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");

        name.setText(team.name);


        type  = intent.getStringExtra("type");
        if("edit".equals(type)){

            doneState = DoneState.DONE;

           // getSupportActionBar().setTitle("그룹 이름 및 커버 설정 ");
            switch (team.scope){
                case "S":
                    onRadioButtonClicked(privateTeam);
                    break;
                case "M":
                    onRadioButtonClicked(nameOpened);
                    break;
                case "P":
                    onRadioButtonClicked(publicTeam);
                    break;

            }

            auth.setChecked("Y".equals(team.joinAck));

        }else{
            team.scope ="N";
            doneState = DoneState.NEXT;
        }

    }



    private void extract(){
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

        team.joinAck = auth.isChecked()?"Y":"N";
    }

    protected void confirm(){

        extract();

//        rest("team", "PUT", team);

        backGroundRest("team", "PUT", team, Response.class, new CallBack<Response>() {

            @Override
            public void success(Response response) {

                succeed();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case Global.TEAM_LIMIT:
                    succeed();
                    break;

            }

        }
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

        team.joinAck = auth.isChecked()?"Y":"N";

        Intent intent = new Intent(TeamTypeActivity.this, TeamLimitActivity.class);
        intent.putExtra("team",team);
        startActivityForResult(intent, Global.TEAM_LIMIT);
    }
}
