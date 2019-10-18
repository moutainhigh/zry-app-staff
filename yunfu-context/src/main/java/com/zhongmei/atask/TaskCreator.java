package com.zhongmei.atask;


public class TaskCreator<T> {

    private TaskManager taskManager;
    private TaskBackground<T> background;
    private String tag;

    public TaskCreator(TaskManager taskManager, TaskBackground<T> background) {
        this.taskManager = taskManager;
        this.background = background;
    }

    public TaskCreator setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public void execute() {
        execute(new TaskResult<T>() {
            @Override
            public void onResult(T result) {

            }
        });
    }

    public void execute(final TaskResult<T> result) {
        taskManager.execute(new SimpleAsyncTask<T>() {
            @Override
            public T doInBackground(Void... params) {
                return background.doInBackground();
            }

            @Override
            public void onPostExecute(T t) {
                super.onPostExecute(t);
                result.onResult(t);
            }
        }, tag);
    }
}
