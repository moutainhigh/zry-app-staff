package com.zhongmei.beauty.order.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhongmei.beauty.order.util.IChangeMiddlePageListener;
import com.zhongmei.beauty.order.util.IOperateListener;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.cashier.orderdishmanager.entity.BatchDishPropertyVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishPropertiesVo;
import com.zhongmei.bty.snack.orderdish.buinessview.BeautyExtraView;
import com.zhongmei.bty.snack.orderdish.buinessview.BeautyExtraView_;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView_;
import com.zhongmei.yunfu.R;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.DishDataItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.bty.basemodule.orderdish.entity.DishBrandProperty;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.enums.ChildDishType;
import com.zhongmei.yunfu.db.enums.ClearStatus;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.shopmanager.interfaces.ChangePageListener;
import com.zhongmei.bty.basemodule.shoppingcart.DinnerShoppingCart;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.util.ViewUtil;
import com.zhongmei.beauty.order.view.BeautyRemarkView;
import com.zhongmei.beauty.order.view.BeautyRemarkView_;
import com.zhongmei.bty.cashier.orderdishmanager.DishPropertyManager;
import com.zhongmei.bty.cashier.shoppingcart.vo.DishStandardVo;
import com.zhongmei.bty.cashier.shoppingcart.vo.PropertyGroupVo;
import com.zhongmei.yunfu.Constant;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.dinner.manager.DinnerTradeItemManager;
import com.zhongmei.bty.entity.event.orderdishevent.EventDishPropertiesNotice;
import com.zhongmei.bty.snack.orderdish.InterfaceListener.OrderDishListenerImp;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomStandardView;
import com.zhongmei.bty.snack.orderdish.buinessview.CustomStandardView_;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class BeautyPropertyUtil {


    private CustomStandardView mCustomStandardView;

    private BeautyRemarkView mRemarkView;

        private String mUuid;
        private String mParentUuid;

    private boolean isFirst = true;
    private List<PropertyGroupVo<DishStandardVo>> standardGroupList;
    private ArrayList<OrderProperty> properties;
    private DishPropertyManager mDishPropertyManager;

    private Button btnStandard;
    private Button btnRemark;
    private Button btnTradeItemRemark;


    private BeautyExtraView mExtraView;


    private List<ExtraInfo> extraInfoList = new ArrayList<>();


    private View mCurrentContentView;

    protected BigDecimal mSelectedQty;

    private TradePrivilege mTradePrivilege;
    private TradeReasonRel mDiscountReason;

        protected DishSetmealManager mDishSetmealManager;

    private int mTargetPageNo = -1;
    Context mContext;
    private IOperateListener iOperateListener;
    private IChangeMiddlePageListener mChangeListener;

    private AsyncTask mAsyncTask;

    protected DinnerShoppingCart mShoppingCart;
    protected DishDataItem mDishDataItem;
    protected ShopcartItem mShopcartItem;
    protected SetmealShopcartItem mSetmealShopcartItem;
    protected ShopcartItemBase<?> mRealItemBase;
    private IShopcartItemBase mShopcartItemBase;
    private String mMemo;
        protected boolean added = true;

    private boolean isOrderRemark = true;
    private LinearLayout vActionBar;
    private ChangePageListener mChangePageLisener;

    public BeautyPropertyUtil(Context context, ChangePageListener changePageListener, IOperateListener iOperateListener, IChangeMiddlePageListener listener) {
        mContext = context;
        this.iOperateListener = iOperateListener;
        mChangeListener = listener;
        mChangePageLisener = changePageListener;
        mShoppingCart = DinnerShoppingCart.getInstance();
    }

    public void initView(LinearLayout vAnchor, Button btnStandard, Button btnRemark,Button btnTradeItemRemark) {
        this.btnStandard = btnStandard;
        this.btnRemark = btnRemark;
        this.btnTradeItemRemark=btnTradeItemRemark;
        vActionBar = vAnchor;
    }

    public void initData(DishDataItem dishDataItem) {
        this.mDishDataItem = dishDataItem;
        if (mDishDataItem == null) {
            initValue();
            return;
        }
        initData();
    }

    protected void initData() {

        initSelectData();
        showDefaultView();
    }

    protected void initSavedData() {
        initValue();
        mShopcartItemBase = mDishDataItem.getBase();
        mUuid = mDishDataItem.getBase().getUuid();
        mParentUuid = mDishDataItem.getBase().getParentUuid();

        ViewUtil.setButtonEnabled(btnStandard, false);
        ViewUtil.setButtonEnabled(btnRemark, true);
                iOperateListener.showCustomContentView(null);
    }


    public void doActiveModifyBtn() {
        btnStandard.setAlpha(1);
        btnStandard.setText(R.string.dish_combo_modify);
        ViewUtil.setButtonEnabled(btnStandard, true);
        ViewUtil.setButtonSelected(vActionBar, btnStandard);
    }


    public boolean isSavedValidSingleOrComboItem() {
        if (mDishDataItem.getBase() == null) {
            return false;
        }

        return mDishDataItem.getBase().getStatusFlag() == StatusFlag.VALID
                && DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)
                && !DinnerTradeItemManager.getInstance().isDishReturnAll(mDishDataItem)                && !DinnerTradeItemManager.getInstance().isUnsavedReadonly(mDishDataItem.getBase())                && !(mDishDataItem.getBase() instanceof ISetmealShopcartItem);    }

    protected void initSelectData() {
        initCommonData(mDishDataItem);
                mDishPropertyManager = new DishPropertyManager();
                if (mRealItemBase != null && !DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            loadProperties();
            mDishSetmealManager = mShopcartItem.getSetmealManager();
        }else{
                        initSavedData();
            showRemarkView();
        }
    }

    private void modifyCombo() {
        if (mChangePageLisener != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Constant.EXTRA_SHOPCART_ITEM_UUID, mUuid);
            bundle.putInt(Constant.EXTRA_LAST_PAGE, ChangePageListener.ORDERDISHLIST);
            bundle.putBoolean(Constant.NONEEDCHECK, false);
            mChangePageLisener.changePage(ChangePageListener.DISHCOMBO, bundle);
        }
        if (mChangeListener != null) {
            mChangeListener.showCombo(mShopcartItem);
        }
        doActiveModifyBtn();
    }


    public void initCommonData(DishDataItem dishDataItem) {
        mDishDataItem = dishDataItem;
        initValue();
        if (mDishDataItem == null) {
            return;
        }

        mUuid = mDishDataItem.getBase().getUuid();
        mParentUuid = mDishDataItem.getBase().getParentUuid();

        if (!TextUtils.isEmpty(mParentUuid)) {

            mShopcartItem = mShoppingCart.getShopcartItemByUUID(mShoppingCart.getShoppingCartVo(), mParentUuid);
            int size =( mShopcartItem==null || mShopcartItem.getSetmealItems() == null) ? 0 : mShopcartItem.getSetmealItems().size();
            for (int i = 0; i < size; i++) {
                if (mShopcartItem.getSetmealItems().get(i).getUuid().equals(mUuid)) {
                    mSetmealShopcartItem = mShopcartItem.getSetmealItems().get(i);
                    mRealItemBase = mSetmealShopcartItem;
                    break;
                }
            }
        } else {
            mShopcartItem = mShoppingCart.getShopcartItem(mShoppingCart.getShoppingCartVo(), mUuid);
            mRealItemBase = mShopcartItem;
        }
        if (mRealItemBase != null) {
            mSelectedQty = mRealItemBase.getOrderDish().getSingleQty();
            mTradePrivilege = mRealItemBase.getPrivilege();
            mDiscountReason = mRealItemBase.getDiscountReasonRel();
            mMemo = mRealItemBase.getMemo();
        }

        mShopcartItemBase = mRealItemBase;
    }


    private void initValue() {
        mShopcartItemBase = null;
        added = true;
        isFirst = true;
        mUuid = null;
        mShopcartItem = null;
        mSetmealShopcartItem = null;
        mRealItemBase = null;
        mDishPropertyManager = null;
        standardGroupList = new ArrayList<>();
        properties = new ArrayList<>();
        extraInfoList.clear();
    }

    private void loadProperties() {
        mAsyncTask = new AsyncTask<Void, Void, EventDishPropertiesNotice>() {

            @Override
            protected EventDishPropertiesNotice doInBackground(Void... params) {
                return mDishPropertyManager.loadData(mRealItemBase, mShoppingCart.getIsSalesReturn(), true);
            }

            @Override
            protected void onPostExecute(EventDishPropertiesNotice eventDishPropertiesNotice) {
                setExtra(eventDishPropertiesNotice.dishPropertiesVo);
            }
        }.execute();
    }

    public void stopAsyncTask() {
        if (mAsyncTask != null && !mAsyncTask.isCancelled()) {
            mAsyncTask.cancel(true);
        }
    }


    private void refreshView(EventDishPropertiesNotice eventDishPropertiesNotice) {
        if (eventDishPropertiesNotice != null && eventDishPropertiesNotice.dishPropertiesVo != null && eventDishPropertiesNotice.uuid.equals(mUuid)) {
                        if (!setStandard(eventDishPropertiesNotice)) {
                return;
            }

            OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
            if (orderDish != null
                    && (orderDish.getDishShop().getClearStatus() == ClearStatus.SALE || mShoppingCart.getIsSalesReturn())) {
                                ArrayList<OrderProperty> newProperties = new ArrayList<OrderProperty>();
                for (OrderProperty orderProperty : properties) {
                    if (orderProperty.getPropertyKind() != PropertyKind.STANDARD) {
                        newProperties.add(orderProperty);
                    }
                }
                Set<DishProperty> set = orderDish.getStandards();
                if (set != null) {
                    for (DishProperty dishProperty : set) {
                        DishPropertyType dishPropertyType =
                                DishCache.getPropertyTypeHolder().get(dishProperty.getPropertyTypeId());
                        newProperties.add(new OrderProperty(dishPropertyType, dishProperty));
                    }
                }
                properties.clear();
                properties.addAll(newProperties);
            } else {
                properties.clear();
            }
                        addOrderDishToCart(eventDishPropertiesNotice);
        }
    }

        private void setExtra(DishPropertiesVo batchDishPropertyVo) {
        extraInfoList.clear();

        if (batchDishPropertyVo.getExtraList() != null) {
            for (OrderExtra orderExtra : batchDishPropertyVo.getExtraList()) {
                ExtraInfo extraInfo = new ExtraInfo(orderExtra, false);
                extraInfo.setQty(BigDecimal.ZERO);
                extraInfoList.add(extraInfo);
            }
        }

        if (Utils.isEmpty(extraInfoList)) {
            showRemarkView();
            return;
        }

        if (mCurrentContentView != null && mCurrentContentView instanceof BeautyExtraView) {
            mExtraView.setListForBatchOperation(extraInfoList);
        }

    }

        protected boolean setStandard(EventDishPropertiesNotice eventDishPropertiesNotice) {
        standardGroupList.clear();
        if (eventDishPropertiesNotice.dishPropertiesVo.getStandardGroupList() != null) {
            standardGroupList.addAll(eventDishPropertiesNotice.dishPropertiesVo.getStandardGroupList());
        }

                if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            btnStandard.setText(R.string.dish_combo_modify);
            btnStandard.setAlpha(1);
            ViewUtil.setButtonEnabled(btnStandard, true);
        } else {
            btnStandard.setText(R.string.dish_standard);
            boolean isHasStandard = Utils.isNotEmpty(standardGroupList);
            ViewUtil.setButtonEnabled(btnStandard, isHasStandard);
            if (!isHasStandard) {
                btnStandard.setAlpha(0.4f);
            }
            if (mCurrentContentView != null && mCurrentContentView instanceof CustomStandardView) {
                ((CustomStandardView) mCurrentContentView).setData(standardGroupList, mDishPropertyManager, mShoppingCart);
            }
        }
        ViewUtil.setViewVisibility(btnStandard, View.VISIBLE);
        if (DinnerTradeItemManager.getInstance().isSaved(mDishDataItem)) {
            btnStandard.setAlpha(0.4f);
            ViewUtil.setViewVisibility(btnStandard, View.GONE);
        }

        return true;
    }


    private void addOrderDishToCart(EventDishPropertiesNotice eventDishPropertiesNotice) {
        OrderDish orderDish = eventDishPropertiesNotice.dishPropertiesVo.getOrderDish();
        if (orderDish != null
                && (orderDish.getDishShop().getClearStatus() == ClearStatus.SALE || mShoppingCart.getIsSalesReturn())) {
            if (isFirst) {
                isFirst = false;
                if (mTargetPageNo == -1) {
                    if (!DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
                        if (btnStandard.isEnabled()) {
                            showStandardView();
                        } else if (btnRemark.isEnabled()) {
                            showRemarkView();
                        } else {
                            iOperateListener.showCustomContentView(null);
                        }
                    } else {
                        iOperateListener.showCustomContentView(null);
                    }
                }
            } else {
                                BigDecimal singleQty = mShopcartItem.getSingleQty();
                                Long relateId = null;
                String relateUuid = null;
                if (mShopcartItem != null) {
                    relateId = mShopcartItem.getRelateTradeItemId();
                    relateUuid = mShopcartItem.getRelateTradeItemUuid();
                }

                                List<ExtraShopcartItem> extraItems = new ArrayList<>();
                DishShop dishShop = orderDish.getDishShop();                if (dishShop == null) {
                    return;
                }
                                DishPropertyManager.DishExtraFilter dishExtraFilter = new DishPropertyManager.DishExtraFilter(dishShop);
                List<DishSetmeal> dishExtraList = DishCache.getDishExtraHolder().filter(dishExtraFilter);
                                if (Utils.isEmpty(dishExtraList)) {
                    extraItems.addAll(mShopcartItem.getExtraItems());
                } else {
                                        Map<Long, DishSetmeal> setmealMap = new HashMap<>();
                    for (DishSetmeal setmeal : dishExtraList) {
                        if (setmeal.getChildDishType() == ChildDishType.EXTRA) {
                            setmealMap.put(setmeal.getChildDishId(), setmeal);
                        }
                    }

                    for (ExtraShopcartItem extraItem : mShopcartItem.getExtraItems()) {
                        if (extraItem.getOrderDish() == null || extraItem.getOrderDish().getSetmeal() == null) {
                            continue;
                        }

                                                Long extraId = extraItem.getOrderDish().getSetmeal().getChildDishId();                        DishSetmeal setmeal = setmealMap.get(extraId);
                        if (setmeal != null) {
                            extraItem.getOrderDish().setSetmeal(setmeal);
                            extraItems.add(extraItem);
                        }
                    }
                }

                                                if (Utils.isNotEmpty(properties)) {
                    for (int i = properties.size() - 1; i >= 0; i--) {
                        OrderProperty property = properties.get(0);
                        if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                            properties.remove(i);
                        }
                    }
                }
                                DishPropertyFilter dishPropertyFilter = new DishPropertyFilter(dishShop);
                List<DishBrandProperty> dishBrandPropertyList = DishCache.getDishPropertyHolder().filter(dishPropertyFilter);
                                boolean hasDishProperty = false;                for (DishBrandProperty brandProperty : dishBrandPropertyList) {
                    if (brandProperty.getPropertyKind() == PropertyKind.PROPERTY) {
                        hasDishProperty = true;
                    }
                }
                if (!hasDishProperty) {
                    for (OrderProperty property : mShopcartItem.getProperties()) {
                        if (property.getPropertyKind() == PropertyKind.PROPERTY) {
                            properties.add(property);
                        }
                    }
                } else {
                                        Map<Long, DishBrandProperty> brandPropertyMap = new HashMap<>();
                    for (DishBrandProperty brandProperty : dishBrandPropertyList) {
                        if (brandProperty.getPropertyKind() == PropertyKind.PROPERTY) {
                            brandPropertyMap.put(brandProperty.getPropertyId(), brandProperty);
                        }
                    }

                    for (OrderProperty property : mShopcartItem.getProperties()) {
                        if (property.getProperty() == null || property.getPropertyKind() != PropertyKind.PROPERTY) {
                            continue;
                        }

                                                Long propertyId = property.getProperty().getId();                        DishBrandProperty brandProperty = brandPropertyMap.get(propertyId);
                        if (brandProperty != null) {
                            properties.add(property);
                        }
                    }
                }

                                String memo = mShopcartItem.getMemo();
                boolean isGroupDish = mShopcartItem.isGroupDish();
                ShopcartItemType shopcartItemType = mShopcartItem.getShopcartItemType();
                                mShopcartItem = new ShopcartItem(mShopcartItem.getUuid(), orderDish);
                BigDecimal increaseUnit = MathDecimal.trimZero(orderDish.getDishShop().getDishIncreaseUnit());
                if (shopcartItemType == ShopcartItemType.MAINBATCH && increaseUnit != null) {
                    increaseUnit = increaseUnit.multiply(mShoppingCart.getOrder().getSubTradeCount());
                }
                                mShopcartItem.changeQty(increaseUnit);
                mShopcartItem.setRelateInfo(relateId, relateUuid);
                mShopcartItem.setExtraItems(extraItems);
                mShopcartItem.setProperties(properties);
                mShopcartItem.setMemo(memo);
                mShopcartItem.setPrivilege(mTradePrivilege);
                mShopcartItem.setDiscountReasonRel(mDiscountReason);
                mShopcartItem.setMemo(mMemo);
                mShopcartItem.setIsGroupDish(isGroupDish);
                mShopcartItem.setShopcartItemType(shopcartItemType);
                mRealItemBase = mShopcartItem;
                mShoppingCart.addDishToShoppingCart(mShopcartItem, false, false);
                if (mChangeListener != null) {
                    mChangeListener.closePage(null);
                }
                added = true;
            }
        } else {
            isFirst = false;
            added = false;
        }
    }

    public void showDefaultView() {
        if (mDishDataItem == null) {
            return;
        }
        if (isSavedValidSingleOrComboItem()) {
            btnStandard.setAlpha(0.4f);
            showRemarkView();
        } else if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            modifyCombo();
        } else {
            btnStandard.setAlpha(1);
            showExtra();
        }
    }



    public void showStandardView() {
        ViewUtil.setButtonSelected(vActionBar, btnStandard);
        if (mCustomStandardView == null) {
            mCustomStandardView = CustomStandardView_.build(mContext);
        }
        mCurrentContentView = mCustomStandardView;

        mCustomStandardView.setData(standardGroupList, mDishPropertyManager, mShoppingCart);
        mCustomStandardView.setListener(new CustomStandardView.PropertListener() {
            @Override
            public void propertyData(EventDishPropertiesNotice eventDishPropertiesNotice) {
                refreshView(eventDishPropertiesNotice);
            }
        });
        iOperateListener.showCustomContentView(mCustomStandardView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mUuid);
    }


    public void showRemarkView() {
        ViewUtil.setButtonSelected(vActionBar, mShopcartItemBase==null?btnRemark:btnTradeItemRemark);
        mRemarkView = BeautyRemarkView_.build(mContext, mShopcartItemBase, mShoppingCart.getShoppingCartVo().getmTradeVo().getTrade().getTradeMemo(), true);
        mRemarkView.setListener(new OrderDishListenerImp() {
            @Override
            public void onMemoChanged(String memo, boolean isOrderRemark) {
                if (isOrderRemark) {
                    mShoppingCart.setDinnerRemarks(memo);
                } else {
                    mMemo = memo;
                    if (added) {
                        mShopcartItemBase.setMemo(memo);
                        mShopcartItemBase.setChanged(true);
                        mShoppingCart.updateDinnerDish(mShopcartItemBase, false);
                    }
                }
            }
        });
        iOperateListener.showCustomContentView(mRemarkView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mUuid);
        Log.e("BeautyPropertyUtils", "showRemark.....>");
    }


    public void showExtra() {
        ViewUtil.setButtonEnabled(btnStandard,true);
        ViewUtil.setButtonSelected(vActionBar, btnStandard);

        if (mExtraView == null) {
            mExtraView = BeautyExtraView_.build(mContext);
            mExtraView.setListener(new OrderDishListenerImp() {

                @Override
                public void onAddMaterial(ExtraInfo extraInfo, BigDecimal count) {
                    if (mShopcartItem == null) {
                        return;
                    }

                    try {
                        OrderExtra orderExtra = extraInfo.orderExtra.clone();
                        BigDecimal qty = mShopcartItem.getExtraQty(orderExtra);
                        mShopcartItem.setExtra(orderExtra, count);
                        mShoppingCart.updateDinnerDish(mShopcartItem, false);
                    } catch (Exception e) {
                        Log.e("BeautyPropertyUtils", e.getMessage(), e);
                    }
                }
            });
        }
        mCurrentContentView = mExtraView;
        mExtraView.setListForBatchOperation(extraInfoList);
        iOperateListener.showCustomContentView(mExtraView);
        mChangeListener.changePage(IChangeMiddlePageListener.DEFAULT_PAGE, mUuid);
    }



    public void showProperty() {
        if (DinnerTradeItemManager.getInstance().isCombo(mRealItemBase)) {
            modifyCombo();
        } else {
            showStandardView();
        }
    }


    private static class DishPropertyFilter implements DishCache.DataFilter<DishBrandProperty> {

        private final DishShop dishShop;

        DishPropertyFilter(DishShop dishShop) {
            this.dishShop = dishShop;
        }

        @Override
        public boolean accept(DishBrandProperty entity) {
            return dishShop.getBrandDishId().equals(entity.getDishId());
        }

    }
}
