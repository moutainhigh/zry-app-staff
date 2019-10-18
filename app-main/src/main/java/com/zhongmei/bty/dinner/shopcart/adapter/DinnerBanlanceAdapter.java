package com.zhongmei.bty.dinner.shopcart.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhongmei.bty.basemodule.discount.bean.MarketRuleVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.bty.basemodule.trade.manager.DinnerCashManager;
import com.zhongmei.bty.basemodule.trade.manager.DinnerShopManager;
import com.zhongmei.bty.snack.event.EventSelectDish;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.context.data.ShopInfoCfg;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.ActivityRuleEffective;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.DensityUtil;
import com.zhongmei.yunfu.util.ResourceUtils;
import com.zhongmei.yunfu.util.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;


public class DinnerBanlanceAdapter extends SuperShopCartAdapter {
    private static final String TAG = DinnerBanlanceAdapter.class.getSimpleName();

    protected Context mContext;

        private boolean isShowRightAnchor = false;
        private boolean isShowLeftAnchor = false;

    private Drawable mAllDiscountIcon;    private Drawable mAllFreeReasonIcon;
    private Drawable mBanquetIcon;
    private Drawable mAllMemoIcon;    protected Drawable mCouponEnabledIcon, mCouponUnEnabledIcon, mIntegralEnableIcon, mIntegralUnEnableIcon, mMinConsumIcon, mServicIcon;
        private Drawable mExtraIcon;

        private List<DishDataItem> unMarketActivityItems = new ArrayList<DishDataItem>();
    private MarketRuleVo marketRuleVo;    protected boolean canRemoveMarketActivity = false;
    public DinnerBanlanceAdapter(Context context) {
        init(context);
        mContext = context;
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        this.mAllDiscountIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_alldiscount_icon);
        this.mAllFreeReasonIcon = context.getResources().getDrawable(R.drawable.ic_cashier_order_center_all_reason);
        this.mAllMemoIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_allmemo_icon);
        this.mCouponEnabledIcon = context.getResources().getDrawable(R.drawable.cashier_order_dish_coupon_enabled_icon);
        this.mCouponUnEnabledIcon = context.getResources().getDrawable(R.drawable.unenabled_coupon_icon);
        this.mIntegralEnableIcon = context.getResources().getDrawable(R.drawable.integral);
        this.mIntegralUnEnableIcon = context.getResources().getDrawable(R.drawable.unenabled_integral_icon);
        this.mExtraIcon = context.getResources().getDrawable(R.drawable.dinner_extra_icon);
        this.mBanquetIcon = context.getResources().getDrawable(R.drawable.shopcart_banquet_icon);
        this.mMinConsumIcon = context.getResources().getDrawable(R.drawable.icon_minconsum);
        this.mServicIcon = context.getResources().getDrawable(R.drawable.dinner_servic_icon);
    }

    protected View loadDishLayout() {
        View convertView = LayoutInflater.from(context).inflate(R.layout.dinner_dish_shopcart_item_balance, null);
        return convertView;
    }

    protected View initDishLayout(ViewHolder holder) {
        View convertView = loadDishLayout();
        initDishCommon(holder, convertView);
        setTopLineMargin(holder.topLine);
        initAnchorLayout(holder, convertView);
        return convertView;
    }

    protected void showTitleTopLine(TitleHolder titleHolder) {
        setTopLineMargin(titleHolder.topLine);
    }

    protected void setTopLineMargin(View view) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.setMargins(DensityUtil.dip2px(mContext, 20), 0, DensityUtil.dip2px(mContext, 20), 0);
    }

    public List<DishDataItem> getUnMarketActivityItems() {
        return unMarketActivityItems;
    }

    public void setUnMarketActivityItems(List<DishDataItem> unMarketActivityItems) {
        this.unMarketActivityItems = unMarketActivityItems;
    }

    public boolean isShowRightAnchor() {
        return isShowRightAnchor;
    }

    public void setShowRightAnchor(boolean showRightAnchor) {
        isShowRightAnchor = showRightAnchor;
    }

    public boolean isShowLeftAnchor() {
        return isShowLeftAnchor;
    }

    public void setShowLeftAnchor(boolean showLeftAnchor) {
        isShowLeftAnchor = showLeftAnchor;
    }


    public boolean isDiscountModle() {
        return isBatchDiscountModle;
    }


    public void setDiscountModle(Boolean isDiscountModle) {
        this.isBatchDiscountModle = isDiscountModle;
        isBatchCoercionModel = false;
        notifyDataSetChanged();
    }

    public void setDiscountModleNoNotify(Boolean isDiscountModle) {
        this.isBatchDiscountModle = isDiscountModle;
    }


    public void setBatchCoercionModel(boolean isBatchCoercionModel) {
        this.isBatchCoercionModel = isBatchCoercionModel;
        if (isBatchCoercionModel) {
            checkCancelAll();
        }
        notifyDataSetChanged();
    }

    public boolean isBatchCoercionModel() {
        return isBatchCoercionModel;
    }


    public void setIsDiscountAll(boolean isDiscountAllMode) {
        this.isDiscountAllMode = isDiscountAllMode;
    }


    public void isShowMemeberDiscount(boolean isShow) {
        isShowAllDiscount = isShow;
    }

    public MarketRuleVo getMarketRuleVo() {
        return marketRuleVo;
    }

    public void setMarketRuleVo(MarketRuleVo marketRuleVo) {
        this.marketRuleVo = marketRuleVo;
    }

    public void isShowFreeDiscount(boolean isShow) {
        this.isShowFreeDiscount = isShow;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        return view;
    }

    protected void showDishLayout(ViewHolder holder, final DishDataItem item, int position) {
        final IShopcartItemBase shopcartItem = item.getBase();
        holder.mainLayout.setAlpha(1f);
        showDiscountDrawable(holder, item);
        updateDishCheck(holder, item);
        holder.tvWeighFlag.setVisibility(View.GONE);
        holder.dishView.setVisibility(View.VISIBLE);
        holder.dish_num.setVisibility(View.VISIBLE);
        setDishLayoutValue(item, holder);
        switch (item.getType()) {
            case SINGLE:            case COMBO:                holder.topLine.setVisibility(View.VISIBLE);
                holder.dishView.setLayoutParams(getNoComboDiyLiWh(context));
                holder.dish_name.setTextAppearance(context, R.style.dinnerOrderTextStyle);
                if (shopcartItem != null) {

                    if (isBatchDiscountModle || isDishCheckMode) {
                    } else {
                        holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                        resetIcon(shopcartItem, holder);
                    }
                } else {
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    holder.dish_name.setCompoundDrawablePadding(0);
                }
                updatePrintState(shopcartItem, holder);
                break;
            case CHILD:                holder.topLine.setVisibility(View.GONE);

                holder.dishView.setLayoutParams(getIsComboDiyLiWh(context));
                holder.dish_name.setTextAppearance(context, R.style.dinnerMemoStyle);
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(this.mChildIcon, null, null, null);
                holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                holder.dish_printstate.setVisibility(View.GONE);

                if (shopcartItem != null) {
                    if (shopcartItem.getActualAmount() != null) {
                        holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedComboDishPrice));
                    } else {
                        holder.dish_price.setText("");
                        holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
                    }
                }
                break;
            default:
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                break;
        }
        showStand(item, holder);
        setTopLine(holder.topLine, item, position);
        setDeleteIcon(item, holder);        setGray(holder, item);        setAnchor(holder, item);
    }

    protected void showDiscountDrawable(ViewHolder holder, DishDataItem item) {
        if (isBatchDiscountModle && (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)) {
                                    if (item.getBase() != null) {
                                if (DinnerCashManager.hasMarketActivity(tradeItemPlanActivityMap, item.getBase())
                        || (!isBatchCoercionModel && item.getBase().getEnableWholePrivilege() == Bool.NO)) {
                    Drawable checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
                    holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
                    holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
                } else {
                    itemSelect(item, holder);
                }
                            } else {
                Drawable checkDrawable = context.getResources().getDrawable(R.drawable.checkbox_cannot_discount);
                holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(checkDrawable, null, null, null);
            }
        }
    }

    protected LinearLayout.LayoutParams getPropertyDiyWh(Context context, boolean isChild) {
        int left = 0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 60);
        } else {
            left = DensityUtil.dip2px(context, 10);
        }
        return getPropertyDiyWh(left, 0, 0, 0);
    }


    protected LinearLayout.LayoutParams getExtraDiyWh(Context context, boolean isChild) {
        int left = 0;
        if (isChild) {
            left = DensityUtil.dip2px(context, 60);
        } else {
            left = DensityUtil.dip2px(context, 26);
        }
        return getExtraDiyWh(left, 0, 0, 0);
    }


    public LinearLayout.LayoutParams getIsComboDiyLiWh(Context context) {
        int left = DensityUtil.dip2px(context, 18);
        int topOrBottom = DensityUtil.dip2px(context, 5);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(left, topOrBottom, 0, topOrBottom);
        return diyWh;
    }


    public LinearLayout.LayoutParams getNoComboDiyLiWh(Context context) {
        int top = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        int bottom = (int) context.getResources().getDimension(R.dimen.dinner_shoppingCard_dishName_margin);
        LinearLayout.LayoutParams diyWh =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        diyWh.setMargins(0, top, 0, bottom);
        return diyWh;
    }

    protected LinearLayout.LayoutParams getPrivilegeDiyWh(Context context, View view) {
        LinearLayout.LayoutParams layoutParams = getPrivilegeDiyWh(context, view, 20);
        return layoutParams;
    }

    protected void showPrivilege(PrivilegeHolder holder, DishDataItem item) {
        holder.privilegeView.setVisibility(View.VISIBLE);
        holder.dish_name.setTextAppearance(context, R.style.dinnerMemoStyle);
        holder.dish_name.setCompoundDrawablePadding(IMAGEMARGINRIGHT);
        Drawable iconDrawable = null;
        switch (item.getType()) {
            case EXCISE_TAX:
            case ADDITIONAL:                iconDrawable = mExtraIcon;
                if (item.getExtraType() == ExtraItemType.MIN_CONSUM) {
                    iconDrawable = mMinConsumIcon;
                }
                break;
            case SERVICE:
                iconDrawable = mServicIcon;
                break;
            case ALL_DISCOUNT:                iconDrawable = mAllDiscountIcon;
                break;
            case BANQUET_PRIVILIGE:                iconDrawable = mBanquetIcon;
                break;
            case COUPONS:            case WECHAT_CARD_COUPONS:
                if (item.isEnabled()) {
                    iconDrawable = mCouponEnabledIcon;
                } else {
                    iconDrawable = mCouponUnEnabledIcon;
                    holder.dish_name.setTextColor(context.getResources()
                            .getColor(R.color.shopcat_item_coupon_unenabled));
                }
                break;
            case INTERGRAL:
                                if (item.isEnabled()) {
                    iconDrawable = mIntegralEnableIcon;
                } else {
                    iconDrawable = mIntegralUnEnableIcon;
                    holder.dish_name.setTextColor(context.getResources()
                            .getColor(R.color.shopcat_item_coupon_unenabled));
                }
                break;
            case CHARGE_PRIVILEGE:
                iconDrawable = mAllDiscountIcon;
                break;
        }
        if (item.getValue() != 0d) {
            holder.dish_price.setText(Utils.formatPrice(item.getValue()));        } else {
            holder.dish_price.setText("");        }
        if (!TextUtils.isEmpty(item.getName())) {
            holder.dish_name.setText(item.getName());             holder.dish_price.setText(Utils.formatPrice(item.getValue()));            holder.dish_price.setTextColor(context.getResources().getColor(R.color.selectedDishPrice));
            if (TextUtils.isEmpty(item.getTradeReason())) {
                holder.dish_memo.setVisibility(View.GONE);
            } else {
                holder.dish_memo.setVisibility(View.VISIBLE);
                holder.dish_memo.setText(item.getTradeReason());
            }
        } else {
            holder.privilegeView.setVisibility(View.GONE);
        }
        holder.dish_name.setCompoundDrawablesWithIntrinsicBounds(iconDrawable, null, null, null);
    }

    protected void showMarketView(MarketHolder marketHolder, final DishDataItem item) {
        marketHolder.llMarketActivity.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(item.getName())) {
            marketHolder.tvMarketActivityName.setText(item.getName());
        } else {
            marketHolder.tvMarketActivityName.setText("");
        }
        String moneySymbol = ShopInfoCfg.getInstance().getCurrencySymbol();
        marketHolder.tvMarketActivityValue.setText(item.getValue() < 0 ? "-" + moneySymbol + (0 - item.getValue()) : moneySymbol
                + item.getValue());
        if (canRemoveMarketActivity) {
            marketHolder.ivClose.setVisibility(View.VISIBLE);
            marketHolder.ivClose.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DinnerShopManager.getInstance().getShoppingCart().removeMarketActivity(item.getTradePlanActivityUuid());
                }
            });
        } else {
            marketHolder.ivClose.setVisibility(View.GONE);
        }
    }

    protected void showSepLine(SeperatorHolder sepHolder) {
        sepHolder.viewGraySeperator.setVisibility(View.VISIBLE);
    }


    protected void setAnchor(ViewHolder holder, DishDataItem item) {
        if (item == null || item.getBase() == null || item.getBase().getStatusFlag() == null) {
            holder.imgAnchorLeft.setVisibility(View.INVISIBLE);
            holder.imgAnchorRight.setVisibility(View.INVISIBLE);
            return;
        }
        if (item.getBase().getStatusFlag() == StatusFlag.VALID && (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)) {
            if (isShowLeftAnchor) {
                holder.imgAnchorLeft.setVisibility(View.VISIBLE);
            } else {
                holder.imgAnchorLeft.setVisibility(View.INVISIBLE);
            }
            if (isShowRightAnchor) {
                holder.imgAnchorRight.setVisibility(View.VISIBLE);
            } else {
                holder.imgAnchorRight.setVisibility(View.INVISIBLE);
            }
        } else {
            holder.imgAnchorLeft.setVisibility(View.INVISIBLE);
            holder.imgAnchorRight.setVisibility(View.INVISIBLE);
        }
    }

    public void updateData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        data.clear();
        deskCount = tradeVo.getDeskCount();
        this.initAllDishCount(BigDecimal.ZERO);        if (tradeVo == null) {
            return;
        }
                sumAllDishCount(dataList);
        initialTradeItemPlanActivity(tradeVo.getTradeItemPlanActivityList());

        unMarketActivityItems.clear();
        if (dataList == null) {
            return;
        }

        List<IShopcartItem> unMarketActivityDataList = new ArrayList<IShopcartItem>(dataList);
        List<MarketActivityVo> marketActivityVos = makeMarketActivityVos(tradeVo, unMarketActivityDataList);
        createMarketActivityItems(marketActivityVos, data);

        if (Utils.isNotEmpty(marketActivityVos) && Utils.isNotEmpty(unMarketActivityDataList)) {
                        DishDataItem item = new DishDataItem(ItemType.GRAY_SEPERATOR);
                        item.setNeedTopLine(false);
            data.add(item);
        }

        if (Utils.isNotEmpty(unMarketActivityDataList)) {
            ShopCartDataVo unSaveVo = new ShopCartDataVo(null);            ShopCartDataVo savedVo = new ShopCartDataVo(null);            if (unMarketActivityDataList != null && unMarketActivityDataList.size() > 0) {
                                for (int i = 0; i < unMarketActivityDataList.size(); i++) {
                    IShopcartItem shopCartItem = unMarketActivityDataList.get(i);

                                        if (shopCartItem instanceof ShopcartItem) {
                        unSaveVo.addData(shopCartItem);
                    } else {
                        savedVo.addData(shopCartItem);
                    }
                }
            }
            if (!unSaveVo.isEmpty()) {
                unMarketActivityItems.addAll(createItems(unSaveVo.getData(), data));
            }
            if (!savedVo.isEmpty()) {
                unMarketActivityItems.addAll(createItems(savedVo.getData(), data));
            }
            if (Utils.isNotEmpty(unMarketActivityItems)) {
                                unMarketActivityItems.get(0).setNeedTopLine(false);
            }
        }
                if (getMarketRuleVo() != null) {
            setMarketActivityItemsCheckStatus(getMarketRuleVo(), tradeVo.getTradeItemPlanActivityList());
            DinnerShopManager.getInstance().getShoppingCart().doDishActivityIsCheck(getUnMarketActivityItems(), getMarketRuleVo());
        }

        updateTrade(tradeVo, isShowInvalid);
    }


    public void updateGroupData(List<IShopcartItem> dataList, TradeVo tradeVo, boolean isShowInvalid) {
        this.initAllDishCount(BigDecimal.ZERO);        super.updateGroupData(dataList, tradeVo, isShowInvalid);
        updateTrade(tradeVo, isShowInvalid);
    }

    public class MarketActivityVo {
        TradePlanActivity tradePlanActivity;

        List<TradeItemPlanActivity> tradeItemPlanActivities;

        List<IShopcartItem> shopcartItems;

        public TradePlanActivity getTradePlanActivity() {
            return tradePlanActivity;
        }

        public void setTradePlanActivity(TradePlanActivity tradePlanActivity) {
            this.tradePlanActivity = tradePlanActivity;
        }

        public List<TradeItemPlanActivity> getTradeItemPlanActivities() {
            return tradeItemPlanActivities;
        }

        public void setTradeItemPlanActivities(List<TradeItemPlanActivity> tradeItemPlanActivities) {
            this.tradeItemPlanActivities = tradeItemPlanActivities;
        }

        public List<IShopcartItem> getShopcartItems() {
            return shopcartItems;
        }

        public void setShopcartItems(List<IShopcartItem> shopcartItems) {
            this.shopcartItems = shopcartItems;
        }

    }


    public void doEditModeItemClick(DishDataItem item, int position) {
        IShopcartItemBase shopcartItem = item.getBase();
        if (shopcartItem == null) {
            return;
        }


        if (isBatchDiscountModle) {
            if (DinnerCashManager.hasMarketActivity(tradeItemPlanActivityMap, item.getBase())) {
                ToastUtil.showShortToast(R.string.cannot_discount);
                return;
            }
        }
        if (!isBatchCoercionModel && shopcartItem.getEnableWholePrivilege() == Bool.NO) {
            ToastUtil.showShortToast(R.string.cannot_discount);
            return;
        }


                double currentPrice = shopcartItem.getActualAmount().doubleValue();
        TradePrivilege tradePrivilege = DinnerShopManager.getInstance().getShoppingCart().getShoppingCartVo().getDishTradePrivilege();
        if (tradePrivilege != null) {
            if (tradePrivilege.getPrivilegeType() == PrivilegeType.REBATE) {
                if ((currentPrice - tradePrivilege.getPrivilegeValue().doubleValue()) <= 0) {
                    ToastUtil.showShortToast(context.getResources().getString(R.string.privilegeError));
                    return;
                }
            }
        }

        DishDataItem selectedItem = data.get(position);
        if (selectedItem.getBase() != null) {

            if (selectedItem.getBase().isSelected()) {
                selectedItem.getBase().setSelected(false);
            } else {
                if (isBatchCoercionModel) {
                    checkCancelAll();
                }
                selectedItem.getBase().setSelected(true);
            }
            notifyDataSetChanged();
            EventSelectDish mEventSelectDish = new EventSelectDish();
            mEventSelectDish.setSelectIndex(position);
            EventBus.getDefault().post(mEventSelectDish);
        }

    }


    public void checkCancelAll() {
        for (DishDataItem dish : getAllData()) {
            if (dish.getType() == ItemType.SINGLE
                    || dish.getType() == ItemType.COMBO) {
                if (dish.getBase() != null) {
                    dish.getBase().setSelected(false);
                }
            }
        }

        List<IShopcartItemBase> listDishData = new ArrayList<>();
        for (DishDataItem dish : getAllData()) {
            if (dish.getType() == ItemType.SINGLE
                    || dish.getType() == ItemType.COMBO) {
                if (dish.getBase() != null && dish.getBase().isSelected()) {
                    IShopcartItemBase mShopcartItemBase = dish.getBase();
                    if (mShopcartItemBase != null) {
                        listDishData.add(mShopcartItemBase);
                    }
                }
            }
        }
        DinnerShopManager.getInstance().getShoppingCart().batchDishPrivilege(listDishData, false);
    }


    protected void initialTradeItemPlanActivity(List<TradeItemPlanActivity> tipaList) {
        tradeItemPlanActivityMap.clear();
        if (Utils.isNotEmpty(tipaList)) {
            for (TradeItemPlanActivity tipa : tipaList) {
                tradeItemPlanActivityMap.put(tipa.getTradeItemUuid(), tipa);
            }
        }
    }


    protected void createMarketActivityItems(List<MarketActivityVo> marketActivityVos, ArrayList<DishDataItem> data) {
        if (Utils.isNotEmpty(marketActivityVos)) {
            for (MarketActivityVo marketActivityVo : marketActivityVos) {
                DishDataItem dishDataItem = new DishDataItem(ItemType.MARKET_ACTIVITY);
                TradePlanActivity tradePlanActivity = marketActivityVo.getTradePlanActivity();
                dishDataItem.setTradePlanActivityUuid(tradePlanActivity.getUuid());
                dishDataItem.setName(tradePlanActivity.getRuleName());
                dishDataItem.setValue(tradePlanActivity.getOfferValue() == null ? 0 : tradePlanActivity.getOfferValue()
                        .doubleValue());
                                dishDataItem.setNeedTopLine(false);
                data.add(dishDataItem);

                ArrayList<DishDataItem> temp = new ArrayList<DishDataItem>();
                createItems(marketActivityVo.getShopcartItems(), temp);
                if (Utils.isNotEmpty(temp)) {
                                        temp.get(0).setNeedTopLine(false);
                    data.addAll(temp);
                }
            }
        }
    }

    public Map<String, TradeItemPlanActivity> getTradeItemPlanActivityMap() {
        return tradeItemPlanActivityMap;
    }

    @Override
    protected void updateTrade(TradeVo tradeVo, boolean isShowUnActive) {
        isHasBanquetOrFree = false;
                if (tradeVo != null && tradeVo.getTrade() != null) {
            buildTradeUser(tradeVo);
                        if (isShowAllDiscount) {
                isHasBanquetOrFree = buildDiscountData(tradeVo, isShowUnActive);
            }
            buildTradeMemo(tradeVo);
        }
    }

    private void buildChargePrivilege(TradeVo tradeVo){
        TradePrivilege chargePrivilege = tradeVo.getTradePrivilege(PrivilegeType.CHARGE_REBATE, PrivilegeType.CHARGE_DISCOUNT);
        if(chargePrivilege != null && chargePrivilege.isValid()){
            DishDataItem item = new DishDataItem(ItemType.CHARGE_PRIVILEGE);
            item.setValue(chargePrivilege.getPrivilegeAmount().doubleValue());
            item.setName(chargePrivilege.getPrivilegeName());

            data.add(item);
        }
    }

    protected boolean buildDiscountData(TradeVo tradeVo, boolean isShowUnActive) {
                boolean hasBanquetOrFree = false;
                buildServiceCharge(tradeVo);
                buildExtraCharge(tradeVo);
                buildOutTimeCharge(tradeVo);
                TradePrivilege tradePrivilege = tradeVo.getTradePrivilege(PrivilegeType.FREE, PrivilegeType.DISCOUNT, PrivilegeType.REBATE);
        if (tradePrivilege != null && (tradePrivilege.isValid())) {
                        DishDataItem item = new DishDataItem(ItemType.ALL_DISCOUNT);
            item.setValue(tradePrivilege.getPrivilegeAmount().doubleValue());
            if (tradePrivilege.getPrivilegeType() == PrivilegeType.FREE) {
                item.setName(context.getResources().getString(R.string.freethisOrder));
                hasBanquetOrFree = true;
                                TradeReasonRel reason = tradeVo.getOperateReason(OperateType.TRADE_DINNER_FREE);
                if (reason != null) {
                    item.setTradeReason(context.getResources().getString(R.string.order_free_reason) + "："
                            + reason.getReasonContent());
                }
            } else if (tradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT) {
                item.setName(tradePrivilege.getPrivilegeName());
                TradeReasonRel discountReason = tradeVo.getOperateReason(OperateType.TRADE_DISCOUNT);
                if (discountReason != null) {
                    item.setTradeReason(context.getResources().getString(R.string.order_discount_reason) + "："
                            + discountReason.getReasonContent());
                }
            } else if (tradePrivilege.getPrivilegeType() == PrivilegeType.REBATE) {
                item.setName(tradePrivilege.getPrivilegeName());
                TradeReasonRel discountReason = tradeVo.getOperateReason(OperateType.TRADE_REBATE);
                if (discountReason != null) {
                    item.setTradeReason(context.getResources().getString(R.string.order_concession_reason) + "："
                            + discountReason.getReasonContent());
                }
            }

            data.add(item);
        } else {
                    }

                buildChargePrivilege(tradeVo);


        buildCoupon(tradeVo, isShowUnActive);

                if (tradeVo.getIntegralCashPrivilegeVo() != null) {
            DishDataItem item = new DishDataItem(ItemType.INTERGRAL);
            item.setIntegralCashPrivilegeVo(tradeVo.getIntegralCashPrivilegeVo());
            TradePrivilege privilege = tradeVo.getIntegralCashPrivilegeVo().getTradePrivilege();
            if (privilege != null && privilege.getPrivilegeAmount() != null) {
                item.setValue(privilege.getPrivilegeAmount().doubleValue());
            }

                        if (tradeVo.getIntegralCashPrivilegeVo().isActived()) {
                item.setEnabled(true);
                int value =
                        privilege.getPrivilegeValue() == null ? 0 : privilege.getPrivilegeValue().intValue();                int amount =
                        privilege.getPrivilegeAmount() == null ? 0 : privilege.getPrivilegeAmount().intValue();                item.setName(ResourceUtils.getString(R.string.use_integralcash, value, (0 - amount)));
            } else {
                item.setEnabled(false);
                item.setName(context.getString(R.string.intergral_ineffective));
            }
                        if ((!tradeVo.getIntegralCashPrivilegeVo().isActived() && isShowUnActive
                    || tradeVo.getIntegralCashPrivilegeVo().isActived())
                    && tradeVo.getIntegralCashPrivilegeVo().isPrivilegeValid()
                    ) {
                data.add(item);
            }
        }

        if (Utils.isNotEmpty(tradeVo.getmWeiXinCouponsVo())) {
            for (WeiXinCouponsVo weiXinCouponsVo : tradeVo.getmWeiXinCouponsVo()) {
                if (weiXinCouponsVo != null && weiXinCouponsVo.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.WECHAT_CARD_COUPONS);
                    item.setWeiXinCouponsVo(weiXinCouponsVo);
                    TradePrivilege weixinPrivilege = weiXinCouponsVo.getmTradePrivilege();
                    if (weixinPrivilege != null) {
                        if (weixinPrivilege.getPrivilegeAmount() != null) {
                            item.setValue(weixinPrivilege.getPrivilegeAmount().doubleValue());
                        } else {
                            item.setValue(0);
                        }

                        if (weixinPrivilege.getPrivilegeName() != null) {
                            item.setName(weixinPrivilege.getPrivilegeName());
                        } else {
                            item.setName("");
                        }
                    }

                                        if (weiXinCouponsVo.isActived()) {
                        item.setEnabled(true);
                    }
                                        if (!weiXinCouponsVo.isActived() && isShowUnActive
                            || weiXinCouponsVo.isActived()) {
                        data.add(item);
                    }
                }
            }
        }

        buildTax(tradeVo);

                if (tradeVo.getBanquetVo() != null && tradeVo.getBanquetVo().getTradePrivilege() != null && tradeVo.getBanquetVo().getTradePrivilege().isValid()) {
            DishDataItem banquetItem = new DishDataItem(ItemType.BANQUET_PRIVILIGE);
            banquetItem.setName(tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeName());
            banquetItem.setValue(tradeVo.getBanquetVo().getTradePrivilege().getPrivilegeAmount().doubleValue());
                        TradeReasonRel reason = tradeVo.getOperateReason(OperateType.TRADE_BANQUET);
            if (reason != null) {
                banquetItem.setTradeReason(context.getResources().getString(R.string.reason_banquet_title) + "："
                        + reason.getReasonContent());
            }
            hasBanquetOrFree = true;
            data.add(banquetItem);
        }


        return hasBanquetOrFree;
    }

    protected void buildTradeUser(TradeVo tradeVo) {
    }


    private void sumAllDishCount(List<IShopcartItem> dataList) {
        if (dataList != null) {
            for (IShopcartItem shopCartItem : dataList) {
                sumAllDishCount(shopCartItem);
            }
        }
    }


    public void setMarketActivityItemsCheckStatus(MarketRuleVo marketRuleVo, List<TradeItemPlanActivity> tipaList) {
        Map<String, DishDataItem> dishDataItemFinder = new HashMap<String, DishDataItem>();
        for (DishDataItem item : data) {
                        if (item.getBase() != null && (item.getType() == ItemType.SINGLE || item.getType() == ItemType.COMBO)) {
                String uuid = item.getBase().getUuid();
                dishDataItemFinder.put(uuid, item);
            }
        }

                List<String> curMarketActivityItemUuids = new ArrayList<String>();
        List<String> otherMarketActivityItemUuids = new ArrayList<String>();
        if (Utils.isNotEmpty(tipaList)) {
            for (TradeItemPlanActivity tipa : tipaList) {
                if (tipa.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                if (Utils.equals(tipa.getRuleId(), marketRuleVo.getRuleId())) {
                    curMarketActivityItemUuids.add(tipa.getTradeItemUuid());
                } else {
                    otherMarketActivityItemUuids.add(tipa.getTradeItemUuid());
                }
            }
        }

                for (String uuid : curMarketActivityItemUuids) {
            DishDataItem item = dishDataItemFinder.get(uuid);
            if (item != null) {
                item.setCheckStatus(DishDataItem.DishCheckStatus.CHECKED);
            }
        }

                for (String uuid : otherMarketActivityItemUuids) {
            DishDataItem item = dishDataItemFinder.get(uuid);
            if (item != null) {
                item.setCheckStatus(DishDataItem.DishCheckStatus.INVALIATE_CHECK);
            }
        }
    }


    private List<MarketActivityVo> makeMarketActivityVos(TradeVo tradeVo, List<IShopcartItem> unMarketActivityDataList) {
        List<MarketActivityVo> marketActivityVos = new ArrayList<MarketActivityVo>();
        if (Utils.isNotEmpty(tradeVo.getTradePlanActivityList())
                && Utils.isNotEmpty(tradeVo.getTradeItemPlanActivityList())) {
                        Map<String, IShopcartItem> shopcartItemFinder = new HashMap<String, IShopcartItem>();
            for (IShopcartItem shopcartItem : unMarketActivityDataList) {
                shopcartItemFinder.put(shopcartItem.getUuid(), shopcartItem);
            }
                        Map<String, List<TradeItemPlanActivity>> tipaListFinder =
                    new HashMap<String, List<TradeItemPlanActivity>>();
            for (TradeItemPlanActivity tipa : tradeVo.getTradeItemPlanActivityList()) {
                if (tipa.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }

                String tradePlanUuid = tipa.getRelUuid();
                List<TradeItemPlanActivity> tipaList = tipaListFinder.get(tradePlanUuid);
                if (tipaList == null) {
                    tipaList = new ArrayList<TradeItemPlanActivity>();
                    tipaListFinder.put(tradePlanUuid, tipaList);
                }
                tipaList.add(tipa);
            }

            for (TradePlanActivity tradePlanActivity : tradeVo.getTradePlanActivityList()) {
                if (tradePlanActivity.getStatusFlag() == StatusFlag.INVALID
                        || tradePlanActivity.getRuleEffective() == ActivityRuleEffective.INVALID) {
                    continue;
                }

                String tradePlanUuid = tradePlanActivity.getUuid();

                                List<TradeItemPlanActivity> tipaList = tipaListFinder.get(tradePlanUuid);
                if (Utils.isNotEmpty(tipaList)) {
                    List<IShopcartItem> shopcartItems = new ArrayList<IShopcartItem>();
                    for (TradeItemPlanActivity tipa : tipaList) {
                                                String tradeItemUuid = tipa.getTradeItemUuid();
                        IShopcartItem shopcartItem = shopcartItemFinder.remove(tradeItemUuid);
                        if (shopcartItem != null) {
                            shopcartItems.add(shopcartItem);
                            unMarketActivityDataList.remove(shopcartItem);
                        }
                    }

                    if (Utils.isNotEmpty(shopcartItems)) {
                        MarketActivityVo marketActivityVo = new MarketActivityVo();
                        marketActivityVo.setTradePlanActivity(tradePlanActivity);
                        marketActivityVo.setShopcartItems(shopcartItems);
                        marketActivityVo.setTradeItemPlanActivities(tipaList);
                        marketActivityVos.add(marketActivityVo);
                    }
                }
            }
        }

        return marketActivityVos;
    }

    public void setCanRemoveMarketActivity(boolean canRemoveMarketActivity) {
        this.canRemoveMarketActivity = canRemoveMarketActivity;
    }

        public void updateOutTimeFeeItem(TradeVo tradeVo) {
    }

    ;

    protected void buildExtraCharge(TradeVo tradeVo) {
        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();
        if (tradeprivileges != null) {
            for (TradePrivilege tradePrivilege : tradeprivileges) {
                if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.isValid()) {
                    DishDataItem item = new DishDataItem(ItemType.ADDITIONAL);
                                                            ExtraCharge extraCharge = tradeVo.getMinconExtraCharge();
                    if (extraCharge != null && extraCharge.getStatusFlag() == StatusFlag.VALID
                            && tradePrivilege.getPromoId() != null && extraCharge.getId().compareTo(tradePrivilege.getPromoId()) == 0) {
                        item.setExtraType(ExtraItemType.MIN_CONSUM);
                    } else {
                        extraCharge = ExtraManager.getExtraChargeById(tradeVo, tradePrivilege.getPromoId());
                        if (extraCharge == null || extraCharge.getStatusFlag() == StatusFlag.INVALID) {
                            continue;
                        }
                    }
                    item.setName(extraCharge.getName());
                    double value = tradePrivilege == null ? 0 : tradePrivilege.getPrivilegeAmount().doubleValue();
                    item.setValue(value);
                    item.setExtraCharge(extraCharge);
                    data.add(item);
                }
            }
        }
    }


    public void updateMinconsum(TradeVo tradeVo) {
        ExtraCharge extraCharge = tradeVo.getMinconExtraCharge();
        if (extraCharge == null) {
            extraCharge = ExtraManager.getMinconsumExtra();
            tradeVo.setMinconExtraCharge(extraCharge);
        }

        List<TradePrivilege> tradeprivileges = tradeVo.getTradePrivileges();

        if (tradeprivileges == null) {
            tradeprivileges = new ArrayList<TradePrivilege>();
            tradeVo.setTradePrivileges(tradeprivileges);
        }

        TradePrivilege minPrivilege = null;
        for (TradePrivilege tradePrivilege : tradeprivileges) {
            if (tradePrivilege.getPrivilegeType() == PrivilegeType.ADDITIONAL && tradePrivilege.getPromoId() != null && extraCharge.getId().compareTo(tradePrivilege.getPromoId()) == 0) {
                minPrivilege = tradePrivilege;
            }
        }
        if (minPrivilege == null) {
            minPrivilege = BuildPrivilegeTool.buildBoxFee(tradeVo, minPrivilege, extraCharge);
            tradeprivileges.add(minPrivilege);
        }

        minPrivilege.setStatusFlag(StatusFlag.VALID);
                        tradeVo.setEnableMinConsum(true);
    }
}
