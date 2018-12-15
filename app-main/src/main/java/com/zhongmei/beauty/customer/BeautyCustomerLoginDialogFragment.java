package com.zhongmei.beauty.customer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.CustomerApplication;
import com.zhongmei.bty.basemodule.customer.operates.interfaces.CustomerDal;
import com.zhongmei.bty.basemodule.devices.display.manager.DisplayServiceManager;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;
import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.atask.SimpleAsyncTask;
import com.zhongmei.atask.TaskContext;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.commonmodule.util.manager.ClickManager;
import com.zhongmei.bty.customer.event.EventLoginView;
import com.zhongmei.bty.dinner.cash.DinnerCustomerLoginBasicDialogFragment;
import com.zhongmei.bty.dinner.cash.DinnerCustomerLoginFragment;
import com.zhongmei.bty.dinner.cash.DinnerCustomerLoginFragment_;
import com.zhongmei.bty.dinner.cash.DinnerCustomerRegisterFragment;
import com.zhongmei.bty.dinner.cash.DinnerCustomerRegisterFragment_;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 美业登录
 */
public class BeautyCustomerLoginDialogFragment extends DinnerCustomerLoginBasicDialogFragment implements View.OnClickListener {

    public static final String TAG = BeautyCustomerLoginDialogFragment.class.getSimpleName();

    private ImageButton mIbClose;

    private ImageButton mIbBack;

    private TextView mTvTitle;

    private DinnerCustomerLoginFragment customerLoginFragment;

    private DinnerCustomerRegisterFragment customerRegisterFragment;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dinner_customer_login_dialog_layout, container, false);
        mIbClose = (ImageButton) view.findViewById(R.id.ib_close);
        mIbBack = (ImageButton) view.findViewById(R.id.back);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mIbClose.setOnClickListener(this);
        mIbBack.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customerLoginFragment = new DinnerCustomerLoginFragment_();
        customerLoginFragment.setListener(new CallbackListener() {
            @Override
            public void setType(int uiType) {
                BeautyCustomerLoginDialogFragment.this.uiType = uiType;
                switchFragment(uiType);
            }
        });
        customerRegisterFragment = new DinnerCustomerRegisterFragment_();
        if (getArguments() != null) {
            customerLoginFragment.setArguments(getArguments());
        }
        mErpDal = OperatesFactory.create(ErpCommercialRelationDal.class);
        initAreaCode();
    }


    void initAreaCode() {
        TaskContext.bindExecute(getActivity(), new SimpleAsyncTask<Map<String, ErpCurrency>>() {
            @Override
            public Map<String, ErpCurrency> doInBackground(Void... params) {
                CustomerDal dal = OperatesFactory.create(CustomerDal.class);
                settings = new boolean[2];
                try {
                    settings[0] = dal.getLoginQrCodeSetting();
                    settings[1] = dal.getopenIdRegisterSetting();
                } catch (SQLException e) {
                    Log.e(TAG, "", e);
                }

                if (erpCurrencyMap == null) {
                    erpCurrencyMap = new HashMap<>();
                }
                List<ErpCurrency> mErpCurrencyList = mErpDal.queryErpCurrenctList();
                if (Utils.isNotEmpty(mErpCurrencyList)) {
                    for (ErpCurrency currency : mErpCurrencyList) {
                        erpCurrencyMap.put(currency.getAreaCode(), currency);
                    }
                }
                return erpCurrencyMap;
            }

            @Override
            public void onPostExecute(Map<String, ErpCurrency> erpCurrencyMap) {
                customerLoginFragment.setErpCurrency(erpCurrencyMap);
                customerRegisterFragment.setErpCurrency(erpCurrencyMap);
                switchFragment(UI_TYPE_LOGIN);
            }
        });
    }

    void switchFragment(int type) {
        if (type == UI_TYPE_LOGIN) {
            this.uiType = type;
            replaceChildFragment(R.id.login_frame_layout, customerLoginFragment, DinnerCustomerLoginFragment.TAG);
            mIbBack.setVisibility(View.GONE);
            mIbClose.setVisibility(View.VISIBLE);
            mTvTitle.setText(getString(R.string.customer_menber_login));
            customerLoginFragment.setShowLoginView();
        } else if (type == UI_TYPE_REGISTER) {
            this.uiType = type;
            VerifyHelper.verifyAlert(getActivity(), CustomerApplication.PERMISSION_CUSTOMER_CREATE,
                    new VerifyHelper.Callback() {
                        @Override
                        public void onPositive(User user, String code, Auth.Filter filter) {
                            super.onPositive(user, code, filter);
                            showSecondDisPlay("");
                            replaceChildFragment(R.id.login_frame_layout, customerRegisterFragment, DinnerCustomerRegisterFragment.TAG);
//                            if (getArguments() != null && getArguments().getInt(BeautyCustomerConstants.KEY_CUSTOMER_LOGIN_FLAG , BeautyCustomerConstants.CustomerLoginLaunchMode.LOGIN) ==  BeautyCustomerConstants.CustomerLoginLaunchMode.RECHARGE){
//                                Bundle bundle = new Bundle();
//                                bundle.putInt(BeautyCustomerConstants.KEY_CUSTOMER_REGIEST_FLAG , BeautyCustomerConstants.CustomerReiestLaunchMode.RECHARGE_REGIEST);
//                                customerRegisterFragment.setArguments(bundle);
//                            }
                            mIbBack.setVisibility(View.VISIBLE);
                            mIbClose.setVisibility(View.GONE);
                            mTvTitle.setText(getString(R.string.member_register));
                        }
                    });
        } else if (type == UI_TYPE_FACE_ERROR) {
            this.uiType = type;
            mIbBack.setVisibility(View.VISIBLE);
            mIbClose.setVisibility(View.GONE);
            mTvTitle.setText(R.string.customer_login_face_title);
        }
    }

    @Override
    public void onClick(View v) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (v.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
            case R.id.back:
                if (uiType == UI_TYPE_REGISTER) {
                    switchFragment(UI_TYPE_LOGIN);
                } else if (uiType == UI_TYPE_FACE_ERROR)
                    customerLoginFragment.setShowLoginView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        if (DinnerShopManager.getInstance().getLoginCustomer() == null) {
            DisplayServiceManager.doCancel(getActivity());
        }
        EventBus.getDefault().post(new EventLoginView(EventLoginView.TYPE_LOGIN_VIEW_END));//add v8.4
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
