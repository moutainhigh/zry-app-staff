package com.zhongmei.bty.dinner.table.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Date：2017/11/17 14:54
 */
public abstract class TablesSeatingAreaPagerAdapter extends PagerAdapter {
    private static final String TAG = TablesSeatingAreaPagerAdapter.class.getSimpleName();

    public static final int PAGE_SIZE = 5;// 每页显示队列

    public static final int STYLE_SEATING = 0;

    public static final int STYLE_CONNECT = 1;

    public Map<Long, Boolean> mDataMap;

    private Context mContext;

    private LayoutInflater mInflater;

    private int[] ItemIds = {R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5};

    private View[] itemViews = new View[ItemIds.length];

    private Long afterData;

    private Long firstData;

    private int number = 1;

    private int style = STYLE_SEATING;

    private Map<Long, TradeExtra> mainTradeExtraMap;

    public void setMainTradeExtraMap(Map<Long, TradeExtra> mainTradeExtraMap) {
        this.mainTradeExtraMap.clear();
        if (mainTradeExtraMap != null)
            this.mainTradeExtraMap.putAll(mainTradeExtraMap);
    }

    public Long getFirstData() {
        return firstData;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public TablesSeatingAreaPagerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        mDataMap = new TreeMap<Long, Boolean>(new Comparator<Long>() {
            public int compare(Long obj1, Long obj2) {
                return obj1.compareTo(obj2);
            }
        });
        mainTradeExtraMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return (mDataMap.size() + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.batch_operation_tables_area_page, container, false);
        for (int i = 0; i < ItemIds.length; i++) {
            itemViews[i] = view.findViewById(ItemIds[i]);
            itemViews[i].setVisibility(View.INVISIBLE);
        }
        List<Long> subList = getSubDataSet(position);
        int size = subList.size();
        for (int i = 0; i < size; i++) {
            // 数据
            final Long data = subList.get(i);
            // 控件
            View itemView = itemViews[i];
            // 名称
            TextView tvName = (TextView) itemView.findViewById(R.id.table_area_name);
            if (style == STYLE_CONNECT) {
                String serialNumber = mainTradeExtraMap.get(data).getSerialNumber();
                tvName.setText(String.format(mContext.getString(R.string.batch_operation_table_connect_num), serialNumber));
                number++;
            } else {
                if (data.longValue() == 0L) {
                    tvName.setText(mContext.getString(R.string.all));
                } else {
                    tvName.setText(String.format(mContext.getString(R.string.batch_operation_table_people), data));
                }
            }
            itemView.setTag(data);
            itemView.setVisibility(View.VISIBLE);
            setSelected(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Long data = (Long) v.getTag();
                    mDataMap.put(afterData, false);
                    mDataMap.put(data, true);
                    afterData = data;
                    notifyDataSetChanged();
                    selectArea(data);
                }
            });
        }

        container.addView(view);
        return view;
    }

    private void setSelected(View itemView) {
        Long data = (Long) itemView.getTag();
        TextView tvName = (TextView) itemView.findViewById(R.id.table_area_name);
        TextView tvSelect = (TextView) itemView.findViewById(R.id.select);
        if (mDataMap.get(data)) {
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            itemView.setBackgroundResource(R.color.group_area_and_time_masking);
            tvSelect.setVisibility(View.VISIBLE);
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.settings_grayword));
            tvSelect.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 选择桌位数
     *
     * @param data
     */
    public abstract void selectArea(Long data);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void setDataSet(Set<Long> dataSet) {
        mDataMap.clear();
        number = 1;
        if (dataSet == null) {
            notifyDataSetChanged();
            return;
        }
        List<Long> dataList = new ArrayList<Long>(dataSet);
        for (int i = 0; i < dataList.size(); i++) {
            mDataMap.put(dataList.get(i), false);
        }
        if (mDataMap.keySet().iterator().hasNext()) {
            firstData = mDataMap.keySet().iterator().next();
            afterData = firstData;
            mDataMap.put(firstData, true);
        }
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    private List<Long> getSubDataSet(int position) {
        int start = position * PAGE_SIZE;
        int end = Math.min((position + 1) * PAGE_SIZE, mDataMap.size());
        return new ArrayList<>(new ArrayList<Long>(mDataMap.keySet()).subList(start, end));
    }
}
