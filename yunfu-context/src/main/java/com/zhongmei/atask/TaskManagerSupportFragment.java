package com.zhongmei.atask;

import android.support.v4.app.Fragment;


public class TaskManagerSupportFragment extends Fragment implements ITaskManagerFragment {

    private TaskManager taskManager;

    public TaskManagerSupportFragment() {
            }

    @Override
    public void setTaskManager(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public TaskManager getTaskManager() {
        return taskManager;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (taskManager != null) {
            taskManager.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (taskManager != null) {
            taskManager.onStop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (taskManager != null) {
            taskManager.onDestroy();
        }
    }
}
