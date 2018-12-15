package com.zhongmei.bty.queue.ui.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.queue.vo.NewQueueBeanVo;
import com.zhongmei.bty.queue.vo.QueueBeanVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by demo on 2018/12/15
 */

public class QueueRemindPopupWindow extends PopupWindow {

    public static final int TYPE_REMIND = 1;

    public static final int TYPE_PRINT = 2;

    private LinearLayout mainView;

    private QueuePopupAdapter adapter;

    private GridView popupList;

    private Context mContext;

    private List<String> nameList;

    private List<Drawable> imageList;

    private NewQueueBeanVo queueVo;

    private OnItemClickListener listener;

    private int anchorWidth = 0;

    private int anchorHeight = 0;

    private int itemWidth;

    private int itemHeight;


    private int type;


    public interface OnItemClickListener {
        void onItemClick(View view, int position, NewQueueBeanVo queueVo);
    }

    public void setAnchorWidth(int anchorWidth) {
        this.anchorWidth = anchorWidth;
    }

    public QueueRemindPopupWindow(Context mContext, int type, NewQueueBeanVo queueVo, OnItemClickListener listener) {
        this.mContext = mContext;
        this.queueVo = queueVo;
        this.listener = listener;
        initDataList(type);
        mainView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.queue_remind_popup_window, null);
        popupList = (GridView) mainView.findViewById(R.id.popup_list);
        ViewGroup.LayoutParams params = popupList.getLayoutParams();
        params.width = itemWidth * nameList.size();
        params.height = itemHeight;
        popupList.setNumColumns(nameList.size());
        popupList.setLayoutParams(params);
        adapter = new QueuePopupAdapter();
        popupList.setAdapter(adapter);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener
        // ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setContentView(mainView);
    }

    void initDataList(int type) {
        nameList = new ArrayList<>();
        imageList = new ArrayList<>();
        if (type == TYPE_REMIND) {
            String sms = mContext.getResources().getString(R.string.queue_remind_sms);
            String call = mContext.getResources().getString(R.string.queue_remind_call);
            String voice = mContext.getResources().getString(R.string.queue_remind_voice);
            Drawable smsImage = mContext.getResources().getDrawable(R.drawable.queue_remind_sms_selector);
            Drawable callImage = mContext.getResources().getDrawable(R.drawable.queue_remind_call_selector);
            Drawable voiceImage = mContext.getResources().getDrawable(R.drawable.queue_remind_voice_selector);
            nameList.addAll(Arrays.asList(new String[]{sms, call, voice}));
            imageList.addAll(Arrays.asList(new Drawable[]{smsImage, callImage, voiceImage}));
        } else if (type == TYPE_PRINT) {
            String reprint = mContext.getString(R.string.queue_reprint_receipt);
            Drawable reprintImage = mContext.getResources().getDrawable(R.drawable.queue_remind_reprint_selector);
            nameList.add(reprint);
            imageList.add(reprintImage);
            if (queueVo.getTradeRelation() != null) {
                String orderDish = mContext.getString(R.string.queue_pre_menu);
                Drawable reprintDish = mContext.getResources().getDrawable(R.drawable.queue_reprint_dish_selector);
                nameList.add(orderDish);
                imageList.add(reprintDish);
            }
        }
        itemWidth = mContext.getResources().getDimensionPixelSize(R.dimen.queue_list_item_popup_item_width);
        itemHeight = mContext.getResources().getDimensionPixelSize(R.dimen.queue_list_item_popup_height);
    }

    public void showPopupWindow(View anchor) {
        if (anchor == null) {
            return;
        }
        int popupHeight = 0;
        if (!isShowing()) {
            if (anchorWidth == 0) anchorWidth = anchor.getWidth();
            anchorHeight = anchor.getHeight();
            int[] location = new int[2];
            anchor.getLocationInWindow(location);
            popupHeight = itemHeight;
            showAsDropDown(anchor, anchorWidth, (-anchorHeight - popupHeight) / 2);
        } else {
            dismiss();
        }
    }

    class QueuePopupAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return nameList.size();
        }

        @Override
        public Object getItem(int position) {
            return nameList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.queue_popup_item_layout, parent, false);
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.iv_image);
            TextView tvName = (TextView) convertView.findViewById(R.id.tv_name);
            ivImage.setImageDrawable(imageList.get(position));
            tvName.setText(nameList.get(position));
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(v, position, queueVo);
                        dismiss();
                    }
                }
            });
            return convertView;
        }
    }

}
