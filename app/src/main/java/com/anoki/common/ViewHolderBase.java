package com.anoki.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by joon on 2015-08-09.
 */
public abstract class ViewHolderBase<T> extends RecyclerView.ViewHolder implements Bindable<T> {

    public ViewHolderBase(View itemView) {
        super(itemView);
    }
}
