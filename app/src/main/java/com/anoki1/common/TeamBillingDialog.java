package com.anoki1.common;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.pojo.DialogData;

import java.text.DecimalFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015-08-20.
 */
public class TeamBillingDialog extends Dialog {

    private DialogData data;

    @Bind(R.id.text)
    TextView text;


    @Bind(R.id.limit)
    TextView limit;

    @Bind(R.id.dalant)
    TextView dalant;

    @Bind(R.id.annual)
    TextView annual;


    @OnClick(R.id.no)
    void no(){
        dismiss();
    }

    @OnClick(R.id.yes)
    void yes(){
        data.onClickListener.onClick(null);
        dismiss();
    }

    public TeamBillingDialog(Context context, DialogData data) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.data = data;

        setContentView(R.layout.layout_team_billing_dialog);

        ButterKnife.bind(this);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


        text.setText(data.text);
        if(data.dalant == 20000) {
            limit.setText("무제한");
        }else{
            limit.setText(Util.getLimit(data.dalant) + " 명");
        }

        DecimalFormat df = new DecimalFormat("#,###");

        dalant.setText(df.format(data.dalant)+ "달란트" );
        annual.setText(df.format(data.dalant*10)+ "달란트" );

    }
}
