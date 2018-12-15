package com.zhongmei.bty.settings.fragment;

import android.view.View;
import android.widget.Button;

import com.zhongmei.yunfu.context.session.core.auth.Auth;
import com.zhongmei.yunfu.context.session.core.user.User;
import com.zhongmei.bty.basemodule.session.support.VerifyHelper;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.auth.application.DinnerApplication;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.bty.commonmodule.database.enums.OrderActionEnum;
import com.zhongmei.bty.basemodule.auth.permission.manager.AuthLogManager;
import com.zhongmei.yunfu.context.util.helper.SpHelper;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

/**
 * Created by demo on 2018/12/15
 */
@EFragment(R.layout.fragment_prepay_setting)
public class PrepaySettingFragment extends BasicFragment {

    @ViewById(R.id.btn_allow_multi_prepay)
    Button btnAllowMultiPrepay;

    @ViewById(R.id.btn_print_kitchen_after_pay)
    Button btnPrintKitchenAfterPay;

    private boolean isAllow = true;
    private boolean isPrintKitchenAfterPay;

    @AfterViews
    void init() {
        isAllow = SpHelper.getDefault().getBoolean(Constant.ALLOW_MULTI_PREPAY_TICKET, true);
        btnAllowMultiPrepay.setSelected(isAllow);

        isPrintKitchenAfterPay = SpHelper.getDefault().getBoolean(Constant.PRINT_KITCHEN_AFTER_PAY, false);
        btnPrintKitchenAfterPay.setSelected(isPrintKitchenAfterPay);
    }

    @Click({R.id.btn_allow_multi_prepay, R.id.btn_print_kitchen_after_pay})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_allow_multi_prepay:
                VerifyHelper.verifyAlert(getActivity(), DinnerApplication.PERMISSION_DINNER_PREREPRINT_SETTING,
                        new VerifyHelper.Callback() {
                            @Override
                            public void onPositive(User user, String code, Auth.Filter filter) {
                                super.onPositive(user, code, filter);
                                doPrepaySetting();
                                AuthLogManager.getInstance().flushNoQuest(OrderActionEnum.ACTION_SETTINGS, null, null, null);
                            }
                        });
                break;
            case R.id.btn_print_kitchen_after_pay:
                doKitchenSetting();
                break;
        }
    }

    private void doPrepaySetting() {
        isAllow = !isAllow;
        SpHelper.getDefault().putBoolean(Constant.ALLOW_MULTI_PREPAY_TICKET, isAllow);
        btnAllowMultiPrepay.setSelected(isAllow);
    }

    private void doKitchenSetting() {
        isPrintKitchenAfterPay = !isPrintKitchenAfterPay;
        SpHelper.getDefault().putBoolean(Constant.PRINT_KITCHEN_AFTER_PAY, isPrintKitchenAfterPay);
        btnPrintKitchenAfterPay.setSelected(isPrintKitchenAfterPay);
    }
}
