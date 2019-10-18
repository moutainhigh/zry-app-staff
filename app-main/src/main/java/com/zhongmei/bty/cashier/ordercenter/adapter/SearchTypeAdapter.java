package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.bty.cashier.ordercenter.view.SearchTypeView;
import com.zhongmei.bty.cashier.ordercenter.view.SearchTypeView_;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;


@EBean
public class SearchTypeAdapter extends RecyclerViewBaseAdapter<String, SearchTypeView> {

    @RootContext
    Context context;

    private int selectItem = -1;

    public void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
        notifyDataSetChanged();
    }

    @Override
    protected SearchTypeView onCreateItemView(ViewGroup parent, int viewType) {
        SearchTypeView view = SearchTypeView_.build(context);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer position = (Integer) v.getTag();
                SearchTypeView view = (SearchTypeView) v;
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
                setSelectItem(position);
            }
        });
        return view;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<SearchTypeView> holder, final int position) {
        SearchTypeView view = holder.getView();
        view.bind(items.get(position));
        if (selectItem == position) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
        view.setTag(position);
    }

}
