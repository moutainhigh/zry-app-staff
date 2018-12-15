package com.zhongmei.atask;

import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
public class TaskManager implements FragmentLifecycle {

    private List<AsyncTaskProxy> taskList = new ArrayList<>();
    private TaskContext taskContext;
    private Object parent;

    public TaskManager(TaskContext taskContext, Object parent) {
        this.taskContext = taskContext;
        this.parent = parent;
    }

    public Object getParent() {
        return parent;
    }

    public <T> TaskCreator task(TaskBackground<T> background) {
        return new TaskCreator<>(this, background);
    }

    public <Progress, Result> void execute(AbsAsyncTask<Progress, Result> asyncTask) {
        execute(asyncTask, null);
    }

    public <Progress, Result> void execute(AbsAsyncTask<Progress, Result> asyncTask, String tag) {
        AsyncTaskProxy<Result> asyncTaskProxy = new AsyncTaskProxy<>(this, asyncTask, tag);
        addAsyncTask(asyncTaskProxy);
        TaskContext.execute(asyncTaskProxy);
    }

    public void addAsyncTask(AsyncTaskProxy asyncTask) {
        removeAsyncTask(asyncTask.getTag());
        taskList.add(asyncTask);
    }

    public void removeAsyncTask(String tag) {
        if (!TextUtils.isEmpty(tag)) {
            Iterator<AsyncTaskProxy> it = taskList.iterator();
            while (it.hasNext()) {
                AsyncTaskProxy task = it.next();
                if (tag.equals(task.getTag())) {
                    task.cancel(true);
                    task.getAsyncTask().cancel(true);
                    it.remove();
                }
            }
        }
    }

    public void removeAsyncTask(AsyncTaskProxy asyncTask) {
        taskList.remove(asyncTask);
    }

    public void cancelTask() {
        for (; taskList.size() > 0; ) {
            AsyncTaskProxy taskProxy = taskList.remove(0);
            AbsAsyncTask<?, ?> task = taskProxy.getAsyncTask();
            if (task.getStatus() != AsyncTask.Status.FINISHED) {
                task.cancel(true);
            }
        }
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {
        taskContext.removeTaskFragment(parent);
        cancelTask();
    }
}
