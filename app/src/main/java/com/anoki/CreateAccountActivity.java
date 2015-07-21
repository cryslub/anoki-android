package com.anoki;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.anoki.common.Util;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;


public class CreateAccountActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        final EditText account = (EditText) findViewById(R.id.account);

        account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.length() > 0) {

                    String accountText = account.getText().toString();
                    if(accountText.contains("@") && accountText.contains(".")) {
                        User user = new User();
                        user.account = account.getText().toString();

                        Response response = Util.rest("user/check", "POST", user, Response.class);
                        if ("0".equals(response.result)) {
                            ImageView imageView = (ImageView) findViewById(R.id.account_check);
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_black_24dp));
                            return;

                        }
                    }
                }
                ImageView imageView = (ImageView) findViewById(R.id.account_check);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_mood_bad_black_24dp));

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_account, menu);
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
}
