package com.zhongmei.atask;


public final class AsyncTaskProxy<Result> extends AbsAsyncTask<Void, Result> {

    private TaskManager taskManager;
    private AbsAsyncTask<?, Result> asyncTask;
    private String tag;

    public AsyncTaskProxy(TaskManager fragment, AbsAsyncTask<?, Result> asyncTask, String tag) {
        this.taskManager = fragment;
        this.asyncTask = asyncTask;
        this.tag = tag;
    }

    public AbsAsyncTask<?, Result> getAsyncTask() {
        return asyncTask;
    }

    public String getTag() {
        if (tag == null) {
            String className = asyncTask.getClass().getName();
            tag = className.substring(className.lastIndexOf('.') + 1);
        }

        String targetTag = "proxy";
        if (taskManager.getParent() != null) {
            targetTag = taskManager.getParent().getClass().getName();
        }

        return String.format("%s[%s]", targetTag, tag);
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        asyncTask.onPreExecute();
    }

    @Override
    public Result doInBackground(Void... params) {
        if (isCancelledProxy()) {
            return null;
        }
        return asyncTask.doInBackground(params);
    }

    @Override
    public void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (isCancelledProxy()) {
            onCancelled(result);
            return;
        }
        asyncTask.onPostExecute(result);
        taskManager.removeAsyncTask(this);
    }

    @Override
    public void onCancelled(Result result) {
        super.onCancelled(result);
        asyncTask.onCancelled(result);
        taskManager.removeAsyncTask(this);
    }

    public boolean isCancelledProxy() {
        return isCancelled() || asyncTask.isCancelled();
    }
}
