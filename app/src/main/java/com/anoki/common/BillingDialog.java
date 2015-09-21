package com.anoki.common;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.DialogData;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015-08-20.
 */
public class BillingDialog extends Dialog {

    private DialogData data;

    @Bind(R.id.text)
    TextView text;


    @Bind(R.id.ex)
    TextView ex;

    @Bind(R.id.amount)
    TextView amount;


    @OnClick(R.id.no)
    void no(){
        dismiss();
    }

    @OnClick(R.id.yes)
    void yes(){
        data.onClickListener.onClick(null);
        dismiss();
    }

    public BillingDialog(Context context,DialogData data) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.data = data;

        setContentView(R.layout.layout_billing_dialog);

        ButterKnife.bind(this);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        text.setText(data.text);

        ex.setText(data.ex+"명");
        amount.setText((data.ex * 10) + "달란트");

    }
}
