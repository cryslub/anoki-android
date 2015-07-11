package com.anoki;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.anoki.common.CallBack;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;

import org.apmem.tools.layouts.FlowLayout;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class WriteActivity extends SubActivityBase {

    private final int PHOTO = 100;
    private final int VIDEO = 200;
    private final int CAMERA = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);
        return true;
    }

    public void next(MenuItem item){

    }

    public void showSelectSource(View view){
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.select_source);
        if(linearLayout.getVisibility() == View.VISIBLE){
            linearLayout.setVisibility(View.INVISIBLE);
        }else{
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    public void photo(View view){
        if(checkContents()) {
            Intent intent = new Intent(this,GalleryActivity.class);
            startActivity(intent);

//            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
 //           photoPickerIntent.setType("image/*");
 //           photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
  //          startActivityForResult(photoPickerIntent, PHOTO);
        }
    }

    public void video(View view){

    }

    public void camera(View view){

    }

    private boolean checkContents(){
        ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);
        if( flowLayout.getChildCount() < 10){
           return true;
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "최대 첨부가능 수는 10개입니다. ", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return false;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case PHOTO:
                if(resultCode == RESULT_OK){
                    Util.uploadSelectedPhoto(data, getContentResolver(), new CallBack() {
                        @Override
                        public void success(String id) {
                            //media list 에 추가
                            ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);

                            ImageView imageView = new ImageView(WriteActivity.this);
                            Bitmap bmp = Util.fetchImage(id);
                            imageView.setImageBitmap(bmp);

                            int size = Util.dpToPixel(getApplicationContext(),60);
                            int margin = Util.dpToPixel(getApplicationContext(),5);
                            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size,size);
                            layoutParams.setMargins(margin, margin, margin, margin);

//                            imageView.setLayoutParams(layoutParams);
                            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


                            flowLayout.addView(imageView,layoutParams);

                        }
                    });


                }
        }
    }

}
