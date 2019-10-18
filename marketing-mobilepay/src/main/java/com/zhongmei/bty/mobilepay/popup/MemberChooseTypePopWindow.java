package com.zhongmei.bty.mobilepay.popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.zhongmei.yunfu.mobilepay.R;
import com.zhongmei.bty.mobilepay.adapter.MemberLoginTypeChooseAdapter;
import com.zhongmei.bty.mobilepay.bean.MemberLoginTypeData;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.bty.commonmodule.adapter.AbstractSpinerAdapter;

import java.util.List;

public class MemberChooseTypePopWindow extends PopupWindow implements OnItemClickListener {

    private Context mContext;
    private ListView mListView;
    private MemberLoginTypeChooseAdapter mAdapter;
    private AbstractSpinerAdapter.IOnItemSelectListener mItemSelectListener;


    public MemberChooseTypePopWindow(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public void setItemListener(AbstractSpinerAdapter.IOnItemSelectListener listener) {
        mItemSelectListener = listener;
    }


    private void init() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.commonmodule_spiner_window_layout, null);
        setContentView(view);
        setWidth(LayoutParams.WRAP_CONTENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x00);
        setBackgroundDrawable(dw);
        setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.member_login_popup_bg));
        mListView = (ListView) view.findViewById(R.id.listview);
        mAdapter = new MemberLoginTypeChooseAdapter(mContext);
        mListView.setAdapter(mAdapter);
        mListView.setDividerHeight(DensityUtil.dip2px(mContext, 0.5f));
        mListView.setOnItemClickListener(this);
    }


    public void refreshData(List<MemberLoginTypeData> list, int uiType) {
        if (list != null) {
            mAdapter.refreshData(list, uiType);
            int totalHeight = 0;
            if (mAdapter.getCount() > 0 && mAdapter.getCount() < 5) {
                View listItem = mAdapter.getView(0, null, mListView);
                listItem.measure(0, 0);                 totalHeight = listItem.getMeasuredHeight() * mAdapter.getCount();             } else {
                View listItem = mAdapter.getView(0, null, mListView);
                listItem.measure(0, 0);                 totalHeight = listItem.getMeasuredHeight() * 5;
            }
            LayoutParams params = mListView.getLayoutParams();
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
