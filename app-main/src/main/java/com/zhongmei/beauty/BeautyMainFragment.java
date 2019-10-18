package com.zhongmei.beauty;

import android.content.Intent;
import android.os.Bundle;

import com.zhongmei.beauty.booking.constants.BeautyBookingEnum;
import com.zhongmei.beauty.dialog.BeautyCreateOrEditBookingDialog;
import com.zhongmei.beauty.utils.BeautyOrderConstants;
import com.zhongmei.beauty.BeautyTradesFragment;
import com.zhongmei.beauty.BeautyTradesFragment_;
import com.zhongmei.bty.basemodule.auth.application.BeautyApplication;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.R;
import com.zhongmei.beauty.customer.BeautyCustomerActivity;
import com.zhongmei.beauty.customer.BeautyCustomerActivity_;
import com.zhongmei.beauty.customer.BeautyCustomerEditActivity_;
import com.zhongmei.beauty.customer.BeautyCustomerLoginDialogFragment;
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.beauty.interfaces.IBeautyOperator;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.customer.CustomerActivity;
import com.zhongmei.yunfu.util.ValueEnums;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;


@EFragment(R.layout.beauty_main_fragment)
public class BeautyMainFragment extends BasicFragment implements IBeautyOperator {

    protected BeautyMainOperatorFragment mOperatorFragment;

    private BeautyTradesFragment mTradesFragment;

    @AfterViews
    public void init() {
        mOperatorFragment = (BeautyMainOperatorFragment) getChildFragmentManager().findFragmentById(R.id.fragment_operator);
        mOperatorFragment.setiBeautyOperatorListener(this);
        toTrades();
    }


    private void toTradesFragment() {
        if (mTradesFragment == null) {
            mTradesFragment = BeautyTradesFragment_.builder().build();
        }
        replaceChildFragment(R.id.layout_contain, mTradesFragment, "tradesFragment");
    }

    @Override
    public void toTrades() {
        toTradesFragment();
    }

    @Override
    public void toTableManager() {
    }

    @Override
    public void toCreateTrade() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BeautyOrderActivity.class);
        startActivity(intent);
    }

    @Override
    public void toCreateCrad() {

        Intent intent = new Intent();
        intent.setClass(getActivity(), BeautyOrderActivity.class);
        intent.putExtra(BeautyOrderConstants.ORDER_BUSINESSTYPE, ValueEnums.toValue(BusinessType.CARD_TIME));
        startActivity(intent);
    }

    @Override
    public void toCharge() {
                BeautyCustomerLoginDialogFragment dialog = new BeautyCustomerLoginDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BeautyCustomerConstants.KEY_CUSTOMER_LOGIN_FLAG, BeautyCustomerConstants.CustomerLoginLaunchMode.RECHARGE);
        dialog.setArguments(bundle);
        dialog.show(getChildFragmentManager(), "BeautyCustomerLoginDialogFragment");
    }

    @Override
    public void toCreateMember() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), BeautyCustomerEditActivity_.class);
        intent.putExtra("type", CustomerActivity.PARAM_ADD);
        intent.putExtra(BeautyCustomerConstants.KEY_CUSTOMER_EDIT_PAGE, BeautyCustomerConstants.CustomerEditPage.MAIN);
        startActivity(intent);
    }

    @Override
    public void toCreateReserver() {
        VerifyHelper.verifyAlert(getActivity(), BeautyApplication.PERMISSION_BEAUTY_CREATE_RESERVER, new VerifyHelper.Callback() {
            @Override
            public void onPositive(User user, String code, Auth.Filter filter) {
                super.onPositive(user, code, filter);
                BeautyCreateOrEditBookingDialog dialog = new BeautyCreateOrEditBookingDialog();
                Bundle bundle = new Bundle();
                bundle.putInt(BeautyBookingEnum.LAUNCHMODE_BOOKING_DIALOG, BeautyBookingEnum.BookingDialogLaunchMode.CREATE);
                dialog.setArguments(bundle);
                dialog.show(getChildFragmentManager(), "BeautyCreateOrEditBookingDialog");
            }

        });
    }

}
