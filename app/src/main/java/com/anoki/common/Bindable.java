package com.anoki.common;

import android.app.Activity;

/**
 * Created by joon on 2015-08-09.
 */
public interface Bindable<T> {

    public void bind(T t);
    public void attach(ActivityBase activity);
}
