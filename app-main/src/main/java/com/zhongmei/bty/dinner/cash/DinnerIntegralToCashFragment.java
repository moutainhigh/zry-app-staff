package com.zhongmei.bty.dinner.cash;

import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.ui.base.BasicFragment;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige;
import com.zhongmei.bty.basemodule.discount.event.ActionDinnerPrilivige.DinnerPriviligeType;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteIntegral;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.TrueOrFalse;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

@EFragment(R.layout.fragment_dinner_integral_to_cash)
public class DinnerIntegralToCashFragment extends BasicFragment {
    private static final String TAG = DinnerIntegralToCashFragment.class.getSimpleName();

    @ViewById(R.id.tv_integral)
    TextView tvIntegral;

    @ViewById(R.id.tv_integral_rule)
    TextView tvIntegralRule;

    @ViewById(R.id.tb_use_integral)
    ToggleButton tbUserIntegral;

    @AfterViews
    void init() {
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        if (customer != null) {
            if (customer.card == null) {
                long integral = customer.integral == null ? 0 : customer.integral;
                tvIntegral.setText(integral + "");
                DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());

                CrmCustomerLevelRights integralRule = customer.customerLevelRights;
                if (integralRule != null && integralRule.getIsExchangeCash() == TrueOrFalse.TRUE) {
                    tvIntegralRule.setText(integralRule.getExchangeIntegralValue() + getString(R.string.intergral_to_cash_to)
                            + ShopInfoCfg.formatCurrencySymbol(integralRule.getExchangeCashValue()));
                }
            } else {
                EcCard card = customer.card;
                long integral = card.getIntegralAccount() != null ? card.getIntegralAccount().getIntegral() : 0;
                tvIntegral.setText(integral + "");
                DinnerPriviligeItemsFragment.showDisplayUserInfo(getActivity());

                EcCardLevelSetting cardLevelSetting = card.getCardLevelSetting();
                String integralValue = cardLevelSetting.getExchangeIntegralValue() == null ? "0" : cardLevelSetting.getExchangeIntegralValue().toString();
                String cashValue = cardLevelSetting.getExchangeCashValue() == null ? "0" : cardLevelSetting.getExchangeCashValue().toString();
                if (cardLevelSetting != null && cardLevelSetting.getIsExchangeCash() == Bool.YES) {
                    tvIntegralRule.setText(integralValue + getString(R.string.intergral_to_cash_to)
                            + ShopInfoCfg.formatCurrencySymbol(cashValue));
                }
            }
        }

        TradeVo tradeVo = DinnerShopManager.getInstance().getShoppingCart().getOrder();
        if (tradeVo != null) {
            tbUserIntegral.setChecked(tradeVo.getIntegralCashPrivilegeVo() != null
                    && tradeVo.getIntegralCashPrivilegeVo().isValid());
        }

        EventBus.getDefault().register(this);
    }


    public void onEventMainThread(ActionSeparateDeleteIntegral action) {
        tbUserIntegral.setChecked(false);
    }

    @Click({R.id.btn_close, R.id.tb_use_integral})
    void click(View v) {
        switch (v.getId()) {
            case R.id.btn_close:
                EventBus.getDefault().post(new ActionDinnerPrilivige(DinnerPriviligeType.PRIVILIGE_ITEMS));
                break;
            case R.id.tb_use_integral:
                if (tbUserIntegral.isChecked()) {
                    CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
                    if (customer == null) {
                        ToastUtil.showShortToast(R.string.pay_member_login_please);
                        tbUserIntegral.setChecked(false);
                        return;
                    }
                    if (customer.card == null) {
                                                long integral = customer.integral == null ? 0 : customer.integral;
                        if (integral <= 0) {
                            ToastUtil.showShortToast(R.string.intergral_is_zero);
                            tbUserIntegral.setChecked(false);
                        } else {
                            CrmCustomerLevelRights integralRule = customer.customerLevelRights;
                            if (integralRule != null && integralRule.getIsExchangeCash() == TrueOrFalse.TRUE) {
                                IntegralCashPrivilegeVo integralCashPrivilegeVo = new IntegralCashPrivilegeVo();
                                integralCashPrivilegeVo.setIntegral(new BigDecimal(integral));
                                integralCashPrivilegeVo.setRule(integralRule);
                                DinnerShopManager.getInstance().getShoppingCart().setIntegralCash(integralCashPrivilegeVo, true, true);
                            } else {
                                ToastUtil.showShortToast(R.string.have_no_level_rights);
                                tbUserIntegral.setChecked(false);
                            }
                        }
                    } else {
                                                EcCard card = customer.card;
                        long integral = card.getIntegralAccount() != null ? card.getIntegralAccount().getIntegral() : 0;
                        if (integral <= 0) {
                            ToastUtil.showShortToast(R.string.intergral_is_zero);
                            tbUserIntegral.setChecked(false);
                        } else {
                            EcCardLevelSetting cardLevelSetting = card.getCardLevelSetting();
                            if (cardLevelSetting != null && cardLevelSetting.getIsExchangeCash() == Bool.YES) {
                                IntegralCashPrivilegeVo integralCashPrivilegeVo = new IntegralCashPrivilegeVo();
                                integralCashPrivilegeVo.setIntegral(new BigDecimal(integral));
                                integralCashPrivilegeVo.setRule(cardLevelSetting);
                                DinnerShopManager.getInstance().getShoppingCart().setIntegralCash(integralCashPrivilegeVo, true, true);
                            } else {
                                ToastUtil.showShortToast(R.string.have_no_level_rights);
                                tbUserIntegral.setChecked(false);
                            }
                        }
                    }
                } else {
                    DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

}
