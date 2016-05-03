package com.anoki1.team;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Team;

import butterknife.Bind;

public class TeamAuthActivity extends SubActivityBase {

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.register)
    TextView register;

    @Bind(R.id.invite)
    TextView invite;

    @Bind(R.id.kick)
    TextView kick;


    Team team;

    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_auth);

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");


    }

    public void load(){
        setAuthText(team.profileAuth,title);
        setAuthText(team.registerAuth,register);
        setAuthText(team.inviteAuth,invite);
        setAuthText(team.leaveAuth,kick);

    }

    private void setAuthText(int prop, TextView textView){
        switch(prop){
            case 3:
                textView.setText("책임 청지기");
                break;
            case 2:
                textView.setText("모든 청지기");
                break;
            case 1:
                textView.setText("모든 멤버");
                break;

        }
    }

    public void title(View view){
        selected = R.id.title;
        showMyPopupMenu(view);

    }

    public void register(View view){
        selected = R.id.register;
        showMyPopupMenu(view);

    }

    public void invite(View view){
        selected = R.id.invite;
        showMyPopupMenu(view);

    }

    public void kick(View view){
        selected = R.id.kick;
        showMyPopupMenu(view);

    }



    protected void showMyPopupMenu(View popup){
        PopupMenu popupMenu = new PopupMenu(this, popup);
        //Inflating the Popup using xml file
        popupMenu.getMenuInflater()
                .inflate(R.menu.menu_auth_popup, popupMenu.getMenu());



        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                int value = 0;

                switch (item.getItemId()) {
                    case R.id.all: {
                        value = 1;
                    }
                    break;
                    case R.id.vice: {
                        value = 2;
                    }
                    break;
                    case R.id.leader: {
                        value = 3;
                    }
                    break;

                }

                if (value != 0) {
                    switch (selected) {
                        case R.id.title:
                            team.profileAuth = value;
                            break;
                        case R.id.register:
                            team.registerAuth = value;
                            break;
                        case R.id.invite:
                            team.inviteAuth = value;
                            break;
                        case R.id.kick:
                            team.leaveAuth = value;
                            break;

                    }

                    backGroundRest("team", "PUT", team, Response.class, new CallBack<Response>() {

                        @Override
                        public void success(Response response) {
                            load();
                        }
                    });

                   // rest("team", "PUT",team);

                }


                return true;
            }
        });

        popupMenu.show();
    }
}
