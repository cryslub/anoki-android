package com.anoki.team;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.anoki.R;
import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Team;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class NewTeamActivity extends WriteActivityBase {

    private Team team;


    @Bind(R.id.profile_image)
    ImageView picture;


    @Bind(R.id.name)
    EditText name;

    @Bind(R.id.text)
    EditText text;


    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        team = new Team();

        Intent intent = getIntent();
        type  = intent.getStringExtra("type");
        if("edit".equals(type)){

            team = (Team) intent.getSerializableExtra("team");
            doneState = DoneState.DONE;

            getSupportActionBar().setTitle("그룹 이름 및 커버 설정 ");

            setPicture(team.picture, picture);

            name.setText(team.name);
            text.setText(team.text);
        }
    }



    @OnTextChanged(R.id.name)
    public void doneStateCheck(){

        if(doneMenu ==null) return;
        if(name.getText().length() == 0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else if("edit".equals(type)) {
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;

        }else{

            doneMenu.setIcon(R.drawable.ic_arrow_forward_white_24dp);
            doneState = DoneState.NEXT;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case Global.PROFILE_PHOTO:
                    team.picture = setProfilePicture(data);
                    break;
            }

        }
    }


    @Override
    protected void confirm(){
        team.name = name.getText().toString();
        team.text = text.getText().toString();

        rest("team","PUT",team);

        succeed();
    }

    @Override
    protected void next(){

        team.name = name.getText().toString();
        team.text = text.getText().toString();

        Intent intent = new Intent(NewTeamActivity.this, TeamTypeActivity.class);
        intent.putExtra("team",team);
        startActivity(intent);
    }
}
