package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.db.entity.TaskRemind;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class TaskAdapter extends RecyclerViewBaseAdapter<TaskRemind, TaskItemView> {
    @RootContext
    Context context;

    TaskItemView.OnOperateListener mOperateListener;

    public void setOperateListener(TaskItemView.OnOperateListener operateListener) {
        this.mOperateListener = operateListener;
    }

    @Override
    protected TaskItemView onCreateItemView(ViewGroup parent, int viewType) {
        TaskItemView view = TaskItemView_.build(context);
        view.setmOperateListener(mOperateListener);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<TaskItemView> holder, int position) {
        TaskItemView view = holder.getView();
        TaskRemind task = (TaskRemind) getItem(position);
        view.refreshUI(task);
    }

}
