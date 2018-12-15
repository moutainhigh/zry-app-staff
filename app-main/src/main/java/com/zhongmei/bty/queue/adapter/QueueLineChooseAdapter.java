package com.zhongmei.bty.queue.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhongmei.yunfu.R;

import java.util.ArrayList;
import java.util.List;

/**
 *

 *
 */
public class QueueLineChooseAdapter extends BaseAdapter {

    public final static Long ALL_QUEUE_LINE_ID = -9999L;

    private Context mContext;

    private List<QueueLineChooseItem> dataList;

    public int chooseQueueLinePos = 0;

    public QueueLineChooseAdapter(Context context) {
        mContext = context;
        this.dataList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        if (dataList == null) {
            return 0;
        }
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.queue_line_spinner_item_layout, parent, false);
        }

        TextView tvQueueLine = (TextView) convertView.findViewById(R.id.tv_queue_line);
        QueueLineChooseItem queueLineChooseItem = dataList.get(position);
        tvQueueLine.setText(queueLineChooseItem.queueLineName);
        if (position == chooseQueueLinePos) {
            tvQueueLine.setSelected(true);
        } else {
            tvQueueLine.setSelected(false);
        }

        return convertView;
    }

    public void refreshData(List<QueueLineChooseItem> dataList, int position) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        chooseQueueLinePos = position;
        notifyDataSetChanged();
    }

    public static class QueueLineChooseItem {

        public Long queueLineId;

        public String queueLineName;

    }

}
