package com.zhongmei.atask;

import android.os.AsyncTask;


public abstract class AbsAsyncTask<Progress, Result> extends AsyncTask<Void, Progress, Result> implements TaskCallback<Result> {

    @Override
    public void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    public abstract Result doInBackground(Void... params);

    @Override
    public void onPostExecute(Result result) {
        super.onPostExecute(result);
    }

    @Override
    public void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
    }

    @Override
    public void onCancelled(Result result) {
        super.onCancelled(result);
    }
}
