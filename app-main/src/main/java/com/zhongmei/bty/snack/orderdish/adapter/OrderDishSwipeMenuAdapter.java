package com.zhongmei.bty.snack.orderdish.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhongmei.yunfu.R;
import com.zhongmei.bty.basemodule.commonbusiness.cache.ServerSettingCache;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.orderdish.bean.IExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.shoppingcart.utils.MathSalesPromotionTool;
import com.zhongmei.bty.basemodule.trade.bean.DepositInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.bty.cashier.shoppingcart.ShoppingCart;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.snack.orderdish.DishDataItem;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ResourceUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrderDishSwipeMenuAdapter extends BaseAdapter {
    private static final String TAG = OrderDishSwipeMenuAdapter.class.getSimpleName();

    final String STR_SEMICOLON = "：";
    final String STR_COMMA = "，";
    private static String NO_HAVE_ACTIVITY_KEY = "no_have_activity_key";

    public static final int ITEM_TYPE_GRAY_SEPERATOR = -1;
    public static final int ITEM_TYPE_SINGLE = 0;
    public static final int ITEM_TYPE_SINGLE_DISCOUNT = 1;
    public static final int ITEM_TYPE_SINGLE_MEMO = 2;
    public static final int ITEM_TYPE_COMBO = 3;
    public static final int ITEM_TYPE_COMBO_DISCOUNT = 4;
    public static final int ITEM_TYPE_COMBO_MEMO = 5;
    public static final int ITEM_TYPE_CHILD = 6;
    public static final int ITEM_TYPE_CHILD_MEMO = 7;
    public static final int ITEM_TYPE_ALL_MEMO = 8;
    public static final int ITEM_TYPE_ALL_DISCOUNT = 9;
    public static final int ITEM_TYPE_CUSTOMER_COUPONS = 10;
    public static final int ITEM_TYPE_INTERGRAL = 11;
    public static final int ITEM_TYPE_FREE_REASON = 12;
    public static final int ITEM_TYPE_EXTRA_CHARGE = 13;
    public static final int ITEM_TYPE_DEPOSIT = 14;
    public static final int ITEM_TYPE_WEIXIN_COUPONS = 15;
    public static final int ITEM_TYPE_MARKET_ACTIVITY = 16;
    public static final int ITEM_TYPE_TAX = 17;
    private ArrayList<DishDataItem> data = new ArrayList<DishDataItem>(10);
    private Context context;

    private Boolean isEditModle = false;

    private Boolean isDiscountAll = false;
    private static boolean mDepositEnable = true;

    private Drawable mChildIcon;

    private Drawable mAllDiscountIcon;

    private Drawable mAllMemoIcon;

    private Drawable mCouponEnabledIcon;

    private Drawable mIntegralEnableIcon;

    private Drawable mExtraChargeIcon;

    private Drawable mAllReasonIcon;

    private Drawable mDepositIconInvalid;

    private Drawable mDepositIcon;

    private Drawable mPackIcon;

    private Drawable mPackAndRepay;

    private Drawable mRepayIcon;

    private Drawable mEnableWholeIcon;

    private Drawable mUnEnableCouponIcon;

    private Drawable mUnEnableIntegralIcon;

    private int IMAGEMARGINRIGHT = 5;

    private boolean isDishCheckMode = false;
    private SalesPromotionRuleVo mSalesPromotionRuleVo = null;

        private Map<String, TradeItemPlanActivity> mTradeItemPlanActivityMap = new HashMap<String, TradeItemPlanActivity>();

    private String mCurSelectUuid = "";
    private int pos = -2;

    public boolean isDishCheckMode() {
        return isDishCheckMode;
    }

    public void setDishCheckMode(boolean isDishCheck) {
        isDishCheckMode = isDishCheck;
        notifyDataSetChanged();
    }

    public SalesPromotionRuleVo getSalesPromotionRuleVo() {
        return mSalesPromotionRuleVo;
    }

    public OrderDishSwipeMenuAdapter(Context context) {
        this.context = context;
        this.mChildIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_child_icon);
        this.mAllDiscountIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_alldiscount_icon);
        this.mAllMemoIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_allmemo_icon);
        this.mCouponEnabledIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_coupon_enabled_icon);
        this.mIntegralEnableIcon = context.getResources().getDrawable(R.drawable.integral);
        this.mAllReasonIcon = context.getResources().getDrawable(R.drawable.ic_cashier_order_center_all_reason);
        this.mExtraChargeIcon = context.getResources().getDrawable(R.drawable.dinner_extra_icon);
        this.mDepositIconInvalid = context.getResources().getDrawable(R.drawable.deposit_icon_invalid);
        this.mDepositIcon = context.getResources().getDrawable(R.drawable.deposit_icon);
        this.mPackIcon = context.getResources().getDrawable(R.drawable.shopping_cart_pack_icon);
        this.mPackAndRepay = context.getResources().getDrawable(R.drawable.pack_repay_icon);
        this.mRepayIcon = context.getResources().getDrawable(R.drawable.repay_icon);
        this.mEnableWholeIcon = context.getResources().getDrawable(R.drawable.enable_whole_privilege_icon);
        this.mUnEnableCouponIcon = context.getResources().getDrawable(R.drawable.unenabled_coupon_icon);
        this.mUnEnableIntegralIcon = context.getResources().getDrawable(R.drawable.unenabled_integral_icon);
    }

    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo) {
        this.data.clear();
        boolean isHasItem = false;
                if (dataList != null && dataList.size() > 0) {
            isHasItem = true;
                        List<TradePlanActivity> tradePlanActivityList = tradeVo.getTradePlanActivityList();            List<TradeItemPlanActivity> tradeItemPlanActivityList = tradeVo.getTradeItemPlanActivityList();            if (tradePlanActivityList == null) {
                tradePlanActivityList = Collections.emptyList();
            }
            if (tradeItemPlanActivityList == null) {
                tradeItemPlanActivityList = Collections.emptyList();
            }
            Map<String, TradePlanActivity> tradePlanActivityMap = new HashMap<String, TradePlanActivity>();            for (TradePlanActivity tradePlanActivity : tradePlanActivityList) {
                if (tradePlanActivity.getStatusFlag() == StatusFlag.INVALID
                        || tradePlanActivity.getRuleEffective() == ActivityRuleEffective.INVALID) {
                    continue;
                }
                tradePlanActivityMap.put(tradePlanActivity.getUuid(), tradePlanActivity);            }
                        mTradeItemPlanActivityMap.clear();
            for (TradeItemPlanActivity tradeItemPlanActivity : tradeItemPlanActivityList) {
                if (tradeItemPlanActivity.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                mTradeItemPlanActivityMap.put(tradeItemPlanActivity.getTradeItemUuid(), tradeItemPlanActivity);
            }

            Map<String, List<IShopcartItem>> tradeItemListMap = new HashMap<String, List<IShopcartItem>>();            tradeItemListMap.put(NO_HAVE_ACTIVITY_KEY, new ArrayList<IShopcartItem>());

            for (String key : tradePlanActivityMap.keySet()) {
                tradeItemListMap.put(key, new ArrayList<IShopcartItem>());            }

            for (IShopcartItem shopcartItem : dataList) {
                TradeItemPlanActivity tradeItemPlanActivity = mTradeItemPlanActivityMap.get(shopcartItem.getUuid());
                if (tradeItemPlanActivity != null && tradePlanActivityMap.get(tradeItemPlanActivity.getRelUuid()) != null) {                    TradePlanActivity tradePlanActivity = tradePlanActivityMap.get(tradeItemPlanActivity.getRelUuid());                    List<IShopcartItem> shopcartItemList = tradeItemListMap.get(tradePlanActivity.getUuid());
                    shopcartItemList.add(shopcartItem);
                } else {
                    List<IShopcartItem> shopcartItemList = tradeItemListMap.get(NO_HAVE_ACTIVITY_KEY);
                    shopcartItemList.add(shopcartItem);
                }
            }

            List<IShopcartItem> noActivityItemList = tradeItemListMap.get(NO_HAVE_ACTIVITY_KEY);
            if (Utils.isNotEmpty(noActivityItemList)) {
                addDishItems(noActivityItemList);
            }

            for (TradePlanActivity tradePlanActivity : tradePlanActivityList) {
                List<IShopcartItem> shopcartItemList = tradeItemListMap.get(tradePlanActivity.getUuid());
                if (Utils.isEmpty(shopcartItemList)) {
                    continue;
                }
                addActivityTitleItem(tradePlanActivity);
                addDishItems(shopcartItemList);
            }

                        if (ServerSettingCache.getInstance().getDepositEnable()) {
                DepositInfo deposit = ServerSettingCache.getInstance().getTradeDeposit();
                DishDataItem item = new DishDataItem(ITEM_TYPE_DEPOSIT);

                if (deposit.getType() == DepositInfo.TYPE_BY_PEOPLE) {
                    BigDecimal cnt = new BigDecimal(tradeVo.getTrade().getTradePeopleCount());
                    BigDecimal dep = deposit.getValue();
                    item.setValue(dep.multiply(cnt).doubleValue());
                } else {
                    item.setValue(deposit.getValue().doubleValue());
                }
                item.setName(context.getString(R.string.order_accept_settings));
                item.setDepositType(deposit.getType());
                data.add(item);
            }
        }
                if (tradeVo != null && tradeVo.getTrade() != null) {
            addOverallAttribute(tradeVo, isHasItem);
        }

        if (isEditModle) {
            for (DishDataItem item : data) {
                if (item.getBase() == null)
                    continue;
                if (item.getBase().isSelected())
                    item.setCheckStatus(DishDataItem.DishCheckStatus.CHECKED);
                else
                    item.setCheckStatus(DishDataItem.DishCheckStatus.NOT_CHECK);
                TradeItemPlanActivity tradeItemPlanActivity = mTradeItemPlanActivityMap.get(item.getBase().getUuid());
                if (tradeItemPlanActivity != null) {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
                }
                if (item.getBase().getEnableWholePrivilege() == Bool.NO) {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
                }
            }
        }

        if (isDishCheckMode && mSalesPromotionRuleVo != null) {
            manualSelectSalesPromotionOperate(mSalesPromotionRuleVo);
        }

        notifyDataSetChanged();
    }

        private void addActivityTitleItem(TradePlanActivity planActivity) {
        DishDataItem item = new DishDataItem(ITEM_TYPE_MARKET_ACTIVITY);
        item.setTradePlanActivityUuid(planActivity.getUuid());
        item.setName(planActivity.getRuleName());
        item.setValue(planActivity.getOfferValue() == null ? 0 : planActivity.getOfferValue().doubleValue());
                item.setNeedTopLine(false);
        data.add(0, item);    }


    private void addDishItems(List<IShopcartItem> dataList) {
        if (dataList == null || dataList.isEmpty())
            return;
        for (int i = dataList.size() - 1; i >= 0; i--) {
            IShopcartItem shopCartItem = dataList.get(i);
            if (shopCartItem.getStatusFlag() == StatusFlag.INVALID) {
                continue;
            }
                        if (shopCartItem.getSetmealItems() != null && shopCartItem.getType() == DishType.COMBO) {

                DishDataItem item = new DishDataItem(ITEM_TYPE_COMBO);                item.setBase(shopCartItem);
                item.setUuid(shopCartItem.getUuid());
                if (i == dataList.size() - 1)
                    item.setNeedTopLine(false);
                data.add(item);

                List<? extends ISetmealShopcartItem> chirldList = shopCartItem.getSetmealItems();

                if (chirldList != null && chirldList.size() > 0) {

                    for (ISetmealShopcartItem chirld : chirldList) {
                                                DishDataItem it = new DishDataItem(ITEM_TYPE_CHILD);
                        it.setBase(chirld);
                        it.setUuid(shopCartItem.getUuid());
                        data.add(it);

                                                if (!TextUtils.isEmpty(chirld.getMemo())) {
                            DishDataItem chMemoIt = new DishDataItem(ITEM_TYPE_CHILD_MEMO);
                            chMemoIt.setBase(chirld);
                            chMemoIt.setUuid(shopCartItem.getUuid());
                            chMemoIt.setName(context.getResources().getString(R.string.order_dish_memo_semicolon)
                                    + chirld.getMemo());
                            data.add(chMemoIt);
                        }
                    }
                }

                                if (shopCartItem.getPrivilege() != null) {
                    DishDataItem it = new DishDataItem(ITEM_TYPE_COMBO_DISCOUNT);
                    it.setBase(shopCartItem);
                    it.setUuid(shopCartItem.getUuid());
                    it.setName(shopCartItem.getPrivilege().getPrivilegeName());
                    it.setValue(shopCartItem.getPrivilege().getPrivilegeAmount().doubleValue());

                    data.add(it);
                }
                                if (!TextUtils.isEmpty(shopCartItem.getMemo())) {
                    DishDataItem it2 = new DishDataItem(ITEM_TYPE_COMBO_MEMO);
                    it2.setBase(shopCartItem);
                    it2.setUuid(shopCartItem.getUuid());
                    it2.setName(context.getResources().getString(R.string.order_dish_memo_semicolon)
                            + shopCartItem.getMemo());
                    data.add(it2);
                }
                            } else {
                DishDataItem item = new DishDataItem(ITEM_TYPE_SINGLE);                item.setBase(shopCartItem);
                item.setUuid(shopCartItem.getUuid());
                if (i == dataList.size() - 1)
                    item.setNeedTopLine(false);
                data.add(item);

                                if (shopCartItem.getPrivilege() != null) {
                    DishDataItem it = new DishDataItem(ITEM_TYPE_SINGLE_DISCOUNT);
                    it.setBase(shopCartItem);
                    it.setUuid(shopCartItem.getUuid());
                    it.setName(shopCartItem.getPrivilege().getPrivilegeName());

                    it.setValue(shopCartItem.getPrivilege().getPrivilegeAmount().doubleValue());

                    data.add(it);
                }

                                if (shopCartItem.getCouponPrivilegeVo() != null) {
                    DishDataItem it = new DishDataItem(ITEM_TYPE_SINGLE_DISCOUNT);
                    it.setBase(shopCartItem);
                    it.setUuid(shopCartItem.getUuid());
                    String couponType = context.getString(R.string.coupon_type_gift);
                    it.setName(couponType + ":" + shopCartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeName());

                    it.setValue(shopCartItem.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeAmount().doubleValue());

                    if (shopCartItem.getCouponPrivilegeVo().isActived()) {
                        it.setCouponEnabled(true);
                    } else {
                        it.setCouponEnabled(false);
                    }
                    data.add(it);
                }

                                if (!TextUtils.isEmpty(shopCartItem.getMemo())) {
                    DishDataItem it2 = new DishDataItem(ITEM_TYPE_SINGLE_MEMO);
                    it2.setBase(shopCartItem);
                    it2.setUuid(shopCartItem.getUuid());
                    it2.setName(context.getResources().getString(R.string.order_dish_memo_semicolon)
                            + shopCartItem.getMemo());
                    data.add(it2);
                }
            }
        }
    }


    private void addOverallAttribute(TradeVo tradeVo, boolean isHasItem) {
                if (tradeVo.getCouponPrivilegeVoList() != null) {
            for (CouponPrivilegeVo couponPrivilegeVo : tradeVo.getCouponPrivilegeVoList()) {
                if (couponPrivilegeVo != null
                        && couponPrivilegeVo.getTradePrivilege() != null
                        && couponPrivilegeVo.getTradePrivilege().getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                DishDataItem item = new DishDataItem(ITEM_TYPE_CUSTOMER_COUPONS);
                item.setCouponPrivilegeVo(couponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege().getPrivilegeAmount() != null)
                    item.setValue(couponPrivilegeVo.getTradePrivilege().getPrivilegeAmount
                            ().doubleValue());

                switch (couponPrivilegeVo.getCoupon().getCouponType()) {

                    case REBATE:                        item.setName(context.getString(R.string.coupon_type_rebate) + STR_SEMICOLON
                                + couponPrivilegeVo.getCoupon().getName());
                        break;

                    case DISCOUNT:                        item.setName(context.getString(R.string.coupon_type_discount) +
                                STR_SEMICOLON + couponPrivilegeVo.getCoupon().getName());
                        break;
                    case GIFT:                        item.setName(context.getString(R.string.coupon_type_gift) + STR_SEMICOLON +
                                couponPrivilegeVo.getCoupon().getName());
                        break;
                    case CASH:                        item.setName(context.getString(R.string.coupon_type_cash) + STR_SEMICOLON +
                                couponPrivilegeVo.getCoupon().getName());
                        break;
                    default:
                        break;
                }
                                if (couponPrivilegeVo.isActived()) {
                    item.setCouponEnabled(true);
                } else {
                    item.setCouponEnabled(false);
                }
                data.add(item);
            }
        }

                List<WeiXinCouponsVo> weiXinCouponsVoList = tradeVo.getmWeiXinCouponsVo();
        if (Utils.isNotEmpty(weiXinCouponsVoList)) {
            for (WeiXinCouponsVo weiXinCouponsVo : weiXinCouponsVoList) {
                DishDataItem item = new DishDataItem(ITEM_TYPE_WEIXIN_COUPONS);
                item.setWeiXinCouponsVo(weiXinCouponsVo);
                TradePrivilege tradePrivilege = weiXinCouponsVo.getmTradePrivilege();
                                item.setName(tradePrivilege.getPrivilegeName());
                                if (tradePrivilege != null && tradePrivilege.getPrivilegeAmount() != null) {
                    item.setValue(tradePrivilege.getPrivilegeAmount().doubleValue());
                }
                                item.setCouponEnabled(weiXinCouponsVo.isActived());
                data.add(item);
            }
        }

        if (tradeVo.getIntegralCashPrivilegeVo() != null) {
            DishDataItem item = new DishDataItem(ITEM_TYPE_INTERGRAL);
            item.setIntegralCashPrivilegeVo(tradeVo.getIntegralCashPrivilegeVo());
            TradePrivilege privilege = tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
            if (privilege.getPrivilegeAmount() != null) {
                item.setValue(privilege.getPrivilegeAmount().doubleValue());
            }

                        if (tradeVo.getIntegralCashPrivilegeVo().isActived()) {
                item.setCouponEnabled(true);
                int value = privilege.getPrivilegeValue() == null ? 0 : privilege.getPrivilegeValue().intValue();                int amount = privilege.getPrivilegeAmount() == null ? 0 : privilege.getPrivilegeAmount().intValue();                item.setName(ResourceUtils.getString(R.string.use_integralcash, value + "", (0 - amount)));
            } else {
                item.setCouponEnabled(false);
                item.setName(context.getResources().getString(R.string.intergral_ineffective));
            }
            data.add(item);
        }
                List<TradePrivilege> datas = tradeVo.getTradePrivileges();
        if (datas != null) {
            for (TradePrivilege temp : datas) {
                Map<Long, ExtraCharge> chargeData = tradeVo.getExtraChargeMap();
                Long Id = temp.getPromoId();
                if (temp.getPrivilegeType() == PrivilegeType.ADDITIONAL
                        && chargeData.containsKey(Id)
                        && temp.getStatusFlag() == StatusFlag.VALID) {
                    DishDataItem item = new DishDataItem(ITEM_TYPE_EXTRA_CHARGE);
                    item.setExtraCharge(chargeData.get(Id));
                    item.setName(chargeData.get(Id).getName());
                    item.setValue(temp.getPrivilegeAmount().doubleValue());
                    data.add(item);
                }
            }
        }

                if (tradeVo.getTradePrivilege() != null) {
            isDiscountAll = true;
            DishDataItem item = new DishDataItem(ITEM_TYPE_ALL_DISCOUNT);
            item.setName(tradeVo.getTradePrivilege().getPrivilegeName());
            item.setValue(tradeVo.getTradePrivilege().getPrivilegeAmount().doubleValue());
            data.add(item);
        } else {
            isDiscountAll = false;
        }

                if (!TextUtils.isEmpty(tradeVo.getTrade().getTradeMemo())) {
            DishDataItem item = new DishDataItem(ITEM_TYPE_ALL_MEMO);
            item.setName(context.getResources().getString(R.string.remarkOrder) + tradeVo.getTrade().getTradeMemo());
            data.add(item);
        }

                String freeReason = getFreeReason(tradeVo);
        if (!TextUtils.isEmpty(freeReason)) {
            DishDataItem item = new DishDataItem(ITEM_TYPE_FREE_REASON);
            item.setName(context.getString(R.string.order_center_detail_reason_free, freeReason));
            data.add(item);
        }
                if (Utils.isEmpty(tradeVo.getTradeTaxs()) || !isHasItem) {
            return;
        }

        for (TradeTax tradeTax : tradeVo.getTradeTaxs()) {
            if (!tradeTax.isValid()) {
                continue;
            }
            DishDataItem item = new DishDataItem(ITEM_TYPE_TAX);
            item.setName(tradeTax.getTaxTypeName());
            if (tradeTax.getTaxAmount() != null)
                item.setValue(tradeTax.getTaxAmount().doubleValue());
            data.add(item);
        }
    }


    private String getFreeReason(TradeVo tradeVo) {
        TradeReasonRel tradeReasonRel = tradeVo.getOperateReason(OperateType.TRADE_FASTFOOD_FREE);
        if (tradeReasonRel != null) {
            return tradeReasonRel.getReasonContent();
        }
        return null;
    }

    public boolean switchDepositEnable() {
        mDepositEnable = !mDepositEnable;
        return mDepositEnable;
    }

    static public boolean getDepositEnable() {
        return mDepositEnable;
    }

    static public void setDepositEnable(boolean en) {
        mDepositEnable = en;
    }

    public void manualSelectSalesPromotionOperate(SalesPromotionRuleVo salesPromotionRuleVo) {
        mSalesPromotionRuleVo = salesPromotionRuleVo;

        if (Utils.isNotEmpty(data) && salesPromotionRuleVo != null) {
            for (DishDataItem item : data) {
                IShopcartItemBase base = item.getBase();
                if (base == null) {
                    continue;
                }

                TradeItemPlanActivity tradeItemPlanActivity = mTradeItemPlanActivityMap.get(base.getUuid());
                if (!MathSalesPromotionTool.isItemCanJoinPlanActivity(base, salesPromotionRuleVo)) {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
                } else if (tradeItemPlanActivity != null) {
                    Long panId = salesPromotionRuleVo.getRule().getPlanId();
                    if (tradeItemPlanActivity.getPlanId() != null && panId != null && tradeItemPlanActivity.getPlanId().compareTo(panId) == 0) {
                        item.setCheckStatus(DishDataItem.DishCheckStatus.CHECKED);
                    } else {
                        item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
                    }
                } else {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.NOT_CHECK);
                }
            }
        }
    }

    public void clear() {
        this.data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        if (arg0 >= data.size()) {
            return null;
        }
        return data.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public int getViewTypeCount() {

        return 11;
    }

    public ArrayList<DishDataItem> getAllData() {
        return data;
    }

    public void setEditModle(Boolean isEditModle) {
        this.isEditModle = isEditModle;
        if (isEditModle) {
            for (DishDataItem item : data) {
                if (item.getBase() == null)
                    continue;
                if (item.getBase().getEnableWholePrivilege() == Bool.NO) {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
                } else {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.NOT_CHECK);
                }
                TradeItemPlanActivity tradeItemPlanActivity = mTradeItemPlanActivityMap.get(item.getBase().getUuid());
                if (tradeItemPlanActivity != null) {
                    item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        DishDataItem item = (DishDataItem) getItem(position);
        if (item.getType() == ITEM_TYPE_FREE_REASON
                || item.getType() == ITEM_TYPE_GRAY_SEPERATOR
                || item.getType() == ITEM_TYPE_MARKET_ACTIVITY
                || ((item.getType() == ITEM_TYPE_CHILD
                || item.getType() == ITEM_TYPE_SINGLE_MEMO
                || item.getType() == ITEM_TYPE_COMBO_MEMO) && item.getBase() != null && (item.getBase() instanceof ReadonlyShopcartItemBase))) {
            return 1;
        } else if (item.getType() == ITEM_TYPE_DEPOSIT) {
            if (mDepositEnable)
                return 2;
            else
                return 3;
        }
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView =
                    LayoutInflater.from(context).inflate(R.layout.cashier_order_dish_selected_dish_swipemenu_item, null);
            holder = new ViewHolder();
            holder.topLine = convertView.findViewById(R.id.topline);
            holder.checkButton = (ImageView) convertView.findViewById(R.id.checkButton);
                        holder.dishView = (LinearLayout) convertView.findViewById(R.id.dishView);
            holder.dish_name = (TextView) convertView.findViewById(R.id.dish_name);
            holder.dish_num = (TextView) convertView.findViewById(R.id.dish_num);
            holder.dish_price = (TextView) convertView.findViewById(R.id.dish_price);
                        holder.dishTasteView = (LinearLayout) convertView.findViewById(R.id.dishTasteView);

                        holder.dishAddtionView = (LinearLayout) convertView.findViewById(R.id.dishAddtionView);
            holder.addtionName = (TextView) convertView.findViewById(R.id.dishAddtionName);

            holder.addtionPrice = (TextView) convertView.findViewById(R.id.dishAddtionPrice);

                        holder.dish_memo = (TextView) convertView.findViewById(R.id.dish_memo);

            holder.customAllowanceIv = (ImageView) convertView.findViewById(R.id.custom_allowance_iv);
            holder.depositDemo = (TextView) convertView.findViewById(R.id.deposit_demo);
            holder.deposit = (LinearLayout) convertView.findViewById(R.id.deposit);

                        holder.rlMarketActivity = (RelativeLayout) convertView.findViewById(R.id.rl_market_activity);
            holder.tvMarketActivityName = (TextView) convertView.findViewById(R.id.tv_market_activity_name);
            holder.tvMarketActivityValue = (TextView) convertView.findViewById(R.id.tv_market_activity_value);
            holder.ivMarketActivityRemove = (ImageView) convertView.findViewById(R.id.iv_market_activity_remove);

                        holder.viewGraySeperator = convertView.findViewById(R.id.view_seperator);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final DishDataItem item = data.get(position);

        final IShopcartItemBase shopcartItemBase = item.getBase();

        if (shopcartItemBase != null && mCurSelectUuid.equals(shopcartItemBase.getUuid())
                && (item.getType() == ITEM_TYPE_COMBO || item.getType() == ITEM_TYPE_SINGLE || item.getType() == ITEM_TYPE_CHILD)) {
            holder.dishView.setBackgroundResource(R.drawable.order_dish_item_shape);
            item.setNeedTopLine(false);
            pos = position;
        } else {
            if (((position == data.size() - 1) && (item.getType() == ITEM_TYPE_COMBO || item.getType() == ITEM_TYPE_SINGLE) && pos == position)
                    || item.getType() == ITEM_TYPE_FREE_REASON
                    || (pos + 1 == position && data.get(pos).getBase() != null && Utils.isEmpty(data.get(pos).getBase().getExtraItems()) && isContainProperty(data.get(pos).getBase().getProperties()))
                    || position == 0) {                item.setNeedTopLine(false);
            } else {
                item.setNeedTopLine(true);
            }
            holder.dishView.setBackgroundResource(0);
        }


        if (isDishCheckMode || isEditModle) {
            if (item.getType() == ITEM_TYPE_SINGLE || item.getType() == ITEM_TYPE_COMBO) {
                holder.checkButton.setVisibility(View.VISIBLE);
                if (item.getCheckStatus() == DishDataItem.DishCheckStatus.CHECKED) {
                    holder.checkButton.setBackgroundResource(R.drawable.checkbox_selected);
                } else if (item.getCheckStatus() == DishDataItem.DishCheckStatus.NOT_CHECK) {
                    holder.checkButton.setBackgroundResource(R.drawable.checkbox_nomal);
                } else {
                    holder.checkButton.setBackgroundResource(R.drawable.checkbox_cannot_discount);
                }
            } else {
                holder.checkButton.setVisibility(View.INVISIBLE);
                holder.checkButton.setBackgroundDrawable(null);
            }
        } else {
            holder.checkButton.setVisibility(View.GONE);
            holder.checkButton.setBackgroundDrawable(null);
        }

        if (item.getType() == ITEM_TYPE_MARKET_ACTIVITY) {
            holder.topLine.setVisibility(View.GONE);
            holder.dishView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.VISIBLE);
            holder.viewGraySeperator.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(item.getName())) {
                holder.tvMarketActivityName.setText(item.getName());
            } else {
                holder.tvMarketActivityName.setText("");
            }
            String moneySymbol = ShopInfoCfg.getInstance().getCurrencySymbol();
            holder.tvMarketActivityValue.setText(item.getValue() < 0 ? "-" + moneySymbol + (0 - item.getValue()) : moneySymbol
                    + item.getValue());
            holder.ivMarketActivityRemove.setVisibility(View.VISIBLE);
            holder.ivMarketActivityRemove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ShoppingCart.getInstance().removeSalesPromotion(item.getTradePlanActivityUuid());
                }
            });

        } else if (item.getType() == ITEM_TYPE_COMBO) {             holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.VISIBLE);
            holder.dish_num.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishOrderTextStyle);
            if (shopcartItemBase != null) {
                holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                holder.dish_name.setText(shopcartItemBase.getSkuName());                 holder.dish_num.setText(MathDecimal.toTrimZeroString(shopcartItemBase.getSingleQty()).toString());                holder.dish_price.setText(formatPrice(shopcartItemBase.getAmount().doubleValue()));                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
                if (shopcartItemBase.getEnableWholePrivilege() == Bool.NO && this.isDiscountAll) {
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(this.mEnableWholeIcon, null, null, null);
                    holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                } else {
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    holder.dish_name.setCompoundDrawablePadding(0);
                }

                setPackAndRepayIcon(shopcartItemBase, holder);

            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(0);
            }
            showPropertys(shopcartItemBase, holder, false);            showExtra(shopcartItemBase, holder, false);        } else if (item.getType() == ITEM_TYPE_SINGLE) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.VISIBLE);
            holder.dish_num.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishOrderTextStyle);
            if (shopcartItemBase != null) {
                holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                holder.dish_name.setText(shopcartItemBase.getSkuName());                 if (shopcartItemBase.getDishShop() != null
                        && shopcartItemBase.getDishShop().getSaleType() != null
                        && shopcartItemBase.getDishShop().getSaleType() == SaleType.WEIGHING) {
                    holder.dish_num.setText(MathDecimal.toTrimThreeZeroString(shopcartItemBase.getSingleQty()).toString());                } else {
                    holder.dish_num.setText(MathDecimal.toTrimZeroString(shopcartItemBase.getSingleQty()).toString());                }
                holder.dish_price.setText(formatPrice(shopcartItemBase.getAmount().doubleValue()));                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));

                if (shopcartItemBase.getEnableWholePrivilege() == Bool.NO && this.isDiscountAll) {
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(this.mEnableWholeIcon, null, null, null);
                    holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                } else {
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    holder.dish_name.setCompoundDrawablePadding(0);
                }
                setPackAndRepayIcon(shopcartItemBase, holder);

            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(0);
            }
            showPropertys(shopcartItemBase, holder, false);            showExtra(shopcartItemBase, holder, false);                    } else if (item.getType() == ITEM_TYPE_CHILD) {
            holder.topLine.setVisibility(View.GONE);
            holder.dishView.setVisibility(View.VISIBLE);
            holder.dish_num.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getIsComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishMemoStyle);
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(this.mChildIcon, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
            if (shopcartItemBase != null) {
                holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                holder.dish_name.setText(shopcartItemBase.getSkuName());                 if (shopcartItemBase.getDishShop() != null
                        && shopcartItemBase.getDishShop().getSaleType() != null
                        && shopcartItemBase.getDishShop().getSaleType() == SaleType.WEIGHING) {
                    holder.dish_num.setText(MathDecimal.toTrimThreeZeroString(shopcartItemBase.getSingleQty()).toString());                } else {
                    holder.dish_num.setText(MathDecimal.toTrimZeroString(shopcartItemBase.getSingleQty()).toString());                }
                                if (shopcartItemBase.getPrice() == null || shopcartItemBase.getPrice().compareTo(new BigDecimal(0)) == 0) {
                    holder.dish_price.setText("");
                } else {
                    holder.dish_price.setText(formatPrice(shopcartItemBase.getAmount().doubleValue()));

                }

            }
            showPropertys(shopcartItemBase, holder, true);            showExtra(shopcartItemBase, holder, true);                    } else if (item.getType() == ITEM_TYPE_SINGLE_MEMO || item.getType() == ITEM_TYPE_COMBO_MEMO) {
            holder.topLine.setVisibility(View.GONE);
            holder.dishView.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dish_memo.setLayoutParams(getMemoDiyWh(context));
            holder.dish_memo.setVisibility(View.VISIBLE);
            holder.dish_memo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(0);
            if (!TextUtils.isEmpty(item.getName())) {
                holder.dish_memo.setText(item.getName());
            } else {
                holder.dish_memo.setText("");
            }
                    } else if (item.getType() == ITEM_TYPE_CHILD_MEMO) {
            holder.topLine.setVisibility(View.GONE);
            holder.dishView.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dish_memo.setLayoutParams(getPropertyDiyWh(context, true));
            holder.dish_memo.setVisibility(View.VISIBLE);
            holder.dish_memo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(0);
            if (!TextUtils.isEmpty(item.getName())) {
                holder.dish_memo.setText(item.getName());
            } else {
                holder.dish_memo.setText("");
            }
                    } else if (item.getType() == ITEM_TYPE_SINGLE_DISCOUNT || item.getType() == ITEM_TYPE_COMBO_DISCOUNT) {
            holder.topLine.setVisibility(View.GONE);
            holder.dishView.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.VISIBLE);
            holder.dishAddtionView.setLayoutParams(getMemoDiyWh(context));
            holder.dish_memo.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.addtionName.setTextColor(context.getResources().getColor(R.color.text_gray));
            holder.addtionName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            if (!TextUtils.isEmpty(item.getName())) {

                if (shopcartItemBase.getPrivilege() != null) {
                    PrivilegeType privilegeType = shopcartItemBase.getPrivilege().getPrivilegeType();
                    if (privilegeType == PrivilegeType.MEMBER_REBATE || privilegeType == PrivilegeType.MEMBER_PRICE || privilegeType == PrivilegeType.AUTO_DISCOUNT) {                        holder.customAllowanceIv.setVisibility(View.VISIBLE);
                        holder.addtionPrice.setTextColor(context.getResources().getColor(R.color.customer_allowance));
                    } else {
                        holder.customAllowanceIv.setVisibility(View.GONE);
                        holder.addtionPrice.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
                    }
                    holder.addtionPrice.setText(formatPrice(item.getValue()));                }


                if (shopcartItemBase.getCouponPrivilegeVo() != null && shopcartItemBase.getCouponPrivilegeVo().getTradePrivilege().getPrivilegeType() == PrivilegeType.COUPON) {
                    holder.customAllowanceIv.setVisibility(View.GONE);
                    if (item.isCouponEnabled()) {

                        holder.addtionName.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
                        holder.addtionName.setCompoundDrawablesWithIntrinsicBounds(mCouponEnabledIcon, null, null, null);
                        holder.addtionName.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                        holder.addtionPrice.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
                        holder.addtionPrice.setText(formatPrice(item.getValue()));
                    } else {
                        holder.addtionPrice.setText("");                        holder.addtionName.setTextColor(context.getResources().getColor(R.color.shopcat_item_coupon_unenabled));
                        holder.addtionName.setCompoundDrawablesWithIntrinsicBounds(mUnEnableCouponIcon, null, null, null);
                        holder.addtionName.setCompoundDrawablePadding(5);
                    }
                }

                holder.addtionName.setText(item.getName());


            } else {
                holder.addtionName.setText("");
                holder.addtionPrice.setText("");
                holder.customAllowanceIv.setVisibility(View.GONE);
            }
                    } else if (item.getType() == ITEM_TYPE_ALL_MEMO) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dish_memo.setLayoutParams(getPropertyDiyWh(context, false));
            holder.dish_memo.setVisibility(View.VISIBLE);
            holder.dish_memo.setCompoundDrawablesWithIntrinsicBounds(mAllMemoIcon, null, null, null);
            holder.dish_memo.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
            if (!TextUtils.isEmpty(item.getName())) {
                SpannableStringBuilder builder = new SpannableStringBuilder(item.getName());
                builder.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.buton_text_bule)),
                        0,
                        0,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.dish_memo.setText(builder);
            } else {
                holder.dish_memo.setText("");
            }
                    } else if (item.getType() == ITEM_TYPE_ALL_DISCOUNT) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishMemoStyle);
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(this.mAllDiscountIcon, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
            if (!TextUtils.isEmpty(item.getName())) {
                holder.dishView.setVisibility(View.VISIBLE);
                holder.dish_name.setText(item.getName());                                 holder.dish_num.setText("");                holder.dish_price.setText(formatPrice(item.getValue()));                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            } else {
                holder.dishView.setVisibility(View.GONE);
            }
                    } else if (item.getType() == ITEM_TYPE_CUSTOMER_COUPONS
                || item.getType() == ITEM_TYPE_WEIXIN_COUPONS) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishMemoStyle);
            if (item.isCouponEnabled()) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mCouponEnabledIcon, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                if (item.getValue() != 0d) {
                    holder.dish_price.setText(formatPrice(item.getValue()));                } else {
                    holder.dish_price.setText("");                }
            } else {
                holder.dish_price.setText("");                holder.dish_name.setTextColor(context.getResources().getColor(R.color.shopcat_item_coupon_unenabled));
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mUnEnableCouponIcon, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(5);
            }

            if (!TextUtils.isEmpty(item.getName())) {
                holder.dishView.setVisibility(View.VISIBLE);
                holder.dish_name.setText(item.getName());                 holder.dish_num.setText("");                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            } else {
                holder.dishView.setVisibility(View.GONE);
            }
                    } else if (item.getType() == ITEM_TYPE_INTERGRAL) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishMemoStyle);
            if (item.isCouponEnabled()) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mIntegralEnableIcon, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                if (item.getValue() != 0d) {
                    holder.dish_price.setText(formatPrice(item.getValue()));                } else {
                    holder.dish_price.setText("");                }
            } else {
                holder.dish_price.setText("");                holder.dish_name.setTextColor(context.getResources().getColor(R.color.shopcat_item_coupon_unenabled));
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mUnEnableIntegralIcon, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(5);
            }

            if (!TextUtils.isEmpty(item.getName())) {
                holder.dishView.setVisibility(View.VISIBLE);
                holder.dish_name.setText(item.getName());                 holder.dish_num.setText("");                holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            } else {
                holder.dishView.setVisibility(View.GONE);
            }
        } else if (item.getType() == ITEM_TYPE_FREE_REASON) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.VISIBLE);
            holder.dish_memo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.dish_memo.setText(item.getName());         } else if (item.getType() == ITEM_TYPE_EXTRA_CHARGE) {            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.dish_num.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishOrderTextStyle);
            holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            holder.dish_name.setText(item.getName());
            holder.dish_price.setText(formatPrice(item.getValue()));
            holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mExtraChargeIcon, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(5);

                    } else if (item.getType() == ITEM_TYPE_DEPOSIT) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.VISIBLE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setCompoundDrawablePadding(5);
            holder.dish_name.setTextAppearance(context, R.style.dishOrderTextStyle);
            holder.dish_name.setText(context.getString(R.string.order_deposit));
            holder.depositDemo.setText(context.getString(R.string.order_deposit_paymode_tip));
            holder.dish_num.setVisibility(View.INVISIBLE);
            holder.dish_price.setText(formatPrice(item.getValue()));            if (mDepositEnable) {
                holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                holder.depositDemo.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDepositIcon, null, null, null);
                holder.dish_price.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
                holder.dish_num.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            } else {
                holder.dish_name.setTextColor(context.getResources().getColor(R.color.deposit_grayword));
                holder.depositDemo.setTextColor(context.getResources().getColor(R.color.deposit_grayword));
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mDepositIconInvalid, null, null, null);
                holder.dish_price.setTextColor(context.getResources().getColor(R.color.deposit_grayword));
                holder.dish_num.setTextColor(context.getResources().getColor(R.color.deposit_grayword));
            }
        } else if (item.getType() == ITEM_TYPE_GRAY_SEPERATOR) {
            holder.topLine.setVisibility(View.GONE);
            holder.dishView.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams diyWh = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(context, 10));
            diyWh.setMargins(0, 0, 0, 0);
            holder.viewGraySeperator.setLayoutParams(diyWh);
        } else if (item.getType() == ITEM_TYPE_TAX) {
            holder.topLine.setVisibility(View.VISIBLE);
            holder.dishView.setVisibility(View.VISIBLE);
            holder.dishTasteView.setVisibility(View.GONE);
            holder.dishAddtionView.setVisibility(View.GONE);
            holder.dish_memo.setVisibility(View.GONE);
            holder.dish_num.setVisibility(View.GONE);
            holder.deposit.setVisibility(View.GONE);
            holder.rlMarketActivity.setVisibility(View.GONE);
            holder.viewGraySeperator.setVisibility(View.GONE);
            holder.dishView.setLayoutParams(getNoComboDiyWh(context));
            holder.dish_name.setTextAppearance(context, R.style.dishOrderTextStyle);
            holder.dish_name.setTextColor(context.getResources().getColor(R.color.orderdish_text_black));
            holder.dish_name.setText(item.getName());
            holder.dish_price.setText(formatPrice(item.getValue()));
            holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mExtraChargeIcon, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(5);
        }

        if (item.isNeedTopLine()) {
            holder.topLine.setVisibility(View.VISIBLE);
        } else {
            holder.topLine.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private void showExtra(IShopcartItemBase shopcartItem, ViewHolder holder, boolean isChild) {
        if (shopcartItem != null && shopcartItem.getExtraItems() != null && shopcartItem.getExtraItems().size() > 0) {
            StringBuilder sb = new StringBuilder(context.getResources().getString(R.string.addExtra));
            int i = 0;
                        BigDecimal extraAmount = BigDecimal.ZERO;
            for (IExtraShopcartItem extra : shopcartItem.getExtraItems()) {
                if (i > 0) {
                    sb.append(STR_COMMA);
                }
                sb.append(extra.getSkuName() + " x" + extra.getSingleQty().toString());
                i++;
                extraAmount = extraAmount.add(extra.getActualAmount());
            }
            if (i > 0) {
                holder.addtionName.setTextColor(context.getResources().getColor(R.color.settings_grayword));
                holder.addtionName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                holder.addtionName.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.dish_memo.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                holder.dishAddtionView.setLayoutParams(getExtraDiyWh(context, isChild));
                holder.dishAddtionView.setVisibility(View.VISIBLE);
                holder.addtionName.setText(sb.toString());

                holder.addtionPrice.setText(formatPrice(extraAmount.doubleValue()));
                holder.addtionPrice.setVisibility(View.VISIBLE);

                holder.customAllowanceIv.setVisibility(View.GONE);
                holder.addtionPrice.setTextColor(context.getResources().getColor(R.color.settings_grayword));

            } else {

                holder.dishAddtionView.setVisibility(View.GONE);
            }
        } else {
            holder.dishAddtionView.setVisibility(View.GONE);
        }
    }


    private void showPropertys(IShopcartItemBase shopcartItem, ViewHolder holder, boolean isChild) {

        if (shopcartItem != null && shopcartItem.getProperties() != null && shopcartItem.getProperties().size() > 0) {
            holder.dishTasteView.removeAllViews();
            LinearLayout.LayoutParams diyWh =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
            diyWh.setMargins(DensityUtil.dip2px(context, 30), 0, DensityUtil.dip2px(context, 20), DensityUtil.dip2px(context, 6));
            holder.dishTasteView.setLayoutParams(diyWh);

            int iPR = 0;
            StringBuilder sbST = new StringBuilder("");
            StringBuilder propertySb = new StringBuilder(context.getString(R.string.addMethod));
            LayoutInflater inflater = LayoutInflater.from(context);
                        BigDecimal propertyAmount = BigDecimal.ZERO;
            for (IOrderProperty property : shopcartItem.getProperties()) {

                if (property.getPropertyKind() == PropertyKind.PROPERTY) {

                    if (iPR > 0) {
                        propertySb.append(STR_COMMA);
                    }
                    propertySb = propertySb.append(property.getPropertyName());
                                        propertyAmount = propertyAmount.add(property.getPropertyPrice().multiply(shopcartItem.getTotalQty()));

                    iPR++;
                }
            }

            if (iPR > 0) {
                LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dish_taste_item, null);

                layout.setLayoutParams(getPropertyDiyWh(context, isChild));
                TextView tasteNameTv = (TextView) layout.findViewById(R.id.taste_name);
                TextView tastePriceTv = (TextView) layout.findViewById(R.id.taste_price);

                tasteNameTv.setText(propertySb.toString());
                tastePriceTv.setText(formatPrice(propertyAmount.doubleValue()));
                holder.dishTasteView.addView(layout);
            }

            for (IOrderProperty property : shopcartItem.getProperties()) {

                if (property.getPropertyKind() == PropertyKind.STANDARD) {
                    sbST.append(property.getPropertyName());
                    sbST.append(STR_COMMA);
                }
            }

                        if (iPR > 0) {
                holder.dishTasteView.setVisibility(View.VISIBLE);

            } else {
                holder.dishTasteView.setVisibility(View.GONE);
            }

            int len = sbST.length();
            if (len > 0) {
                sbST.setLength(len - 1);
                holder.dish_name.setText(shopcartItem.getSkuName() + "(" + sbST + ")");
            }

        } else {
            holder.dishTasteView.setVisibility(View.GONE);
        }

                                                                                                                                                                    }

    public LinearLayout.LayoutParams getIsComboDiyWh(Context context) {
        int left = 0;
        int topOrBottom = DensityUtil.dip2px(context, 0);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }

    public LinearLayout.LayoutParams getNoComboDiyWh(Context context) {
        int top = DensityUtil.dip2px(context, 0);
        int bottom = DensityUtil.dip2px(context, 0);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, top, 0, bottom);
        return diyWh;
    }


    private LinearLayout.LayoutParams getPropertyDiyWh(Context context, boolean isChild) {
        int left = 0, topOrBottom = 0;
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        if (isChild) {
            left = 0;
            topOrBottom = DensityUtil.dip2px(context, 5);
        } else {
            left = 0;
            topOrBottom = DensityUtil.dip2px(context, 5);
        }
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }


    private LinearLayout.LayoutParams getMemoDiyWh(Context context) {
        int left = 0, topOrBottom = 0;
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        left = DensityUtil.dip2px(context, 10);
        topOrBottom = DensityUtil.dip2px(context, 5);
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }


    private void setPackAndRepayIcon(IShopcartItemBase shopcartItemBase, ViewHolder holder) {
        if (shopcartItemBase.getPack() && shopcartItemBase instanceof ReadonlyShopcartItemBase) {
            if (isDiscountAll) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mAllDiscountIcon, null, mPackAndRepay, null);
            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, mPackAndRepay, null);
            }
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        } else if (shopcartItemBase.getPack()) {
            if (isDiscountAll) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mAllDiscountIcon, null, mPackIcon, null);
            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, mPackIcon, null);
            }
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        } else if (shopcartItemBase instanceof ReadonlyShopcartItem) {
            if (isDiscountAll) {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(mAllDiscountIcon, null, this.mRepayIcon, null);
            } else {
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, this.mRepayIcon, null);
            }
            holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        } else if (!isDiscountAll) {
            holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            holder.dish_name.setCompoundDrawablePadding(0);
        }
    }



    public void setCurSelectPos(String pos) {
        mCurSelectUuid = pos;
        this.pos = -2;
    }


    public String getCurSelectUuid() {
        return mCurSelectUuid;
    }


    public LinearLayout.LayoutParams getExtraDiyWh(Context context, boolean isChild) {
        int left = 0, top = 0, bottom = 0;
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        if (isChild) {
            left = DensityUtil.dip2px(context, 10);
            top = 0;
            bottom = DensityUtil.dip2px(context, 2);
        } else {
            left = DensityUtil.dip2px(context, 10);
            top = 0;
            bottom = DensityUtil.dip2px(context, 2);
        }
        diyWh.setMargins(left, top, 0, bottom);
        return diyWh;
    }

    class ViewHolder {
        View topLine;

        ImageView checkButton;

        LinearLayout dishView;

        TextView dish_name;

        TextView dish_num;

        TextView dish_price;

        LinearLayout dishTasteView;
        LinearLayout dishAddtionView;
        TextView addtionName;

        TextView addtionPrice;

        TextView dish_memo;

        ImageView customAllowanceIv;
        TextView depositDemo;

        LinearLayout deposit;

        RelativeLayout rlMarketActivity;
        TextView tvMarketActivityName;
        TextView tvMarketActivityValue;
        ImageView ivMarketActivityRemove;
        View viewGraySeperator;
    }



    public static String formatPrice(double value) {
        try {
            DecimalFormat df = new DecimalFormat("0.00");
            if (value >= 0) {

                return ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(value);

            } else {

                return "-" + ShopInfoCfg.getInstance().getCurrencySymbol() + df.format(Math.abs(value));
            }
        } catch (Exception e) {

            Log.e(TAG, "", e);

            return value + "";
        }
    }


    private boolean isContainProperty(List<? extends IOrderProperty> properties) {
        if (Utils.isEmpty(properties)) {
            return true;
        }
        for (IOrderProperty property : properties) {

            if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                return false;
            }
        }
        return true;
    }
}
