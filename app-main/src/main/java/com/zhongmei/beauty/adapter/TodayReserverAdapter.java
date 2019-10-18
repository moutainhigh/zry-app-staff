package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.booking.bean.BeautyBookingVo;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class TodayReserverAdapter extends RecyclerViewBaseAdapter<BeautyBookingVo, TodayReserverItemView> {
    @RootContext
    Context context;

    TodayReserverItemView.OnOperateListener mOperateListener;

    public void setOperateListener(TodayReserverItemView.OnOperateListener operateListener) {
        this.mOperateListener = operateListener;
    }

    @Override
    protected TodayReserverItemView onCreateItemView(ViewGroup parent, int viewType) {
        TodayReserverItemView view = TodayReserverItemView_.build(context);
        view.setmOperateListener(mOperateListener);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<TodayReserverItemView> holder, int position) {
        TodayReserverItemView view = holder.getView();
        BeautyBookingVo reserver = (BeautyBookingVo) getItem(position);
        view.refreshUI(reserver);
    }

}
