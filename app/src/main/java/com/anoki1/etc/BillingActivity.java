package com.anoki1.etc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.Global;
import com.anoki1.common.RestService;
import com.anoki1.common.SubActivityBase;
import com.anoki1.pojo.Prayer;
import com.anoki1.pojo.Response;

import butterknife.Bind;
import butterknife.OnClick;


public class BillingActivity extends SubActivityBase {

    private Prayer prayer;

    @Bind(R.id.calc) TextView calc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);


        Intent intent = getIntent();
        prayer = (Prayer) intent.getSerializableExtra("prayer");
        int total = prayer.friends.size() + prayer.phone.size();
        int ex = total - Global.FREE_FRIENDS_COUNT;

        TextView totalText = (TextView)findViewById(R.id.total);
        totalText.setText(total + "명");

        TextView exText = (TextView)findViewById(R.id.ex);
        exText.setText(ex + "명");


        TextView sum = (TextView)findViewById(R.id.sum);
        sum.setText("총 "+ex+"명");


        calc.setText(" ("+ex+"명 X "+Global.DALANT_PER_PERSON+" 달란트)");

        TextView price = (TextView)findViewById(R.id.price);
        price.setText( (ex*Global.DALANT_PER_PERSON)+ "달란트");

    }


    @OnClick(R.id.pay)
    public void pay(View view){
        int total = prayer.friends.size() + prayer.phone.size();
        int ex = total - Global.FREE_FRIENDS_COUNT;

        prayer.dalant = ex *Global.DALANT_PER_PERSON;

        RestService.makePrayer(prayer, new CallBack<Response>() {
            @Override
            public void success(Response result) {
                succeed();

            }
        });

//        Intent intent = Util.inviteIntent(prayer);
//        startActivityForResult(intent, Global.SMS);

    }


}
