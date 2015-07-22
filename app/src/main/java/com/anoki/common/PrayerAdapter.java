package com.anoki.common;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Prayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-07-22.
 */

public class PrayerAdapter extends RecyclerView.Adapter<PrayerAdapter.ViewHolder> {
    protected List<Prayer> visibleObjects;
    protected List<Prayer> allObjects;
    private Activity parentActivity;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView text;
        TextView date;

        TextView prayCount;
        TextView replyCount;

        Button pray;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            text = (TextView) itemLayoutView.findViewById(R.id.text);
            date = (TextView) itemLayoutView.findViewById(R.id.date);
            prayCount = (TextView) itemLayoutView.findViewById(R.id.pray_count);
            replyCount = (TextView) itemLayoutView.findViewById(R.id.reply_count);
            pray = (Button) itemLayoutView.findViewById(R.id.pray);

        }

        public void bind(final Prayer prayer){
            text.setText(prayer.text);
            date.setText(prayer.time);
            prayCount.setText(prayer.prayCount);
            replyCount.setText(prayer.replyCount);

            pray.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(RestService.pray(prayer)){
                        ((OnPrayListener) parentActivity).onPray();
                    }
                }
            });

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
    }

}
