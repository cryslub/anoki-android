package com.anoki.common;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Friend;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-07-22.
 */

public class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.ViewHolder> {
    protected List<Prayer> visibleObjects;
    protected List<Prayer> allObjects;
    private Activity parentActivity;

    public void setFilter(String queryText) {
        visibleObjects = new ArrayList<>();
        for (Prayer item: allObjects) {
            if(item.text.contains(queryText) || item.userName.contains(queryText))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();

    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView picture;
        TextView name;
        TextView text;
        TextView date;

        TextView prayCount;
        TextView replyCount;

        Button pray;
        Button scrap;

        LinearLayout container;



        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.name);
            text = (TextView) itemLayoutView.findViewById(R.id.text);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            prayCount = (TextView) itemLayoutView.findViewById(R.id.pray_count);
            replyCount = (TextView) itemLayoutView.findViewById(R.id.reply_count);
            pray = (Button) itemLayoutView.findViewById(R.id.pray);
            scrap = (Button) itemLayoutView.findViewById(R.id.scrap);

            picture = (ImageView) itemLayoutView.findViewById(R.id.picture);

            container = (LinearLayout) itemLayoutView.findViewById(R.id.container);

        }

        public void bind(final Prayer prayer){
            text.setText(prayer.text);
            date.setText(prayer.long_time);
            prayCount.setText(prayer.prayCount);
            replyCount.setText(prayer.replyCount);

            if("null".equals(prayer.scrapd) || prayer.scrapd == null){
                pray.setVisibility(View.GONE);
            }else{
                scrap.setVisibility(View.GONE);
            }


            pray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (RestService.pray(prayer)) {
                        ((OnPrayListener) parentActivity).onPray();
                    }
                }
            });

            scrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Util.rest("prayer/scrap","POST",prayer,Response.class);
                }
            });

            if(((OnPrayListener) parentActivity).showPicture()){
                name.setText(prayer.userName);
                Util.setPicture(prayer.userPicture, picture, parentActivity.getResources().getDrawable(R.drawable.ic_person_black_48dp));
            }else{
                container.removeView(name);
            }

        }
    }

    public PrayerAdapter(List<Prayer> itemsData,Activity parentActivity)
    {

        this.allObjects = itemsData;
        this.visibleObjects = itemsData;
        this.parentActivity = parentActivity;
    }

    public void updateList(List<Prayer> itemsData){
        this.allObjects = itemsData;
        flushFilter();
    }

    public void flushFilter(){
        visibleObjects=new ArrayList<>();
        visibleObjects.addAll(allObjects);
        notifyDataSetChanged();
    }


    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_prayer_row, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        final Prayer prayer = visibleObjects.get(position);

        viewHolder.bind(prayer);

    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        System.out.println(visibleObjects.size());
        return visibleObjects.size();
    }

    public interface OnPrayListener{
        public void onPray();
        public boolean showPicture();
    }


}
