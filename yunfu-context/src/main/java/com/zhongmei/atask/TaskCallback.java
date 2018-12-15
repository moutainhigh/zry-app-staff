package com.zhongmei.atask;

/**
 * Created by demo on 2018/12/15
 */
public interface TaskCallback<Result> {

    void onPreExecute();

    Result doInBackground(Void... params);

    void onPostExecute(Result result);

    void onCancelled(Result result);
}
