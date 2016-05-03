package com.anoki1.team;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.DoneState;
import com.anoki1.common.Global;
import com.anoki1.common.RestService;
import com.anoki1.common.TeamBillingDialog;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.pojo.DialogData;
import com.anoki1.pojo.Response;
import com.anoki1.pojo.Team;
import com.anoki1.etc.ChooseContactsActivity;

import butterknife.Bind;

public class TeamLimitActivity extends WriteActivityBase {

    private static final int BILL_DIALOG = 200;
    private Team team;


    @Bind(R.id.name)
    TextView name;



    @Bind(R.id.thirty)
    RadioButton thirty;

    @Bind(R.id.fifty)
    RadioButton fifty;

    @Bind(R.id.hundred)
    RadioButton hundred;


    @Bind(R.id.five_hundred)
    RadioButton fiveHundred;

    @Bind(R.id.thousand)
    RadioButton thousand;

    @Bind(R.id.unlimited)
    RadioButton unlimited;


    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_limit);

        doneState = DoneState.DONE;

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");

        name.setText(team.name);

        type  = intent.getStringExtra("type");
        if("edit".equals(type)) {
            switch (team.dalant){
                case 0:
                    onRadioButtonClicked(thirty);
                    break;
                case 2000:
                    onRadioButtonClicked(fifty);
                    break;
                case 4000:
                    onRadioButtonClicked(hundred);
                    break;
                case 7000:
                    onRadioButtonClicked(fiveHundred);
                    break;
                case 10000:
                    onRadioButtonClicked(thousand);
                    break;
                case 20000:
                    onRadioButtonClicked(unlimited);
                    break;

            }
        }

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


        if("edit".equals(type)){
            backGroundRest("team", "PUT", team, Response.class, new CallBack<Response>() {

                @Override
                public void success(Response response) {
                    succeed();

                }
            });

        }else {


            if (team.dalant > 0) {
                showDialog(BILL_DIALOG);
            } else {
               RestService.makeTeam(team, new CallBack<Team>() {
                    @Override
                    public void success(Team team) {
                        TeamLimitActivity.this.team = team;

                        Global.reloadMe();

                        succeed();


                    }
                });
//                Intent intent = new Intent(TeamLimitActivity.this, ChooseContactsActivity.class);
 //               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
 //               intent.putExtra("type", "team");
  //              intent.putExtra("team", team);
   //             startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case Global.TEAM_BILLING:
                    succeed();
                    break;

            }

        }
    }


    public Dialog onCreateDialog(int id) {

        DialogData data = new DialogData();
        data.dalant = team.dalant;

        switch (id){

            case BILL_DIALOG:
                data.text = "유료 서비스를 선택하여 결제가 필요합니다.\n결제를 진행하시겠습니까?";
                data.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TeamLimitActivity.this, TeamBillingActivity.class);
                        intent.putExtra("team",team);
                        startActivityForResult(intent, Global.TEAM_BILLING);
                    }
                };
                break;

        }



        return new TeamBillingDialog(this,data);

    }
}
