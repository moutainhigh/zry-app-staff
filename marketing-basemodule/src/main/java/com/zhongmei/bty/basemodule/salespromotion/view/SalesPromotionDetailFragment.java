package com.zhongmei.bty.basemodule.salespromotion.view;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionDish;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRule;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleLimitPeriod;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionValidityPeriod;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionWeekday;
import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ViewFinder;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.ui.base.BasicDialogFragment;

import java.math.BigDecimal;
import java.util.List;


/**
 * 促销活动详情
 */
public class SalesPromotionDetailFragment extends BasicDialogFragment {
    private static final String TAG = SalesPromotionDetailFragment.class.getSimpleName();

    private SalesPromotionRuleVo mSalesPromotionRuleVo;

    private TextView mName, mType, mDate, mTime, mCondition, mCustomer, mTermType, mDeliveryType, mDishInfo;

    private TextView tvWeek;

    public static void show(FragmentManager fragmentManager, SalesPromotionRuleVo salesPromotionRuleVo) {
        SalesPromotionDetailFragment f = new SalesPromotionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SalesPromotionConstant.Extra.EXTRA_SALES_PROMOTION, salesPromotionRuleVo);
        f.setArguments(bundle);
        f.show(fragmentManager, TAG);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mSalesPromotionRuleVo = (SalesPromotionRuleVo) bundle.getSerializable(SalesPromotionConstant.Extra.EXTRA_SALES_PROMOTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.fragment_sales_promotion_detail, container);
        mName = (TextView) view.findViewById(R.id.tv_name);
        mType = (TextView) view.findViewById(R.id.tv_type);
        mDate = (TextView) view.findViewById(R.id.tv_date);
        tvWeek = ViewFinder.findViewById(view, R.id.tv_week);
        mTime = (TextView) view.findViewById(R.id.tv_time);
        mCondition = (TextView) view.findViewById(R.id.tv_condition);
        mCustomer = (TextView) view.findViewById(R.id.tv_customer_info);
        mTermType = (TextView) view.findViewById(R.id.tv_termType);
        mDeliveryType = (TextView) view.findViewById(R.id.tv_deliveryType);
        mDishInfo = (TextView) view.findViewById(R.id.tv_dish_info);
        view.findViewById(R.id.btn_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            setDialogWidthAndHeight(window, view);
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        bindView();
    }

    private void setDialogWidthAndHeight(Window window, View view) {
        view.measure(0, 0);
        Resources resources = getActivity().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int desiredWidth = metrics.widthPixels;
        int desiredHeight = metrics.heightPixels;
        window.setLayout(desiredWidth, desiredHeight);
        window.getAttributes().y = 0;
    }

    private void bindView() {
        bindSpecifiedView(mName, getPlanNameStr());
        bindSpecifiedView(mType, getTypeStr());
        bindSpecifiedView(mCondition, getConditionStr());
        bindSpecifiedView(mDate, getDateStr());
        bindSpecifiedView(tvWeek, getWeekStr());
        bindSpecifiedView(mTime, getTimeStr());
        bindSpecifiedView(mCustomer, getCustomerStr());
        bindSpecifiedView(mDeliveryType, getDeliveryTypeStr());
        bindSpecifiedView(mTermType, getTermTypeStr());
        bindSpecifiedView(mDishInfo, getDishInfoStr());
    }

    private void bindSpecifiedView(TextView textView, String value) {
        if (textView != null) {
            textView.setText(value);
            textView.setVisibility(TextUtils.isEmpty(value) ? View.GONE : View.VISIBLE);
        }
    }

    private String getPlanNameStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                return salesPromotionRule.getPlanName();
            }
        }

        return "";
    }

    private String getTypeStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                Integer policyDetailType = salesPromotionRule.getPolicyDetailType();
                BigDecimal policyValue1 = salesPromotionRule.getPolicyValue1();
                if (SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_DISCOUNT == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_specified_goods_discount, policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_REBATE == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_specified_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.NEXT_GOODS_DISCOUNT == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_next_goods_discount, policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.NEXT_GOODS_REBATE == policyDetailType) {
                    return ShopInfoCfg.getInstance().getCurrencySymbol() + policyValue1;
                } else if (SalesPromotionConstant.PolicyDetail.NEXT_GOODS_FIXED_PRICE == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_next_goods_fixed_price, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_DISCOUNT == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_multiple_goods_discount, policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_REBATE == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_multiple_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_REBATE_LOWEST == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_multiple_goods_rebate_lowest);
                } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_FIXED_PRICE == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_multiple_goods_fixed_price, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_DISCOUNT == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_single_goods_discount, policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_REBATE == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_single_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_FIXED_PRICE == policyDetailType) {
                    return getString(R.string.sales_promotion_policy_detail_single_goods_fixed_price, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.GIVE_GOODS == policyDetailType) {
                    return getString(R.string.sales_promotion_rule_restrict_give_goods, policyValue1);
                } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_RAISE_PRICE == policyDetailType) {
                    return getString(R.string.sales_promotion_rule_restrict_give_goods, salesPromotionRule.getPolicyValue2());
                }
            }
        }

        return "";
    }

    private String getConditionStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                Integer ruleSubjectType = salesPromotionRule.getRuleSubjectType();
                int ruleLogic = salesPromotionRule.getRuleLogic();
                BigDecimal logicValue = salesPromotionRule.getLogicValue();
                Integer policySubjectType = salesPromotionRule.getPolicySubjectType();
                if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType) {
                    if (SalesPromotionConstant.RuleLogic.GTE == ruleLogic && logicValue != null) {
                        if (SalesPromotionConstant.PolicySubject.MULTIPLE_GOODS == policySubjectType
                                || SalesPromotionConstant.PolicySubject.GIVE_GOODS == policySubjectType) {
                            return getString(R.string.sales_promotion_rule_restrict_match_quantity_once_multiple_goods, MathDecimal.trimZero(logicValue));
                        } else if (SalesPromotionConstant.PolicySubject.NEXT_GOODS == policySubjectType) {
                            return getString(R.string.sales_promotion_rule_restrict_match_quantity_once_next_goods, MathDecimal.trimZero(logicValue));
                        }
                        if (SalesPromotionConstant.PolicySubject.RAISE_PRICE_BUY_GOODS == policySubjectType) {
                            return getString(R.string.sales_promotion_rule_restrict_raise_price_goods, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(salesPromotionRule.getPolicyValue1()));
                        }
                    }
                } else if (SalesPromotionConstant.RuleSubject.MATCH_AMOUNT_ONCE == ruleSubjectType) {
                    if (SalesPromotionConstant.RuleLogic.GTE == ruleLogic && logicValue != null) {
                        if (SalesPromotionConstant.PolicySubject.SPECIFIED_GOODS == policySubjectType
                                || SalesPromotionConstant.PolicySubject.GIVE_GOODS == policySubjectType) {
                            return getString(R.string.sales_promotion_rule_restrict_match_amount_once_specified_goods, ShopInfoCfg.getInstance().getCurrencySymbol(), MathDecimal.trimZero(logicValue));
                        } else if (SalesPromotionConstant.PolicySubject.RAISE_PRICE_BUY_GOODS == policySubjectType) {
                            return getString(R.string.sales_promotion_rule_restrict_raise_price_goods1, logicValue, MathDecimal.trimZero(salesPromotionRule.getPolicyValue1()));
                        }
                    }
                }
            }
        }

        return "";
    }

    private String getDateStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                SalesPromotionValidityPeriod salesPromotionValidityPeriod = salesPromotionRule.getValidityPeriod();
                if (salesPromotionValidityPeriod != null
                        && (!TextUtils.isEmpty(salesPromotionValidityPeriod.getStartDate()) || !TextUtils.isEmpty(salesPromotionValidityPeriod.getEndDate()))) {
                    return salesPromotionValidityPeriod.getStartDate() + "-" + salesPromotionValidityPeriod.getEndDate();
                }
            }
        }

        return "";
    }

    private String getWeekStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                SparseArray<SalesPromotionWeekday> weekDay = salesPromotionRule.getWeekDay();
                if (weekDay != null) {
                    int size = weekDay.size();
                    StringBuilder stringBuilder = new StringBuilder();
                    boolean needPrefix = false;//是否需要前置
                    for (int i = 0; i < size; i++) {
                        SalesPromotionWeekday salesPromotionWeekday = weekDay.valueAt(i);
                        if (salesPromotionWeekday != null && salesPromotionWeekday.isEnable() && !TextUtils.isEmpty(salesPromotionWeekday.getName())) {
                            if (needPrefix) {
                                stringBuilder.append("、");
                            }
                            stringBuilder.append(salesPromotionWeekday.getName());
                            needPrefix = true;
                        }
                    }

                    return stringBuilder.toString();
                }
            }
        }

        return "";
    }

    private String getTimeStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                SalesPromotionRuleLimitPeriod limitPeriod = salesPromotionRule.getLimitPeriod();
                if (limitPeriod != null) {
                    return limitPeriod.getStartPeriod() + "-" + limitPeriod.getEndPeriod();
                } else {
                    return getString(R.string.sales_promotion_detail_limit_period_non);
                }
            }
        }

        return "";
    }

    private String getCustomerStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                int applyCrowd = salesPromotionRule.getApplyCrowd();
                if (SalesPromotionConstant.ApplyCrowd.ALL == applyCrowd) {
                    return getString(R.string.sales_promotion_detail_apply_crowd_all);
                } else if (SalesPromotionConstant.ApplyCrowd.MEMBER == applyCrowd) {
                    return getString(R.string.sales_promotion_detail_apply_crowd_member);
                } else if (SalesPromotionConstant.ApplyCrowd.NONMEMBER == applyCrowd) {
                    return getString(R.string.sales_promotion_detail_apply_crowd_nonmember);
                }
            }
        }

        return "";
    }

    private String getDeliveryTypeStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                return getString(R.string.sales_promotion_detail_delivery_type_all);
            }
        }

        return "";
    }

    private String getTermTypeStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                return getString(R.string.sales_promotion_detail_terminal_type_pos);
            }
        }

        return "";
    }

    private String getDishInfoStr() {
        if (mSalesPromotionRuleVo != null) {
            SalesPromotionRule salesPromotionRule = mSalesPromotionRuleVo.getRule();
            if (salesPromotionRule != null) {
                int marketSubjectType = salesPromotionRule.getMarketSubjectType();
                if (SalesPromotionConstant.MarketSubject.ALL_GOODS_USABLE == marketSubjectType) {
                    return getString(R.string.sales_promotion_detail_activity_dish_all);
                } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE == marketSubjectType) {
                    List<SalesPromotionDish> salesPromotionDishes = mSalesPromotionRuleVo.getActivityDishs();
                    if (Utils.isEmpty(salesPromotionDishes)) {
                        return getString(R.string.sales_promotion_detail_activity_dish_all_unusable);
                    } else {
                        return getString(R.string.sales_promotion_market_subject_part_goods_usable);
                    }
                } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType) {
                    List<SalesPromotionDish> salesPromotionDishes = mSalesPromotionRuleVo.getActivityDishs();
                    if (Utils.isEmpty(salesPromotionDishes)) {
                        return getString(R.string.sales_promotion_detail_activity_dish_all);
                    } else {
                        return getString(R.string.sales_promotion_market_subject_part_goods_unusable);
                    }
                }
            }
        }

        return "";
    }
}
