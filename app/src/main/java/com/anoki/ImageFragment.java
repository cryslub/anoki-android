package com.anoki;

import android.app.Activity;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageFragment extends Fragment {

    private Bitmap bmp;
    private boolean selected= false;

    public ImageFragment(){

    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_image, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageBitmap(bmp);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected){
                    selected = false;
                    view.setBackgroundResource(0);
                }else{
                    view.setBackgroundResource(R.drawable.border);
                    selected =true;
                }
            }
        });

        return view;
    }


}
