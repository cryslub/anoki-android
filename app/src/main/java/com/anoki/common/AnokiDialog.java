package com.anoki.common;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.DialogData;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015-08-20.
 */
public class AnokiDialog extends Dialog {


    private DialogData data;

    @Bind(R.id.title)
    TextView title;


    @Bind(R.id.text)
    TextView text;

    @OnClick(R.id.no)
    void no(){
        dismiss();
    }

    @OnClick(R.id.yes)
    void yes(){
        data.onClickListener.onClick(null);
        dismiss();
    }

    public AnokiDialog(Context context,DialogData data) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);

        this.data = data;

        setContentView(R.layout.layout_anoki_dialog);

        ButterKnife.bind(this);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.7f;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        title.setText(data.title);
        text.setText(data.text);


    }
}
