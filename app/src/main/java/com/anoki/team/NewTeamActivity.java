package com.anoki.team;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.anoki.R;
import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Team;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class NewTeamActivity extends WriteActivityBase {

    private Team team;

    @Bind(R.id.name)
    EditText name;

    @Bind(R.id.text)
    EditText text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_team);

        team = new Team();
    }



    @OnTextChanged(R.id.name)
    public void doneStateCheck(){

        if(name.getText().length() == 0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
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
    protected void next(){

        team.name = name.getText().toString();
        team.text = text.getText().toString();

        Intent intent = new Intent(NewTeamActivity.this, TeamTypeActivity.class);
        intent.putExtra("team",team);
        startActivity(intent);
    }
}
