package com.anoki1.common;

import android.os.AsyncTask;

/**
 * Created by joon on 2015-11-22.
 */
public class RestTask<T> extends AsyncTask<Task, Void, T> {

    private CallBack<T> callback;

    @Override
    protected T doInBackground(Task... params) {

        callback = (CallBack<T>) params[1];

        return params[0].execute();

    }
    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(T result) {
        callback.success(result);
    }

}
