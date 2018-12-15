package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterChildTabView;
import com.zhongmei.bty.cashier.ordercenter.view.OrderCenterChildTabView_;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by demo on 2018/12/15
 */
@EBean
public class OrderCenterChildTabAdapter extends RecyclerViewBaseAdapter<Pair<String, Integer>, OrderCenterChildTabView> {

    @RootContext
    Context context;

    private int selectItem = -1;

    private boolean isCheck = true;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    @Override
    protected OrderCenterChildTabView onCreateItemView(ViewGroup parent, int viewType) {
        OrderCenterChildTabView view = OrderCenterChildTabView_.build(context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
                setSelectItem(position);
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<OrderCenterChildTabView> holder, final int position) {
        OrderCenterChildTabView view = holder.getView();
        Pair<String, Integer> item = items.get(position);
        view.bind(item.first);
        if (!isCheck || (item.second != DbQueryConstant.UNPROCESSED_NEW_ORDER && item.second != DbQueryConstant.UNPROCESSED_CANCEL_REQUEST)) {
            view.setNum(0);
        }
        if (selectItem == position) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
        view.setTag(position);
    }
}
