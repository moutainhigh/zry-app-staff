package com.zhongmei.bty.queue.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.queue.vo.QueueAreaVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：LiuYang
 * Date：2016/6/28 14:54
 * E-mail：liuy0
 */
public abstract class AreaPagerAdapter extends PagerAdapter {
    private static final String TAG = AreaPagerAdapter.class.getSimpleName();

    public static final int PAGE_SIZE = 3;// 每页显示队列

    private final List<QueueAreaVo> mDataSet;

    private final Context mContext;

    protected final LayoutInflater mInflater;
    //区域是否可以点击
    protected boolean canClick = true;

    protected final int[] ItemIds = {R.id.item1, R.id.item2, R.id.item3};

    protected final View[] itemViews = new View[ItemIds.length];

    public AreaPagerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mDataSet = new ArrayList<QueueAreaVo>();

    }

    @Override
    public int getCount() {
        return (mDataSet.size() + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.area_page, container, false);
        for (int i = 0; i < ItemIds.length; i++) {
            itemViews[i] = view.findViewById(ItemIds[i]);
            itemViews[i].setVisibility(View.INVISIBLE);
        }
        List<QueueAreaVo> subList = getSubDataSet(position);
        int size = subList.size();
        for (int i = 0; i < size; i++) {
            // 数据
            final QueueAreaVo vo = subList.get(i);
            final CommercialArea area = vo.getArea();
            // 控件
            View itemView = itemViews[i];
            // 名称
            TextView tvName = (TextView) itemView.findViewById(R.id.areaName);
            if (vo.getArea() != null) {
                tvName.setText(vo.getArea().getAreaName());
            }
            // 空闲桌台数
            TextView tvIdleNum = (TextView) itemView.findViewById(R.id.areaIdleTableNum);
            if (vo.getTablesList() != null && vo.getTablesList().size() > 0) {
                int tableCount = getIdleTableCount(vo.getTablesList()); //vo.getTablesList().size();
                if (tableCount > 9) {
                    tvIdleNum.setText("9+");
                } else {
                    tvIdleNum.setText("" + tableCount);
                }
                tvIdleNum.setVisibility(View.VISIBLE);
            } else {
                tvIdleNum.setVisibility(View.INVISIBLE);
            }

            itemView.setTag(vo);
            itemView.setVisibility(View.VISIBLE);
            setSelectde(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canClick) {
                        Log.i(TAG, area.getAreaName());
                        selectArea(area);
                    }
                }
            });
        }

        container.addView(view);
        return view;
    }

    private int getIdleTableCount(List<Tables> tablesList) {
        int count = 0;
        for (Tables table : tablesList) {
            if (table.getTableStatus() == TableStatus.EMPTY) {
                count++;
            }
        }
        return count;
    }

    public abstract void selectArea(CommercialArea area);

    protected void setSelectde(View itemView) {
        QueueAreaVo vo = (QueueAreaVo) itemView.getTag();
        setSelected(itemView, vo.isSelected());
    }

    protected void setSelected(View itemView, boolean isSelected) {
        QueueAreaVo vo = (QueueAreaVo) itemView.getTag();
        TextView tvName = (TextView) itemView.findViewById(R.id.areaName);
        TextView tvIdleNum = (TextView) itemView.findViewById(R.id.areaIdleTableNum);
        TextView tvSelect = (TextView) itemView.findViewById(R.id.select);
        if (isSelected) {
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            tvSelect.setVisibility(View.VISIBLE);
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.settings_grayword));
            tvSelect.setVisibility(View.INVISIBLE);
        }
    }

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
    public void setDataSet(List<QueueAreaVo> dataSet) {
        mDataSet.clear();
        if (dataSet != null) {
            mDataSet.addAll(dataSet);
        }
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    protected List<QueueAreaVo> getSubDataSet(int position) {
        int start = position * PAGE_SIZE;
        int end = Math.min((position + 1) * PAGE_SIZE, mDataSet.size());
        return new ArrayList<QueueAreaVo>(mDataSet.subList(start, end));
    }

    public void setCanClick(Boolean isCanClick) {

        if (isCanClick == null) {
            return;
        }
        this.canClick = isCanClick;
    }
}
