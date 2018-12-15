package com.zhongmei.bty.cashier.ordercenter.adapter;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;

/**
 * 简单自定义布局Adapter , <br>
 * 使用时直接new一个子类传入对应的{@link AdapterData}, {@link AdapterView}
 * 即可
 *
 * @param <T> List, Grid中的列表数据类型
 * @param <K> 列表中的自定义View
 * @date 2014-7-23
 */
public abstract class EsayAdapter<T extends TradeItemVo, K extends AdapterView<T>> extends BaseAdapter {
    private static final String TAG = EsayAdapter.class.getSimpleName();

    private List<T> mListData = new ArrayList<T>();
    ;

    private Context mContext;

    public EsayAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return mListData == null ? 0 : mListData.size();
    }

    @Override
    public T getItem(int position) {
        return mListData == null ? null : mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setList(List<T> list) {
        mListData.clear();
        if (list != null) {
            mListData.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void addItem(T t) {
        mListData.add(t);
        notifyDataSetChanged();
    }

    public List<T> getList() {
        return mListData;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        K adapterView = null;
        if (convertView == null) {
            try {
                adapterView =
                        (K) ((Class) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1]).getDeclaredMethod("build",
                                Context.class)
                                .invoke(null, mContext);
                convertView = adapterView.getView();
                convertView.setTag(adapterView);
            } catch (Exception e) {
                Log.e(TAG, "", e);
            }
        } else {
            adapterView = (K) convertView.getTag();
        }

        T data = getItem(position);
        adapterView.bindData(position, data);

        return convertView;
    }

}
