package com.anoki.common;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.anoki.PrayerImageFragment;
import com.anoki.R;

/**
 * Created by joon on 2015-07-25.
 */
public abstract class WriteActivityBase extends SubActivityBase implements PrayerImageFragment.OnFragmentInteractionListener{

    protected  String pictureId;


    protected MenuItem doneMenu;
    protected DoneState doneState = DoneState.CLEAR;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_write, menu);

        doneMenu = menu.findItem(R.id.action_done);

        switch (doneState){
            case NEXT:
                doneMenu.setIcon(R.drawable.ic_arrow_forward_white_24dp);
                break;
            case DONE:
                doneMenu.setIcon(R.drawable.ic_done_white_24dp);
                break;
        }

        return true;
    }


    public void done(MenuItem menuItem) {
        switch (doneState){
            case DONE:
                confirm();
                break;
            case CLEAR:
                finish();
                break;
            case NEXT:
                next();
                break;
        }

    }

    public void photo(View view){
        Intent photoLibraryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoLibraryIntent.setType("image/*");
        startActivityForResult(photoLibraryIntent, Global.PHOTO);

    }

    protected void setDonState(DoneState doneState){

        this.doneState = doneState;

        switch (doneState){
            case CLEAR:
                doneMenu.setIcon(R.drawable.ic_clear_white_24dp);
                break;
            case DONE:
                doneMenu.setIcon(R.drawable.ic_done_white_24dp);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Global.PHOTO:
                    final Activity self = this;
                    Util.upload(data.getData(), getContentResolver(), new CallBack() {
                        @Override
                        public void success(String id) {
                            pictureId = id;
                            ViewGroup flowLayout = (ViewGroup) findViewById(R.id.media_list);
                            flowLayout.removeAllViews();
                            Util.addMedia(self, id);
                        }
                    });
                    break;

            }
        }
    }



    @Override
    public void onDeleteFragment(String id) {
        pictureId = null;
    }


    protected void confirm(){

    }

    protected void next(){

    }
}
