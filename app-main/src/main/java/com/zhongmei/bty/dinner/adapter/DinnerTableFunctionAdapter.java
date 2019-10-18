package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.dinner.table.bean.FunctionBean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class DinnerTableFunctionAdapter extends BaseAdapter {
    private Context mContext;
    private List<FunctionBean> mListFunction;
    private LayoutInflater mInflater;
    private Set<Integer> mCheckFunction;    private OnFunctionChangeListener mFunctionChangeListener;
    private HoldView holdView = null;
    ;

    public DinnerTableFunctionAdapter(Context context, List<FunctionBean> data) {
        this.mContext = context;
        this.mListFunction = data;
        this.mInflater = LayoutInflater.from(context);
        this.mCheckFunction = new HashSet<>();
    }

    public void setFunctionChangeListener(OnFunctionChangeListener listener) {
        this.mFunctionChangeListener = listener;
    }

    public void setCacheItems(Set<Integer> checkItems) {
        if (checkItems != null) {
            mCheckFunction.addAll(checkItems);
        }
    }

    @Override
    public int getCount() {
        if (mListFunction == null) {
            return 0;
        }
        return mListFunction.size();
    }

    @Override
    public FunctionBean getItem(int position) {
        return mListFunction.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_lv_dinner_table_function, null);
            holdView = new HoldView();
            holdView.rb_function = (RadioButton) convertView.findViewById(R.id.rb_item);
            convertView.setTag(holdView);
        } else {
            holdView = (HoldView) convertView.getTag();
        }

        FunctionBean functionItem = getItem(position);
        holdView.rb_function.setFocusable(true);
        holdView.rb_function.setFocusableInTouchMode(true);
        holdView.rb_function.setText(functionItem.functionName);
        holdView.rb_function.setChecked(mCheckFunction.contains(functionItem.functionCode));
        holdView.rb_function.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onItemClick(position);
            }
        });
        return convertView;
    }

    public int getCheckFunctionNum() {
        return mCheckFunction.size();
    }

    public void onItemClick(int position) {
        FunctionBean functionItem = getItem(position);
        boolean contain = mCheckFunction.contains(functionItem.functionCode) ? mCheckFunction.remove(functionItem.functionCode) : mCheckFunction.add(functionItem.functionCode);
        if (mFunctionChangeListener != null) {
            mFunctionChangeListener.onFunctionChange(functionItem, mCheckFunction.contains(functionItem.functionCode));
        }
        notifyDataSetChanged();
    }

    class HoldView {
        public RadioButton rb_function;
    }

    public interface OnFunctionChangeListener {
        void onFunctionChange(FunctionBean function, boolean isCheck);
    }
}
