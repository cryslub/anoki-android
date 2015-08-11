package com.anoki;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.ParentListener;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.common.WriteActivityBase;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryActivity extends WriteActivityBase {

    private DoneState doneState = DoneState.CLEAR;

    private static Uri[] mUrls = null;
    private static String[] strUrls = null;
    private String[] mNames = null;
    private TableLayout gridview = null;
    private Cursor cc = null;
    private Button btnMoreInfo = null;
    private ProgressDialog myProgressDialog = null;
    private Map<Integer,Integer> selectionMap = new HashMap<Integer,Integer>();

    private int size;
    private int margin;

    Cursor cursor;
    private ArrayList<VideoViewInfo> _videoRows;

    private View.OnClickListener thumbNailClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(selectionMap.get(v.getId()) == null){
                selectionMap.put(v.getId(), 1);

                v.findViewById(R.id.overlay).setVisibility(View.VISIBLE);
                TextView number = (TextView) v.findViewById(R.id.number);
                number.setText(selectionMap.size()+"");
                number.setVisibility(View.VISIBLE);

//                v.setBackgroundResource(R.drawable.border);
            }else{
                selectionMap.remove(v.getId());
                v.findViewById(R.id.overlay).setVisibility(View.GONE);
                v.findViewById(R.id.number).setVisibility(View.GONE);

//                v.setBackgroundResource(0);
            }
            doneStateCheck();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridview = (TableLayout) findViewById(R.id.media_list);

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

    class VideoViewInfo {
        String filePath;
        String mimeType;
        String thumbPath;
        String title;
    }

    private void loadVideos(){


        String targetPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath();

//        Toast.makeText(getApplicationContext(), targetPath, Toast.LENGTH_LONG)
  //              .show();
        String[] thumbColumns = { MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID };

        String[] mediaColumns = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE };

        cursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaColumns, null, null, null);

        ArrayList<VideoViewInfo> videoRows = new ArrayList<VideoViewInfo>();
        if (cursor.moveToFirst()) {
            do {

                VideoViewInfo newVVI = new VideoViewInfo();
                int id = cursor.getInt(cursor
                        .getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = managedQuery(
                        MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID
                                + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    newVVI.thumbPath = thumbCursor.getString(thumbCursor
                            .getColumnIndex(MediaStore.Video.Thumbnails.DATA));
                    Log.v("", newVVI.thumbPath);
                }

                newVVI.filePath = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                newVVI.title = cursor.getString(cursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.TITLE));
                Log.v("", newVVI.title);
                newVVI.mimeType = cursor
                        .getString(cursor
                                .getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE));
                Log.v("", newVVI.mimeType);
                videoRows.add(newVVI);
            } while (cursor.moveToNext());
        }
        _videoRows = videoRows;
        insertPhoto(videoRows);


    }
    void insertPhoto(ArrayList<VideoViewInfo> videoRows) {
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(size,size);
        layoutParams.setMargins(margin, margin, margin, margin);

        for (int i = 0; i < videoRows.size(); i++) {

            Bitmap bmThumbnail;

            bmThumbnail = ThumbnailUtils.createVideoThumbnail(
                    videoRows.get(i).filePath, MediaStore.Video.Thumbnails.MICRO_KIND);

            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // imageView.setBackgroundResource(R.drawable.canada);
            imageView.setImageBitmap(bmThumbnail);
            imageView.setPadding(20, 40, 20, 40);
            imageView.setTag(videoRows.get(i).filePath);

            imageView.setId(i+1000);
            imageView.setOnClickListener(thumbNailClickListener);

            add(imageView, layoutParams);

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

                rowLayout.setId(i+1000);

// add rowLayout to the root layout somewhere here

                ImageFragment imageFragment = new ImageFragment();
                imageFragment.setBmp(bmp);
                imageFragment.setUri(mUrls[i]);
                fragTransaction.add(rowLayout.getId(), imageFragment, "fragment" + i);
                fragTransaction.commit();

//                rowLayout
                rowLayout.setOnClickListener(thumbNailClickListener);

                add(rowLayout);
                //               gridview.addView(imageView,layoutParams);
            }


        }
    }


    private void add(LinearLayout rowLayout){
        TableRow tableRow = null;
        if(gridview.getChildCount() > 0) {
            tableRow = (TableRow) gridview.getChildAt(gridview.getChildCount() - 1);
            if(tableRow.getChildCount()>=3){
                tableRow = new TableRow(this);
                tableRow.addView(rowLayout);
                gridview.addView(tableRow);
            }else{

                tableRow.addView(rowLayout);
            }
        }else{
            tableRow = new TableRow(this);
            tableRow.addView(rowLayout);
            gridview.addView(tableRow);
        }
    }

    private void add(View view,TableLayout.LayoutParams layoutParams){
        TableRow tableRow = null;
        if(gridview.getChildCount() > 0) {
            tableRow = (TableRow) gridview.getChildAt(gridview.getChildCount() - 1);
            if(tableRow.getChildCount()>=3){
                tableRow = new TableRow(this);
                tableRow.addView(view,layoutParams);
                gridview.addView(tableRow);
            }else{

                tableRow.addView(view,layoutParams);
            }
        }else{
            tableRow = new TableRow(this);
            tableRow.addView(view,layoutParams);
            gridview.addView(tableRow);
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
    protected void confirm() {
        Intent intent = new Intent();
        ArrayList<Uri> uriList = new ArrayList<Uri>();
        for (Map.Entry<Integer, Integer> entry : selectionMap.entrySet())
        {
            uriList.add(mUrls[entry.getKey()-1000]);
        }
        intent.putExtra("uriList",uriList);
        setResult(RESULT_OK, intent);
        onBackPressed();
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
                    ? options.outHeight / 200
                    : options.outWidth / 200;
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
