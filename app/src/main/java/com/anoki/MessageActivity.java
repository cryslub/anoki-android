package com.anoki;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.anoki.common.DoneState;
import com.anoki.common.WriteActivityBase;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class MessageActivity extends WriteActivityBase {

    @Bind(R.id.text)
    EditText text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);



    }



    @OnTextChanged(R.id.text)
    public void doneStateCheck(){
        if(text.getText().toString().length() > 0){
            setDonState(DoneState.DONE);
        }else{
            setDonState(DoneState.CLEAR);
        }
    }


    @Override
    protected void confirm() {

        finish();
    }
}
