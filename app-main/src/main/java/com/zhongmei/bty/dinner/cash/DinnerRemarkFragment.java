package com.zhongmei.bty.dinner.cash;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.db.entity.crm.CustomerGroupLevel;
import com.zhongmei.bty.basemodule.customer.operates.CustomerOperates;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.net.volley.VolleyError;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.commonmodule.http.LoadingResponseListener;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.yunfu.resp.ResponseObject;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.customer.bean.EnjoyManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.greenrobot.event.EventBus;

/**
 * 喜好/备注/分组
 */
@EFragment(R.layout.dinner_customer_memo)
public class DinnerRemarkFragment extends BasicFragment {

    private CustomerOperates mCustomerOperates;

    private static final String TAG = "DinnerRemarkFragment";

    @ViewById(R.id.tvLike_Memo)
    TextView tvLike;

    @ViewById(R.id.tvRemark_Memo)
    TextView tvRemark;

    @ViewById(R.id.tvGroup_Memo)
    TextView tvGroup;

    @ViewById(R.id.tvCustomerName_Memo)
    TextView tvCustomerName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomerOperates = OperatesFactory.create(CustomerOperates.class);
    }

    @AfterViews
    void init() {
        CustomerResp customerNew = DinnerShopManager.getInstance().getLoginCustomer();
        tvCustomerName.setText(customerNew.customerName);
        if (customerNew != null && customerNew.customerId != null) {
            queryCustomerById(customerNew.customerId);
        }
    }

    @Click({R.id.btn_close})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    private void queryCustomerById(Long customerId) {
        ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp> listener = new ResponseListener<com.zhongmei.bty.basemodule.customer.message.CustomerResp>() {
            @Override
            public void onResponse(ResponseObject<com.zhongmei.bty.basemodule.customer.message.CustomerResp> response) {
                try {
                    if (ResponseObject.isOk(response)) {
                        CustomerResp customerNew = response.getContent().result;
                        if (customerNew != null) {
                            tvLike.setText(TextUtils.isEmpty(customerNew.hobby) ? getString(R.string.customer_wu) : EnjoyManager.getInstance().getDtailEnjoyString(customerNew.hobby));
                            tvRemark.setText(TextUtils.isEmpty(customerNew.memo) ? getString(R.string.customer_wu) : customerNew.memo);
                            if (!TextUtils.isEmpty(customerNew.groupName)) {
                                tvGroup.setText(customerNew.groupName);
                                tvGroup.setVisibility(View.VISIBLE);
                            } else {
                                if (customerNew.groupId != null) {
                                    tvGroup.setText(getString(R.string.customer_group_more_label) + getGroupName(customerNew.groupId));
                                    tvGroup.setVisibility(View.VISIBLE);
                                } else {
                                    tvGroup.setVisibility(View.GONE);
                                }
                            }
                        }
                    }
                    dismissLoadingProgressDialog();
                } catch (Exception e) {
                    Log.e(TAG, "", e);
                }
            }

            @Override
            public void onError(VolleyError error) {
                ToastUtil.showLongToast(error.getMessage());
            }
        };
        mCustomerOperates.getCustomerById(customerId, false, LoadingResponseListener.ensure(listener, getChildFragmentManager()));
    }


    /**
     * 根据groupId 获取GroupName
     */
    private String getGroupName(Long groupId) {
        try {
            CustomerDal customerDal = OperatesFactory.create(CustomerDal.class);
            CustomerGroupLevel customerGroup = customerDal.findCustomerGroupById(groupId);
            if (customerGroup.getGroupName() != null) {
                return customerGroup.getGroupName();
            }
        } catch (Exception e) {
        }
        return getString(R.string.customer_not_set);

    }

}
