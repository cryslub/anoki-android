package com.anoki1.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.anoki1.R;
import com.anoki1.prayer.ZoomInActivity;

import java.lang.reflect.Field;


public class ImageFragment extends Fragment{

    private Bitmap bmp;
    private boolean selected= false;
    private Uri uri;

    public ImageFragment(){

    }

    public void setBmp(Bitmap bmp) {
        this.bmp = bmp;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
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

        final ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setImageBitmap(bmp);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        ImageView zoom = (ImageView) view.findViewById(R.id.zoom);
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ZoomInActivity.class);
                intent.putExtra("uri",uri);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
