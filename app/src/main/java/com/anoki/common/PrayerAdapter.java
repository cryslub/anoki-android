package com.anoki.common;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anoki.R;
import com.anoki.pojo.Prayer;
import com.anoki.pojo.Response;
import com.makeramen.dragsortadapter.DragSortAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by Administrator on 2015-07-22.
 */


class PrayerViewHolder extends  PrayerViewHolderBase implements View.OnLongClickListener {

    Prayer prayer;
    View view;

    @Bind(R.id.picture)ImageView picture;
    @Bind(R.id.name)
    TextView name;
    TextView text;
    TextView date;

    TextView prayCount;
    TextView replyCount;


    LinearLayout container;

    PrayerAdapter adapter;


    @Bind(R.id.picture_container)
    RelativeLayout pictureContainer;

    @OnClick(R.id.pray) void pray(){
        if (RestService.pray(prayer)) {
            ((PrayerAdapter.OnPrayListener) parentActivity).onPray();
        }
    }

    @OnClick(R.id.scrap) void scrap(){
        prayer.apiKey = Global.apiKey;
        Util.rest("prayer/scrap", "POST", prayer, Response.class);
        ((PrayerAdapter.OnPrayListener) parentActivity).onScrap();
    }



    public PrayerViewHolder(PrayerAdapter adapter, View itemLayoutView,Activity parentActivity) {
        super(adapter, itemLayoutView,parentActivity);

        ButterKnife.bind(this, itemLayoutView);

        view = itemLayoutView;
        text = (TextView) itemLayoutView.findViewById(R.id.text);
        date = (TextView) itemLayoutView.findViewById(R.id.date);
        prayCount = (TextView) itemLayoutView.findViewById(R.id.pray_count);
        replyCount = (TextView) itemLayoutView.findViewById(R.id.reply_count);


        container = (LinearLayout) itemLayoutView.findViewById(R.id.container);

        this.adapter = adapter;
        this.parentActivity = adapter.parentActivity;

    }

    public void bind(final Prayer prayer){

        super.bind(prayer);

        this.prayer = prayer;

        //view.setId(prayer.id);

        text.setText(prayer.simpleText());

        date.setText(prayer.long_time);
        prayCount.setText(prayer.prayCount);
        replyCount.setText(prayer.replyCount);

        if(prayer.checkPrayable()){
            pray.setImageResource(R.drawable.btn_pray_mint);
        }else{
            pray.setImageResource(R.drawable.btn_pray_gray);
        }

        if((prayer.userId != Global.me.id) && ("null".equals(prayer.scrapd) || prayer.scrapd == null)){
            pray.setVisibility(View.GONE);
        }else{
            scrap.setVisibility(View.GONE);
        }

        if(((PrayerAdapter.OnPrayListener) parentActivity).showPicture()){
            name.setText(prayer.userName);
            Util.setPicture(prayer.userPicture, picture, parentActivity.getResources().getDrawable(R.drawable.ic_person_black_48dp));
        }else{
            name.setVisibility(View.GONE);
            pictureContainer.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onLongClick(@NonNull View v) {

        if(adapter.editable) {
            startDrag();
        }
        return true;
    }
}
public class PrayerAdapter extends DragSortAdapter<PrayerViewHolder> {

    public List<Prayer> visibleObjects;
    protected List<Prayer> allObjects;
    public Activity parentActivity;
    public boolean editable = false;


    public void setFilter(String queryText) {
        visibleObjects = new ArrayList<>();
        for (Prayer item: allObjects) {
            if(item.text.contains(queryText) || item.userName.contains(queryText))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();

    }

    @Override
    public int getPositionForId(long id) {
        System.out.println("getPositionForId - " + id);
        for(final Prayer item : visibleObjects){
            if(item.id == id){
                return visibleObjects.indexOf(item);
            }
        }
        return 0;
    }

    @Override
    public boolean move(int fromPosition, int toPosition) {
        if (fromPosition < 0 || fromPosition >= visibleObjects.size()) return false;
        visibleObjects.add(toPosition, visibleObjects.remove(fromPosition));
        return true;
    }

    @Override
    public long getItemId(int position) {
        return visibleObjects.get(position).id;
    }



    public PrayerAdapter(RecyclerView recyclerView,List<Prayer> itemsData,Activity parentActivity)
    {
        super(recyclerView);

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
    public PrayerViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_prayer_row, null);

        // create ViewHolder


        PrayerViewHolder viewHolder = new PrayerViewHolder(this,itemLayoutView,parentActivity);
        itemLayoutView.setOnLongClickListener(viewHolder);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PrayerViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        final Prayer prayer = visibleObjects.get(position);

        System.out.println("getDraggingId -" + getDraggingId());

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
        public void onScrap();
    }


}
