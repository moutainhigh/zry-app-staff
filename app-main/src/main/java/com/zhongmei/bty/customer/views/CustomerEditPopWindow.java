package com.zhongmei.bty.customer.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.R;


/**
 * 顾客编辑页面popWindow
 */
public class CustomerEditPopWindow extends PopupWindow {

    /**
     * Width
     */
    private static final int WINDOW_WIDTH = 150;

    private Activity context;

    private ImageButton button;

    private ListView listView;

    private OnItemClickListener itemClick;

    private BaseAdapter adpater;

    public CustomerEditPopWindow(Activity context, BaseAdapter adapter, ImageButton button, OnItemClickListener itemClick) {
        super(LayoutInflater.from(context).inflate(R.layout.customergroup_pop, null), WINDOW_WIDTH, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        this.itemClick = itemClick;
        this.context = context;
        this.button = button;
        this.adpater = adapter;
        init();
    }

    @SuppressLint("InflateParams")
    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.customergroup_pop, null);
        ListView listView = (ListView) view.findViewById(R.id.pop_lv);
        if (adpater.getCount() > 6) {
            listView.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.customer_popu_list_max_heigh);
        }
        listView.setAdapter(adpater);
        listView.setOnItemClickListener(itemClick);
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.showAsDropDown(button, -(WINDOW_WIDTH - button.getWidth()), 0);
    }

    public void setAdapter(BaseAdapter adapter) {
        if (listView != null) {
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
