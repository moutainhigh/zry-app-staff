package com.zhongmei.bty.snack.orderdish.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;



public abstract class RecyclerViewBaseAdapter<T, V extends View> extends RecyclerView.Adapter<ViewWrapper<V>> {
    protected List<T> items = new ArrayList<>();

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        int preSize = this.items.size();
        if (preSize > 0) {
            this.items.clear();
            notifyItemRangeRemoved(0, preSize);
        }
        this.items.addAll(items);
        notifyItemRangeChanged(0, items.size());
    }

    protected OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public ViewWrapper<V> onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewWrapper<V>(onCreateItemView(parent, viewType));
    }


    protected abstract V onCreateItemView(ViewGroup parent, int viewType);

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return (Utils.isEmpty(items) || items.size() <= position) ? null : items.get(position);
    }
}
