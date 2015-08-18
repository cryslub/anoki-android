package com.anoki;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.anoki.common.Util;
import com.anoki.pojo.Phone;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateAccountActivity extends ActionBarActivity implements EditTextFragment.OnFragmentInteractionListener{

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()-+=\\\\|/?.,<>]).{8,16})";

    private boolean accountOk = false;
    private boolean passOk = false;
    private boolean confirmOk = false;

    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        Phone phone = (Phone) intent.getSerializableExtra("phone");
        user.phone = phone.number;
        user.country = phone.country;

        final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

        final EditText editText = (EditText)findViewById(R.id.calc_txt_Prise);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                if (s.length() > 0) {

                    String accountText = editText.getText().toString();
                    if (accountText.contains("@") && accountText.contains(".")) {
                        User user = new User();
                        user.account = accountText;

                        Response response = Util.rest("user/check", "POST", user, Response.class);
                        if ("0".equals(response.result)) {
                            accountOk = true;
                            return;

                        }
                    }
                }
                accountOk = false;
            }
        });

        final EditText pass = (EditText) findViewById(R.id.pass);

        pass.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String passText = pass.getText().toString();
                if(passText.length()>=8 && passText.length()<=16){
                    Matcher matcher = pattern.matcher(passText);
                    if(matcher.matches()){
                        passOk = true;
                        return;
                    }

                }

                passOk = false;
            }
        });


        final EditText confirm = (EditText) findViewById(R.id.confirm);

        confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String passText = pass.getText().toString();
                String confirmText = confirm.getText().toString();
                if(passText.equals(confirmText)){
                    confirmOk = true;
                    return;
                }
                confirmOk = false;
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

    @OnClick(R.id.next)
    public void next(View view){

        if(accountOk && passOk && confirmOk){

            Intent intent = new Intent(CreateAccountActivity.this, SetNameActivity.class);

            final EditText editText = (EditText)findViewById(R.id.calc_txt_Prise);
            final EditText pass = (EditText)findViewById(R.id.pass);

            user.account = editText.getText().toString();
            user.pass = pass.getText().toString();

            intent.putExtra("user",user);
            startActivity(intent);

        }

    }

    @Override
    public void textChanged(Editable s) {

    }
}
