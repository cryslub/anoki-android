package com.anoki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.anoki.Singleton.Util;
import com.anoki.pojo.Country;
import com.anoki.pojo.Phone;
import com.anoki.pojo.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class InputPhoneNumberActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_phone_number);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        ArrayList<Country> list  = loadCountryData();

        ArrayAdapter<Country> dataAdapter = new ArrayAdapter<Country>(this,
                android.R.layout.simple_spinner_item, list);

// Drop down layout style - list view
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner =   (Spinner) findViewById(R.id.country_spinner);

// attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        spinner.setSelection(193);

        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        EditText editText = (EditText)findViewById(R.id.calc_txt_Prise);
        editText.setText(mPhoneNumber, TextView.BufferType.EDITABLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_input_phone_number, menu);
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


    protected ArrayList<Country> loadCountryData() {
        ArrayList<Country> data = new ArrayList<Country>(233);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(getApplicationContext().getAssets().open("countries.dat"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String line;
            int i =0;
            while ((line = reader.readLine()) != null) {
                //process line

                Country c = new Country(getApplicationContext(), line, i++);
                data.add(c);
            }
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }

        return data;
    }


    public void next(View view){

        Spinner spinner =   (Spinner) findViewById(R.id.country_spinner);
        EditText editText = (EditText)findViewById(R.id.calc_txt_Prise);

        Country country = (Country) spinner.getSelectedItem();

        Phone phone = new Phone();
        phone.number = editText.getText().toString();
        phone.country = country.getCountryCode()+"";


        //서버에 인증요청
        Response response = Util.rest("auth/request","POST", phone,Response.class);

        if("0".equals(response.result)){
            Intent intent = new Intent(this, InputAuthCodeActivity.class);
            intent.putExtra("phone",phone);

            startActivity(intent);
        }else{

        }

    }
}
