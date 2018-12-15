package com.zhongmei.bty.dinner.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class LeisureTableDialogPagerAdapter extends PagerAdapter {
    private static final String TAG = LeisureTableDialogPagerAdapter.class.getSimpleName();

    private static final int ALL_COUNT = 0;

    public static final int PAGE_SIZE = 5;// 每页显示队列

    private Context mContext;

    private List<Tables> emptTabls;

    private ArrayList<Integer> tablePeoples;

    private LayoutInflater mInflater;

    private int[] ItemIds = {R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5};

    private View[] itemViews = new View[ItemIds.length];

    private ArrayList<View> items;

    private SelectContent selectContent;

    public void setSelectContent(SelectContent selectContent) {
        this.selectContent = selectContent;
    }

    public LeisureTableDialogPagerAdapter(Context mContext, List<Tables> emptTabls) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        this.emptTabls = emptTabls;
        tablePeoples = new ArrayList<>();
        items = new ArrayList<>();
        init();
    }

    void init() {
        for (Tables t : emptTabls) {
            tablePeoples.add(t.getTablePersonCount());
        }
        tablePeoples = new ArrayList<Integer>(new HashSet<Integer>(tablePeoples));
        tablePeoples.add(0, ALL_COUNT);
        Collections.sort(tablePeoples, new PeopleCountAreaComparator());
    }


    public void setEmptTabls(List<Tables> emptTabls) {
        this.emptTabls.clear();
        this.emptTabls.addAll(emptTabls);
        init();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (tablePeoples.size() + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.empty_tables_area_page, container, false);
        for (int i = 0; i < ItemIds.length; i++) {
            itemViews[i] = view.findViewById(ItemIds[i]);
            itemViews[i].setVisibility(View.INVISIBLE);
        }
        List<Integer> subList = getSubDataSet(position);
        int size = subList.size();
        for (int i = 0; i < size; i++) {
            Integer integer = subList.get(i);
            // 控件
            final View itemView = itemViews[i];
            // 名称
            TextView tvName = (TextView) itemView.findViewById(R.id.table_area_name);
            TextView tvSelect = (TextView) itemView.findViewById(R.id.select);
            if (i == 4) {
                View view1 = itemView.findViewById(R.id.view_line);
                view1.setVisibility(View.GONE);
            }
            if (integer.intValue() == 0) {
                tvName.setText(mContext.getString(R.string.all));
                tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
                tvSelect.setVisibility(View.VISIBLE);
            } else {
                tvName.setText(String.format(mContext.getString(R.string.batch_operation_table_people), integer));
            }

            //itemView.setClickable(true);
            itemView.setVisibility(View.VISIBLE);
            itemView.setTag(subList.get(i));
            items.add(itemView);
            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectContent != null) {
                        selectContent.send((Integer) itemView.getTag());
                    }
                    for (int j = 0; j < items.size(); j++) {
                        if (v.equals(items.get(j))) {
                            items.get(j).setClickable(false);
                            setSelected(items.get(j));
                        } else {
                            items.get(j).setClickable(true);
                            setSelected(items.get(j));
                        }
                    }
                }
            });
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    private void setSelected(View itemView) {
        TextView tvName = (TextView) itemView.findViewById(R.id.table_area_name);
        TextView tvSelect = (TextView) itemView.findViewById(R.id.select);
        if (!itemView.isClickable()) {
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            tvSelect.setVisibility(View.VISIBLE);
            itemView.setClickable(false);
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.settings_grayword));
            tvSelect.setVisibility(View.INVISIBLE);
            itemView.setClickable(true);
        }
    }

    public List<Integer> getSubDataSet(int position) {
        int start = position * PAGE_SIZE;
        int end = Math.min((position + 1) * PAGE_SIZE, tablePeoples.size());
        return new ArrayList<Integer>(tablePeoples.subList(start, end));
    }

    private class PeopleCountAreaComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1.compareTo(o2);
        }
    }

    public interface SelectContent {
        void send(Integer peopleCount);
    }
}
