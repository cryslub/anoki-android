package com.anoki.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.RecentActivity;
import com.anoki.common.Common;
import com.anoki.common.DBManager;
import com.anoki.common.RestService;
import com.anoki.pojo.Response;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        TextView forgot = (TextView) findViewById(R.id.forgot);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @OnClick(R.id.login)
    public void login(View view){
        EditText account = (EditText) findViewById(R.id.account);
        EditText pass = (EditText) findViewById(R.id.pass);

        String accountText = account.getText().toString();
        String passText = pass.getText().toString();


        Response response = RestService.log(account.getText().toString(), pass.getText().toString());
        if("0".equals(response.result)){
            final DBManager dbManager = new DBManager(getApplicationContext(), "Anoki.db", null, 1);
            dbManager.setAccount(accountText,passText);

//            ContactManage.checkContact(getContentResolver(), getApplicationContext());

            //최근 화면으로
            Intent intent = new Intent(LoginActivity.this, RecentActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            Common.toast(this, "아이디나 패스워드가 올바르지 않습니다.");
        }
    }

    public void forgot(View view){

    }

    @OnClick(R.id.signUp)
    public void signUp(View view){
        Intent intent = new Intent(LoginActivity.this, InputPhoneNumberActivity.class);
        startActivity(intent);
    }
}
