package com.anoki;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anoki.singleton.Util;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;

public class SetNameActivity extends Activity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);

        tv = (TextView)findViewById(R.id.text_length);

        EditText textMessage = (EditText)findViewById(R.id.name);
        textMessage.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {

                tv.setText(s.length()+"");
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_set_name, menu);
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

    public void start(View view){
        //서버에 이름및 사진 변경 요청

        User user = new User();

        EditText name = (EditText) findViewById(R.id.name);
        user.name = name.getText().toString();

        Response response = Util.rest("user", "PUT", user, Response.class);

        if("0".equals(response.result)) {
            //최근 화면으로

        }

    }

    public void changeImage(View view){
        Log.v("setName","changeImage");
    }
}
