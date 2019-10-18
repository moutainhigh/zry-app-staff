package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.view.OnControlListener;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class TablesAdapter extends RecyclerViewBaseAdapter<DinnertableModel, TableItemView> {
    @RootContext
    Context context;

    private OnControlListener mOnControlListener;

    public void setControlListener(OnControlListener onControlListener) {
        this.mOnControlListener = onControlListener;
    }

    @Override
    protected TableItemView onCreateItemView(ViewGroup parent, int viewType) {
        TableItemView view = TableItemView_.build(context);
        view.setmOnControlListener(mOnControlListener);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<TableItemView> holder, int position) {
        TableItemView view = holder.getView();
        DinnertableModel tableModel = (DinnertableModel) getItem(position);
        view.refreshUI(tableModel);
    }

}
