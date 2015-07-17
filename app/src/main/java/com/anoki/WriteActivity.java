package com.anoki;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.anoki.common.CallBack;
import com.anoki.common.DoneState;
import com.anoki.common.Global;
import com.anoki.common.RestService;
import com.anoki.common.SubActivityBase;
import com.anoki.common.Util;
import com.anoki.pojo.Prayer;

import org.apmem.tools.layouts.FlowLayout;

import java.util.List;

public class WriteActivity extends SubActivityBase implements PrayerImageFragment.OnFragmentInteractionListener {



    Prayer prayer = new Prayer();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                doneStateCheck();
            }
        };

        EditText back = (EditText) findViewById(R.id.back);
        back.addTextChangedListener(textWatcher);

        EditText text = (EditText) findViewById(R.id.text);
        text.addTextChangedListener(textWatcher);


    }

    public void doneStateCheck(){
        EditText back = (EditText) findViewById(R.id.back);
        EditText text = (EditText) findViewById(R.id.text);

        if(back.getText().length() == 0 && text.getText().length() == 0){
            doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
            doneState = DoneState.CLEAR;
        }else{
            CheckBox pub = (CheckBox) findViewById(R.id.pub);
            if(pub.isChecked()){
                doneMenu.setIcon(R.drawable.ic_done_white_24dp);
                doneState = DoneState.DONE;

            }else{
                doneMenu.setIcon(R.drawable.ic_arrow_forward_white_24dp);
                doneState = DoneState.NEXT;

            }
        }
    }


    public void done(MenuItem item){

        EditText back = (EditText) findViewById(R.id.back);
        EditText text = (EditText) findViewById(R.id.text);
        CheckBox pub = (CheckBox) findViewById(R.id.pub);

        prayer.back = back.getText().toString();
        prayer.text = text.getText().toString();
        prayer.pub = pub.isChecked()?"Y" : "N";


        switch (doneState){
            case CLEAR:
                onBackPressed();
                break;
            case DONE:
            {
                RestService.makePrayer(prayer);
                succeed();
            }
                break;
            case NEXT:
                Intent intent = new Intent(this,ChooseFriendsActivity.class);
                intent.putExtra("prayer",prayer);
                startActivityForResult(intent, Global.FRIENDS);
                break;
        }
    }


    public void pub(View view){
        doneStateCheck();
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
            Intent intent = new Intent(this,GalleryActivity.class);
            intent.putExtra("requestCode", Global.VIDEO);
            startActivityForResult(intent, Global.VIDEO);
        }

    }

    public void camera(View view){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Global.CAMERA);
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

        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.PHOTO:
                    photoResult( data);
                    break;
                case Global.VIDEO:
                    break;
                case Global.CAMERA:
                    break;
                case Global.FRIENDS:
                    succeed();
                    break;

            }
        }
    }

    private void cameraResult(int resultCode, Intent data) {
        Uri currImageURI = data.getData();
    //    tv.setText("CAMERA : " + getRealPathFromURI(currImageURI));
        Util.upload(currImageURI, getContentResolver(), new CallBack() {
            @Override
            public void success(String id) {

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
//                    imageFragment.setUri(mUrls[i]);
                    fragTransaction.add(rowLayout.getId(), imageFragment, "fragment" + id);
                    fragTransaction.commit();


                    flowLayout.addView(rowLayout,layoutParams);

                }
            });
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
