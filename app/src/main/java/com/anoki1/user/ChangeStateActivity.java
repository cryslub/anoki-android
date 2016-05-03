package com.anoki1.user;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anoki1.fragment.EditTextFragment;
import com.anoki1.R;
import com.anoki1.common.DoneState;
import com.anoki1.common.Global;
import com.anoki1.common.Util;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.pojo.Response;

public class ChangeStateActivity extends WriteActivityBase implements EditTextFragment.OnFragmentInteractionListener {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_state);

        EditTextFragment editTextFragment = (EditTextFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        View view=editTextFragment.getView();

        editText = (EditText) view.findViewById(R.id.calc_txt_Prise);

        editText.setText(Global.me.text);
    }

    @Override
    protected void confirm() {
        Global.me.text =editText.getText().toString();

        Util.rest("user", "PUT", Global.me, Response.class);

        succeed();
    }


    public void textChanged(Editable s) {
        TextView tv = (TextView)findViewById(R.id.text_length);
        tv.setText(s.length() + "");

        doneStateCheck();
    }

    public void doneStateCheck(){
        if(doneMenu == null) return;

        TextView  tv = (TextView)findViewById(R.id.text_length);

        if("0".equals(tv.getText().toString())){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }
}
