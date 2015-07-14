package com.anoki;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.ParentListener;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends SubActivityBase{

    private MenuItem doneMenu;
    private DoneState doneState = DoneState.CLEAR;

    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private ViewGroup gridview = null;
    private Cursor cc = null;
    private Button btnMoreInfo = null;
    private ProgressDialog myProgressDialog = null;
    private Map<Integer,Integer> selectionMap = new HashMap<Integer,Integer>();

    private int size;
    private int margin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridview = (ViewGroup) findViewById(R.id.media_list);

        size = Util.dpToPixel(getApplicationContext(),100);
        margin = Util.dpToPixel(getApplicationContext(),5);

        Intent intent = getIntent();
        int requestCode = intent.getIntExtra("requestCode", -1);

        switch (requestCode) {
            case Global.PHOTO:
                loadImages();
                break;
            case Global.VIDEO:
                loadVideos();
                break;
        }

    }

    private void loadVideos(){
        // Set up an array of the Thumbnail Image ID column we want
        String[] projection = {MediaStore.Images.Thumbnails._ID};
        // Create the cursor pointing to the SDCard
        cc = managedQuery( MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                projection, // Which columns to return
                null,       // Return all rows
                null,
                MediaStore.Images.Thumbnails.IMAGE_ID);
        // Get the column index of the Thumbnails Image ID
        int columnIndex = cc.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
        // Move cursor to current position
        cc.moveToFirst();


        for (int i = 0; i < cc.getCount(); i++) {


            ImageView picturesView;
            picturesView = new ImageView(this);


            cc.moveToPosition(i);

            // Get the current value for the requested column
            int imageID = cc.getInt(columnIndex);
            // Set the content of the image based on the provided URI
            picturesView.setImageURI(Uri.withAppendedPath(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID));


            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size,size);
            layoutParams.setMargins(margin, margin, margin, margin);

            picturesView.setScaleType(ImageView.ScaleType.FIT_CENTER);


            gridview.addView(picturesView, layoutParams);
        }


    }

    private void loadImages(){

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



            for (int i = 0; i < cc.getCount(); i++) {
                Bitmap bmp = decodeURI(mUrls[i].getPath());
                //BitmapFactory.decodeFile(mUrls[position].getPath());





                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size,size);
               layoutParams.setMargins(margin, margin, margin, margin);

                LinearLayout rowLayout = new LinearLayout(GalleryActivity.this);

                FragmentManager fragMan = getFragmentManager();
                FragmentTransaction fragTransaction = fragMan.beginTransaction();

                rowLayout.setId(i);

// add rowLayout to the root layout somewhere here

                ImageFragment imageFragment = new ImageFragment();
                imageFragment.setBmp(bmp);
                fragTransaction.add(rowLayout.getId(), imageFragment, "fragment" + i);
                fragTransaction.commit();

//                rowLayout
                rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(selectionMap.get(v.getId()) == null){
                            selectionMap.put(v.getId(),1);
                            v.setBackgroundResource(R.drawable.border);
                        }else{
                            selectionMap.remove(v.getId());
                            v.setBackgroundResource(0);
                        }
                        doneStateCheck();
                    }
                });

                gridview.addView(rowLayout);

                //               gridview.addView(imageView,layoutParams);
            }


        }
    }

    public void doneStateCheck(){
        if(selectionMap.size() == 0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            doneMenu.setIcon(R.drawable.ic_done_white_24dp);
            doneState = DoneState.DONE;
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
        switch (doneState) {
            case CLEAR:
                onBackPressed();
                break;
            case DONE:
                Intent intent = new Intent();
                ArrayList<Uri> uriList = new ArrayList<Uri>();
                for (Map.Entry<Integer, Integer> entry : selectionMap.entrySet())
                {
                    uriList.add(mUrls[entry.getKey()]);
                }
                intent.putExtra("uriList",uriList);
                setResult(RESULT_OK, intent);
                onBackPressed();
                break;
        }
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
