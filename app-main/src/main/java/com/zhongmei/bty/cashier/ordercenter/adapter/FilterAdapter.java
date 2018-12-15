package com.zhongmei.bty.cashier.ordercenter.adapter;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;

import com.zhongmei.bty.cashier.ordercenter.bean.FilterData;
import com.zhongmei.bty.cashier.ordercenter.view.FilterItemView;
import com.zhongmei.bty.cashier.ordercenter.view.FilterItemView_;
import com.zhongmei.bty.cashier.ordercenter.view.FilterTitleView;
import com.zhongmei.bty.cashier.ordercenter.view.FilterTitleView_;
import com.zhongmei.bty.snack.orderdish.adapter.RecyclerViewBaseAdapter;
import com.zhongmei.yunfu.ui.view.recycler.ViewWrapper;
import com.zhongmei.yunfu.util.ValueEnum;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */
@EBean
public class FilterAdapter extends RecyclerViewBaseAdapter<FilterData, View> {
    private static final int VIEW_TYPE_HEAD = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private List<Pair<String, ValueEnum>> selectPair = new ArrayList<>();

    @RootContext
    Context context;

    public List<Pair<String, ValueEnum>> getSelectItem() {
        return selectPair;
    }

    /**
     * 设置已经选择的item,用于重新弹框的状态恢复
     *
     * @param selectPair
     */
    public void setSelectCondition(List<Pair<String, ValueEnum>> selectPair) {
        this.selectPair = selectPair;
    }

    /**
     * 设置item选中
     *
     * @param position
     * @return
     */
    public void setItemSelect(int position) {
        Pair<String, ValueEnum> enumPair = (Pair<String, ValueEnum>) getItem(position);
        if (selectPair.isEmpty()) {//不存在直接添加
            selectPair.add(enumPair);
            return;
        }
        isItemSelect(enumPair, true);
        selectPair.add(enumPair);
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == VIEW_TYPE_HEAD) {
            view = FilterTitleView_.build(context);
        } else if (viewType == VIEW_TYPE_ITEM) {
            view = FilterItemView_.build(context);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Integer position = (Integer) v.getTag();
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(v, position);
                    }
                    Pair<String, ValueEnum> enumPair = (Pair<String, ValueEnum>) getItem(position);
                    if (isItemSelect(enumPair, true)) {//如果选中,取消选择状态
                    } else {
                        setItemSelect(position);
                    }
                    notifyDataSetChanged();
                }
            });
        }
        return view;
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (FilterData item : items) {
            count += item.getItemCount();
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return isHeader(position) ? VIEW_TYPE_HEAD : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(ViewWrapper<View> holder, final int position) {
        holder.setIsRecyclable(false);
        Object item = getItem(position);
        if (isHeader(position)) {
            FilterTitleView view = (FilterTitleView) holder.getView();
            view.bind((String) item);
            view.setTag(position);
        } else {
            FilterItemView view = (FilterItemView) holder.getView();
            Pair<String, ValueEnum> enumPair = (Pair<String, ValueEnum>) item;
            view.setSelect(isItemSelect(enumPair, false));
            view.bind(enumPair.first);
            view.setTag(position);
        }
    }

    public void clearSelectState() {
        selectPair.clear();
        notifyDataSetChanged();
    }

    /**
     * 判断Item是否已经选中
     *
     * @param enumPair
     * @param remove   是否从集合中移除该项
     * @return
     */
    private boolean isItemSelect(Pair<String, ValueEnum> enumPair, boolean remove) {
        for (Pair<String, ValueEnum> valueEnumPair : selectPair) {
            ValueEnum valueEnum = valueEnumPair.second;
            String valueEnumName = valueEnum.getClass().getSimpleName();
            ValueEnum compareEnum = enumPair.second;
            String compareName = compareEnum.getClass().getSimpleName();
            if (valueEnumName.equals(compareName)) {
                if (valueEnum.value() == compareEnum.value()) {
                    if (remove) {
                        selectPair.remove(valueEnumPair);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isHeader(int position) {
        Object o = selectByType(2, position);
        return o != null && (boolean) o;
    }

    @Override
    public Object getItem(int position) {
        return selectByType(1, position);
    }

    private Object selectByType(int type, int position) {
        int firstIndex = 0;
        for (FilterData data : items) {
            int size = data.getItemCount();
            int categoryIndex = position - firstIndex;
            if (type == 1) {
                if (categoryIndex < size) {
                    return data.getItem(categoryIndex);
                }
            } else if (type == 2) {
                if (categoryIndex == 0) {
                    return true;
                }
            }
            firstIndex += size;
        }
        return null;
    }
}
