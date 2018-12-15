package com.zhongmei.bty.mobilepay.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.bean.MobilePayMenuItem;
import com.zhongmei.yunfu.context.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Date：2018/6/5 14:54
 */
public abstract class MobilePayModeChoosePagerAdapter extends PagerAdapter {
    private static final String TAG = MobilePayModeChoosePagerAdapter.class.getSimpleName();

    public static final int PAGE_SIZE = 3;// 每页显示队列

    public List<MobilePayMenuItem> menuItemList;

    private Context mContext;

    private LayoutInflater mInflater;

    private int[] ItemIds = {R.id.item1, R.id.item2, R.id.item3};

    private TextView[] itemViews = new TextView[ItemIds.length];

    public MobilePayModeChoosePagerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        menuItemList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return (menuItemList.size() + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.mobile_pay_mode_item_page, container, false);
        for (int i = 0; i < ItemIds.length; i++) {
            itemViews[i] = (TextView) view.findViewById(ItemIds[i]);
            itemViews[i].setVisibility(View.INVISIBLE);
        }
        List<MobilePayMenuItem> subList = getSubDataSet(position);
        int size = subList.size();
        for (int i = 0; i < size; i++) {
            // 数据
            final MobilePayMenuItem data = subList.get(i);
            // 控件
            TextView itemView = itemViews[i];
            itemView.setText(subList.get(i).menuName);
            itemView.setTag(data);
            itemView.setVisibility(View.VISIBLE);
            setSelected(itemView);
            if (data.isSelected) selectArea(data);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobilePayMenuItem data = (MobilePayMenuItem) v.getTag();
                    boolean temp = false;
                    for (MobilePayMenuItem item : menuItemList) {
                        if (item.isSelected) {
                            temp = true;
                            if (item.payModeId.value().longValue() != data.payModeId.value().longValue()) {
                                item.isSelected = false;
                                data.isSelected = true;
                            }
                        }
                    }
                    if (!temp) {
                        data.isSelected = true;
                    }
                    notifyDataSetChanged();
                    selectArea(data);
                }
            });
        }

        container.addView(view);
        return view;
    }

    private void setSelected(TextView itemView) {
        MobilePayMenuItem data = (MobilePayMenuItem) itemView.getTag();
        if (data.isSelected) {
            itemView.setTextColor(mContext.getResources().getColor(R.color.color_ffffff));
            itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_32adf6));
        } else {
            itemView.setTextColor(mContext.getResources().getColor(R.color.color_32adf6));
            itemView.setBackgroundColor(mContext.getResources().getColor(R.color.color_ffffff));
        }
    }

    /**
     * 选择桌位数
     *
     * @param data
     */
    public abstract void selectArea(MobilePayMenuItem data);

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    public void setDataSet(List<MobilePayMenuItem> menuItemList) {
        if (Utils.isNotEmpty(menuItemList)) {
            this.menuItemList.clear();
            this.menuItemList.addAll(menuItemList);
        }
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    private List<MobilePayMenuItem> getSubDataSet(int position) {
        int start = position * PAGE_SIZE;
        int end = Math.min((position + 1) * PAGE_SIZE, menuItemList.size());
        return new ArrayList<>((menuItemList.subList(start, end)));
    }
}
