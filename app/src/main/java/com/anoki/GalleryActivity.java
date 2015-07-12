package com.anoki;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;

import org.apmem.tools.layouts.FlowLayout;

public class GalleryActivity extends SubActivityBase {

    private MenuItem doneMenu;
    private DoneState doneState = DoneState.CLEAR;

    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private ViewGroup gridview = null;
    private Cursor cc = null;
    private Button btnMoreInfo = null;
    private ProgressDialog myProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        cc = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                null);

        if (cc != null) {


            cc.moveToFirst();
            mUrls = new Uri[cc.getCount()];
            strUrls = new String[cc.getCount()];
            mNames = new String[cc.getCount()];
            for (int i = 0; i < cc.getCount(); i++) {
                cc.moveToPosition(i);
                mUrls[i] = Uri.parse(cc.getString(1));
                strUrls[i] = cc.getString(1);
                mNames[i] = cc.getString(3);
                //Log.e("mNames[i]",mNames[i]+":"+cc.getColumnCount()+ " : " +cc.getString(3));
            }

            gridview = (ViewGroup) findViewById(R.id.media_list);

            for (int i = 0; i < cc.getCount(); i++) {
                Bitmap bmp = decodeURI(mUrls[i].getPath());
                //BitmapFactory.decodeFile(mUrls[position].getPath());


                ImageView imageView = new ImageView(GalleryActivity.this);
                imageView.setImageBitmap(bmp);

//                            imageView.setLayoutParams(layoutParams);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                int size = Util.dpToPixel(getApplicationContext(),100);
                int margin = Util.dpToPixel(getApplicationContext(),5);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size,size);
                layoutParams.setMargins(margin, margin, margin, margin);

                gridview.addView(imageView,layoutParams);
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gallery, menu);

        doneMenu = menu.findItem(R.id.action_done);

        return true;
    }

    public void done(MenuItem item){

    }


    public Bitmap decodeURI(String filePath){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Only scale if we need to
        // (16384 buffer for img processing)
        Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);
        if(options.outHeight * options.outWidth * 2 >= 16384){
            // Load, scaling to smallest power of 2 that'll get it <= desired dimensions
            double sampleSize = scaleByHeight
                    ? options.outHeight / 100
                    : options.outWidth / 100;
            options.inSampleSize =
                    (int)Math.pow(2d, Math.floor(
                            Math.log(sampleSize)/Math.log(2d)));
        }

        // Do the actual decoding
        options.inJustDecodeBounds = false;
        options.inTempStorage = new byte[512];
        Bitmap output = BitmapFactory.decodeFile(filePath, options);

        return output;
    }
}
