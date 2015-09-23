package com.anoki;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anoki.common.CallBack;
import com.anoki.common.Common;
import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.Util;
import com.anoki.common.WriteActivityBase;
import com.anoki.pojo.Media;
import com.anoki.pojo.Prayer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apmem.tools.layouts.FlowLayout;
import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class WriteActivity extends WriteActivityBase implements PrayerImageFragment.OnFragmentInteractionListener {

    class PubInfo{
        String key;
        String next;
        String text;

        PubInfo(String key,String next,String text){
            this.key = key;
            this.next = next;
            this.text = text;
        }
    }

    Prayer prayer = new Prayer();

    Map<String,Media> mediaMap = new HashMap<String,Media>();
    boolean edit = false;

    @Bind(R.id.pub)
    TextView pub;

    @Bind(R.id.text)
    EditText text;


    Map<String,PubInfo> pubMap = new HashMap<String,PubInfo>(){{
        put("전체공개",new PubInfo("P","친구선택","내가 선택한 사람들에게만 기도가 보여집니다."));
        put("친구선택",new PubInfo("F","나만보기","나만 볼 수 있습니다"));
        put("나만보기",new PubInfo("S","전체공개","내 모든 친구들에게 내 기도가 보여집니다"));
    }};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        final TextWatcher textWatcher  = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String lineSep = System.getProperty("line.separator");

                String str = text.getText().toString();
                String arr[] = str.split(lineSep);
                String merge = "";
                int i = 1;
                for(String line : arr){
                    merge +=i+". "+line+lineSep;
                    i++;
                }

                text.removeTextChangedListener(this);
                int position = text.getSelectionStart();
                text.setText(merge);
                text.setSelection(position);
                text.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(doneMenu !=null)
                    doneStateCheck();
            }
        };




        EditText back = (EditText) findViewById(R.id.back);

        text.addTextChangedListener(textWatcher);


        getFragmentManager().beginTransaction().commitAllowingStateLoss();

        Intent intent = getIntent();
        Prayer prayer = (Prayer) intent.getSerializableExtra("prayer");
        if(prayer !=null){
            edit = true;
            this.prayer = prayer;
            back.setText(prayer.back);
            text.setText(prayer.text);
            pub.setText(Common.pubKeyMap.get(prayer.pub));

            for(Media media : prayer.media ){
                addMedia(media.id);
            }
        }

    }

    @OnTextChanged(R.id.back)
    void format(){
        if(doneMenu !=null)
            doneStateCheck();
    }

    public void doneStateCheck(){
        EditText back = (EditText) findViewById(R.id.back);
        EditText text = (EditText) findViewById(R.id.text);

        if(back.getText().length() == 0 && text.getText().length() == 0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            if("전체공개".equals(pub.getText().toString())|| "나만보기".equals(pub.getText().toString()) || edit){
                doneMenu.setIcon(R.drawable.ic_done_white_24dp);
                doneState = DoneState.DONE;

            }else{
                doneMenu.setIcon(R.drawable.ic_arrow_forward_white_24dp);
                doneState = DoneState.NEXT;

            }
        }
    }


    @Override
    protected void confirm() {
        if(edit) {
            Util.rest("prayer","PUT",prayer);
        }else{
            RestService.makePrayer(prayer);
        }
        succeed();

    }

    public void done(MenuItem item){

        EditText back = (EditText) findViewById(R.id.back);
        EditText text = (EditText) findViewById(R.id.text);

        prayer.back = back.getText().toString();
        prayer.text = text.getText().toString();

        prayer.pub = pubMap.get(pub.getText().toString()).key;


        prayer.media = new ArrayList<Media>(mediaMap.values());

        switch (doneState){
            case CLEAR:
                onBackPressed();
                break;
            case DONE:
            {
                confirm();
            }
                break;
            case NEXT:
                Intent intent = new Intent(this,ChooseContactsActivity.class);
                intent.putExtra("prayer",prayer);
                startActivityForResult(intent, Global.FRIENDS);
                break;
        }
    }

    @OnClick(R.id.pub)
    public void pub(View view){

        pub.setText(pubMap.get(pub.getText().toString()).next);

        toast(pubMap.get(pub.getText().toString()).text);

        doneStateCheck();

    }


    public void photo(View view){
        if(checkContents()) {
            Intent intent = new Intent(this,GalleryActivity.class);
            intent.putExtra("requestCode", Global.PHOTO);
            startActivityForResult(intent, Global.PHOTO);

//            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
 //           photoPickerIntent.setType("image/*");
 //           photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
  //          startActivityForResult(photoPickerIntent, PHOTO);
        }
    }

    public void video(View view){
        if(checkContents()) {
//            Intent intent = new Intent(this,GalleryActivity.class);
//            intent.putExtra("requestCode", Global.VIDEO);
 //           startActivityForResult(intent, Global.VIDEO);
            Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoLibraryIntent.setType("video/*");
            startActivityForResult(photoLibraryIntent, Global.VIDEO);

        }

    }

    public void camera(View view){
        if(checkContents()) {
            Intent intent = new Intent();
            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, Global.CAMERA);
        }
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

    public void showOptions(View view){
        LinearLayout options = (LinearLayout) findViewById(R.id.options);
        options.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


//        super.onActivityResult(requestCode, resultCode, data);
        doneStateCheck();

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.PHOTO:
                    photoResult( data);
                    break;
                case Global.VIDEO:
                    videoResult(data);
                    break;
                case Global.CAMERA:
                    cameraResult(data);
                    break;
                case Global.FRIENDS:
                    succeed();
                    break;

            }
        }
    }
    private void videoResult(Intent data) {
        Uri selectedImageUri = data.getData();
        final String selectedPath = getRealPathFromURI(selectedImageUri);

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(selectedPath, MediaStore.Video.Thumbnails.MICRO_KIND);


        Util.uploadBitmap(bitmap, new CallBack() {

            @Override
            public void success(String id) {
                try {
                    uploadVideo(selectedPath, id);
                    addMedia(id);
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },"V");

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String mString = cursor.getString(column_index);

        return mString;

    }

    private String uploadVideo(String videoPath,String id) throws ParseException, IOException {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://anoki.co.kr/anoki/rest/prayer/upload/video");

        FileBody filebodyVideo = new FileBody(new File(videoPath));

        StringBody idBody = new StringBody(id);

        MultipartEntity reqEntity = new MultipartEntity();
        reqEntity.addPart("uploaded", filebodyVideo);
        reqEntity.addPart("id", idBody);

        httppost.setEntity(reqEntity);

        // DEBUG
        System.out.println( "executing request " + httppost.getRequestLine( ) );
        HttpResponse response = httpclient.execute( httppost );
        HttpEntity resEntity = response.getEntity( );

        // DEBUG
        System.out.println( response.getStatusLine( ) );
        if (resEntity != null) {
            return  EntityUtils.toString(resEntity) ;
        } // end if

        if (resEntity != null) {
            resEntity.consumeContent( );
        } // end if

        httpclient.getConnectionManager( ).shutdown( );

        return "";
    } // end of uploadVideo( )


    private void cameraResult(Intent data) {

        Bitmap bmp = (Bitmap)data.getExtras().get("data");

    //    tv.setText("CAMERA : " + getRealPathFromURI(currImageURI));
        Util.uploadBitmap(bmp, new CallBack() {
            @Override
            public void success(String id) {
                addMedia(id);

            }
        });
    }

    private void photoResult(Intent data){
        List<Uri> uriList = (List<Uri>) data.getSerializableExtra("uriList");

        for(Uri uri : uriList) {
            Uri fullUri = Uri.parse("file://" + uri);
            Util.upload(fullUri, getContentResolver(), new CallBack() {
                @Override
                public void success(String id) {
                    //media list 에 추가

                    addMedia(id);

                }
            });
        }
    }

    private void addMedia(String id){
        ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);

        ImageView imageView = new ImageView(WriteActivity.this);
        Bitmap bmp = Util.fetchImage(id);
        imageView.setImageBitmap(bmp);


        int size = Util.dpToPixel(getApplicationContext(), 80);
        int margin = Util.dpToPixel(getApplicationContext(), 5);
//                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size, size);
//                    layoutParams.setMargins(margin, margin, margin, margin);

//                            imageView.setLayoutParams(layoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);


        //                  flowLayout.addView(imageView, layoutParams);




        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(size,size);
        layoutParams.setMargins(margin, margin, margin, margin);

        LinearLayout rowLayout = new LinearLayout(WriteActivity.this);

        FragmentManager fragMan = getFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();

        rowLayout.setId(Integer.parseInt(id));

// add rowLayout to the root layout somewhere here

        PrayerImageFragment imageFragment = new PrayerImageFragment();
        imageFragment.setBmp(bmp);
        imageFragment.setId(id);
//                    imageFragment.setUri(mUrls[i]);
        fragTransaction.add(rowLayout.getId(), imageFragment, "fragment" + id);
        fragTransaction.commit();


        flowLayout.addView(rowLayout, layoutParams);

        mediaMap.put(id,new Media(id));

    }


    @Override
    public void onDeleteFragment(String id) {
        mediaMap.remove(id);
        doneStateCheck();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

}
