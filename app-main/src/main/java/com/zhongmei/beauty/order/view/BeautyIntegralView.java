package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRule;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.customer.manager.CustomerManager;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCard;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.entity.CrmCustomerLevelRights;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.discount.CustomerScoreRuleVo;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.bty.commonmodule.database.enums.TrueOrFalse;
import com.zhongmei.yunfu.util.ToastUtil;
import com.zhongmei.bty.dinner.action.ActionSeparateDeleteIntegral;
import com.zhongmei.bty.dinner.cash.DinnerPriviligeItemsFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.math.BigDecimal;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.beauty_integral_to_cash)
public class BeautyIntegralView extends LinearLayout {
    private static final String TAG = BeautyIntegralView.class.getSimpleName();

    @ViewById(R.id.tv_integral)
    TextView tvIntegral;

    @ViewById(R.id.tv_integral_rule)
    TextView tvIntegralRule;

    @ViewById(R.id.tb_use_integral)
    ToggleButton tbUserIntegral;

    private FragmentActivity mActivity;

    public BeautyIntegralView(Context context) {
        super(context);
        mActivity = (FragmentActivity) context;
    }


    @AfterViews
    void init() {
        CustomerResp customer = CustomerManager.getInstance().getDinnerLoginCustomer();
        if (customer != null) {
            if (customer.card == null) {
                long integral = customer.integral == null ? 0 : customer.integral;
                tvIntegral.setText(integral + "");
                DinnerPriviligeItemsFragment.showDisplayUserInfo(mActivity);

                CustomerScoreRuleVo rule = CustomerManager.getInstance().getIntegerRule();
                if (rule != null) {
                    tvIntegralRule.setText(rule.getConvertValue() + mActivity.getString(R.string.intergral_to_cash_to)
                            + ShopInfoCfg.formatCurrencySymbol(BigDecimal.ONE)+","+String.format(mActivity.getString(R.string.integral_max_value),rule.getMaxUserInteger()==null?integral+"":rule.getMaxUserInteger().intValue()+""));
                }
            } else {
                EcCard card = customer.card;
                long integral = card.getIntegralAccount() != null ? card.getIntegralAccount().getIntegral() : 0;
                tvIntegral.setText(integral + "");
                DinnerPriviligeItemsFragment.showDisplayUserInfo(mActivity);

                EcCardLevelSetting cardLevelSetting = card.getCardLevelSetting();
                String integralValue = cardLevelSetting.getExchangeIntegralValue() == null ? "0" : cardLevelSetting.getExchangeIntegralValue().toString();
                String cashValue = cardLevelSetting.getExchangeCashValue() == null ? "0" : cardLevelSetting.getExchangeCashValue().toString();
                if (cardLevelSetting != null && cardLevelSetting.getIsExchangeCash() == Bool.YES) {
                    tvIntegralRule.setText(integralValue + mActivity.getString(R.string.intergral_to_cash_to)
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

    /**
     * 监听拆单购物车的事件，删除积分抵现
     *
     * @param action
     */
    public void onEventMainThread(ActionSeparateDeleteIntegral action) {
        tbUserIntegral.setChecked(false);
    }

    @Click({R.id.tb_use_integral})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tb_use_integral:
                if (tbUserIntegral.isChecked()) {
                    CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
                    if (customer == null) {
                        ToastUtil.showShortToast(R.string.pay_member_login_please);
                        tbUserIntegral.setChecked(false);
                        return;
                    }

                    //会员
                    long integral = customer.integral == null ? 0 : customer.integral;
                    if (integral <= 0) {
                        ToastUtil.showShortToast(R.string.intergral_is_zero);
                        tbUserIntegral.setChecked(false);
                    } else {
                        CustomerScoreRuleVo rule = CustomerManager.getInstance().getIntegerRule();
                        if (rule != null) {
                            IntegralCashPrivilegeVo integralCashPrivilegeVo = new IntegralCashPrivilegeVo();
                            integralCashPrivilegeVo.setIntegral(new BigDecimal(integral));
                            integralCashPrivilegeVo.setRule(rule);
                            integralCashPrivilegeVo.setMaxInteger(rule.getMaxUserInteger());
                            DinnerShopManager.getInstance().getShoppingCart().setIntegralCash(integralCashPrivilegeVo, true, true);
                        } else {
                            ToastUtil.showShortToast(R.string.have_no_level_rights);
                            tbUserIntegral.setChecked(false);
                        }
                    }

//                    if (customer.card == null) {
//
//                    } else {
//                        //实体卡
//                        EcCard card = customer.card;
//                        long integral = card.getIntegralAccount() != null ? card.getIntegralAccount().getIntegral() : 0;
//                        if (integral <= 0) {
//                            ToastUtil.showShortToast(R.string.intergral_is_zero);
//                            tbUserIntegral.setChecked(false);
//                        } else {
//                            EcCardLevelSetting cardLevelSetting = card.getCardLevelSetting();
//                            if (cardLevelSetting != null && cardLevelSetting.getIsExchangeCash() == Bool.YES) {
//                                IntegralCashPrivilegeVo integralCashPrivilegeVo = new IntegralCashPrivilegeVo();
//                                integralCashPrivilegeVo.setIntegral(new BigDecimal(integral));
//                                integralCashPrivilegeVo.setRule(cardLevelSetting);
//                                DinnerShopManager.getInstance().getShoppingCart().setIntegralCash(integralCashPrivilegeVo, true, true);
//                            } else {
//                                ToastUtil.showShortToast(R.string.have_no_level_rights);
//                                tbUserIntegral.setChecked(false);
//                            }
//                        }
//                    }
                } else {
                    DinnerShopManager.getInstance().getShoppingCart().removeIntegralCash();
                }
                break;
            default:
                break;
        }
    }


}
