package com.anoki1;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.anoki1.common.CallBack;
import com.anoki1.common.DBManager;
import com.anoki1.common.DoneState;
import com.anoki1.common.RestService;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.pojo.Account;
import com.anoki1.pojo.Response;
import com.anoki1.user.LoginActivity;
import com.anoki1.user.PassActivity;

import butterknife.Bind;
import butterknife.OnTextChanged;

public class CheckPassActivity extends WriteActivityBase {


    @Bind(R.id.pass)
    TextView pass;

    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_pass);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        if("start".equals(type)){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(pass, InputMethodManager.SHOW_IMPLICIT);


    }


    @Override
    protected void confirm(){
        String input = pass.getText().toString();

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);
        String pass = dbManager.getPass();
        if(input.equals(pass)){

            if("start".equals(type)) {
                init();
            }else{
                Intent intent = new Intent(CheckPassActivity.this, PassActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);

            }
        }else{
            toast("암호가 일치하지 않습니다.");
        }

    }

    @OnTextChanged(R.id.pass)
    public void doneStateCheck(){
        if(doneMenu == null) return;

        if(pass.getText().toString().length()==0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
        }
    }

    private void init(){

        final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);

        //sqlite의 로그인 정보 확인
        Account account = dbManager.getAccount();

        if(account == null){
            //로그인 정보가 없으면 로그인 화면으로
            Intent intent = new Intent(CheckPassActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else {

            //SQLITE의 암호 확인
            //암호가 있으면 암호 화면으로

            //암호가 없으면 최근 화면으로
            RestService.log(account.email, account.pass, new CallBack<Response>() {
                @Override
                public void success(Response response) {
                    if("0".equals(response.result)){

                        Intent intent = new Intent(CheckPassActivity.this, RecentActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            });

        }
    }

}
