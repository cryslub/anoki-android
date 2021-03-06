package com.anoki1.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.common.CallBack;
import com.anoki1.common.Global;
import com.anoki1.common.Util;
import com.anoki1.fragment.EditTextFragment;
import com.anoki1.pojo.Phone;
import com.anoki1.pojo.Response;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class InputAuthCodeActivity extends FragmentActivity implements EditTextFragment.OnFragmentInteractionListener {

    private Phone phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_auth_code);

        ButterKnife.bind(this);

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
        setResult(RESULT_OK, null);
        finish();
    }



    @OnClick(R.id.confirm)
    public void confirm(View view){
        //서버에 인증번호 전송

//        EditText authCode = (EditText) findViewById(R.id.auth_code);
        EditText editText = (EditText)findViewById(R.id.calc_txt_Prise);

        phone.auth = editText.getText().toString();
        if("".equals(phone.auth)){
            return;
        }

     //   Response response = Util.rest("auth/send/number", "POST", phone, Response.class);
        Util.backGroundRest("auth/send/number", "POST", phone, Response.class, new CallBack<Response>(){

            @Override
            public void success(Response response) {
                if("0".equals(response.result)){
                    //성공시 다음 화면으로

                    Intent intent = new Intent(InputAuthCodeActivity.this,CreateAccountActivity.class);
                    intent.putExtra("phone",phone);
                    startActivity(intent);
                }
            }
        });



    }

    @OnClick(R.id.resend)
    public void resend(View view) {

        Util.backGroundRest("auth/send/number", "POST", phone, Response.class, new CallBack<Response>() {

            @Override
            public void success(Response result) {

            }
        });
    }

    @Override
    public void textChanged(Editable s) {

    }
}
