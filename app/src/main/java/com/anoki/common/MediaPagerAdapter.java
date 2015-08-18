package com.anoki.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.VideoView;

import com.anoki.R;
import com.anoki.ZoomInActivity;
import com.anoki.pojo.Media;

import java.util.List;

/**
 * Created by Administrator on 2015-08-07.
 */
public class MediaPagerAdapter extends PagerAdapter {


        LayoutInflater mLayoutInflater;
        Activity activity;

        List<Media> mediaList;

        public MediaPagerAdapter(Activity activity,List<Media> media) {
            this.activity = activity;
            mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mediaList = media;
        }

        @Override
        public int getCount() {
            return mediaList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = mLayoutInflater.inflate(R.layout.layout_prayer_image_page, container, false);

            final Media media =mediaList.get(position);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.zoom(media,activity);
                }
            });

            Util.setMediaView(itemView,media);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }


}
