package com.zhongmei.bty.queue.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView;

import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.commonmodule.adapter.AbstractSpinerAdapter;
import com.zhongmei.bty.queue.adapter.QueueLineChooseAdapter;

import java.util.List;

public class QueueLineChoosePopWindow extends PopupWindow implements AdapterView.OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private QueueLineChooseAdapter mAdapter;
    private AbstractSpinerAdapter.IOnItemSelectListener mItemSelectListener;


    public QueueLineChoosePopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public void setItemListener(AbstractSpinerAdapter.IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(com.zhongmei.yunfu.mobilepay.R.layout.commonmodule_spiner_window_layout, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        setBackgroundDrawable(mContext.getResources().getDrawable(com.zhongmei.yunfu.mobilepay.R.drawable.member_login_popup_bg));
        mListView = (ListView) view.findViewById(com.zhongmei.yunfu.mobilepay.R.id.listview);
        mAdapter = new QueueLineChooseAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(DensityUtil.dip2px(mContext, 0.5f));
        mListView.setOnItemClickListener(this);
    }


    public void refreshData(List<QueueLineChooseAdapter.QueueLineChooseItem> list, int position) {
        if (list != null) {
            mAdapter.refreshData(list, position);
            int totalHeight = 0;
            if (mAdapter.getCount() > 0 && mAdapter.getCount() < 5) {
                View listItem = mAdapter.getView(0, null, mListView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight = listItem.getMeasuredHeight() * mAdapter.getCount(); // 统计所有子项的总高度
            } else if (mAdapter.getCount() >= 5) {
                View listItem = mAdapter.getView(0, null, mListView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight = listItem.getMeasuredHeight() * 5;
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = totalHeight;
            mListView.setLayoutParams(params);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
        dismiss();
        if (mItemSelectListener != null) {
            mItemSelectListener.onItemClick(pos);
        }
    }

}
