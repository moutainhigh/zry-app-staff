package com.zhongmei.bty.customer;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

@EFragment(R.layout.customer_card_order_content)
public class CustomerOrdercenterFragment extends BasicFragment {
    private CustomerOrdercenterListFragment customerOrdercenterListFragment;

    private CustomerOrdercenterDetailFragment customerOrdercenterDetailFragment;

    @AfterViews
    protected void initView() {
        customerOrdercenterListFragment = new CustomerOrdercenterListFragment_();
        if (getArguments() != null) {
            customerOrdercenterListFragment.setArguments(getArguments());
        }
        customerOrdercenterDetailFragment = new CustomerOrdercenterDetailFragment_();
        replaceFragment(R.id.left_content, customerOrdercenterListFragment, customerOrdercenterListFragment.getClass().getName());
        replaceFragment(R.id.right_content, customerOrdercenterDetailFragment, customerOrdercenterDetailFragment.getClass().getName());
    }

    public void update(WindowToken window, OrderCategory orderCategory) {
        customerOrdercenterListFragment.update(window, orderCategory);
    }

    public enum WindowToken {
        CARD_SALE(0), MEMBER_STORE_VALUE(1), CARD_STORE_VALUE(2), ENTITY_CARD_CHANGE(3);

        private final int value;

        WindowToken(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }

        public static WindowToken valueOf(int value) {
            switch (value) {
                case 0:
                    return CARD_SALE;
                case 1:
                    return MEMBER_STORE_VALUE;
                case 2:
                    return CARD_STORE_VALUE;
                case 3:
                    return ENTITY_CARD_CHANGE;
                default:
                    break;
            }
            return CARD_SALE;
        }
    }

    public enum OrderCategory {
        NOT_PAYED, PAYED, RETURNED, INVALID
    }
}
