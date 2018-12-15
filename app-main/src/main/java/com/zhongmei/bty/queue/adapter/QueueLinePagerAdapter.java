package com.zhongmei.bty.queue.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.queue.QueueMainActivity;
import com.zhongmei.bty.queue.vo.NewQueueAreaVo;

import java.util.ArrayList;
import java.util.List;

public abstract class QueueLinePagerAdapter extends PagerAdapter {

    public static final String TAG = QueueLinePagerAdapter.class.getSimpleName();

    private static final int PAGE_SIZE = 6;// 每页显示队列

    private List<NewQueueAreaVo> queueAreaVoList;

    private Context mContext;

    private LayoutInflater mInflater;

    private int[] ItemIds = {R.id.item1, R.id.item2, R.id.item3, R.id.item4, R.id.item5, R.id.item6};

    private View[] itemViews = new View[ItemIds.length];

    private int type = QueueMainActivity.PAGE_QUEUE_LIST;

    public QueueLinePagerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.queueAreaVoList = new ArrayList<>();
    }

    public void setType(int type) {
        this.type = type;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return (queueAreaVoList.size() + PAGE_SIZE - 1) / PAGE_SIZE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mInflater.inflate(R.layout.queueline_page, container, false);
        for (int i = 0; i < ItemIds.length; i++) {
            itemViews[i] = view.findViewById(ItemIds[i]);
            itemViews[i].setVisibility(View.INVISIBLE);
        }
        List<NewQueueAreaVo> subList = getSubDataSet(position);
        int size = subList.size();
        for (int i = 0; i < size; i++) {
            // 数据
            final NewQueueAreaVo vo = subList.get(i);
            final CommercialQueueLine queueLine = vo.getQueueLine();

            // 控件
            View itemView = itemViews[i];
            // 人数
            TextView tvPerson = (TextView) itemView.findViewById(R.id.queueLinePersonCount);
            if (type == QueueMainActivity.PAGE_QUEUE_LIST)
                tvPerson.setText(vo.getQueueingCount() + "");
            else if (type == QueueMainActivity.PAGE_QUEUE_HISTORY_LIST)
                tvPerson.setText(vo.getQueueHistoryCount() + "");

            // 名称
            TextView tvName = (TextView) itemView.findViewById(R.id.queueLineName);
            if (vo.isQueueTypeAll()) {
                tvName.setText(mContext.getString(R.string.queue_line_name_all));
            } else {
                String name = queueLine.getQueueName();
                if (name.length() > 4) {
                    name = name.substring(0, 3) + "...";
                }

                if (queueLine.getMaxPersonCount() != null) {
                    tvName.setText(String.format(mContext.getString(R.string.queue_line_name), name, queueLine.getMinPersonCount(), queueLine.getMaxPersonCount()));
                } else {
                    tvName.setText(String.format(mContext.getString(R.string.queue_line_name2), name, queueLine.getMinPersonCount()));
                }
            }

            itemView.setTag(vo);
            itemView.setVisibility(View.VISIBLE);
            setSelectde(itemView);
            if (vo.isSelected()) selectQueueLine(vo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (NewQueueAreaVo queueAreaVo : queueAreaVoList) {
                        queueAreaVo.setSelected(false);
                    }
                    vo.setSelected(true);
                    notifyDataSetChanged();
                    selectQueueLine(vo);
                }
            });
        }

        container.addView(view);
        return view;
    }

    private void setSelectde(View itemView) {
        NewQueueAreaVo vo = (NewQueueAreaVo) itemView.getTag();
        TextView tvName = (TextView) itemView.findViewById(R.id.queueLineName);
        TextView tvPerson = (TextView) itemView.findViewById(R.id.queueLinePersonCount);
        TextView tvSelect = (TextView) itemView.findViewById(R.id.select);
        if (vo.isSelected()) {
            tvName.setTextColor(mContext.getResources().getColor(R.color.text_blue));
            tvPerson.setSelected(true);
            tvSelect.setVisibility(View.VISIBLE);
            itemView.setBackgroundColor(Color.parseColor("#E7F6FF"));
        } else {
            tvName.setTextColor(mContext.getResources().getColor(R.color.color_333333));
            tvPerson.setSelected(false);
            tvSelect.setVisibility(View.INVISIBLE);
            itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    /**
     * @param queueAreaVo
     */
    public abstract void selectQueueLine(NewQueueAreaVo queueAreaVo);

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
     * @param queueAreaVoList
     */
    public void setDataSet(List<NewQueueAreaVo> queueAreaVoList) {
        this.queueAreaVoList.clear();
        if (queueAreaVoList != null) {
            this.queueAreaVoList.addAll(queueAreaVoList);
        }
        notifyDataSetChanged();
    }

    /**
     * @param position
     * @return
     */
    private List<NewQueueAreaVo> getSubDataSet(int position) {
        int start = position * PAGE_SIZE;
        int end = Math.min((position + 1) * PAGE_SIZE, queueAreaVoList.size());
        return new ArrayList<NewQueueAreaVo>(queueAreaVoList.subList(start, end));
    }
}