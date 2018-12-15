package com.zhongmei.bty.customer.views;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.customer.adapter.CustomerPopWindowAdapter;

public class CustomerPopWindow extends PopupWindow {


    private Activity context;
    //	private int width, height;
    private View view;
    OnItemClickListener listener;
    CustomerPopWindowAdapter adapter;

    interface callback {

    }

    public CustomerPopWindow(Activity context, CustomerPopWindowAdapter adatper, OnItemClickListener listener) {
        super(LayoutInflater.from(context).inflate(R.layout.customer_pop_list, null), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        this.context = context;
        this.adapter = adatper;
        this.listener = listener;

        init();
    }

//
//	public PopWindow(View contentView, int width, int height, Activity context) {
//
//		super(contentView, width, height, false);
//		this.context = context;
//		this.view = contentView;
//		this.setWidth(width);
//		this.setHeight(height);
//		init();
//	}

    private void init() {
        view = LayoutInflater.from(context).inflate(R.layout.customer_pop_list, null);
        ListView lv = (ListView) view.findViewById(R.id.customer_list_lv);
        if (adapter.getCount() > 6) {
            lv.getLayoutParams().height = (int) context.getResources().getDimension(R.dimen.customer_popu_list_max_heigh);
        }
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(listener);

        this.setContentView(view);
        this.setOutsideTouchable(true);
        this.setFocusable(true);
        this.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);

    }


    @Override
    public void dismiss() {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = 1.0f;
        context.getWindow().setAttributes(lp);
        super.dismiss();
    }
}
