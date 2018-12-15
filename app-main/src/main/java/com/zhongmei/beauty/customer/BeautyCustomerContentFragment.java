package com.zhongmei.beauty.customer;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.customer.customerarrive.CouponListFragment;
import com.zhongmei.bty.customer.customerarrive.CouponListFragment_;
import com.zhongmei.bty.customer.event.CustomerFragmentReplaceListener;
import com.zhongmei.bty.customer.event.EventRefreshDetail;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.customer_content)
public class BeautyCustomerContentFragment extends BasicFragment {

    public static final int FRAGMENT_CUSTOMER_LIST = 0;

    public static final int FRAGMENT_CUSTOMER_DETAIL = 1;

    public static final int FRAGMENT_COUPON_LIST = 2;

    public int rightFragmentTag = FRAGMENT_CUSTOMER_DETAIL;

    BeautyCustomerListFragment customerListFragment = new BeautyCustomerListFragment_();

    BeautyCustomerDetailFragment customerDetailFragment = new BeautyCustomerDetailFragment_();

    CouponListFragment couponListFragment = new CouponListFragment_();


    @AfterViews
    protected void initView() {
        replaceFragment(R.id.left_content, customerListFragment, BeautyCustomerListFragment_.class.getSimpleName());
        replaceFragment(R.id.right_content, customerDetailFragment, BeautyCustomerDetailFragment_.class.getSimpleName());
        rightFragmentTag = FRAGMENT_CUSTOMER_DETAIL;
        initListener();
    }

    private void initListener() {
        customerDetailFragment.setReplaceListener(new CustomerFragmentReplaceListener() {
            @Override
            public void getCustomer(CustomerResp customer) {
                addChildFragment(R.id.right_content, couponListFragment, CouponListFragment.class.getSimpleName());
                couponListFragment.setArguments(couponListFragment.createArguments(CouponListFragment.LAUNCHMODE_CUSTOMER, customer));
                rightFragmentTag = FRAGMENT_COUPON_LIST;
            }
        });

        customerListFragment.setReplaceListener(new CustomerFragmentReplaceListener() {
            @Override
            public void getCustomer(CustomerResp customer) {
                if (rightFragmentTag == FRAGMENT_COUPON_LIST) {
                    removeChileFragment(couponListFragment, CouponListFragment.class.getSimpleName());
                    rightFragmentTag = FRAGMENT_CUSTOMER_DETAIL;
                }
                EventBus.getDefault().post(new EventRefreshDetail(customer.customerId));
            }
        });

        couponListFragment.setReplaceListener(new CustomerFragmentReplaceListener() {
            @Override
            public void getCustomer(CustomerResp customer) {
                removeChileFragment(couponListFragment, CouponListFragment.class.getSimpleName());
                rightFragmentTag = FRAGMENT_CUSTOMER_DETAIL;
                if (customer != null && customer.customerId != null) {
                    EventBus.getDefault().post(new EventRefreshDetail(customer.customerId));
                }
            }
        });
    }


}
