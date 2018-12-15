package com.zhongmei.bty.basemodule.salespromotion.adapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhongmei.yunfu.basemodule.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRule;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionValidityPeriod;
import com.zhongmei.bty.basemodule.discount.salespromotion.SalesPromotionConstant;
import com.zhongmei.bty.basemodule.salespromotion.view.SalesPromotionDetailFragment;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalesPromotionExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private FragmentManager mFragmentManager;
    private LayoutInflater mLayoutInflater;
    private List<Item> mDataSet;
    private List<Long> mSelectedSalesPromotionRules;
    private CustomerResp mCustomerNew;

    public SalesPromotionExpandableListAdapter(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mLayoutInflater = LayoutInflater.from(mContext);
        mDataSet = new ArrayList<Item>();
        mSelectedSalesPromotionRules = new ArrayList<Long>();
    }

    public List<Long> getSelectedSalesPromotionRules() {
        return mSelectedSalesPromotionRules;
    }

    public void setDataSet(List<SalesPromotionRuleVo> salesPromotionRuleVos, List<Long> selectedSalesPromotionRules, CustomerResp customerNew) {
        mDataSet.clear();
        mSelectedSalesPromotionRules.clear();
        if (salesPromotionRuleVos != null) {
            int size = salesPromotionRuleVos.size();
            for (int i = 0; i < size; i++) {
                SalesPromotionRuleVo salesPromotionRuleVo = salesPromotionRuleVos.get(i);
                List<SalesPromotionRuleVo> itemSalesPromotionRuleVos = new ArrayList<SalesPromotionRuleVo>();
                itemSalesPromotionRuleVos.add(salesPromotionRuleVo);
                Item item = new Item();
                item.setSalesPromotionRuleVos(itemSalesPromotionRuleVos);
                item.setPlanId(salesPromotionRuleVo.getRule().getPlanId());
                item.setPlanName(salesPromotionRuleVo.getRule().getPlanName());
                mDataSet.add(item);
            }
        }
        mSelectedSalesPromotionRules.addAll(selectedSalesPromotionRules);
        mCustomerNew = customerNew;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return mDataSet.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDataSet.get(groupPosition).getSalesPromotionRuleVos().size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return mDataSet.get(groupPosition).getPlanName();
    }

    @Override
    public SalesPromotionRuleVo getChild(int groupPosition, int childPosition) {
        return mDataSet.get(groupPosition).getSalesPromotionRuleVos().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.common_sales_promotion_list_item_group, parent, false);
            groupHolder = new GroupHolder();
            groupHolder.tvLabel = (TextView) convertView.findViewById(R.id.tv_title);
            groupHolder.ivIndicator = (ImageView) convertView.findViewById(R.id.iv_indicator);
            convertView.setTag(groupHolder);
        }

        bindGroupView(groupPosition, isExpanded, convertView);

        return convertView;
    }

    private void bindGroupView(int groupPosition, boolean isExpanded, View convertView) {
        setGroupLabel(groupPosition, convertView);
        setIndicatorState(isExpanded, convertView);
    }

    private void setGroupLabel(int groupPosition, View convertView) {
        GroupHolder groupHolder = (GroupHolder) convertView.getTag();
        String title = getGroup(groupPosition);
        groupHolder.tvLabel.setText(title);
    }

    public void setIndicatorState(boolean isExpanded, View convertView) {
        GroupHolder groupHolder = (GroupHolder) convertView.getTag();
        if (isExpanded) {
            groupHolder.ivIndicator.setImageResource(R.drawable.sales_promotion_ic_indicator_state_expanded);
        } else {
            groupHolder.ivIndicator.setImageResource(R.drawable.sales_promotion_ic_indicator_state_collapse);
        }
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.common_sales_promotion_list_item_child, parent, false);
            childHolder = new ChildHolder();
            childHolder.flContent = convertView.findViewById(R.id.fl_content);
            childHolder.ivStateTag = (ImageView) convertView.findViewById(R.id.iv_state_tag);
            childHolder.rlRuleInfo = convertView.findViewById(R.id.rl_rule_info);
            childHolder.tvPolicyDetail = (TextView) convertView.findViewById(R.id.tv_policy_detail);
            childHolder.tvRuleRestrict = (TextView) convertView.findViewById(R.id.tv_rule_restrict);
            childHolder.tvActivityDish = (TextView) convertView.findViewById(R.id.tv_activity_dish);
            childHolder.tvEndDate = (TextView) convertView.findViewById(R.id.tv_end_date);
            childHolder.tvViewDetails = (TextView) convertView.findViewById(R.id.tv_view_details);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        bindChildView(groupPosition, childPosition, childHolder);

        return convertView;
    }

    private void bindChildView(int groupPosition, int childPosition, ChildHolder childHolder) {
        SalesPromotionRuleVo salesPromotionRuleVo = getChild(groupPosition, childPosition);
        setPolicyDetailView(childHolder.tvPolicyDetail, salesPromotionRuleVo);
        setRuleRestrictView(childHolder.tvRuleRestrict, salesPromotionRuleVo);
        setActivityDishView(childHolder.tvActivityDish, salesPromotionRuleVo);
        setEndDateView(childHolder.tvEndDate, salesPromotionRuleVo);
        setViewDetailsView(childHolder.tvViewDetails, salesPromotionRuleVo);
        setRuleInfoView(childHolder.rlRuleInfo, salesPromotionRuleVo);
        setStateTagView(childHolder.ivStateTag, salesPromotionRuleVo);
        setContentView(childHolder.flContent, salesPromotionRuleVo);
    }

    private void setPolicyDetailView(TextView policyDetail, SalesPromotionRuleVo salesPromotionRuleVo) {
        String policyDetailStr = "";
        SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
        if (salesPromotionRule != null) {
            int policyDetailType = salesPromotionRule.getPolicyDetailType();
            BigDecimal policyValue1 = salesPromotionRule.getPolicyValue1();
            if (SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_DISCOUNT == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_specified_goods_discount, policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_REBATE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_specified_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.NEXT_GOODS_DISCOUNT == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_next_goods_discount, policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.NEXT_GOODS_REBATE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_next_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.NEXT_GOODS_FIXED_PRICE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_next_goods_fixed_price, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_DISCOUNT == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_multiple_goods_discount, policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_REBATE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_multiple_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_REBATE_LOWEST == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_multiple_goods_rebate_lowest);
            } else if (SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_FIXED_PRICE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_multiple_goods_fixed_price, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_DISCOUNT == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_single_goods_discount, policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_REBATE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_single_goods_rebate, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_FIXED_PRICE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_policy_detail_single_goods_fixed_price, ShopInfoCfg.getInstance().getCurrencySymbol(), policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.GIVE_GOODS == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_rule_restrict_give_goods, policyValue1);
            } else if (SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_RAISE_PRICE == policyDetailType) {
                policyDetailStr = mContext.getString(R.string.sales_promotion_rule_restrict_give_goods, salesPromotionRule.getPolicyValue2());
            }


        }
        policyDetail.setText(policyDetailStr);
        policyDetail.setEnabled(salesPromotionRuleVo.isCurrentEnable(mCustomerNew));
        policyDetail.setVisibility(TextUtils.isEmpty(policyDetailStr) ? View.INVISIBLE : View.VISIBLE);
    }

    private void setRuleRestrictView(TextView ruleRestrict, SalesPromotionRuleVo salesPromotionRuleVo) {
        String ruleRestrictStr = "";
        SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
        if (salesPromotionRule != null) {
            int ruleSubjectType = salesPromotionRule.getRuleSubjectType();
            int ruleLogic = salesPromotionRule.getRuleLogic();
            BigDecimal logicValue = salesPromotionRule.getLogicValue();
            int policySubjectType = salesPromotionRule.getPolicySubjectType();
            if (SalesPromotionConstant.RuleSubject.MATCH_QUANTITY_ONCE == ruleSubjectType) {
                if (logicValue != null) {
                    if (SalesPromotionConstant.PolicySubject.MULTIPLE_GOODS == policySubjectType
                            || SalesPromotionConstant.PolicySubject.GIVE_GOODS == policySubjectType) {
                        ruleRestrictStr = mContext.getString(R.string.sales_promotion_rule_restrict_match_quantity_once_multiple_goods, MathDecimal.trimZero(logicValue));
                    } else if (SalesPromotionConstant.PolicySubject.SINGLE_GOODS == policySubjectType) {
                        // TODO: 2018/7/18 要做点什么
                    } else if (SalesPromotionConstant.PolicySubject.NEXT_GOODS == policySubjectType) {
                        ruleRestrictStr = mContext.getString(R.string.sales_promotion_rule_restrict_match_quantity_once_next_goods, MathDecimal.trimZero(logicValue));
                    } else if (SalesPromotionConstant.PolicySubject.RAISE_PRICE_BUY_GOODS == policySubjectType) {
                        ruleRestrictStr = mContext.getString(R.string.sales_promotion_rule_restrict_raise_price_goods, MathDecimal.trimZero(logicValue), MathDecimal.trimZero(salesPromotionRule.getPolicyValue1()));
                    }
                }
            } else if (SalesPromotionConstant.RuleSubject.MATCH_AMOUNT_ONCE == ruleSubjectType) {
                if (SalesPromotionConstant.RuleLogic.GTE == ruleLogic && logicValue != null) {
                    if (SalesPromotionConstant.PolicySubject.SPECIFIED_GOODS == policySubjectType
                            || SalesPromotionConstant.PolicySubject.GIVE_GOODS == policySubjectType) {
                        ruleRestrictStr = mContext.getString(R.string.sales_promotion_rule_restrict_match_amount_once_specified_goods, ShopInfoCfg.getInstance().getCurrencySymbol(), MathDecimal.trimZero(logicValue));
                    } else if (SalesPromotionConstant.PolicySubject.RAISE_PRICE_BUY_GOODS == policySubjectType) {
                        ruleRestrictStr = mContext.getString(R.string.sales_promotion_rule_restrict_raise_price_goods1, logicValue, MathDecimal.trimZero(salesPromotionRule.getPolicyValue1()));
                    }
                }
            }
        }
        ruleRestrict.setText(ruleRestrictStr);
        ruleRestrict.setEnabled(salesPromotionRuleVo.isCurrentEnable(mCustomerNew));
        ruleRestrict.setVisibility(TextUtils.isEmpty(ruleRestrictStr) ? View.INVISIBLE : View.VISIBLE);
    }

    private void setActivityDishView(TextView activityDish, SalesPromotionRuleVo salesPromotionRuleVo) {
        String activityDishStr = "";
        SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
        if (salesPromotionRule != null) {
            int marketSubjectType = salesPromotionRule.getMarketSubjectType();
            if (SalesPromotionConstant.MarketSubject.ALL_GOODS_USABLE == marketSubjectType) {
                activityDishStr = mContext.getString(R.string.sales_promotion_market_subject_all_goods_usable);
            } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_USABLE == marketSubjectType) {
                activityDishStr = mContext.getString(R.string.sales_promotion_market_subject_part_goods_usable);
            } else if (SalesPromotionConstant.MarketSubject.PART_GOODS_UNUSABLE == marketSubjectType) {
                activityDishStr = mContext.getString(R.string.sales_promotion_market_subject_part_goods_unusable);
            }
        }
        activityDish.setText(activityDishStr);
        activityDish.setVisibility(TextUtils.isEmpty(activityDishStr) ? View.INVISIBLE : View.VISIBLE);
    }

    private void setEndDateView(TextView endDate, final SalesPromotionRuleVo salesPromotionRuleVo) {
        String endDateStr = "";
        SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
        if (salesPromotionRule != null) {
            SalesPromotionValidityPeriod salesPromotionValidityPeriod = salesPromotionRule.getValidityPeriod();
            if (salesPromotionValidityPeriod != null) {
                endDateStr = salesPromotionValidityPeriod.getEndDate();
            }
        }
        endDate.setText(mContext.getString(R.string.sales_promotion_end_date, endDateStr));
        endDate.setVisibility(TextUtils.isEmpty(endDateStr) ? View.INVISIBLE : View.VISIBLE);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //展示促销详情界面
                SalesPromotionDetailFragment.show(mFragmentManager, salesPromotionRuleVo);
            }
        });
    }

    //设置查看详情控件
    private void setViewDetailsView(TextView viewDetails, final SalesPromotionRuleVo salesPromotionRuleVo) {
        if (salesPromotionRuleVo.isCurrentEnable(mCustomerNew)) {
            viewDetails.setTextColor(mContext.getResources().getColor(R.color.color_666666));
        } else {
            viewDetails.setTextColor(mContext.getResources().getColor(R.color.color_999999));
        }
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //展示促销详情界面
                SalesPromotionDetailFragment.show(mFragmentManager, salesPromotionRuleVo);
            }
        });
    }

    //设置规则信息控件
    private void setRuleInfoView(View ruleInfo, SalesPromotionRuleVo salesPromotionRuleVo) {
        SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
        if (salesPromotionRule != null) {
            int policyDetailType = salesPromotionRule.getPolicyDetailType();
            if (SalesPromotionConstant.PolicyDetail.SPECIFIED_GOODS_DISCOUNT == policyDetailType
                    || SalesPromotionConstant.PolicyDetail.NEXT_GOODS_DISCOUNT == policyDetailType
                    || SalesPromotionConstant.PolicyDetail.SINGLE_PRICE_DISCOUNT == policyDetailType
                    || SalesPromotionConstant.PolicyDetail.MULTIPLE_GOODS_DISCOUNT == policyDetailType
                    || SalesPromotionConstant.PolicyDetail.SINGLE_GOODS_DISCOUNT == policyDetailType) {
                ruleInfo.setBackgroundResource(R.drawable.sales_promotion_selector_bg_rule_info_discount);
            } else {
                ruleInfo.setBackgroundResource(R.drawable.sales_promotion_selector_bg_rule_info_rebate);
            }
        }

        ruleInfo.setEnabled(salesPromotionRuleVo.isCurrentEnable(mCustomerNew));
    }

    private void setStateTagView(View stateTag, SalesPromotionRuleVo salesPromotionRuleVo) {
        if (salesPromotionRuleVo.isCurrentEnable(mCustomerNew)) {
            SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
            if (salesPromotionRule != null && mSelectedSalesPromotionRules.contains(salesPromotionRule.getId())) {
                stateTag.setVisibility(View.VISIBLE);
            } else {
                stateTag.setVisibility(View.INVISIBLE);
            }
        } else {
            stateTag.setVisibility(View.INVISIBLE);
        }
    }

    //设置内容控件
    private void setContentView(View content, SalesPromotionRuleVo salesPromotionRuleVo) {
        if (salesPromotionRuleVo.isCurrentEnable(mCustomerNew)) {
            content.setBackgroundResource(R.drawable.sales_promotion_selector_bg_item_child);
            SalesPromotionRule salesPromotionRule = salesPromotionRuleVo.getRule();
            if (salesPromotionRule != null && mSelectedSalesPromotionRules.contains(salesPromotionRule.getId())) {
                content.setSelected(true);
            } else {
                content.setSelected(false);
            }
        } else {
            content.setBackgroundResource(R.color.transparent);
        }
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    private static class Item {
        private long planId;
        private String planName;
        List<SalesPromotionRuleVo> salesPromotionRuleVos;

        public long getPlanId() {
            return planId;
        }

        public void setPlanId(long planId) {
            this.planId = planId;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public List<SalesPromotionRuleVo> getSalesPromotionRuleVos() {
            return salesPromotionRuleVos;
        }

        public void setSalesPromotionRuleVos(List<SalesPromotionRuleVo> salesPromotionRuleVos) {
            this.salesPromotionRuleVos = salesPromotionRuleVos;
        }
    }

    private static class GroupHolder {
        TextView tvLabel;
        ImageView ivIndicator;
    }

    private static class ChildHolder {
        View flContent;
        ImageView ivStateTag;
        View rlRuleInfo;
        TextView tvPolicyDetail;
        TextView tvRuleRestrict;
        TextView tvActivityDish;
        TextView tvEndDate;
        TextView tvViewDetails;
    }
}
