package com.anoki1;

import android.os.Bundle;
import android.widget.TextView;

import com.anoki1.common.DBManager;
import com.anoki1.common.DoneState;
import com.anoki1.common.WriteActivityBase;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class SetPassActivity extends WriteActivityBase {

    @Bind(R.id.pass)
    TextView pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pass);

    }


    @Override
    protected void confirm(){
        String pass =this.pass.getText().toString();
        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);
        dbManager.setPass(pass);

        succeed();
    }


    @OnTextChanged(R.id.pass)
    public void doneStateCheck(){
        if(doneMenu == null) return;

        if(pass.getText().toString().length()!=4){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }
}
