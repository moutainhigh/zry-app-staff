package com.zhongmei.beauty.order.view;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.MainApplication;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.bean.req.CustomerResp;
import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.cache.MarketRuleCache;
import com.zhongmei.bty.basemodule.discount.entity.MarketActivityDish;
import com.zhongmei.yunfu.db.entity.discount.MarketActivityRule;
import com.zhongmei.bty.basemodule.discount.enums.PromotionType;
import com.zhongmei.bty.basemodule.discount.enums.UserType;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.beauty.order.event.MarketActivityEvent;
import com.zhongmei.bty.common.adpter.ViewHolder;
import com.zhongmei.bty.common.adpter.abslistview.CommonAdapter;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.DeliveryType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.ui.view.CommonDialogFragment;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.snack.orderdish.MarketActivityDetailDialog;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by demo on 2018/12/15
 */
@EViewGroup(R.layout.beauty_market_activity_layout)
public class BeautyMarketActivityView extends LinearLayout implements AdapterView.OnItemClickListener {

    @ViewById(R.id.layout_content)
    LinearLayout layout_content;

    @ViewById(R.id.gv_single)
    GridView mSingleActivity;
    @ViewById(R.id.include_empty_status)
    RelativeLayout empty_status_view;

    private CommonAdapter<MarketVo> mAdapter;

    private List<MarketVo> mMarketVoList;

    private MarketVo mLastMarketVo;

    private FragmentActivity mActivity;

    public BeautyMarketActivityView(Context context) {
        super(context);
        mActivity = (FragmentActivity) context;
    }

    @AfterViews
    void init() {
        // 获取正餐当前有用的活动信息
        UserType typeUser = null;
        CustomerResp customer = DinnerShopManager.getInstance().getLoginCustomer();
        if (customer != null) {
            typeUser = UserType.MEMBER;
        } else {
            typeUser = UserType.MEMBERNON;
        }
        // 缓存获取当前可参与活动
        List<MarketRuleVo> ruleVoList =
                MarketRuleCache.getFilteredMarketRule(typeUser, BusinessType.DINNER, DeliveryType.HERE);

        List<MarketRuleVo> marketruleVoList = ruleVoList;
        // 构建适配器数据
        mMarketVoList = createMarketVo(marketruleVoList);

        mSingleActivity.setNumColumns(1);

        initListData();

        mSingleActivity.setOnItemClickListener(this);

    }

    private void initListData() {
        mAdapter = new CommonAdapter<MarketVo>(mActivity, R.layout.activity_list_item, mMarketVoList) {
            @Override
            public void convert(ViewHolder holder, final MarketVo marketVo) {
                holder.setOnClickListener(R.id.item_bottom_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        MarketActivityDetailDialog detailDialog =
                                MarketActivityDetailDialog.newInstance(marketVo.getRuleVo());
                        detailDialog.show(mActivity.getSupportFragmentManager(), "MarketActivityDetailDialog");

                    }
                });
                MarketActivityRule rule = marketVo.getRuleVo().getMarketActivityRule();
                holder.getView(R.id.item_layout).getBackground().setAlpha(40);
                LinearLayout bg = (LinearLayout) holder.getView(R.id.item_layout_parent);
                if (marketVo.isSelected) {
                    bg.setBackground(mContext.getResources().getDrawable(R.drawable.ic_activity_item_bg));

                } else {
                    bg.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));

                }
                // 使用次数
                int count = DinnerShopManager.getInstance().getShoppingCart().getPlanUsageCountById(rule.getId());
                TextView countTV = (TextView) holder.getView(R.id.tv_count);
                if (count > 0) {
                    countTV.setText(count + "");
                    countTV.setVisibility(View.VISIBLE);

                } else {
                    countTV.setVisibility(View.GONE);
                }

                holder.setText(R.id.tv_name, marketVo.getRuleVo().getMarketPlan().getPlanName());
                holder.setText(R.id.tv_restrict, marketVo.getDisNames());
                String to = mContext.getResources().getString(R.string.to);
                String date = mContext.getResources().getString(R.string.data_validity_pre);
                date = date + marketVo.getRuleVo().getMarketPlan().getPlanStartDay() + to
                        + marketVo.getRuleVo().getMarketPlan().getPlanEndDay();
                holder.setText(R.id.tv_date, date);
                String text = null;
                switch (marketVo.getRuleVo().getPromotionType()) {
                    case MINUS: // 满减

                        holder.setBackgroundRes(R.id.type_title, R.drawable.marketing_card_minus);
                        text = ShopInfoCfg.getInstance().getCurrencySymbol() + MathDecimal.trimZero(rule.getReduce());
                        break;
                    case DISCOUNT: // 折扣

                        holder.setBackgroundRes(R.id.type_title, R.drawable.marketing_card_discount);
                        text = MathDecimal.trimZero(rule.getDiscount()) + getResources().getString(R.string.discount1);
                        break;
                    case GIFT: // 赠送
                        holder.setBackgroundRes(R.id.type_title, R.drawable.marketing_card_gift);
                        text = getResources().getString(R.string.give);
                        break;
                    case SPECAILPRICE: // 特价

                        holder.setBackgroundRes(R.id.type_title, R.drawable.marketing_card_specail_price);
                        text = getResources().getString(R.string.special_price);
                        break;
                    default:
                        break;
                }
                holder.setText(R.id.tv_rule, text);
            }
        };
        mSingleActivity.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mSingleActivity.setAdapter(mAdapter);
        if (Utils.isEmpty(mMarketVoList)) {
            layout_content.setVisibility(View.GONE);
            empty_status_view.setVisibility(View.VISIBLE);
        } else {
            layout_content.setVisibility(View.VISIBLE);
            empty_status_view.setVisibility(View.GONE);
        }
    }

    // 取消选择并刷新界面
    public void cancelSelected() {
        if (mLastMarketVo != null && mLastMarketVo.isSelected) {
            mLastMarketVo.setSelected(false);
        }
        updateAdapterView();
    }

    // 刷新界面
    public void updateAdapterView() {
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // 如果有整单折扣，就不参与营销活动
        if (DinnerShopManager.getInstance().getShoppingCart().havePrivilegeByType(PrivilegeType.FREE)
                || DinnerShopManager.getInstance().getShoppingCart().havePrivilegeByType(PrivilegeType.DISCOUNT)
                || DinnerShopManager.getInstance().getShoppingCart().havePrivilegeByType(PrivilegeType.REBATE)) {

            showErrorDialog(mActivity, mActivity.getSupportFragmentManager(),
                    mActivity.getString(R.string.dinner_discountandplanhint));
            return;
        }
        MarketVo marketVo = mMarketVoList.get(position);
        EventBus.getDefault().post(new MarketActivityEvent(!marketVo.isSelected, marketVo.getRuleVo()));

        if (marketVo.isSelected) {
            marketVo.setSelected(false);
        } else {
            marketVo.setSelected(true);
        }
        if (mLastMarketVo != null) {
            if (mLastMarketVo != marketVo)
                mLastMarketVo.setSelected(false);
            mLastMarketVo = marketVo;

        } else {
            mLastMarketVo = marketVo;
        }
        updateAdapterView();
    }

    public void showErrorDialog(Context context, final FragmentManager fragmentManager, String errortitle) {

        new CommonDialogFragment.CommonDialogFragmentBuilder(context).title(errortitle)
                .iconType(CommonDialogFragment.ICON_WARNING)
                .positiveText(R.string.know)
                .positiveLinstner(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {

                    }
                })
                .build()
                .show(fragmentManager, "showErrorDialog");

    }


    public List<MarketVo> createMarketVo(List<MarketRuleVo> ruleVoList) {
        List<MarketVo> list = new ArrayList<MarketVo>();
        MarketVo vo = null;
        if (ruleVoList != null && !ruleVoList.isEmpty()) {
            for (MarketRuleVo ruleVo : ruleVoList) {
                vo = new MarketVo(ruleVo);
                list.add(vo);
            }
        }

        return list;
    }

    /**
     * @Date：2016-5-24 下午2:33:28
     * @Description: 封装UI要用的营销活动信息
     * @Version: 1.0
     * <p>
     * rights reserved.
     */
    private static class MarketVo {
        private MarketRuleVo ruleVo;

        private boolean isSelected;

        private String disNames = "";

        public MarketVo(MarketRuleVo marketRuleVo) {
            ruleVo = marketRuleVo;
            if (ruleVo.getPromotionType() == PromotionType.SPECAILPRICE) {// 如果特价
                // 6.9
                // 不支持特价菜

            } else {// 其它优惠
                if (ruleVo.isAllDish()) {
                    disNames = MainApplication.getInstance().getString(R.string.dinner_formcenter_goods_all);
                } else {
                    List<MarketActivityDish> dishList = ruleVo.getMarketActivityDishList();
                    if (dishList != null && !dishList.isEmpty()) {
                        int i = 0;
                        for (MarketActivityDish dish : dishList) {
                            if (dish.getDishName() != null) {
                                if (i > 0)
                                    disNames += "/";

                                disNames += dish.getDishName();
                                i++;
                                if (i >= 3)
                                    break;
                            }
                        }
                        if (i < dishList.size())
                            disNames += " 。。。。。。";
                    } else {
                        disNames = "。。。。。。";
                    }
                }
            }
        }

        public MarketRuleVo getRuleVo() {
            return ruleVo;
        }

        public void setRuleVo(MarketRuleVo ruleVo) {
            this.ruleVo = ruleVo;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

        public String getDisNames() {
            return disNames;
        }

        public void setDisNames(String disNames) {
            this.disNames = disNames;
        }
    }
}
