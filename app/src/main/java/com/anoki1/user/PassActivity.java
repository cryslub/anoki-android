package com.anoki1.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.anoki1.R;
import com.anoki1.SetPassActivity;
import com.anoki1.common.DBManager;
import com.anoki1.common.SubActivityBase;

import butterknife.Bind;

public class PassActivity extends SubActivityBase {


    @Bind(R.id.lock)
    CheckBox lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass);

        final DBManager dbManager = new DBManager(this);
        String pass =dbManager.getPass();
        lock.setChecked(pass!=null);

    }

    public void lock(View view){

        if(lock.isChecked()){
            Intent intent = new Intent(PassActivity.this, SetPassActivity.class);
            startActivityForResult(intent, 100);
        }else{
            final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);
            dbManager.removePass();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK) {
            lock.setChecked(false);
        }
    }

}
