package com.zhongmei.bty.queue.ui;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.queue.bean.QueueVo;

import java.util.List;

/**
 * @date 2015年8月19日上午9:25:17
 * @deprecated
 */
public class CalmPopupWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    public enum Direction {
        LEFT(0), TOP(1), RIGHT(2), BOTTOM(3);
        private final int value;

        Direction(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public interface OnClickListener {
        void onItemClick(View view, int position, QueueVo queueVo);
    }

    private final Context mContext;

    private final OnClickListener onClickListener;

    private final List<String> dataList;

    private final LinearLayout mainView;

    private int width, height;

    private final int anchor_width = 50;

    private final int tv_width = 120;

    private final int tv_height = 40;

    private final PopupAdapter popupAdapter;

    private final ListView mListView;

    private final QueueVo currentQueueVo;

    private int anchorWidth;
    private int anchorHeight;

    public CalmPopupWindow(Context context, OnClickListener clickListener, List<String> dataList, QueueVo queueVo) {
        // TODO Auto-generated constructor stub
        mContext = context;
        onClickListener = clickListener;
        this.dataList = dataList;
        currentQueueVo = queueVo;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        mainView = (LinearLayout) inflater.inflate(R.layout.calm_popupwindow, null);
        mListView = (ListView) mainView.findViewById(R.id.popup_list);
        popupAdapter = new PopupAdapter();
        mListView.setAdapter(popupAdapter);
        mListView.setOnItemClickListener(this);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener
        // ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setContentView(mainView);
    }

    public void showAsDropDown(View anchor, Direction direction) {

        if (anchor == null) {
            return;
        }
        if (!isShowing()) {
            anchorWidth = anchor.getWidth();
            anchorHeight = anchor.getHeight();
            int[] location = new int[2];
            anchor.getLocationInWindow(location);

            int offsetX = 0;
            int offsetY = 0;
            // 默认底部显示
            switch (direction) {
                case LEFT:
                    width = anchor_width + tv_width;
                    height = tv_height * dataList.size();
                    offsetX = -width;
                    offsetY = (-anchorHeight - height) / 2;
                    // mainView.setBackgroundResource(R.drawable.popup_left_bg);
                    mListView.setRight(anchor_width);
                    break;
                case TOP:
                    width = tv_width;
                    height = anchor_width + tv_height * dataList.size();
                    offsetX = (anchorWidth - width) / 2;
                    offsetY = -(10 + height);
                    mListView.setBottom(anchor_width);
                    mainView.setBackgroundResource(R.drawable.calm_popup_top_bg);
                    break;
                case RIGHT:
                    width = anchor_width + tv_width;
                    height = tv_height * dataList.size();
                    offsetX = anchorWidth;
                    offsetY = (-anchorHeight - height) / 2;
                    mListView.setLeft(anchor_width);
                    mainView.setBackgroundResource(R.drawable.calm_popup_right_bg);
                    break;
                case BOTTOM:
                default:
                    width = tv_width;
                    height = anchor_width + tv_height * dataList.size();
                    offsetX = (anchorWidth - width) / 2;
                    mListView.setTop(anchor_width);
                    // mainView.setBackgroundResource(R.drawable.popup_bottom_bg);
                    break;
            }

            this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            this.setWidth(width);
//			System.out.println("aW:" + anchorWidth + "-aH:" + anchorHeight + "-L1:" + location[0] + "-L2:" + location[1]
//				+ "-" + getWidth() + "-" + getHeight());
            popupAdapter.notifyDataSetChanged();
            showAsDropDown(anchor, offsetX, offsetY);
        } else {
            dismiss();
        }
    }

    public void update(View anchor, Direction direction) {
        // TODO Auto-generated method stub
        if (anchor == null) {
            return;
        }
        if (anchor.getWidth() != 0) {
            anchorWidth = anchor.getWidth();
        }
        if (anchor.getHeight() != 0) {
            anchorHeight = anchor.getHeight();
        }
        int[] location = new int[2];
        anchor.getLocationInWindow(location);

        int offsetX = 0;
        int offsetY = 0;
        // 默认底部显示
        switch (direction) {
            case LEFT:
                width = anchor_width + tv_width;
                height = tv_height * dataList.size();
                offsetX = -width;
                offsetY = (-anchorHeight - height) / 2;
                // mainView.setBackgroundResource(R.drawable.popup_left_bg);
                mListView.setRight(anchor_width);
                break;
            case TOP:
                width = tv_width;
                height = anchor_width + tv_height * dataList.size();
                offsetX = (anchorWidth - width) / 2;
                offsetY = -(10 + height);
                mListView.setBottom(anchor_width);
                mainView.setBackgroundResource(R.drawable.calm_popup_top_bg);
                break;
            case RIGHT:
                width = anchor_width + tv_width;
                height = tv_height * dataList.size();
                offsetX = anchorWidth;
                offsetY = (-anchorHeight - height) / 2;
                mListView.setLeft(anchor_width);
                mainView.setBackgroundResource(R.drawable.calm_popup_right_bg);
                break;
            case BOTTOM:
            default:
                width = tv_width;
                height = anchor_width + tv_height * dataList.size();
                offsetX = (anchorWidth - width) / 2;
                mListView.setTop(anchor_width);
                // mainView.setBackgroundResource(R.drawable.popup_bottom_bg);
                break;
        }

        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(width);
//		System.out.println("aW:" + anchorWidth + "-aH:" + anchorHeight + "-L1:" + location[0] + "-L2:" + location[1]
//				+ "-" + getWidth() + "-" + getHeight());
        popupAdapter.notifyDataSetChanged();
        update(anchor, offsetX, offsetY, width, height);
    }

    private class PopupAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if (dataList == null) {
                return 0;
            }
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView tv = null;
            if (convertView == null) {
                tv = new TextView(mContext);
                tv.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                tv.setHeight(tv_height);
                tv.setTextColor(ColorStateList.valueOf(Color.BLACK));
                tv.setTextSize(18);
                tv.setGravity(Gravity.CENTER);
                tv.setBackgroundResource(R.drawable.popup_item_selector);
                convertView = tv;
            }
            if (tv == null) {
                tv = (TextView) convertView;
            }
            String data = dataList.get(position);
            tv.setText(data);
            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // TODO Auto-generated method stub
        if (onClickListener != null) {
            onClickListener.onItemClick(view, position, currentQueueVo);
        }
        dismiss();
    }

    public int dip2px(float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
