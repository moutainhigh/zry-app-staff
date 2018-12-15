package com.zhongmei.bty.cashier.ordercenter.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.util.ViewFinder;
import com.zhongmei.bty.cashier.ordercenter.bean.DispatchFailOrder;
import com.zhongmei.bty.cashier.ordercenter.presenter.DispatchFailOrderListPresenter;
import com.zhongmei.bty.cashier.ordercenter.presenter.contract.DispatchFailOrderListContract;
import com.zhongmei.bty.common.adpter.ViewHolder;
import com.zhongmei.bty.common.adpter.abslistview.CommonAdapter;
import com.zhongmei.bty.snack.base.AbstractMVPBaseDialogFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DispatchFailOrderListFragment extends AbstractMVPBaseDialogFragment<DispatchFailOrderListContract.View, DispatchFailOrderListPresenter> implements DispatchFailOrderListContract.View, View.OnClickListener {
    private static final String EXTRA_FAIL_ORDER = "extra_fail_order";
    private ListView lvContentDetail;
    private List<DispatchFailOrder> mDataSet;

    public static DispatchFailOrderListFragment newInstance(List<DispatchFailOrder> dispatchFailOrders) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_FAIL_ORDER, (Serializable) dispatchFailOrders);

        DispatchFailOrderListFragment fragment = new DispatchFailOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public DispatchFailOrderListFragment() {
        mDataSet = new ArrayList<DispatchFailOrder>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mDataSet = (List<DispatchFailOrder>) args.getSerializable(EXTRA_FAIL_ORDER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        View view = inflater.inflate(R.layout.fragment_dispatch_fail_order_list, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View parent) {
        ViewFinder.findViewById(parent, R.id.iv_close).setOnClickListener(this);
        lvContentDetail = ViewFinder.findViewById(parent, R.id.lv_content_detail);
    }

    private void initData() {
        CommonAdapter<DispatchFailOrder> mAdapter = new CommonAdapter<DispatchFailOrder>(getContext(), R.layout.dispatch_fail_order_list_item, mDataSet) {
            @Override
            public void convert(ViewHolder holder, DispatchFailOrder dispatchFailOrder) {
                holder.setText(R.id.tv_serial_number, (holder.getPosition() + 1) + "");
                holder.setText(R.id.tv_description, mContext.getString(R.string.order_center_dispatch_fail_order_list_item_description, dispatchFailOrder.getTradeNo()));
                holder.setText(R.id.tv_reason, mContext.getString(R.string.order_center_dispatch_fail_order_list_item_reason, dispatchFailOrder.getReason()));
            }
        };
        lvContentDetail.setAdapter(mAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(R.drawable.bg_round_white);
        }
    }

    @Override
    protected DispatchFailOrderListPresenter createPresenter() {
        return new DispatchFailOrderListPresenter();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_close) {
            dismiss();
        }
    }
}
