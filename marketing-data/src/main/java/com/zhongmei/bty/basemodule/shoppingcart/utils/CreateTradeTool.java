package com.zhongmei.bty.basemodule.shoppingcart.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.yunfu.ShopInfoManager;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.discount.utils.BuildPrivilegeTool;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ISetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.TakeOutInfo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.DomainType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.SourceChild;
import com.zhongmei.yunfu.db.enums.SourceId;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.enums.TradePayForm;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateTradeTool {

    private static final String TAG = CreateTradeTool.class.getSimpleName();

    public static void buildMainTradeVo(TradeVo mTradeVo) {

        Trade mTrade = createTrade();



        if (Session.getAuthUser() != null) {
            mTrade.setCreatorId(Session.getAuthUser().getId());
            mTrade.setCreatorName(Session.getAuthUser().getName());
        } else {
            return;
        }



        mTrade.validateCreate();
        mTradeVo.setTrade(mTrade);
        mTradeVo.getTrade().setTradeNo(SystemUtils.getBillNumber());
        mTradeVo.setTradeExtra(new TradeExtra());
    }

    public static Trade createTrade() {
        Trade trade = new Trade();
        trade.setUuid(SystemUtils.genOnlyIdentifier());

                trade.setDomainType(DomainType.RESTAURANT);

        trade.setBusinessType(BusinessType.SNACK);
        trade.setTradePayForm(TradePayForm.OFFLINE);
        trade.setStatusFlag(StatusFlag.VALID);

        if (Session.getAuthUser() != null) {
            trade.setCreatorId(Session.getAuthUser().getId());
            trade.setCreatorName(Session.getAuthUser().getName());
        }


                trade.setChanged(false);
                trade.setBrandIdenty(ShopInfoManager.getInstance().getShopInfo().getBrandId());
                trade.setShopIdenty(ShopInfoManager.getInstance().getShopInfo().getShopId());
                trade.setDeviceIdenty(SystemUtils.getMacAddress());

                        trade.setSource(SourceId.POS);
                trade.setSourceChild(SourceChild.ANDROID);

                        trade.setTradePayStatus(TradePayStatus.UNPAID);


                trade.setPrivilegeAmount(new BigDecimal(0));


                trade.setSaleAmount(new BigDecimal(0));


                trade.setTradeAmountBefore(new BigDecimal(0));
        trade.setTradeAmount(new BigDecimal(0));

                                trade.setTradeStatus(TradeStatus.CONFIRMED);

                trade.setDishKindCount(0);

                        trade.setTradeTime((new Date()).getTime());


        trade.setTradePeopleCount(1);

                                trade.setTradeType(TradeType.SELL);
        return trade;
    }


    public static TradeItemVo dishToTradeItem(ShopcartItemBase mShopcartItemBase, TradeVo mTradeVo, BigDecimal deskCount) {

        TradeItemVo mTradeItemVo = new TradeItemVo();

                mTradeItemVo.setTradeItem(buildTradeItem(mShopcartItemBase, mTradeVo, deskCount));
        mTradeItemVo.setTradeItemExtraDinner(mShopcartItemBase.getTradeItemExtraDinner());
        List<TradeItemProperty> tradeItemPropertyList = new ArrayList<TradeItemProperty>();

                List<IOrderProperty> listOrderProperty = mShopcartItemBase.getProperties();
        if (listOrderProperty != null) {
            for (IOrderProperty property : listOrderProperty) {
                TradeItemProperty mTradeItemProperty = buildTradeItemProperty(mShopcartItemBase, property, deskCount);
                tradeItemPropertyList.add(mTradeItemProperty);
            }
        }


        mTradeItemVo.setTradeItemPropertyList(tradeItemPropertyList);
        TradePrivilege mTradePrivilege =
                BuildPrivilegeTool.buildPrivilege(mShopcartItemBase, mTradeVo.getTrade().getUuid());
        mTradeItemVo.setTradeItemPrivilege(mTradePrivilege);
                if (mShopcartItemBase.getDiscountReasonRel() != null) {
            mTradeItemVo.setDiscountReason(mShopcartItemBase.getDiscountReasonRel());
        }

        CouponPrivilegeVo couponPrivilegeVo = mShopcartItemBase.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null)
            couponPrivilegeVo.setShopcartItem(null);
        mTradeItemVo.setCouponPrivilegeVo(couponPrivilegeVo);
                if (couponPrivilegeVo != null && couponPrivilegeVo.getTradePrivilege() != null) {
            couponPrivilegeVo.getTradePrivilege().setTradeUuid(mTradeVo.getTrade().getUuid());
            couponPrivilegeVo.getTradePrivilege().setTradeId(mTradeVo.getTrade().getId());
        }
        if (mShopcartItemBase instanceof IShopcartItem) {
                        IShopcartItem iShopcartItem = (IShopcartItem) mShopcartItemBase;
            mTradeItemVo.setTradeItemOperations(iShopcartItem.getTradeItemOperations());
        } else if (mShopcartItemBase instanceof ISetmealShopcartItem) {
            ISetmealShopcartItem iShopcartItem = (ISetmealShopcartItem) mShopcartItemBase;
            mTradeItemVo.setTradeItemOperations(iShopcartItem.getTradeItemOperations());
        }
        mTradeItemVo.setShopcartItemType(mShopcartItemBase.getShopcartItemType());
        if (mShopcartItemBase.getTradeItemMainBatchRelList() != null) {
            mTradeItemVo.setTradeItemMainBatchRelList(mShopcartItemBase.getTradeItemMainBatchRelList());
        }
        mTradeItemVo.setTradeItemUserList(mShopcartItemBase.getTradeItemUserList());
        mTradeItemVo.setCardServicePrivilegeVo(mShopcartItemBase.getCardServicePrivilgeVo());
        mTradeItemVo.setAppletPrivilegeVo(mShopcartItemBase.getAppletPrivilegeVo());
        mTradeItemVo.setShopcartItemType(mShopcartItemBase.getShopcartItemType());
        return mTradeItemVo;
    }


    public static TradeItem buildTradeItem(ShopcartItemBase mShopcartItem, TradeVo mTradeVo, BigDecimal deskCount) {
        TradeItem mTradeItem = new TradeItem();
        mTradeItem.validateCreate();
        if (mShopcartItem.creatorId != null) {            mTradeItem.setCreatorId(mShopcartItem.creatorId);
            mTradeItem.setCreatorName(mShopcartItem.creatorName);
        }
        mTradeItem.validateUpdate();
                mTradeItem.setUuid(mShopcartItem.getUuid());
                mTradeItem.setQuantity(mShopcartItem.getTotalQty());
                mTradeItem.setPrice(mShopcartItem.getPrice());
                mTradeItem.setAmount(mShopcartItem.getAmount());
                mTradeItem.setPropertyAmount(mShopcartItem.getPropertyAmount());
        mTradeItem.setFeedsAmount(mShopcartItem.getFeedsAmount());
                mTradeItem.setActualAmount(mShopcartItem.getActualAmount());
                                mTradeItem.setParentUuid(mShopcartItem.getParentUuid());
                mTradeItem.setDishName(mShopcartItem.getSkuName());
                mTradeItem.setSkuUuid(mShopcartItem.getSkuUuid());
        mTradeItem.setDishId(mShopcartItem.getSkuId());
                if (mShopcartItem instanceof SetmealShopcartItem) {
            SetmealShopcartItem setmealItem = (SetmealShopcartItem) mShopcartItem;
            mTradeItem.setDishSetmealGroupId(setmealItem.getSetmealGroupId());
        }
                                                mTradeItem.setTradeMemo(mShopcartItem.getMemo());
        mTradeItem.setType(mShopcartItem.getType());

                mTradeItem.setUnitName(mShopcartItem.getUnitName());
                mTradeItem.setSaleType(mShopcartItem.getSaleType());
        mTradeItem.setTradeUuid(mTradeVo.getTrade().getUuid());
        mTradeItem.setTradeId(mTradeVo.getTrade().getId());
                mTradeItem.setIssueStatus(mShopcartItem.getIssueStatus());
        boolean isGroupBusiness = mTradeVo.getTrade().getBusinessType() == BusinessType.GROUP;
                if (Utils.isNotEmpty(mTradeVo.getTradeTableList()) && !isGroupBusiness) {
            mTradeItem.setTradeTableUuid(mTradeVo.getTradeTableList().get(0).getUuid());
            mTradeItem.setTradeTableId(mTradeVo.getTradeTableList().get(0).getId());
        }
                mTradeItem.setEnableWholePrivilege(mShopcartItem.getEnableWholePrivilege());
                mTradeItem.setIsChangePrice(mShopcartItem.getIsChangePrice());

        mTradeItem.setRelateTradeItemId(mShopcartItem.getRelateTradeItemId());
        mTradeItem.setRelateTradeItemUuid(mShopcartItem.getRelateTradeItemUuid());
        mTradeItem.setInvalidType(mShopcartItem.getInvalidType());
        mTradeItem.setStatusFlag(mShopcartItem.getStatusFlag());

        mTradeItem.setBatchNo(getServerExtraInfo(mShopcartItem.getDishShop()));
        doRelateShell(mShopcartItem, mTradeVo, mTradeItem, deskCount);
        return mTradeItem;
    }

    private static String getServerExtraInfo(DishShop dishShop){
        JSONObject obj=new JSONObject();
        try {
            obj.put("timeValue",dishShop.getMinNum());
            obj.put("timeType",dishShop.getMaxNum());
            obj.put("serversCount",dishShop.getSaleTotal());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }


    private static void doRelateShell(ShopcartItemBase mShopcartItem, TradeVo mTradeVo, TradeItem tradeItem, BigDecimal deskCount) {
        if (!mShopcartItem.isGroupDish() || mTradeVo.getMealShellVo() == null) {
            return;
        }
        if (TextUtils.isEmpty(tradeItem.getParentUuid())) {
            tradeItem.setParentUuid(mTradeVo.getMealShellVo().getUuid());
            tradeItem.setParentId(mTradeVo.getMealShellVo().getId());
        }
        tradeItem.setActualAmount(BigDecimal.ZERO);
        tradeItem.setAmount(BigDecimal.ZERO);
        tradeItem.setPrice(BigDecimal.ZERO);
        tradeItem.setPropertyAmount(BigDecimal.ZERO);
        BigDecimal quntity = mShopcartItem.getTotalQty().multiply(deskCount);
        tradeItem.setQuantity(quntity);
    }


    public static TradeItemProperty buildTradeItemProperty(ShopcartItemBase mShopcartItemBase,
                                                           DishProperty mDishPropertyVo) {


        TradeItemProperty mTradeItemProperty = new TradeItemProperty();
                BigDecimal price = mDishPropertyVo.getReprice();
        BigDecimal qty = mShopcartItemBase.getTotalQty();
        BigDecimal amount = qty.multiply(price);
        mTradeItemProperty.setAmount(amount);
        mTradeItemProperty.setCreatorId(Session.getAuthUser().getId());
        mTradeItemProperty.setCreatorName(Session.getAuthUser().getName());
                mTradeItemProperty.setPrice(price);
        mTradeItemProperty.setPropertyName(mDishPropertyVo.getName());
                mTradeItemProperty.setPropertyType(mDishPropertyVo.getPropertyKind());
        mTradeItemProperty.setPropertyUuid(mDishPropertyVo.getUuid());
        mTradeItemProperty.setQuantity(qty);
                mTradeItemProperty.setTradeItemUuid(mShopcartItemBase.getUuid());

                        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.setStatusFlag(StatusFlag.VALID);
        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.validateCreate();

        return mTradeItemProperty;
    }


    public static TradeItemProperty buildTradeItemProperty(ShopcartItemBase mShopcartItemBase,
                                                           IOrderProperty mOrderProperty, BigDecimal deskCount) {

        TradeItemProperty mTradeItemProperty = new TradeItemProperty();
                BigDecimal price = mOrderProperty.getPropertyPrice();
        BigDecimal qty = mShopcartItemBase.getTotalQty();
        BigDecimal amount = qty.multiply(price);
        mTradeItemProperty.setAmount(amount);

        AuthUser authUser = Session.getAuthUser();
        if (authUser != null) {
            mTradeItemProperty.setCreatorId(authUser.getId());
            mTradeItemProperty.setCreatorName(authUser.getName());
        }

                mTradeItemProperty.setPrice(price);
        mTradeItemProperty.setPropertyName(mOrderProperty.getPropertyName());
                mTradeItemProperty.setPropertyType(mOrderProperty.getPropertyKind());
        mTradeItemProperty.setPropertyUuid(mOrderProperty.getPropertyUuid());
        mTradeItemProperty.setQuantity(qty);
                mTradeItemProperty.setTradeItemUuid(mShopcartItemBase.getUuid());

                        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.setStatusFlag(StatusFlag.VALID);
        mTradeItemProperty.setUuid(SystemUtils.genOnlyIdentifier());
        mTradeItemProperty.validateCreate();
        doChangeProperty(mShopcartItemBase, mTradeItemProperty, deskCount);
        return mTradeItemProperty;
    }



    private static void doChangeProperty(ShopcartItemBase mShopcartItem, TradeItemProperty mTradeItemProperty, BigDecimal deskCount) {
        if (!mShopcartItem.isGroupDish()) {
            return;
        }
        mTradeItemProperty.setAmount(BigDecimal.ZERO);
        mTradeItemProperty.setPrice(BigDecimal.ZERO);
        BigDecimal quntity = mShopcartItem.getSingleQty().multiply(deskCount);
        mTradeItemProperty.setQuantity(quntity);
    }


    public static void createTradeVo(List<ShopcartItem> listShopcart, List<IShopcartItem> readonlyItem,
                                     TradeVo mTradeVo, TakeOutInfo mTakeOutInfo) {

        if (mTradeVo.getTradeItemList() == null) {
            List<TradeItemVo> listTradeItem = new ArrayList<TradeItemVo>();
            mTradeVo.setTradeItemList(listTradeItem);
        } else {
            List<TradeItemVo> listItem = mTradeVo.getTradeItemList();
            for (int i = listItem.size() - 1; i >= 0; i--) {
                TradeItemVo itemVo = listItem.get(i);
                                if (itemVo.getTradeItem().getId() == null) {
                    listItem.remove(i);
                }
            }

        }

        List<TradeItemVo> listTradeItem = mTradeVo.getTradeItemList();

        mTradeVo.setTradeItemExtraList(new ArrayList<TradeItemExtra>());
        BigDecimal deskCount = mTradeVo.getDeskCount();

        addReadonlyTradeItemVo(listTradeItem, readonlyItem, mTradeVo.getOldDeskCount(), deskCount);

        for (TradeItemVo tradeItemVo : listTradeItem) {
            if (tradeItemVo.getTradeItemExtra() != null && tradeItemVo.getTradeItemExtra().getId() != null
                    && tradeItemVo.getTradeItemExtra().getIsPack() == Bool.YES) {
                mTradeVo.getTradeItemExtraList().add(tradeItemVo.getTradeItemExtra());
            }
        }

        boolean isAddItemMode = Utils.isNotEmpty(mTradeVo.getTradeItemList());        for (int i = 0; i < listShopcart.size(); i++) {
            ShopcartItem mShopcartItem = listShopcart.get(i);
            TradeItemVo tradeItem = dishToTradeItem(mShopcartItem, mTradeVo, deskCount);
            tradeItem.getTradeItem().setSort(listTradeItem.size());
                        if (isAddItemMode) {
                tradeItem.getTradeItem().setItemSource(2);
            }

            createTradeItemExtra(mShopcartItem, tradeItem.getTradeItem(), mTradeVo);

                        if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                tradeItem.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
            }
            listTradeItem.add(tradeItem);

                        Collection<? extends ExtraShopcartItem> listExtra = mShopcartItem.getExtraItems();
            if (listExtra != null) {
                for (ExtraShopcartItem extra : listExtra) {
                    TradeItemVo extraTrade = dishToTradeItem(extra, mTradeVo, deskCount);
                    extraTrade.getTradeItem().setSort(listTradeItem.size());
                    if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                        extraTrade.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
                    }
                    extraTrade.setShopcartItemType(mShopcartItem.getShopcartItemType());
                    listTradeItem.add(extraTrade);
                }
            }

                        List<SetmealShopcartItem> listSetmeal = mShopcartItem.getSetmealItems();
            if (listSetmeal != null) {
                for (SetmealShopcartItem setmeal : listSetmeal) {
                    TradeItemVo setmealTrade = dishToTradeItem(setmeal, mTradeVo, deskCount);
                    setmealTrade.getTradeItem().setSort(listTradeItem.size());
                    if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                        setmealTrade.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
                    }
                                        if (isAddItemMode) {
                        setmealTrade.getTradeItem().setItemSource(2);
                    }
                    setmealTrade.setShopcartItemType(mShopcartItem.getShopcartItemType());
                    listTradeItem.add(setmealTrade);

                    createTradeItemExtra(setmeal, setmealTrade.getTradeItem(), mTradeVo);

                                        Collection<? extends ExtraShopcartItem> childListExtra = setmeal.getExtraItems();
                    if (childListExtra != null) {
                        for (ExtraShopcartItem extraItem : childListExtra) {
                            TradeItemVo extraTrade = dishToTradeItem(extraItem, mTradeVo, deskCount);
                            extraTrade.getTradeItem().setSort(listTradeItem.size());
                            extraTrade.setShopcartItemType(mShopcartItem.getShopcartItemType());
                            if (!TextUtils.isEmpty(mShopcartItem.getTradeTableUuid())) {
                                extraTrade.getTradeItem().setTradeTableUuid(mShopcartItem.getTradeTableUuid());
                            }
                            listTradeItem.add(extraTrade);
                        }
                    }
                }
            }

        }

                if (mTradeVo.getTradeExtra() == null) {
            TradeExtra mTradeExtra = new TradeExtra();
            mTradeVo.setTradeExtra(mTradeExtra);
            mTradeVo.getTradeExtra().validateCreate();
        } else if (mTradeVo.getTradeExtra() != null && !mTradeVo.getTradeExtra().hasValidateCreate()) {
            mTradeVo.getTradeExtra().validateCreate();
        }
        mTradeVo.getTradeExtra().setTradeId(mTradeVo.getTrade().getId());        mTradeVo.getTradeExtra().setTradeUuid(mTradeVo.getTrade().getUuid());
        if (mTradeVo.getTradeExtra().getUuid() == null) {
            mTradeVo.getTradeExtra().setUuid(SystemUtils.genOnlyIdentifier());
        }

                if (mTakeOutInfo != null) {
            List<TradeCustomer> listCustomer = mTradeVo.getTradeCustomerList();
            if (listCustomer != null) {
                Boolean isHadBook = false;
                for (TradeCustomer mCustomer : listCustomer) {
                    if (mCustomer.getCustomerType() == CustomerType.BOOKING) {
                        isHadBook = true;
                    }
                }
                if (!isHadBook) {
                    TradeCustomer mTradeCustomer = createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo);
                    if (mTradeCustomer != null) {
                        listCustomer.add(createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo));
                    }

                }
            } else {
                listCustomer = new ArrayList<TradeCustomer>();
                TradeCustomer mTradeCustomer = createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo);
                if (mTradeCustomer != null) {
                    listCustomer.add(createCustomer(mTradeVo.getTrade().getUuid(), mTakeOutInfo));
                }

            }
            mTradeVo.setTradeCustomerList(listCustomer);
        }

        for (TradeItemVo itemVo : mTradeVo.getTradeItemList()) {
            TradeItem tradeItem = itemVo.getTradeItem();
            tradeItem.setTradeId(mTradeVo.getTrade().getId());
            tradeItem.setTradeUuid(mTradeVo.getTrade().getUuid());
        }
    }



    public static void createTradeItemExtra(ShopcartItemBase mShopcartItemBase, TradeItem tradeItem, TradeVo tradeVo) {
        if (mShopcartItemBase.getPack()) {
            TradeItemExtra tradeItemExtra = new TradeItemExtra();
            tradeItemExtra.setStatusFlag(StatusFlag.VALID);
            tradeItemExtra.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
            tradeItemExtra.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
            tradeItemExtra.setDeviceIdenty(BaseApplication.sInstance.getDeviceIdenty());
            tradeItemExtra.setClientCreateTime(System.currentTimeMillis());
            tradeItemExtra.setClientUpdateTime(System.currentTimeMillis());
            tradeItemExtra.setChanged(true);
            tradeItemExtra.setUuid(SystemUtils.genOnlyIdentifier());
            tradeItemExtra.setTradeItemId(tradeItem.getId());
            tradeItemExtra.setTradeItemUuid(tradeItem.getUuid());
            tradeItemExtra.setIsPack(Bool.YES);
            if (Session.getAuthUser() != null) {
                tradeItemExtra.setCreatorId(Session.getAuthUser().getId());
                tradeItemExtra.setCreatorName(Session.getAuthUser().getName());
                tradeItemExtra.setUpdatorId(Session.getAuthUser().getId());
                tradeItemExtra.setUpdatorName(Session.getAuthUser().getName());
            }
            if (tradeVo.getTradeItemExtraList() == null) {
                List<TradeItemExtra> tradeItemExtras = new ArrayList<TradeItemExtra>();
                tradeItemExtras.add(tradeItemExtra);
                tradeVo.setTradeItemExtraList(tradeItemExtras);
            } else {
                tradeVo.getTradeItemExtraList().add(tradeItemExtra);
            }
        }
    }


    public static void addReadonlyTradeItemVo(List<TradeItemVo> listTradeItem, List<IShopcartItem> readonlyItem, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
                Map<String, TradeItemVo> tempTradeItem = new HashMap<String, TradeItemVo>();
        for (TradeItemVo mTradeItemVo : listTradeItem) {
            tempTradeItem.put(mTradeItemVo.getTradeItem().getUuid(), mTradeItemVo);
        }
        if (readonlyItem != null) {
            for (IShopcartItem mIShopcartItem : readonlyItem) {

                corretTradeItemQuantity(mIShopcartItem, oldDeskCount, newDeskCount);

                TradeItemVo mTradeItemVo = tempTradeItem.get(mIShopcartItem.getUuid());
                if (mTradeItemVo != null) {
                                        addReasons2TradeItemVo(mTradeItemVo, mIShopcartItem);
                    mTradeItemVo.setShopcartItemType(mIShopcartItem.getShopcartItemType());
                    mTradeItemVo.getTradeItem().setChanged(mIShopcartItem.isChanged());
                                        if (Utils.isNotEmpty(mIShopcartItem.getSetmealItems())) {
                        for (ISetmealShopcartItem setmealShopcartItem : mIShopcartItem.getSetmealItems()) {
                            TradeItemVo setmealItemVo = tempTradeItem.get(setmealShopcartItem.getUuid());
                            if (setmealItemVo != null) {
                                addReasons2TradeItemVo(setmealItemVo, setmealShopcartItem);
                            }
                        }
                    }
                } else if (mIShopcartItem instanceof ReadonlyShopcartItemBase) {
                                        ReadonlyShopcartItemBase mReadonlyShopcartItemBase = (ReadonlyShopcartItemBase) mIShopcartItem;
                    TradeItemVo tradeItemVo = buildReadOnlyTradeItemVo(mReadonlyShopcartItemBase);
                    tradeItemVo.setShopcartItemType(mReadonlyShopcartItemBase.getShopcartItemType());
                    listTradeItem.add(tradeItemVo);

                                        List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
                    if (listSetmeal != null) {
                        for (ISetmealShopcartItem item : listSetmeal) {
                            ReadonlyShopcartItemBase readonlySetmeal = (ReadonlyShopcartItemBase) item;
                            if (item instanceof ReadonlyShopcartItemBase) {
                                TradeItemVo setmealItemVo = buildReadOnlyTradeItemVo(readonlySetmeal);
                                                                setmealItemVo.setShopcartItemType(mIShopcartItem.getShopcartItemType());
                                listTradeItem.add(setmealItemVo);
                            }

                                                        Collection<ReadonlyExtraShopcartItem> listExtra = readonlySetmeal.getExtraItems();
                            if (listExtra != null) {
                                for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                                    TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                                    extraItemVo.setShopcartItemType(mIShopcartItem.getShopcartItemType());
                                    listTradeItem.add(extraItemVo);
                                }
                            }

                        }
                    }

                                        Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
                    if (listExtra != null) {
                        for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                            TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                            extraItemVo.setShopcartItemType(mReadonlyShopcartItemBase.getShopcartItemType());
                            listTradeItem.add(extraItemVo);
                        }
                    }

                }
            }
        }
    }


    private static void addReasons2TradeItemVo(TradeItemVo tradeItemVo, IShopcartItemBase iShopcartItem) {
                if (tradeItemVo != null) {
            if (iShopcartItem.getReturnQtyReason() != null) {
                tradeItemVo.setRejectQtyReason(iShopcartItem.getReturnQtyReason());
            }
            if (iShopcartItem.getDiscountReasonRel() != null) {
                TradeReasonRel tradeReasonRel = tradeItemVo.getReason(iShopcartItem.getDiscountReasonRel().getOperateType());
                if (tradeReasonRel != null) {
                    tradeReasonRel.setReasonContent(iShopcartItem.getDiscountReasonRel().getReasonContent());
                    tradeReasonRel.setReasonId(iShopcartItem.getDiscountReasonRel().getReasonId());
                }
            }

            tradeItemVo.setCouponPrivilegeVo(iShopcartItem.getCouponPrivilegeVo());
            if (iShopcartItem.getCouponPrivilegeVo() != null && iShopcartItem.getCouponPrivilegeVo().getTradePrivilege() != null) {
                iShopcartItem.getCouponPrivilegeVo().getTradePrivilege().setTradeUuid(tradeItemVo.getTradeItem().getTradeUuid());
                iShopcartItem.getCouponPrivilegeVo().getTradePrivilege().setTradeId(tradeItemVo.getTradeItem().getId());
            }

                        tradeItemVo.setTradeItemOperations(iShopcartItem.getTradeItemOperations());
            tradeItemVo.setTradeItemExtraDinner(iShopcartItem.getTradeItemExtraDinner());
            tradeItemVo.setTradeItemUserList(iShopcartItem.getTradeItemUserList());
            tradeItemVo.setCardServicePrivilegeVo(iShopcartItem.getCardServicePrivilgeVo());
            tradeItemVo.setAppletPrivilegeVo(iShopcartItem.getAppletPrivilegeVo());
        }
    }


    private static void corretTradeItemQuantity(IShopcartItem mIShopcartItem, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
        if (mIShopcartItem instanceof ReadonlyShopcartItemBase) {
            ReadonlyShopcartItemBase mReadonlyShopcartItemBase = (ReadonlyShopcartItemBase) mIShopcartItem;
            if (oldDeskCount == null || oldDeskCount.compareTo(newDeskCount) == 0 || !mIShopcartItem.isGroupDish()) {
                return;
            }
                        if (mReadonlyShopcartItemBase.isGroupDish()) {
                corretTradeItemQuantity(mReadonlyShopcartItemBase.tradeItem, oldDeskCount, newDeskCount);
                corretTradeItemPropertyQuantity(mReadonlyShopcartItemBase, oldDeskCount, newDeskCount);
            }
            List<? extends ISetmealShopcartItem> listSetmeal = mIShopcartItem.getSetmealItems();
            if (listSetmeal != null) {
                for (ISetmealShopcartItem item : listSetmeal) {
                    ReadonlyShopcartItemBase readonlySetmeal = (ReadonlyShopcartItemBase) item;
                    if (item instanceof ReadonlyShopcartItemBase) {
                        TradeItemVo setmealItemVo = buildReadOnlyTradeItemVo(readonlySetmeal);
                        if (item.isGroupDish()) {
                            corretTradeItemQuantity(setmealItemVo.getTradeItem(), oldDeskCount, newDeskCount);
                            corretTradeItemPropertyQuantity(readonlySetmeal, oldDeskCount, newDeskCount);
                        }
                    }

                                        Collection<ReadonlyExtraShopcartItem> listExtra = readonlySetmeal.getExtraItems();
                    if (listExtra != null) {
                        for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                            TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                            if (mReadonlyExtraShopcartItem.isGroupDish())
                                corretTradeItemQuantity(extraItemVo.getTradeItem(), oldDeskCount, newDeskCount);
                        }
                    }
                }
            }

                        Collection<ReadonlyExtraShopcartItem> listExtra = mReadonlyShopcartItemBase.getExtraItems();
            if (listExtra != null) {
                for (ReadonlyExtraShopcartItem mReadonlyExtraShopcartItem : listExtra) {
                    TradeItemVo extraItemVo = buildReadOnlyTradeItemVo(mReadonlyExtraShopcartItem);
                    if (mReadonlyExtraShopcartItem.isGroupDish())
                        corretTradeItemQuantity(extraItemVo.getTradeItem(), oldDeskCount, newDeskCount);
                }
            }
        }
    }

    private static void corretTradeItemQuantity(TradeItem tradeItem, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
                if (tradeItem.getStatusFlag() == StatusFlag.INVALID) {
            return;
        }
        BigDecimal newQuantitiy = newDeskCount.multiply(tradeItem.getQuantity()).divide(oldDeskCount);
        tradeItem.setQuantity(newQuantitiy);
        tradeItem.validateUpdate();
    }


    private static void corretTradeItemPropertyQuantity(ReadonlyShopcartItemBase mReadonlyShopcartItemBase, BigDecimal oldDeskCount, BigDecimal newDeskCount) {
        List<ReadonlyOrderProperty> listReadOnlyProperty = mReadonlyShopcartItemBase.getProperties();
        if (Utils.isEmpty(listReadOnlyProperty)) {
            return;
        }
        for (ReadonlyOrderProperty orderProperty : listReadOnlyProperty) {
            BigDecimal newQuantitiy = newDeskCount.multiply(orderProperty.tradeItemProperty.getQuantity()).divide(oldDeskCount);
            orderProperty.tradeItemProperty.setQuantity(newQuantitiy);
            orderProperty.tradeItemProperty.validateUpdate();
        }
    }


    public static TradeItemVo buildReadOnlyTradeItemVo(ReadonlyShopcartItemBase shopcartItem) {

        TradeItemVo tradeItemVo = new TradeItemVo();
        TradeItem mTradeItem = shopcartItem.tradeItem;
        tradeItemVo.setTradeItem(mTradeItem);
        tradeItemVo.setTradeItemPrivilege(shopcartItem.getPrivilege());
        tradeItemVo.setTradeItemExtra(shopcartItem.getTradeItemExtra());
        tradeItemVo.setCouponPrivilegeVo(shopcartItem.getCouponPrivilegeVo());
        tradeItemVo.setTradeItemExtraDinner(shopcartItem.getTradeItemExtraDinner());

                if (shopcartItem.getDiscountReasonRel() != null) {
            tradeItemVo.setDiscountReason(shopcartItem.getDiscountReasonRel());
        }

        List<ReadonlyOrderProperty> listReadOnlyProperty = shopcartItem.getProperties();
        List<TradeItemProperty> tradeItemPropertyList = new ArrayList<TradeItemProperty>();
        if (listReadOnlyProperty != null) {
            for (ReadonlyOrderProperty mReadonlyOrderProperty : listReadOnlyProperty) {
                tradeItemPropertyList.add(mReadonlyOrderProperty.tradeItemProperty);
            }
        }

        tradeItemVo.setTradeItemPropertyList(tradeItemPropertyList);
        tradeItemVo.setTradeItemUserList(shopcartItem.getTradeItemUserList());
        tradeItemVo.setCardServicePrivilegeVo(shopcartItem.getCardServicePrivilgeVo());
        tradeItemVo.setAppletPrivilegeVo(shopcartItem.getAppletPrivilegeVo());
        return tradeItemVo;
    }

    public static TradeCustomer createCustomer(String tradeUUid, TakeOutInfo entity) {
        TradeCustomer mCustomer = new TradeCustomer();
        mCustomer.setCustomerType(CustomerType.BOOKING);
        mCustomer.setCustomerId(entity.getCustomerID());
        mCustomer.setCustomerName(entity.getReceiverName());
        mCustomer.setCustomerSex(entity.getReceiverSex());
        mCustomer.setCustomerPhone(entity.getReceiverTel());
        mCustomer.setCreatorId(Session.getAuthUser().getId());
        mCustomer.setCreatorName(Session.getAuthUser().getName());
        mCustomer.setUuid(SystemUtils.genOnlyIdentifier());
        mCustomer.setTradeUuid(tradeUUid);
        mCustomer.validateCreate();
        return mCustomer;

    }


    public static void updateTradeItemPrivilgeOfRelate(TradeVo mTradeVo, List<IShopcartItem> listShopcartItem) {

        for (TradeItemVo tradeItemVo : mTradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            TradePrivilege oldTradePrivilege = tradeItemVo.getTradeItemPrivilege();
            TradeReasonRel oldTradeReasonRel = tradeItemVo.getReasonLast2();             for (IShopcartItem shopcartItem : listShopcartItem) {
                if (shopcartItem.getUuid() != null
                        && shopcartItem.getUuid().equals(tradeItem.getUuid())) {

                                        TradeReasonRel newTradeReasonRel = shopcartItem.getDiscountReasonRel();
                    if (shopcartItem.getInvalidType() == InvalidType.RETURN_QTY) {
                        newTradeReasonRel = shopcartItem.getReturnQtyReason();
                    }
                    if (newTradeReasonRel != null) {
                        if (oldTradeReasonRel != null) {
                            oldTradeReasonRel.setStatusFlag(newTradeReasonRel.getStatusFlag());
                            oldTradeReasonRel.setReasonContent(newTradeReasonRel.getReasonContent());
                            oldTradeReasonRel.setReasonId(newTradeReasonRel.getReasonId());
                            oldTradeReasonRel.validateUpdate();
                        } else {
                            newTradeReasonRel.setRelateId(tradeItem.getId());
                            newTradeReasonRel.setRelateUuid(tradeItem.getUuid());
                            tradeItemVo.setDiscountReason(newTradeReasonRel);
                        }
                    } else {
                                                if (oldTradeReasonRel != null) {
                            tradeItemVo.removeTradeReasonRel(oldTradeReasonRel, oldTradeReasonRel.getOperateType());
                        }
                    }


                                        if (shopcartItem.getPrivilege() != null) {
                        TradePrivilege mTradePrivilege = cloneTradePrivilege(shopcartItem.getPrivilege());
                        mTradePrivilege.setTradeId(tradeItem.getTradeId());
                        mTradePrivilege.setTradeUuid(tradeItem.getTradeUuid());
                        mTradePrivilege.setTradeItemId(tradeItem.getId());
                        mTradePrivilege.setTradeItemUuid(tradeItem.getUuid());
                        if (oldTradePrivilege != null) {
                            mTradePrivilege.setId(oldTradePrivilege.getId());
                            mTradePrivilege.setUuid(oldTradePrivilege.getUuid());
                            mTradePrivilege.setServerCreateTime(oldTradePrivilege.getServerCreateTime());
                            mTradePrivilege.setServerUpdateTime(oldTradePrivilege.getServerUpdateTime());
                        }
                        mTradePrivilege.setChanged(true);
                        tradeItemVo.setTradeItemPrivilege(mTradePrivilege);
                        break;

                    } else {
                                                if (oldTradePrivilege != null) {
                            if (tradeItem.getId() == null) {
                                tradeItemVo.setTradeItemPrivilege(null);
                                break;
                            }
                            TradePrivilege tradePrivilege = cloneTradePrivilege(oldTradePrivilege);
                            tradePrivilege.setStatusFlag(StatusFlag.INVALID);
                            tradePrivilege.setChanged(true);
                            tradeItemVo.setTradeItemPrivilege(tradePrivilege);
                            break;
                        }
                    }
                }
            }
        }
    }

    public static TradePrivilege cloneTradePrivilege(TradePrivilege theRef) {
        if (theRef == null) {
            return null;
        }
        TradePrivilege theNew = new TradePrivilege();
        try {
            Beans.copyProperties(theRef, theNew);
        } catch (Exception e) {
            Log.e(TAG, "Copy error!", e);
        }
        return theNew;
    }


    public static TradeItemExtraDinner buildTradeItemExtraDinner(IShopcartItem shopcartItem, TableSeat tableSeat) {
        if (tableSeat == null) {
            return null;
        }
        TradeItemExtraDinner tradeItemExtraDinner = new TradeItemExtraDinner();
        tradeItemExtraDinner.setStatusFlag(StatusFlag.VALID);
        tradeItemExtraDinner.setChanged(true);
        tradeItemExtraDinner.setTradeItemId(shopcartItem.getId());
        tradeItemExtraDinner.setTradeItemUuid(shopcartItem.getUuid());
        tradeItemExtraDinner.setSeatId(tableSeat.getId());
        tradeItemExtraDinner.setSeatNumber(tableSeat.getSeatName());
        tradeItemExtraDinner.setClientCreateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setClientUpdateTime(System.currentTimeMillis());
        tradeItemExtraDinner.setShopIdenty(BaseApplication.sInstance.getShopIdenty());
        tradeItemExtraDinner.setBrandIdenty(BaseApplication.sInstance.getBrandIdenty());
        tradeItemExtraDinner.setCreatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setCreatorName(Session.getAuthUser().getName());
        tradeItemExtraDinner.setUpdatorId(Session.getAuthUser().getId());
        tradeItemExtraDinner.setUpdatorName(Session.getAuthUser().getName());
        return tradeItemExtraDinner;
    }

}
