package com.anoki;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.anoki.common.Util;
import com.anoki.pojo.Phone;
import com.anoki.pojo.Response;

public class InputAuthCodeActivity extends Activity {

    private Phone phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_auth_code);

        Intent intent = new Intent(this.getIntent());

        phone = (Phone) intent.getSerializableExtra("phone");
        TextView phoneNumber = (TextView) findViewById(R.id.phone_number);
        phoneNumber.setText("+"+phone.country+"-"+phone.number+"로 전송받은 4자리 인증번호를 입력해주세요.");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_auth_code, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void back(View view){
        onBackPressed();
    }

    public void confirm(View view){
        //서버에 인증번호 전송

        Response response = Util.rest("auth/send/number", "POST", phone, Response.class);

        if("0".equals(response.result)){

        }
        //성공시 다음 화면으로

    }

    public void resend(View view){

    }
}
