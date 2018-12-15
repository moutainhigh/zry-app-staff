package com.zhongmei.beauty.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.zhongmei.beauty.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.beauty.entity.SchedulingVo;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

/**
 * Created by demo on 2018/12/15
 * 技师／顾问／销售员 适配器
 */
@EBean
public class SchedulingAdapter extends RecyclerViewBaseAdapter<SchedulingVo, SchedulingItemView> {

    @RootContext
    protected Context mContext;


    public SchedulingAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    protected SchedulingItemView onCreateItemView(ViewGroup parent, int viewType) {
        SchedulingItemView itemView = SchedulingItemView_.build(mContext);
        return itemView;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<SchedulingItemView> holder, int position) {
        SchedulingItemView itemView = holder.getView();
    }
}
