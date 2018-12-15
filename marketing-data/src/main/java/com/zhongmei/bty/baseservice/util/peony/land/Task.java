package com.zhongmei.bty.baseservice.util.peony.land;

import android.os.AsyncTask;
import android.support.v4.os.AsyncTaskCompat;

/**
 * Created by demo on 2018/12/15
 */

public abstract class Task<Result> {

    private static Executor simpleExecutor = new Executor() {

        @Override
        public <T> void execute(Task<T> task) {
            AsyncTaskCompat.executeParallel(new InnerAsyncTask<>(task));
        }

        class InnerAsyncTask<R> extends AsyncTask<Void, Void, R> {

            final Task<R> task;

            InnerAsyncTask(Task<R> task) {
                this.task = task;
            }

            @Override
            protected R doInBackground(Void... params) {
                return this.task.onExecute();
            }

            @Override
            protected void onPostExecute(R result) {
                super.onPostExecute(result);
                this.task.callback(result);
            }
        }
    };

    private static Executor params;

    private static Executor EXECUTOR = new Executor() {

        @Override
        public <T> void execute(Task<T> task) {
            if (params == null) {
                simpleExecutor.execute(task);
            } else {
                params.execute(task);
            }
        }
    };

    public static void reset(Executor executor) {
        params = executor;
    }

    private Callback<Result> callback;

    public final Result execute() {
        return onExecute();
    }

    public final void execute(Callback<Result> callback) {
        this.callback = callback;
        commit();
    }

    private void commit() {
        EXECUTOR.execute(this);
    }

    protected abstract Result onExecute();

    private void callback(Result result) {
        if (this.callback != null) {
            this.callback.callback(result);
        }
    }

    public interface Executor {
        <T> void execute(Task<T> task);
    }

    public interface Callback<Result> {
        void callback(Result result);
    }
}
