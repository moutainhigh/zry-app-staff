package com.zhongmei.bty.booking.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.util.ConvertUtils;


public class ConditionPopuWindow extends PopupWindow {
    private Activity context;

    private TextView textView;

    private ListView listView;

    private PopupItemClick itemClick;


    private boolean isHiden;

    public ConditionPopuWindow(Activity context, TextView textView, PopupItemClick itemClick, boolean isHiden) {
        super(textView, ConvertUtils.dp2px(context, 120), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        this.itemClick = itemClick;
        this.context = context;
        this.textView = textView;
        this.isHiden = isHiden;
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        View orderTimePopup = context.getLayoutInflater().inflate(R.layout.booking_order_time_popup, null, false);
        this.listView = (ListView) orderTimePopup.findViewById(R.id.booking_order_time_popup);
        this.setContentView(orderTimePopup);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.booking_status_popup));
        this.showAsDropDown(textView, (textView.getWidth() - 150) / 2, 0);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemClick.click(position);
                if (isHiden) {
                    ConditionPopuWindow.this.dismiss();
                }
            }
        });
    }

    public void setAdapter(BaseAdapter adapter) {
        if (listView != null) {
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public static interface PopupItemClick {

        void click(int position);
    }

}
