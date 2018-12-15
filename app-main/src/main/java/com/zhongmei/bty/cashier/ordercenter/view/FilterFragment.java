package com.zhongmei.bty.cashier.ordercenter.view;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ValueEnum;
import com.zhongmei.bty.cashier.ordercenter.adapter.FilterAdapter;
import com.zhongmei.bty.cashier.ordercenter.bean.FilterData;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.util.ArrayList;
import java.util.List;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.order_center_filter_layout)
public class FilterFragment extends BasicDialogFragment {

    @ViewById(R.id.order_center_filter_view)
    RecyclerView mRecylerView;

    @ViewById(R.id.order_center_filter_reset)
    TextView mReset;

    @ViewById(R.id.order_center_filter_ok)
    TextView mOk;

    @Bean
    FilterAdapter mFilterAdapter;

    private View mRelativeView;
    private List<Pair<String, ValueEnum>> selectPair = new ArrayList<>();
    private List<FilterData> filterData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.common_transparent_dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setLayout(DensityUtil.dip2px(MainApplication.getInstance(), 440), -2);
        if (mRelativeView != null) {
            int[] position = new int[2];
            mRelativeView.getLocationOnScreen(position);
            int x = position[0] - DensityUtil.dip2px(MainApplication.getInstance(), 385);
            int y = position[1] + mRelativeView.getHeight();
            WindowManager.LayoutParams wl = window.getAttributes();
            wl.x = x;
            wl.y = y;
            wl.gravity = Gravity.START | Gravity.TOP;
            window.setAttributes(wl);
        }
    }

    @AfterViews
    void intViews() {
        mRecylerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 4);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mFilterAdapter.isHeader(position) ? layoutManager.getSpanCount() : 1;
            }
        });
        mRecylerView.setLayoutManager(layoutManager);
        mRecylerView.setAdapter(mFilterAdapter);

        if (selectPair != null && !selectPair.isEmpty()) {
            mFilterAdapter.setSelectCondition(selectPair);
        }
        if (filterData != null) {
            mFilterAdapter.setItems(filterData);
        }
    }

    @Click({R.id.order_center_filter_reset, R.id.order_center_filter_ok})
    void buttonClick(View v) {
        switch (v.getId()) {
            case R.id.order_center_filter_reset:
                mFilterAdapter.clearSelectState();
                break;
            case R.id.order_center_filter_ok:
                if (mOnFilterClickListener != null) {
                    mOnFilterClickListener.filterOkClick(mFilterAdapter.getSelectItem());
                }
                dismiss();
                break;
        }
    }

    public void showAsDown(FragmentTransaction transaction, String tag, View view) {
        mRelativeView = view;
        show(transaction, tag);
    }

    private OnFilterClickListener mOnFilterClickListener;

    public void setOnFilterClickListener(OnFilterClickListener listener) {
        mOnFilterClickListener = listener;
    }

    public interface OnFilterClickListener {
        void filterOkClick(List<Pair<String, ValueEnum>> values);
    }

    public void setFilterCondition(List<Pair<String, ValueEnum>> selectPair) {
        if (selectPair != null) {
            List<Pair<String, ValueEnum>> temp = new ArrayList<Pair<String, ValueEnum>>();
            temp.addAll(selectPair);
            this.selectPair = temp;
        } else {
            this.selectPair = selectPair;
        }

    }

    public void setFilterData(List<FilterData> filterData) {
        if (filterData != null) {
            this.filterData.addAll(filterData);
        }
    }
}
