package com.anoki1.prayer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.anoki1.fragment.ImageFragment;
import com.anoki1.R;
import com.anoki1.common.DoneState;
import com.anoki1.common.Global;
import com.anoki1.common.Util;
import com.anoki1.common.WriteActivityBase;
import com.anoki1.pojo.Prayer;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GalleryActivity extends WriteActivityBase {


    class ImageAdapter extends BaseAdapter {

        private Context mContext;


        private Uri[] mUrls = null;


        public Bitmap decodeURI(String filePath){

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);

            // Only scale if we need to
            // (16384 buffer for img processing)
            Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math.abs(options.outWidth - 100);

            // Do the actual decoding
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[512];
            options.inSampleSize = 8;

            Bitmap output = BitmapFactory.decodeFile(filePath, options);

            return output;
        }

        // Constructor
        public ImageAdapter(Context c,Uri[] mUrls){
            mContext = c;
            this.mUrls = mUrls;
        }

        public int getCount() {
            return mUrls.length;
        }

        public Object getItem(int position) {
            return mUrls[position];
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            View itemLayoutView = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_image, null);
            ImageView imageView = (ImageView) itemLayoutView.findViewById(R.id.image);

            Bitmap bmp = decodeURI(mUrls[position].getPath());
            imageView.setImageBitmap(bmp);

            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selectionMap.get(position) == null){

                        selectionMap.put(position, 1);

                        TextView number = (TextView) v.findViewById(R.id.number);
                        number.setText(selectionMap.size()+"");
                        number.setVisibility(View.VISIBLE);

//                v.setBackgroundResource(R.drawable.border);
                    }else{
                        selectionMap.remove(position);
                        v.findViewById(R.id.number).setVisibility(View.GONE);

//                v.setBackgroundResource(0);
                    }
                    doneStateCheck();
                }
            });

            ImageView zoom = (ImageView) itemLayoutView.findViewById(R.id.zoom);
            zoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,ZoomInActivity.class);
                    intent.putExtra("uri",mUrls[position]);
                    mContext.startActivity(intent);
                }
            });

            return itemLayoutView;
        }


    }



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

//        gridview = (TableLayout) findViewById(R.id.media_list);

///        size = Util.dpToPixel(getApplicationContext(),100);
 //       margin = Util.dpToPixel(getApplicationContext(),5);

        Intent intent = getIntent();
        int requestCode = intent.getIntExtra("requestCode", -1);

        switch (requestCode) {
            case Global.PHOTO: {

                loadImages();
            }
                break;
            case Global.VIDEO:
                loadVideos();
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
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

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        cc = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                orderBy + " DESC");

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


            GridView gridView = (GridView) findViewById(R.id.grid_view);

            // Instance of ImageAdapter Class
            gridView.setAdapter(new ImageAdapter(this,mUrls));

        }


    }

    private void loadImages2(){

        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        cc = this.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                orderBy + " DESC");

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

                new LoadImageTask(i).execute();
            }



        }
    }



      class LoadImageTask extends AsyncTask<String, Void, String> {

           Bitmap bmp;
          int id;

          public LoadImageTask(int id) {
              this.id= id;
          }

          @Override
          protected String doInBackground(String... params) {
              bmp = decodeURI(mUrls[id].getPath());
              return null;
          }

          // onPostExecute displays the results of the AsyncTask.
          @Override
          protected void onPostExecute(String result) {

              //BitmapFactory.decodeFile(mUrls[position].getPath());

              RelativeLayout itemView  = (RelativeLayout) getLayoutInflater().inflate(R.layout.layout_image, null);
              ImageView image = (ImageView) itemView.findViewById(R.id.image);
              image.setImageBitmap(bmp);

              add(itemView);

// add rowLayout to the root layout somewhere here

 //             imageFragment.setBmp(bmp);
//              imageFragment.setUri(mUrls[id]);


          }

      }

    private void add(RelativeLayout rowLayout){
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
            uriList.add(mUrls[entry.getKey()]);
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
