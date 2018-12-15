package com.zhongmei.bty.snack.orderdish.adapter;

import java.util.List;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;
import com.zhongmei.bty.snack.orderdish.view.ExtraOrderItemView;
import com.zhongmei.bty.snack.orderdish.view.ExtraOrderItemView_;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by demo on 2018/12/15
 */
@EBean
public class ExtraOrderAdapter extends
        RecyclerViewBaseAdapter<String, ExtraOrderItemView> {

    @RootContext
    Context context;

    @Override
    protected ExtraOrderItemView onCreateItemView(ViewGroup parent,
                                                  int viewType) {
        ExtraOrderItemView view = ExtraOrderItemView_.build(context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
                }
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<ExtraOrderItemView> holder,
                                 final int position) {
        holder.setIsRecyclable(false);
        ExtraOrderItemView view = holder.getView();
        view.bind(items.get(position));
        view.setTag(position);
    }

    @Override
    public void setItems(List<String> items) {
        int preSize = this.items.size();
        if (preSize > 0) {
            this.items.clear();
        }
        this.items.addAll(items);
        notifyDataSetChanged();
    }
}
