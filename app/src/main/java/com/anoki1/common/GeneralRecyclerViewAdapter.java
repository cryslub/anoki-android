package com.anoki1.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by joon on 2015-08-09.
 */
public class GeneralRecyclerViewAdapter<T,V extends ViewHolderBase<T>>  extends RecyclerView.Adapter<V >{

        private List<T> itemsData;
       protected List<T> visibleObjects;
        private Class<V> clazz;
        private int id;
        private ActivityBase activity;

        public GeneralRecyclerViewAdapter(List<T> itemsData,int id,Class<V> clazz) {
            this.itemsData = itemsData;
            this.id = id;
            this.clazz = clazz;
        }

        public GeneralRecyclerViewAdapter(List<T> itemsData,int id,Class<V> clazz,ActivityBase activity) {
            this.itemsData = itemsData;
            this.id = id;
            this.clazz = clazz;
            this.activity = activity;
        }


    public void updateList(List<T> itemsData){
            this.itemsData = itemsData;
            notifyDataSetChanged();
        }
        // Create new views (invoked by the layout manager)
        @Override
        public V onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View itemLayoutView = LayoutInflater.from(parent.getContext())
                    .inflate(id, null);

            // create ViewHolder

            V viewHolder = null;

            try {
                Constructor<V> constructor = clazz.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                viewHolder = constructor.newInstance(itemLayoutView);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            return viewHolder;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(V viewHolder, int position) {

            // - get data from your itemsData at this position
            // - replace the contents of the view with that itemsData

            final T t = itemsData.get(position);

            if(this.activity != null){
                viewHolder.attach(this.activity);
            }

            viewHolder.bind(t);


        }


        // Return the size of your itemsData (invoked by the layout manager)
        @Override
        public int getItemCount() {

            if(itemsData ==null) return 0;
            return itemsData.size();
        }

    public void setFilter(String queryText) {



        notifyDataSetChanged();

    }
}

