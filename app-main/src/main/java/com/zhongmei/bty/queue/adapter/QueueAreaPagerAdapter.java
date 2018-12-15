package com.zhongmei.bty.queue.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.queue.event.TableSelectEvent;
import com.zhongmei.bty.queue.vo.QueueAreaVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Date： 2018/7/9
 * @Description:
 * @Version: 1.0
 */
public abstract class QueueAreaPagerAdapter extends AreaPagerAdapter {

    private TableSelectEvent currentTableSelect = new TableSelectEvent(0, 0);
    private PositionSelected positionSelected;

    public class PositionSelected {
        int itemPosition = 0;
        int childPosition = 0;

        public boolean isSelected(int position, int subPosition) {
            return itemPosition == position && childPosition == subPosition;
        }

        public void setPosition(int position, int subPosition) {
            this.itemPosition = position;
            this.childPosition = subPosition;
        }
    }

    public QueueAreaPagerAdapter(Context context) {
        super(context);
        positionSelected = new PositionSelected();
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
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
            int count = 0;
            if (vo.getTablesList() != null && vo.getTablesList().size() > 0) {
                if (currentTableSelect.maxPersonCount == 0) {

                    List<Tables> tmp = new ArrayList<>();
                    for (Tables tables : vo.getTablesList()) {
                        if (tables.getTableStatus() == TableStatus.EMPTY)
                            tmp.add(tables);
                    }
                    count = tmp.size();
                } else if (currentTableSelect.maxPersonCount == -1) {
                    List<Tables> tmp = new ArrayList<>();
                    for (Tables tables : vo.getTablesList()) {
                        if (tables.getTablePersonCount() >= currentTableSelect.minPersonCount && tables.getTableStatus() == TableStatus.EMPTY)
                            tmp.add(tables);
                    }
                    count = tmp.size();
                } else {
                    List<Tables> tmp = new ArrayList<>();
                    for (Tables tables : vo.getTablesList()) {
                        if (tables.getTablePersonCount() >= currentTableSelect.minPersonCount
                                && tables.getTablePersonCount() <= currentTableSelect.maxPersonCount
                                && tables.getTableStatus() == TableStatus.EMPTY)
                            tmp.add(tables);
                    }
                    count = tmp.size();
                }
            }
            if (count > 0) {
                //int tableCount = getIdleTableCount(vo.getTablesList()); //vo.getTablesList().size();
                if (count > 9) {
                    tvIdleNum.setText("9+");
                } else {
                    tvIdleNum.setText("" + count);
                }
                tvIdleNum.setVisibility(View.VISIBLE);
            } else {
                tvIdleNum.setVisibility(View.INVISIBLE);
            }

            itemView.setTag(vo);
            itemView.setVisibility(View.VISIBLE);
            setSelected(itemView, positionSelected.isSelected(position, i));
            final int childPosition = i;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (canClick) {
                        //Log.i(TAG, area.getAreaName());
                        selectArea(area);
                        positionSelected.setPosition(position, childPosition);
                        notifyDataSetChanged();
                    }
                }
            });
        }

        container.addView(view);
        return view;
    }

    public void setCurrentTableSelect(TableSelectEvent currentTableSelect) {
        this.currentTableSelect = currentTableSelect;
        notifyDataSetChanged();
    }
}
