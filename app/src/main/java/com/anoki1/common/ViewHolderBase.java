package com.anoki1.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by joon on 2015-08-09.
 */
public abstract class ViewHolderBase<T> extends RecyclerView.ViewHolder implements Bindable<T> {


    protected ActivityBase activity;

    public ViewHolderBase(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void attach(ActivityBase activity){
        this.activity = activity;
    }
}
