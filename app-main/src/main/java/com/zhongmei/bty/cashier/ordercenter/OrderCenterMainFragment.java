package com.zhongmei.bty.cashier.ordercenter;

import android.os.Bundle;

import com.zhongmei.bty.basemodule.database.utils.DbQueryConstant;
import com.zhongmei.bty.basemodule.notifycenter.enums.NotificationType;
import com.zhongmei.bty.constants.OCConstant;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.util.ValueEnum;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import java.io.Serializable;
import java.util.List;



@EFragment(R.layout.frg_order_center_main)
public class OrderCenterMainFragment extends BasicFragment {
    public static final String TAG = OrderCenterMainFragment.class.getSimpleName();

        private int mFromType = OCConstant.FromType.FROM_TYPE_SNACK;

    private int mCurrentTab = DbQueryConstant.ALL_UNPROCESS_ORDER;

    private boolean mSquareMod = false;

    private NotificationType mNotifyType;

    private List<ValueEnum> filterConditions;


    public static OrderCenterMainFragment newInstance(int fromType, int currentTab) {
        return newInstance(fromType, currentTab, false);
    }

    public static OrderCenterMainFragment newInstance(int fromType, int currentTab, boolean isSquareMod) {
        return newInstance(fromType, currentTab, isSquareMod, null);
    }

    public static OrderCenterMainFragment newInstance(int fromType, int currentTab, boolean isSquareMod, NotificationType notificationType) {
        return newInstance(fromType, currentTab, isSquareMod, notificationType, null);
    }


    public static OrderCenterMainFragment newInstance(int fromType, int currentTab, boolean isSquareMod, NotificationType notificationType, List<ValueEnum> filterCond) {
        Bundle args = new Bundle();
        args.putInt(Constant.EXTRA_FROM_TYPE, fromType);
        args.putInt(Constant.EXTRA_CURRENT_TAB, currentTab);
        args.putBoolean(Constant.EXTRA_SQUARE_MOD, isSquareMod);
        if (notificationType != null) {
                    }
        args.putSerializable("filterCond", (Serializable) filterCond);
        OrderCenterMainFragment orderCenterMainFragment = new OrderCenterMainFragment_();
        orderCenterMainFragment.setArguments(args);
        return orderCenterMainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mFromType = arguments.getInt(Constant.EXTRA_FROM_TYPE);
            mCurrentTab = arguments.getInt(Constant.EXTRA_CURRENT_TAB);
            mSquareMod = arguments.getBoolean(Constant.EXTRA_SQUARE_MOD);
                        filterConditions = (List<ValueEnum>) arguments.getSerializable("filterCond");
        }
    }

    @AfterViews
    protected void initView() {
                OrderCenterListFragment orderCenterListFragment = OrderCenterListFragment.newInstance(mFromType, mCurrentTab, mSquareMod, mNotifyType, filterConditions);
        replaceChildFragment(R.id.v_order_center_left, orderCenterListFragment, OrderCenterListFragment.class.getSimpleName());

                OrderCenterDetailFragment orderCenterDetailFragment = OrderCenterDetailFragment.newInstance(mFromType);
        replaceChildFragment(R.id.v_order_center_right, orderCenterDetailFragment, OrderCenterDetailFragment.class.getSimpleName());
    }
}
