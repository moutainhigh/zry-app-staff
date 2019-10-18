package com.zhongmei.beauty.customer.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.beauty.customer.BeautyCouponListFragment;
import com.zhongmei.beauty.customer.BeautyCouponListFragment_;
import com.zhongmei.beauty.customer.BeautyCustomerContentFragment;
import com.zhongmei.beauty.customer.BeautyCustomerDetailFragment;
import com.zhongmei.beauty.customer.BeautyCustomerDetailFragment_;
import com.zhongmei.beauty.customer.constants.BeautyCustomerConstants;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;
import com.zhongmei.bty.customer.event.CustomerFragmentReplaceListener;
import com.zhongmei.bty.customer.event.EventRefreshDetail;

import de.greenrobot.event.EventBus;


public class BeautyCustomerDetailDialogFragment extends BasicDialogFragment implements View.OnClickListener {

    public static final String TAG = BeautyCustomerDetailDialogFragment.class.getSimpleName();

    private ImageButton mIbClose;

    private LinearLayout mLlTitle;

    private BeautyCustomerDetailFragment customerDetailFragment = new BeautyCustomerDetailFragment_();

    private BeautyCouponListFragment couponListFragment = new BeautyCouponListFragment_();

    public int rightFragmentTag = BeautyCustomerContentFragment.FRAGMENT_CUSTOMER_DETAIL;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        return dialog;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beauty_customer_detail_dialog, container, false);
        mIbClose = (ImageButton) view.findViewById(R.id.btn_close);
        mLlTitle = (LinearLayout) view.findViewById(R.id.ll_title);
        mIbClose.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showDetailFragment();
        initListener();
    }

    private void showDetailFragment() {
        Bundle bundle = new Bundle();
        bundle.putLong(BeautyCustomerConstants.KEY_CUSTOMER_ID, getArguments().getLong(BeautyCustomerConstants.KEY_CUSTOMER_ID));
        bundle.putInt(BeautyCustomerConstants.KEY_CUSTOMER_PAGE_FROM, BeautyCustomerConstants.CustomerDetailFrom.DIALOG_TO_DETAIL);
        customerDetailFragment.setArguments(bundle);
        replaceChildFragment(R.id.fl_content_detail, customerDetailFragment, "BeautyCustomerDetailFragment");
    }


    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_close:
                dismissAllowingStateLoss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initListener() {
        customerDetailFragment.setReplaceListener(new CustomerFragmentReplaceListener() {
            @Override
            public void getCustomer(CustomerResp customer) {
                mLlTitle.setVisibility(View.GONE);
                addFragment(R.id.fl_content_detail, couponListFragment, BeautyCouponListFragment.class.getSimpleName());
                Bundle bundle = couponListFragment.createArguments(BeautyCouponListFragment.LAUNCHMODE_CUSTOMER, customer);
                bundle.putInt(BeautyCustomerConstants.KEY_CUSTOMER_PAGE_FROM, BeautyCustomerConstants.CustomerDetailFrom.DIALOG_TO_DETAIL);
                couponListFragment.setArguments(bundle);
                rightFragmentTag = BeautyCustomerContentFragment.FRAGMENT_COUPON_LIST;
            }
        });

        couponListFragment.setReplaceListener(new CustomerFragmentReplaceListener() {
            @Override
            public void getCustomer(CustomerResp customer) {
                mLlTitle.setVisibility(View.VISIBLE);
                removeFragment(couponListFragment, BeautyCouponListFragment.class.getSimpleName());
                rightFragmentTag = BeautyCustomerContentFragment.FRAGMENT_CUSTOMER_DETAIL;
                if (customer != null && customer.customerId != null) {
                    EventBus.getDefault().post(new EventRefreshDetail(customer.customerId, customer.mobile));
                }
            }
        });
    }

}
