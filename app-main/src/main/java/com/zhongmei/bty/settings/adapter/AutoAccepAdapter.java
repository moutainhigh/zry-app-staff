package com.zhongmei.bty.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.basemodule.trade.entity.TradeDealSettingItem;
import com.zhongmei.yunfu.context.util.helper.SpHelper;
import com.zhongmei.bty.cashier.tradedeal.TradeDealServiceUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Date：2015年9月14日 下午6:14:31
 * @Description: 自动接单设置界面适配器
 * @Version: 1.0
 * <p>
 * rights reserved.
 */
public class AutoAccepAdapter extends BaseAdapter {
    private Context context;

    private List<TradeDealSettingItem> mDataSet;

    public AutoAccepAdapter(Context context) {
        this.context = context;
        mDataSet = new ArrayList<TradeDealSettingItem>();
    }

    @Override
    public int getCount() {
        return mDataSet.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSet.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.auto_accept_item, parent, false);
            viewHolder.toggleButton = (ToggleButton) convertView.findViewById(R.id.settings_accept_swtich);
            viewHolder.showTime = (TextView) convertView.findViewById(R.id.time);
            viewHolder.showSingles = (TextView) convertView.findViewById(R.id.singles);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        bindView(viewHolder, position);
        return convertView;
    }

    public void bindView(final ViewHolder viewHolder, int position) {
        TradeDealSettingItem tradeDealSettingItem = (TradeDealSettingItem) getItem(position);
        viewHolder.showTime.setText(context.getString(R.string.time_frame_msg, tradeDealSettingItem.getStartTime(), tradeDealSettingItem.getEndTime()));
        viewHolder.showSingles.setText(context.getString(R.string.order_number_msg, tradeDealSettingItem.getOrderNum()));
        // 设置项开关
        Set<String> keys =
                SpHelper.getDefault().getStringSet(Constant.SP_AUTO_ACCEPT_SWITCH, new HashSet<String>());
        final String key = TradeDealServiceUtil.generateAutoAcceptSettingKey(tradeDealSettingItem.getId());
        viewHolder.toggleButton.setChecked(keys.contains(key));
        viewHolder.toggleButton.setVisibility(View.VISIBLE);
        viewHolder.toggleButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                Set<String> set =
                        SpHelper.getDefault()
                                .getStringSet(Constant.SP_AUTO_ACCEPT_SWITCH, new HashSet<String>());
                if (viewHolder.toggleButton.isChecked()) {
                    set.add(key);
                    SpHelper.getDefault().putLong(key, System.currentTimeMillis());
                } else {
                    set.remove(key);
                    SpHelper.getDefault().remove(key);
                }
                SpHelper.getDefault().putStringSet(Constant.SP_AUTO_ACCEPT_SWITCH, set);
            }
        });
    }

    public void setDataSet(List<TradeDealSettingItem> dataSet) {
        mDataSet.clear();
        if (dataSet != null) {
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    public final class ViewHolder {
        ToggleButton toggleButton;

        TextView showTime;

        TextView showSingles;
    }
}
