package com.anoki1.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.anoki1.prayer.PrayerDetailActivity;
import com.anoki1.R;
import com.anoki1.pojo.Media;
import com.anoki1.pojo.Prayer;

/**
 * Created by Administrator on 2015-08-07.
 */
public class MediaPagerAdapter extends PagerAdapter {


        LayoutInflater mLayoutInflater;
        Activity activity;

        Prayer prayer;

        public MediaPagerAdapter(Activity activity,Prayer prayer) {
            this.activity = activity;
            mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.prayer = prayer;
        }

        @Override
        public int getCount() {
            return prayer.media.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View itemView = mLayoutInflater.inflate(R.layout.layout_prayer_image_page, container, false);

            final Media media =prayer.media.get(position);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity, PrayerDetailActivity.class);
                    intent.putExtra("prayerId", prayer.id);
                    intent.putExtra("position", position);
                    activity.startActivityForResult(intent, Global.PRAYER);

                }
            });

            Util.setMediaView(itemView,media);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


}
