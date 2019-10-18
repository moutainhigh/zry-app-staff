package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.entity.UnpaidTradeVo;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class UnpaidTradeAdapter extends RecyclerViewBaseAdapter<UnpaidTradeVo, UnpaidTradeItemView> {
    @RootContext
    Context context;

    private UnpaidTradeItemView.OnOperateListener mOperateListener;

    public void setOperateListener(UnpaidTradeItemView.OnOperateListener operateListener) {
        this.mOperateListener = operateListener;
    }

    @Override
    protected UnpaidTradeItemView onCreateItemView(ViewGroup parent, int viewType) {
        UnpaidTradeItemView view = UnpaidTradeItemView_.build(context);
        view.setmOperateListener(mOperateListener);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<UnpaidTradeItemView> holder, int position) {
        UnpaidTradeItemView view = holder.getView();
        UnpaidTradeVo tradeVo = (UnpaidTradeVo) getItem(position);
        view.refreshUI(tradeVo);
    }

}
