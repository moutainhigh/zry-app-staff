package com.zhongmei.bty.common.util;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by demo on 2018/12/15
 */
public abstract class ExtraAsyncTask<Param, Progress, Result> extends AsyncTask<Param, Progress, Result> {

    private static final String TAG = ExtraAsyncTask.class.getSimpleName();

    private final Object[] objects;

    public ExtraAsyncTask(Object... objects) {
        this.objects = objects;
    }

    public <T> T get(int position) {
        try {
            return (T) getObject(position);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            return null;
        }
    }

    public Object getObject(int position) {
        return (objects == null || objects.length <= position) ? null : objects[position];
    }
}
