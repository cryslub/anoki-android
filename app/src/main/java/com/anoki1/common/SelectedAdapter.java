package com.anoki1.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anoki1.R;
import com.anoki1.pojo.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015-10-12.
 */
public class SelectedAdapter extends ArrayAdapter<Friend> {
    protected List<Friend> visibleObjects;
    protected List<Friend> allObjects;

    private final Context context;

    public SelectedAdapter(Context context, int resource, List<Friend> objects) {
        super(context, resource, objects);
        this.allObjects = objects;
        this.visibleObjects = objects;
        this.context = context;

    }


    public void updateList(List<Friend> itemsData){
        clear();
        addAll(itemsData);
        this.allObjects = itemsData;
        flushFilter();
    }

    public void flushFilter(){
        visibleObjects=new ArrayList<>();
        visibleObjects.addAll(allObjects);

        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {

        visibleObjects = new ArrayList<>();
        for (Friend item: allObjects) {
            if(item.name.contains(queryText) || item.phone.contains(queryText))
                visibleObjects.add(item);
        }
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_selected_friend_row, parent, false);


        TextView name = (TextView) rowView.findViewById(R.id.name);
        ImageView picture = (ImageView) rowView.findViewById(R.id.picture);


        final Friend friend = visibleObjects.get(position);

        Util.setPicture(friend.picture,picture);
        name.setText(visibleObjects.get(position).name);


        return rowView;
    }
}
