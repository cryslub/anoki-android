package com.anoki.common;

import android.content.Context;
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
import com.anoki.pojo.Media;

import java.util.List;

/**
 * Created by Administrator on 2015-08-07.
 */
public class MediaPagerAdapter extends PagerAdapter {


        Context mContext;
        LayoutInflater mLayoutInflater;

        List<Media> mediaList;

        public MediaPagerAdapter(Context context,List<Media> media) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            Media media =mediaList.get(position);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
            VideoView videoView = (VideoView) itemView.findViewById(R.id.video);


            if("I".equals(media.type)){
                Util.setPicture(media.id, imageView, null);
                videoView.setVisibility(View.GONE);
            }else{

                String path = "http://anoki.co.kr/anoki/images/video/"+media.id;
                Uri uri=Uri.parse(path);

                System.out.println(path);

                videoView.setVideoURI(uri);

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        // TODO Auto-generated method stub
                        mp.setLooping(true);
                        mp.start();
                    }
                });

                imageView.setVisibility(View.GONE);

            }

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
}
