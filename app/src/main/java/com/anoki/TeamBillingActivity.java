package com.anoki;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.Util;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Team;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.OnClick;

public class TeamBillingActivity extends WriteActivityBase {

    private Team team;

    @Bind(R.id.one_month_dalant)
    TextView oneMonthDalant;

    @Bind(R.id.one_month_won)
    TextView oneMonthWon;

    @Bind(R.id.three_month_dalant)
    TextView threeMonthDalant;

    @Bind(R.id.three_month_won)
    TextView threeMonthWon;

    @Bind(R.id.six_month_dalant)
    TextView sixMonthDalant;

    @Bind(R.id.six_month_won)
    TextView sixMonthWon;

    @Bind(R.id.one_year_dalant)
    TextView oneYearDalant;

    @Bind(R.id.one_year_won)
    TextView oneYearWon;


    @Bind(R.id.limit)
    TextView limit;

    @Bind(R.id.dalant)
    TextView dalant;

    @Bind(R.id.sum)
    TextView sum;

    @Bind(R.id.price)
    TextView price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_billing);

        doneState = DoneState.DONE;

        Intent intent = getIntent();
        team = (Team) intent.getSerializableExtra("team");


        DecimalFormat df = new DecimalFormat("#,###");

        oneMonthDalant.setText("1달: " + df.format(team.dalant) + " 달란트");
        oneMonthWon.setText(df.format(team.dalant)+" 원");

        threeMonthDalant.setText("3달: "+df.format(team.dalant*3)+" 달란트");
        threeMonthWon.setText(df.format(team.dalant*3)+" 원");

        sixMonthDalant.setText("6달: "+df.format(team.dalant*6)+" 달란트");
        sixMonthWon.setText(df.format(team.dalant*6)+" 원");

        oneYearDalant.setText("1년: "+df.format(team.dalant*10)+" 달란트");
        oneYearWon.setText(df.format(team.dalant*10)+" 원");

        limit.setText(Util.getLimit(team.dalant) + " 명");



        dalant.setText(df.format(team.dalant)+ " 달란트");



        price.setText(df.format(team.dalant)+ " 달란트");

    }


    protected void afterRadio(){

        DecimalFormat df = new DecimalFormat("#,###");

        int multi = 1;


        switch (selected){
            case R.id.one_month:
                multi = 1;
                break;
            case R.id.three_month:
                multi = 3;
                break;
            case R.id.six_month:
                multi = 6;
                break;
            case R.id.one_year:
                multi = 10;
                break;
        }


        if(multi == 10){
            sum.setText("총 1년 결제");
        }else{
            sum.setText("총 "+multi+"달 결제");
        }

        price.setText(df.format(team.dalant*multi)+ " 달란트");
    }


    @OnClick(R.id.pay)
    void pay(){

    }
}
