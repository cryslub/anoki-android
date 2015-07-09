package com.anoki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.anoki.common.Util;
import com.anoki.pojo.Response;
import com.anoki.pojo.User;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

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
       // Log.v("setName","changeImage");

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 100:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = data.getData();
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
                        String id = executeMultipartPost(yourSelectedImage);
                        if(!"-1".equals(id)){
                            ImageButton  button = (ImageButton) findViewById(R.id.profileImage);
                            Bitmap bmp = Util.fetchImage(id);
                            button.setImageBitmap(bmp);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public String executeMultipartPost(Bitmap bm)  {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            byte[] data = bos.toByteArray();
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost postRequest = new HttpPost(
                    "http://anoki.co.kr/anoki/rest/prayer/upload");
            ByteArrayBody bab = new ByteArrayBody(data, "profile.jpg");
            // File file= new File("/mnt/sdcard/forest.png");
            // FileBody bin = new FileBody(file);
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("uploaded", bab);
            reqEntity.addPart("photoCaption", new StringBody("profile"));
            postRequest.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(postRequest);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent(), "UTF-8"));
            String sResponse;
            StringBuilder s = new StringBuilder();

            while ((sResponse = reader.readLine()) != null) {
                s = s.append(sResponse);
            }
            System.out.println("Response: " + s);
            return s.toString();
        } catch (Exception e) {
            // handle exception here
            Log.e(e.getClass().getName(), e.getMessage());
        }

        return null;
    }

    public Object fetch(String address) throws MalformedURLException,IOException {

        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }

    private Drawable ImageOperations(Context ctx, String url, String saveFilename) {
        try {
            InputStream is = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(is, saveFilename);
            return d;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
