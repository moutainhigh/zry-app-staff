package com.zhongmei.bty.dinner.ordercenter.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.bty.basemodule.trade.bean.TablesAreaVo;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;

public abstract class TablesAreaAdapter extends PagerAdapter {
    private static final String TAG = TablesAreaAdapter.class.getSimpleName();

    private List<TablesAreaVo> mDataSet;

    private Context mContext;

    private LayoutInflater mInflater;

    private int layoutResId;
    private int[] ItemResIds;
    private View[] itemViews;

    public TablesAreaAdapter(Context context) {
        this(context, R.layout.area_page, new int[]{R.id.item1, R.id.item2, R.id.item3});
    }

    public TablesAreaAdapter(Context context, int layoutResId, int[] itemResIds) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDataSet = new ArrayList<>();
        this.layoutResId = layoutResId;
        this.ItemResIds = itemResIds;
        this.itemViews = new View[ItemResIds.length];
    }

    @Override
    public int getCount() {
        return (mDataSet.size() + ItemResIds.length - 1) / ItemResIds.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(layoutResId, container, false);
        for (int i = 0; i < ItemResIds.length; i++) {
            itemViews[i] = view.findViewById(ItemResIds[i]);
            itemViews[i].setVisibility(View.INVISIBLE);
        }
        List<TablesAreaVo> subList = getSubDataSet(position);
        int size = subList.size();
        for (int i = 0; i < size; i++) {
            // 数据
            final TablesAreaVo vo = subList.get(i);
            final CommercialArea area = vo.getTablesArea();

            // 控件
            View itemView = itemViews[i];

            // 名称
            TextView tvName = (TextView) itemView.findViewById(R.id.areaName);
            TextView tvCount = (TextView) itemView.findViewById(R.id.areaIdleTableNum);
            tvName.setText(area.getAreaName());
            tvCount.setText(Utils.isNotEmpty(vo.getTablesVoList()) ? vo.getTablesVoList().size() + "" : "0");
            itemView.setTag(vo);
            itemView.setVisibility(View.VISIBLE);
            setSelected(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (TablesAreaVo areaVo : mDataSet) {
                        areaVo.setSelected(false);
                    }
                    vo.setSelected(true);
                    notifyDataSetChanged();
                    selectArea(vo);
                }
            });
        }

        container.addView(view);
        return view;
    }

    private void setSelected(View itemView) {
        TablesAreaVo vo = (TablesAreaVo) itemView.getTag();
        TextView tvName = (TextView) itemView.findViewById(R.id.areaName);
        TextView tvSelect = (TextView) itemView.findViewById(R.id.select);
        if (vo.isSelected()) {
            selectArea(vo);
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            tvSelect.setVisibility(View.VISIBLE);
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.settings_grayword));
            tvSelect.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * @param
     */
    public abstract void selectArea(TablesAreaVo vo);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    /**
     * @param dataSet
     */
    public void setDataSet(List<TablesAreaVo> dataSet) {
        mDataSet.clear();
        if (Utils.isNotEmpty(dataSet)) {
            dataSet.get(0).setSelected(true);
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    private List<TablesAreaVo> getSubDataSet(int position) {
        int start = position * ItemResIds.length;
        int end = Math.min((position + 1) * ItemResIds.length, mDataSet.size());
        return new ArrayList<TablesAreaVo>(mDataSet.subList(start, end));
    }
}