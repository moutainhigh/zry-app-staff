package com.zhongmei.atask;


public interface TaskCallback<Result> {

    void onPreExecute();

    Result doInBackground(Void... params);

    void onPostExecute(Result result);

    void onCancelled(Result result);
}
