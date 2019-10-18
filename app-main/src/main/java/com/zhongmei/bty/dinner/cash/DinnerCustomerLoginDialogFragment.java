package com.zhongmei.bty.dinner.cash;

import android.util.Log;
import android.view.View;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


@EFragment(R.layout.dinner_customer_login_dialog_layout)
public class DinnerCustomerLoginDialogFragment extends DinnerCustomerLoginBasicDialogFragment {

    public static final String TAG = DinnerCustomerLoginDialogFragment.class.getSimpleName();

    @ViewById(R.id.ib_close)
    ImageButton mIbClose;

    @ViewById(R.id.back)
    ImageButton mIbBack;

    @ViewById(R.id.tv_title)
    TextView mTvTitle;

    private DinnerCustomerLoginFragment customerLoginFragment;

    private DinnerCustomerRegisterFragment customerRegisterFragment;

    private Map<String, ErpCurrency> erpCurrencyMap;

    private ErpCommercialRelationDal mErpDal;


    @AfterViews
    void initView() {
        customerLoginFragment = new DinnerCustomerLoginFragment_();
        customerLoginFragment.setListener(new CallbackListener() {
            @Override
            public void setType(int uiType) {
                DinnerCustomerLoginDialogFragment.this.uiType = uiType;
                switchFragment(uiType);
            }
        });
        customerRegisterFragment = new DinnerCustomerRegisterFragment_();
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
                            replaceChildFragment(R.id.login_frame_layout, customerRegisterFragment, DinnerCustomerRegisterFragment.TAG);
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

    @Click({R.id.ib_close, R.id.back})
    void onClick(View view) {
        if (ClickManager.getInstance().isClicked()) {
            return;
        }
        switch (view.getId()) {
            case R.id.ib_close:
                dismiss();
                break;
            case R.id.back:
                if (uiType == UI_TYPE_REGISTER) {
                    switchFragment(UI_TYPE_LOGIN);
                } else if (uiType == UI_TYPE_FACE_ERROR)
                    switchFragment(UI_TYPE_LOGIN);
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
        EventBus.getDefault().post(new EventLoginView(EventLoginView.TYPE_LOGIN_VIEW_END));        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
