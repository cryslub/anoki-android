package com.anoki;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anoki.common.AnokiDialog;
import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.TeamBillingDialog;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.DialogData;
import com.anoki.pojo.Team;

import butterknife.Bind;

public class TeamLimitActivity extends WriteActivityBase {

    private static final int EX_DIALOG = 100;
    private static final int BILL_DIALOG = 200;
    private Team team;


    @Bind(R.id.name)
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_limit);

        doneState = DoneState.DONE;

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");

        name.setText(team.name);
    }

    @Override
    protected void confirm(){

        switch (selected){
            case R.id.thirty:
                team.dalant = 0;
                break;
            case R.id.fifty:
                team.dalant =2000;
                break;
            case R.id.hundred:
                team.dalant =4000;
                break;
            case R.id.five_hundred:
                team.dalant = 7000;
                break;
            case R.id.thousand:
                team.dalant =10000;
                break;
            case R.id.unlimited:
                team.dalant =20000;
                break;


        }

        if(team.dalant > Global.me.dalant){
            showDialog(EX_DIALOG);
        }else if(team.dalant > 0) {
            showDialog(BILL_DIALOG);
        }else{

            RestService.makeTeam(team);
            Global.reloadMe();

            Intent intent = new Intent(TeamLimitActivity.this, TeamMainActivity.class);
            intent.putExtra("team", team);
            startActivity(intent);
        }
    }



    public Dialog onCreateDialog(int id) {

        DialogData data = new DialogData();
        data.dalant = team.dalant;

        switch (id){
            case EX_DIALOG:
                data.text = "충전된 금액이 부족합니다. 결제를 진행하시겠습니까?";
                data.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeamLimitActivity.this, TeamChargeActivity.class);
                        intent.putExtra("team",team);
                        startActivity(intent);
                    }
                };
                break;
            case BILL_DIALOG:
                data.text = "유료 서비스를 선택하여 결제가 필요합니다.\n결제를 진행하시겠습니까?";
                data.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeamLimitActivity.this, TeamBillingActivity.class);
                        intent.putExtra("team",team);
                        startActivity(intent);
                    }
                };
                break;

        }



        return new TeamBillingDialog(this,data);

    }
}
