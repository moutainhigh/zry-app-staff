package com.zhongmei.bty.basemodule.orderdish.utils;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.DishVo;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderDish;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderExtra;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderSetmeal;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyOrderProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlySetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ReadonlyShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.dish.DishPropertyType;
import com.zhongmei.yunfu.db.entity.dish.DishSetmeal;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.DishUnitDictionary;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.manager.DishManager;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.PropertyKind;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @version: 1.0
 * @date 2015年11月3日
 */
public final class ShopcartItemUtils {
    private ShopcartItemUtils() {
    }

    private static final String TAG = ShopcartItemUtils.class.getSimpleName();

    private static String uuid() {
        return SystemUtils.genOnlyIdentifier();
    }

    /**
     * 计算TradeVo中的明细项的单份数量
     *
     * @param mItemVos
     * @return 返回的包含每项明细的单份数量的Map，其key为明细项的uuid，value为单份数量
     */
    public static Map<String, BigDecimal> computeSingleQty(List<TradeItemVo> mItemVos) {
        List<TradeItemVo> itemVoList = mItemVos;
        Map<String, TradeItem> parentFinder = new HashMap<String, TradeItem>();
        for (TradeItemVo itemVo : itemVoList) {
            TradeItem item = itemVo.getTradeItem();
            parentFinder.put(item.getUuid(), item);
        }
        Map<String, BigDecimal> resultMap = new HashMap<String, BigDecimal>();
        for (TradeItem item : parentFinder.values()) {
            String uuid = item.getUuid();
            TradeItem parent = parentFinder.get(item.getParentUuid());
            TradeItem grandfather = null;
            if (parent != null && !TextUtils.isEmpty(parent.getParentUuid())) {
                grandfather = parentFinder.get(parent.getParentUuid());
            }
            BigDecimal singleQty = computeSingleQty(item.getQuantity(), parent, grandfather);
            resultMap.put(uuid, singleQty);
        }
        return resultMap;
    }

    /**
     * 计算条目的不受父条目影响的数量 获取退货的数量
     *
     * @param totalQty
     * @param parent      父级
     * @param grandfather 父级的父级
     * @return
     */
    public static BigDecimal computeReturnSingleQty(BigDecimal totalQty, TradeItem parent, TradeItem grandfather) {
        if (parent == null) {
            return totalQty;
        }
        // 祖先中有非称重商品时就需要除以最近的非称重祖先条目的数量
        BigDecimal qtyRef = null;
        if (parent.getSaleType() != SaleType.WEIGHING) {
            qtyRef = parent.getReturnQuantity();
        } else if (grandfather != null && grandfather.getSaleType() != SaleType.WEIGHING) {
            qtyRef = grandfather.getReturnQuantity();
        }
        if (qtyRef == null) {
            return totalQty;
        }
        return MathDecimal.div(totalQty, qtyRef);
    }

    /**
     * 计算条目的不受父条目影响的数量
     *
     * @param totalQty
     * @param parent      父级
     * @param grandfather 父级的父级
     * @return
     */
    public static BigDecimal computeSingleQty(BigDecimal totalQty, TradeItem parent, TradeItem grandfather) {
        if (parent == null) {
            return totalQty;
        }
        // 祖先中有非称重商品时就需要除以最近的非称重祖先条目的数量
        BigDecimal qtyRef = null;
        if (parent.getSaleType() != SaleType.WEIGHING) {
            qtyRef = parent.getQuantity();
        } else if (grandfather != null && grandfather.getSaleType() != SaleType.WEIGHING) {
            qtyRef = grandfather.getQuantity();
        }
        if (qtyRef == null) {
            return totalQty;
        }
        return MathDecimal.div(totalQty, qtyRef);
    }

    public static BigDecimal computeSingleQty(BigDecimal totalQty, ReadonlyShopcartItemBase parent) {
        TradeItem parentItem = null;
        TradeItem grandfatherItem = null;
        if (parent != null) {
            parentItem = parent.tradeItem;
            ReadonlyShopcartItemBase grandfather = parent.getParent();
            if (grandfather != null) {
                grandfatherItem = grandfather.tradeItem;
            }
        }
        return computeSingleQty(totalQty, parentItem, grandfatherItem);
    }

    private static BigDecimal getParentQtyRef(IShopcartItemBase parent, IShopcartItemBase grandfather) {
        if (parent == null) {
            return BigDecimal.ONE;
        }
        if (parent.getSaleType() != SaleType.WEIGHING) {
            return parent.getTotalQty();
        }
        if (grandfather != null && grandfather.getSaleType() != SaleType.WEIGHING) {
            return grandfather.getTotalQty();
        }
        return BigDecimal.ONE;
    }

    public static BigDecimal getParentQtyRef(ShopcartItemBase<?> parent) {
        IShopcartItemBase grandfather = null;
        if (parent != null) {
            grandfather = parent.getParent();
        }
        return getParentQtyRef(parent, grandfather);
    }

    public static BigDecimal computeTotalQty(BigDecimal singleQty, ShopcartItemBase<?> parent) {
        if (parent == null) {
            return singleQty;
        }
        BigDecimal qtyRef = getParentQtyRef(parent);
        return singleQty.multiply(qtyRef);
    }

    /**
     * 计算受父条目影响的总数量
     *
     * @param singleQty
     * @param parent
     * @return
     */
    private static BigDecimal computeTotalQty(BigDecimal singleQty, ReadonlyShopcartItemBase parent) {
        if (parent == null) {
            return singleQty;
        }
        BigDecimal qtyRef = getParentQtyRef(parent, parent.getParent());
        return singleQty.multiply(qtyRef);
    }

    private static BigDecimal computeActualAmount(ReadonlyShopcartItemBase itemBase) {
        BigDecimal actualAmount = itemBase.tradeItem.getAmount();
        if (itemBase.getPropertyAmount() != null) {
            actualAmount = actualAmount.add(itemBase.getPropertyAmount());
        }
        if (itemBase.getFeedsAmount() != null) {
            actualAmount = actualAmount.add(itemBase.getFeedsAmount());
        }
        return actualAmount;
    }

    /**
     * 撤消购物车条目的被拆单状态。该方法将撤消条目及其下所有子条目的被拆单状态
     *
     * @param item
     */
    public static void cancelSplit(ShopcartItem item) {
        if (item.ensureCancelSplit()) {
            completeCancelSplit(item);
            if (item.hasSetmeal()) {
                for (SetmealShopcartItem setmealItem : item.getSetmealItems()) {
                    if (setmealItem.ensureCancelSplit()) {
                        completeCancelSplit(setmealItem);
                    }
                }
            }
        }
    }

    private static <T extends ShopcartItemBase<?>> void completeCancelSplit(T item) {
        if (item.hasExtra()) {
            for (ExtraShopcartItem extraItem : item.getExtraItems()) {
                extraItem.ensureCancelSplit();
            }
        }
    }

    /**
     * 撤消购物车条目的被拆单状态。该方法将撤消条目及其下所有子条目的被拆单状态
     *
     * @param item
     */
    public static void cancelSplit(ReadonlyShopcartItem item) {
        if (item.ensureCancelSplit()) {
            completeCancelSplit(item);
            if (item.hasSetmeal()) {
                for (ReadonlySetmealShopcartItem setmealItem : item.getSetmealItems()) {
                    if (setmealItem.ensureCancelSplit()) {
                        completeCancelSplit(setmealItem);
                    }
                }
            }
        }
    }

    private static <T extends ReadonlyShopcartItemBase> void completeCancelSplit(T item) {
        if (item.hasExtra()) {
            for (ReadonlyExtraShopcartItem extraItem : item.getExtraItems()) {
                extraItem.ensureCancelSplit();
            }
        }
    }

    /**
     * 将指定的购物车条目进行拆单操作，并返回新的购物车条目
     *
     * @param ref
     * @return
     */
    public static ShopcartItem splitCopy(ShopcartItem ref) {
        ShopcartItem newItem = null;
        if (ref.ensureSplit()) {
            newItem = completeSplitCopy(ref, new ShopcartItem(uuid(), ref.orderDish, ref.metadata));
            if (newItem == null) {
                return null;
            }
            // 套餐明细
            if (ref.hasSetmeal()) {
                newItem.setSetmealItems(new ArrayList<SetmealShopcartItem>());
                for (SetmealShopcartItem refSetmealItem : ref.getSetmealItems()) {
                    SetmealShopcartItem newSetmealItem = spliteCopySetmeal(refSetmealItem, newItem);
                    if (newSetmealItem == null) {
                        return null;
                    }
                    newItem.getSetmealItems().add(newSetmealItem);
                }
            }
            // 保留tradeTableUuid，以便completeTradeTable方法中使用
            newItem.setTradeTable(ref.getTradeTableUuid(), null);
        }
        return newItem;
    }

    /**
     * 对指定的套餐明细条目进行拆单操作，返回新的套餐明细条目
     *
     * @param ref
     * @param newParent 新套餐明细的父条目
     * @return
     */
    private static SetmealShopcartItem spliteCopySetmeal(SetmealShopcartItem ref, ShopcartItem newParent) {
        if (ref.ensureSplit()) {
            return completeSplitCopy(ref, new SetmealShopcartItem(uuid(), ref.orderDish, newParent));
        }
        return null;
    }


    /**
     * 对theRef条目中的加料、属性、优惠进行拆单操作，并将生成的新条目设置到theNew中
     *
     * @param theRef
     * @param theNew
     * @return
     */
    private static <T extends ShopcartItemBase<?>> T completeSplitCopy(T theRef, T theNew) {
        theNew.setRelateInfo(theRef.getId(), theRef.getUuid());
        if (theRef.hasExtra()) {
            List<ExtraShopcartItem> newExtraItems = new ArrayList<ExtraShopcartItem>();
            for (ExtraShopcartItem refExtraItem : theRef.getExtraItems()) {
                if (refExtraItem.ensureSplit()) {
                    ExtraShopcartItem extraItem = new ExtraShopcartItem(uuid(), refExtraItem.orderDish, theNew);
                    extraItem.setRelateInfo(refExtraItem.getId(), refExtraItem.getUuid());
                    newExtraItems.add(extraItem);
                } else {
                    return null;
                }
            }
            theNew.setExtraItems(newExtraItems);
        }
        if (theRef.hasProperty()) {
            List<OrderProperty> newProperties = new ArrayList<OrderProperty>();
            for (OrderProperty refProperty : theRef.getProperties()) {
                newProperties.add(new OrderProperty(refProperty.getPropertyType(), refProperty.getProperty()));
            }
            theNew.setProperties(newProperties);
        }
        // 优惠信息。会员折扣不复制
        TradePrivilege refPrivilege = theRef.getPrivilege();
        if (refPrivilege != null && refPrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT && refPrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE) {
            TradePrivilege newPrivilege = newEntity(refPrivilege, theNew.getUuid());
            if (newPrivilege == null) {
                return null;
            }
            theNew.setPrivilege(newPrivilege);
        }

        //礼品劵
        CouponPrivilegeVo couponPrivilegeVo = theRef.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null) {
            CouponPrivilegeVo newCouponPrivilegeVo = new CouponPrivilegeVo();
            try {
                Beans.copyProperties(couponPrivilegeVo, newCouponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    TradePrivilege newPrivilege = newEntity(couponPrivilegeVo.getTradePrivilege(), theNew.getUuid());
                    newCouponPrivilegeVo.setTradePrivilege(newPrivilege);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            theNew.setCouponPrivilegeVo(newCouponPrivilegeVo);
        }

        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            if (newReasonRel == null) {
                return null;
            }
            theNew.setDiscountReasonRel(newReasonRel);
        }
        if (theRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(theRef.getTradeItemExtraDinner(), theNew.getUuid());
            theNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }
        return theNew;
    }

    /**
     * 将指定的购物车条目进行拆单操作，并返回新的购物车条目
     *
     * @param ref
     * @return
     */
    public static ReadonlyShopcartItem spliteCopy(ReadonlyShopcartItem ref) {
        ReadonlyShopcartItem newItem = null;
        if (ref.ensureSplit()) {
            TradeItem newTradeItem = relateCopyBySplit(ref.tradeItem, null);
            if (newTradeItem == null) {
                return null;
            }
            newItem = completeSplitCopy(ref, new ReadonlyShopcartItem(newTradeItem));
            if (newItem == null) {
                return null;
            }
            if (ref.hasSetmeal()) {
                newItem.setSetmealItems(new ArrayList<ReadonlySetmealShopcartItem>());
                for (ReadonlySetmealShopcartItem refSetmealItem : ref.getSetmealItems()) {
                    ReadonlySetmealShopcartItem newSetmealItem = spliteCopySetmeal(refSetmealItem, newItem);
                    if (newSetmealItem == null) {
                        return null;
                    }
                    newItem.getSetmealItems().add(newSetmealItem);
                }
            }
        }
        return newItem;
    }


    /**
     * 对指定的套餐明细条目进行拆单操作，返回新的套餐明细条目
     *
     * @param ref
     * @param newParent 新套餐明细的父条目
     * @return
     */
    private static ReadonlySetmealShopcartItem spliteCopySetmeal(ReadonlySetmealShopcartItem ref,
                                                                 ReadonlyShopcartItem newParent) {
        if (ref.ensureSplit()) {
            TradeItem newTradeItem = relateCopyBySplit(ref.tradeItem, newParent.getUuid());
            if (newTradeItem == null) {
                Log.w(TAG, "splite failed! ref=" + ref);
                return null;
            }
            return completeSplitCopy(ref, new ReadonlySetmealShopcartItem(newTradeItem, newParent));
        }
        Log.w(TAG, "splite failed! ref=" + ref);
        return null;
    }

    /**
     * 对theRef条目中的加料、属性、优惠进行拆单操作，并将生成的新条目设置到theNew中
     *
     * @param theRef
     * @param theNew
     * @return
     */
    private static <T extends ReadonlyShopcartItemBase> T completeSplitCopy(T theRef, T theNew) {
        if (theRef.hasExtra()) {
            // 复制加料
            List<ReadonlyExtraShopcartItem> newExtraItems = new ArrayList<ReadonlyExtraShopcartItem>();
            for (ReadonlyExtraShopcartItem refExtraItem : theRef.getExtraItems()) {
                if (refExtraItem.ensureCopy(true)) {
                    TradeItem newExtraTradeItem = relateCopyBySplit(refExtraItem.tradeItem, theNew.getUuid());
                    if (newExtraTradeItem == null) {
                        return null;
                    }
                    newExtraItems.add(new ReadonlyExtraShopcartItem(newExtraTradeItem, theNew));
                }
            }
            theNew.setExtraItems(newExtraItems);
        }
        if (theRef.hasProperty()) {
            // 复制属性
            List<ReadonlyOrderProperty> newProperties = new ArrayList<ReadonlyOrderProperty>();
            for (ReadonlyOrderProperty refProperty : theRef.getProperties()) {
                TradeItemProperty newTradeItemProperty = newEntity(refProperty.tradeItemProperty, theNew.getUuid());
                if (newTradeItemProperty == null) {
                    return null;
                }
                newProperties.add(new ReadonlyOrderProperty(newTradeItemProperty));
            }
            theNew.setProperties(newProperties);
        }
        // 优惠信息。会员折扣不复制
        TradePrivilege refPrivilege = theRef.getPrivilege();
        if (refPrivilege != null && refPrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT && refPrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE) {
            TradePrivilege newPrivilege = newEntity(refPrivilege, theNew.getUuid());
            theNew.setPrivilege(newPrivilege);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            if (newReasonRel == null) {
                return null;
            }
            theNew.setDiscountReasonRel(newReasonRel);
        }

        //礼品劵
        CouponPrivilegeVo couponPrivilegeVo = theRef.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null) {
            CouponPrivilegeVo newCouponPrivilegeVo = new CouponPrivilegeVo();
            try {
                Beans.copyProperties(couponPrivilegeVo, newCouponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    TradePrivilege newPrivilege = newEntity(couponPrivilegeVo.getTradePrivilege(), theNew.getUuid());
                    newCouponPrivilegeVo.setTradePrivilege(newPrivilege);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            theNew.setCouponPrivilegeVo(newCouponPrivilegeVo);
        }

        return theNew;
    }


    /**
     * 撤消退菜
     *
     * @param item
     */
    public static void cancelReturnQty(ReadonlyShopcartItem item) {
        completeReturnQty(item);
        if (item.hasSetmeal()) {
            for (ReadonlySetmealShopcartItem setmealItem : item.getSetmealItems()) {
                completeReturnQty(setmealItem);
            }
        }
    }

    private static void completeReturnQty(ReadonlyShopcartItemBase itemBase) {
        itemBase.clearReturnQtyState();
        if (itemBase.hasExtra()) {
            for (ReadonlyExtraShopcartItem extraItem : itemBase.getExtraItems()) {
                extraItem.clearReturnQtyState();
            }
        }
        if (itemBase.hasProperty()) {
            for (ReadonlyOrderProperty property : itemBase.getProperties()) {
                property.tradeItemProperty.setStatusFlag(StatusFlag.VALID);
                property.tradeItemProperty.validateUpdate();
            }
        }
    }

    public static void cancelModifyDish(ReadonlyShopcartItem item) {
        completeModifyDish(item);
        if (item.hasSetmeal()) {
            for (ReadonlySetmealShopcartItem setmealItem : item.getSetmealItems()) {
                completeModifyDish(setmealItem);
            }
        }
    }

    private static void completeModifyDish(ReadonlyShopcartItemBase itemBase) {
        itemBase.clearModifyDishState();
        if (itemBase.hasExtra()) {
            for (ReadonlyExtraShopcartItem extraItem : itemBase.getExtraItems()) {
                extraItem.clearModifyDishState();
            }
        }
        if (itemBase.hasProperty()) {
            for (ReadonlyOrderProperty property : itemBase.getProperties()) {
                property.tradeItemProperty.setStatusFlag(StatusFlag.VALID);
                property.tradeItemProperty.validateUpdate();
            }
        }
    }

    public static ShopcartItem modifyDishCopy(ReadonlyShopcartItem theRef) {
        String skuUuid = theRef.getSkuUuid();
        DishShop dishShop = DishCache.getDishHolder().get(skuUuid);
        if (dishShop != null) {
            DishManager dishManager = new DishManager();
            List<DishVo> dishVos = dishManager.getDishsVo(Arrays.asList(dishShop));
            if (Utils.isNotEmpty(dishVos)) {
                ShopcartItem theNew = createShopcartItem(dishVos.get(0), theRef.getSingleQty());
                theNew.creatorId = theRef.tradeItem.getCreatorId();
                theNew.creatorName = theRef.tradeItem.getCreatorName();//设置原菜的creator
                theNew.setIsGroupDish(theRef.isGroupDish());
                theNew.setShopcartItemType(theRef.getShopcartItemType());
                if (theRef.isGroupDish()) {
                    theNew.changeQty(theRef.getSigleDeskQuantity());
                }
                //临时菜改名后名字变化
                theNew.changeName(theRef.getSkuName());
                theRef.setupModifyDishState();
                completeByMd(theRef, theNew);
                if (theRef.hasSetmeal()) {
                    createSetmealShopcartItemsMD(theNew, theRef);
                }
                return theNew;
            }
        }

        return null;
    }

    /**
     * 退菜复制
     *
     * @param theRef
     * @param returnQty
     * @return
     */
    public static ReadonlyShopcartItem returnQtyCopy(final ReadonlyShopcartItem theRef, final BigDecimal returnQty) {
		/*if (returnQty.compareTo(BigDecimal.ZERO) >= 0) {
			throw new RuntimeException("The returnQty is wrong.");
		}*/
        BigDecimal newTotalQty = theRef.getTotalQty().add(returnQty);
		/*if (newTotalQty.compareTo(BigDecimal.ZERO) < 0) {
			throw new RuntimeException("The returnQty is wrong.");
		}
		if (theRef.getSaleType() == SaleType.WEIGHING && newTotalQty.compareTo(BigDecimal.ZERO) != 0) {
			throw new RuntimeException("Must be all returns.");
		}*/

        boolean allReturns = true;
        TradeItem newTradeItem = relateCopyByReturnQty(theRef.tradeItem, theRef.tradeItem.getParentUuid());
        newTradeItem.setParentId(theRef.tradeItem.getParentId());//v8.10自助连台针对于单品作废
        if (newTotalQty.compareTo(BigDecimal.ZERO) > 0) {
            newTradeItem.setQuantity(newTotalQty);
            newTradeItem.setAmount(newTradeItem.getPrice().multiply(newTotalQty));
            allReturns = false;
        }

        BigDecimal newActualAmount = BigDecimal.ZERO;
        ReadonlyShopcartItem theNew = new ReadonlyShopcartItem(newTradeItem, theRef.getReturnQtyReason());
        theNew.setIsGroupDish(theRef.isGroupDish());
        theNew.setShopcartItemType(theRef.getShopcartItemType());
        if (theRef.hasSetmeal()) {
            List<ReadonlySetmealShopcartItem> setmealItems = new ArrayList<ReadonlySetmealShopcartItem>();
            for (ReadonlySetmealShopcartItem itemRef : theRef.getSetmealItems()) {
                if (itemRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                ReadonlySetmealShopcartItem itemNew = relateCopyByRQ(itemRef, theNew, allReturns);
                itemNew.setIsGroupDish(itemRef.isGroupDish());
                itemNew.setShopcartItemType(itemRef.getShopcartItemType());
                setmealItems.add(itemNew);
                // 累加套餐明细的金额
                newActualAmount = newActualAmount.add(itemNew.getActualAmount());
            }
            theNew.setSetmealItems(setmealItems);
        }
        theNew = completeByRQ(theRef, theNew, allReturns);
        if (allReturns) {
            // 全退时主条目的数量和金额均为0，状态为有效。
            theNew.tradeItem.setQuantity(BigDecimal.ZERO);
            theNew.tradeItem.setAmount(BigDecimal.ZERO);
            theNew.tradeItem.setPropertyAmount(BigDecimal.ZERO);
            theNew.tradeItem.setFeedsAmount(BigDecimal.ZERO);
            theNew.tradeItem.setActualAmount(BigDecimal.ZERO);
        } else {
            newActualAmount = newActualAmount.add(computeActualAmount(theNew));
            theNew.tradeItem.setActualAmount(newActualAmount);
        }
        theRef.setupReturnQtyState(returnQty);
        return theNew;
    }

    private static ReadonlySetmealShopcartItem relateCopyByRQ(ReadonlySetmealShopcartItem itemRef,
                                                              ReadonlyShopcartItem newParent, boolean allReturns) {
        TradeItem tradeItem = relateCopyByReturnQty(itemRef.tradeItem, newParent.getUuid());
        if (allReturns) {
            // 全退时明细条目的数量和金额不变，状态为无效。
            tradeItem.setStatusFlag(StatusFlag.INVALID);
            itemRef.setupReturnQtyState(tradeItem.getQuantity().negate());
        } else {
            BigDecimal totalQty = computeTotalQty(itemRef.getSingleQty(), newParent);
            tradeItem.setQuantity(totalQty);
            tradeItem.setAmount(tradeItem.getPrice().multiply(tradeItem.getQuantity()));
            BigDecimal returnValue = tradeItem.getQuantity().subtract(itemRef.getTotalQty());
            itemRef.setupReturnQtyState(returnValue);
        }
        ReadonlySetmealShopcartItem theNew = new ReadonlySetmealShopcartItem(tradeItem, newParent);
        theNew = completeByRQ(itemRef, theNew, allReturns);
        theNew.setShopcartItemType(itemRef.getShopcartItemType());
        theNew.tradeItem.setActualAmount(computeActualAmount(theNew));
        return theNew;
    }

    private static void completeByMd(ReadonlyShopcartItem theRef, ShopcartItem theNew) {
        theNew.setRelateInfo(theRef.getId(), theRef.getUuid());
        theNew.changePrice(theRef.getPrice());
        // 复制加料
        if (theRef.hasExtra()) {
            List<ExtraShopcartItem> newExtraItems = new ArrayList<ExtraShopcartItem>();
            for (ReadonlyExtraShopcartItem itemRef : theRef.getExtraItems()) {
                if (itemRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                itemRef.setupModifyDishState();

                String extraSkuUuid = itemRef.tradeItem.getSkuUuid();
                DishShop setmealDishShop = DishCache.getExtraHolder().get(extraSkuUuid);
                if (setmealDishShop == null) {
                    continue;
                }
                Long dishId = theNew.getOrderDish().getBrandDishId();
                Long childDishId = setmealDishShop.getBrandDishId();
                DishSetmeal dishSetmeal = DishCache.getDishExtraHolder().get(dishId, childDishId);
//				if (dishSetmeal == null) {
//					// 获取加料对应的公共DishSetmeal
//					dishSetmeal = DishCache.getDishExtraHolder().get(childDishId);
//					if(dishSetmeal == null) {
//						continue;
//					}
//				}
                OrderExtra orderExtra = new OrderExtra(setmealDishShop, dishSetmeal);
                BigDecimal parentQty = theNew.getTotalQty();
                if (parentQty != null) {
                    try {
                        BigDecimal singleQty = computeSingleQty(itemRef.tradeItem.getQuantity(), theRef);
                        orderExtra.setQty(singleQty, itemRef.tradeItem.getQuantity());
                    } catch (Exception e) {
                        Log.e(TAG, "计算加料份数量出错：qty=" + itemRef.tradeItem.getQuantity() + ", parentQty=" + parentQty, e);
                        continue;
                    }
                } else {
                    orderExtra.setQty(itemRef.tradeItem.getQuantity(), itemRef.tradeItem.getQuantity());
                }
                ExtraShopcartItem newExtraItem = new ExtraShopcartItem(uuid(), orderExtra, theNew);
                //称重商品存的实际数量
                if (theRef.isGroupDish() && itemRef.getDeskCount() != null && theRef.getSaleType() == SaleType.WEIGHING) {
                    newExtraItem.getOrderDish().setQty(newExtraItem.getSingleQty().divide(itemRef.getDeskCount()), newExtraItem.getTotalQty());
                }
                newExtraItem.setIsGroupDish(theRef.isGroupDish());
                newExtraItem.setRelateInfo(itemRef.getId(), itemRef.getUuid());
                newExtraItem.setShopcartItemType(itemRef.getShopcartItemType());
                newExtraItems.add(newExtraItem);
            }
            theNew.setExtraItems(newExtraItems);
        }
        // 复制属性
        if (theRef.hasProperty()) {
            List<OrderProperty> newProperties = new ArrayList<>();
            OrderDish orderDish = theNew.getOrderDish();
            // 添加规格类属性。根据一个商品就可以找出其规格列表
            for (DishProperty property : orderDish.getStandards()) {
                Long propertyTypeId = property.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                if (propertyType == null) {
                    continue;
                }
                newProperties.add(new OrderProperty(propertyType, property));
            }

            // 判断非规格类属性
            for (ReadonlyOrderProperty propertyRef : theRef.getProperties()) {
                if (propertyRef.tradeItemProperty.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }

                if (propertyRef.getPropertyKind() != PropertyKind.STANDARD) {
                    String propertyUuid = propertyRef.getPropertyUuid();
                    DishProperty property = DishCache.getPropertyHolder().get(propertyUuid);
                    if (property == null) {
                        continue;
                    }
                    Long propertyTypeId = property.getPropertyTypeId();
                    DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                    if (propertyType == null) {
                        continue;
                    }
//					DishBrandProperty dishProperty = DishCache.getDishPropertyHolder().get(orderDish.getBrandDishId(), property.getId());
//					if (dishProperty == null) {
//						continue;
//					}
                    newProperties.add(new OrderProperty(propertyType, property));
                }

                //原菜所有属性置为无效
                propertyRef.tradeItemProperty.setStatusFlag(StatusFlag.INVALID);
                propertyRef.tradeItemProperty.validateUpdate();
            }
            theNew.setProperties(newProperties);
        }

        // 优惠信息。会员折扣不复制
        TradePrivilege refPrivilege = theRef.getPrivilege();
        if (refPrivilege != null && refPrivilege.isValid()
                && refPrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT
                && refPrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE) {
            TradePrivilege newPrivilege = newEntity(refPrivilege, theNew.getUuid());
            theNew.setPrivilege(newPrivilege);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            if (newReasonRel == null) {
                return;
            }
            theNew.setDiscountReasonRel(newReasonRel);
        }

        //礼品劵
        CouponPrivilegeVo couponPrivilegeVo = theRef.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null) {
            CouponPrivilegeVo newCouponPrivilegeVo = new CouponPrivilegeVo();
            try {
                Beans.copyProperties(couponPrivilegeVo, newCouponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    TradePrivilege newPrivilege = newEntity(couponPrivilegeVo.getTradePrivilege(), theNew.getUuid());
                    newCouponPrivilegeVo.setTradePrivilege(newPrivilege);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            theNew.setCouponPrivilegeVo(newCouponPrivilegeVo);
        }
        if (theRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(theRef.getTradeItemExtraDinner(), theNew.getUuid());
            theNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }

        theNew.setMemo(theRef.getMemo());
    }

    private static <T extends ReadonlyShopcartItemBase> T completeByRQ(T theRef, T theNew, boolean allReturns) {
        // 复制加料
        if (theRef.hasExtra()) {
            BigDecimal feedsAmount = BigDecimal.ZERO;
            List<ReadonlyExtraShopcartItem> newExtraItems = new ArrayList<ReadonlyExtraShopcartItem>();
            for (ReadonlyExtraShopcartItem itemRef : theRef.getExtraItems()) {
                if (itemRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                TradeItem tradeItem = relateCopyByReturnQty(itemRef.tradeItem, theNew.getUuid());
                if (allReturns) {
                    // 全退时明细条目的数量和金额不变，状态为无效。
                    tradeItem.setStatusFlag(StatusFlag.INVALID);
                    itemRef.setupReturnQtyState(tradeItem.getQuantity().negate());
                } else {
                    BigDecimal totalQty = computeTotalQty(itemRef.getSingleQty(), theNew);
                    tradeItem.setQuantity(totalQty);
                    tradeItem.setAmount(tradeItem.getPrice().multiply(tradeItem.getQuantity()));
                    tradeItem.setActualAmount(tradeItem.getAmount());
                    BigDecimal returnQty = tradeItem.getQuantity().subtract(itemRef.getTotalQty());
                    itemRef.setupReturnQtyState(returnQty);
                }
                ReadonlyExtraShopcartItem itemNew = new ReadonlyExtraShopcartItem(tradeItem, theNew);
                itemNew.setShopcartItemType(itemRef.getShopcartItemType());
                newExtraItems.add(itemNew);
                feedsAmount = MathDecimal.add(feedsAmount, tradeItem.getAmount());
            }
            theNew.setExtraItems(newExtraItems);
            theNew.tradeItem.setFeedsAmount(feedsAmount);
        }
        // 复制属性
        if (theRef.hasProperty()) {
            BigDecimal propertyAmount = BigDecimal.ZERO;
            List<ReadonlyOrderProperty> newProperties = new ArrayList<ReadonlyOrderProperty>();
            for (ReadonlyOrderProperty propertyRef : theRef.getProperties()) {
                if (propertyRef.tradeItemProperty.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                TradeItemProperty itemProperty = newEntity(propertyRef.tradeItemProperty, theNew.getUuid());
                if (!allReturns) {
                    itemProperty.setQuantity(theNew.getTotalQty());
                    if (itemProperty.getPrice() != null) {
                        itemProperty.setAmount(itemProperty.getPrice().multiply(itemProperty.getQuantity()));
                    }
                }
                newProperties.add(new ReadonlyOrderProperty(itemProperty));
                propertyAmount = MathDecimal.add(propertyAmount, itemProperty.getAmount());
            }
            theNew.setProperties(newProperties);
            theNew.tradeItem.setPropertyAmount(propertyAmount);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            theNew.setDiscountReasonRel(newReasonRel);
        }
        //全退时不拷贝原菜的菜品操作记录
        if (!allReturns) {
            if (theRef instanceof IShopcartItem && Utils.isNotEmpty(((IShopcartItem) theRef).getTradeItemOperations())) {
                List<TradeItemOperation> newOperations = new ArrayList<TradeItemOperation>();
                for (TradeItemOperation operationRef : ((IShopcartItem) theRef).getTradeItemOperations()) {
                    if (operationRef.getStatusFlag() == StatusFlag.INVALID) {
                        continue;
                    }
                    TradeItemOperation itemOperation = newEntity(operationRef, theNew.getUuid());
                    newOperations.add(itemOperation);
                }
                if (Utils.isNotEmpty(newOperations) && theNew instanceof IShopcartItem) {
                    ((IShopcartItem) theNew).setTradeItemOperations(newOperations);
                }
            }
        }

        if (theRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(theRef.getTradeItemExtraDinner(), theNew.getUuid());
            theNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }

        return theNew;
    }

    /**
     * 完全复制tradeItem
     *
     * @param theRef
     * @return
     */
    private static TradeItem relateCopyComplete(TradeItem theRef) {
        TradeItem theNew = new TradeItem();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setServerCreateTime(null);
            theNew.setServerUpdateTime(null);
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return theNew;
    }

    private static TradeItem relateCopyBySplit(TradeItem theRef, String newParentUuid) {
        // 注意，保留tradeTableUuid，因为在complete方法中要使用
        TradeItem theNew = relateCopy(theRef, newParentUuid);
        theNew.setCreatorId(theRef.getCreatorId());
        theNew.setCreatorName(theRef.getCreatorName());
        theNew.setTradeId(null);
        theNew.setTradeUuid(null);
        theNew.setTradeTableId(null);
        return theNew;
    }

    private static TradeItem relateCopyByReturnQty(TradeItem theRef, String newParentUuid) {
        TradeItem theNew = relateCopy(theRef, newParentUuid);
        theNew.setCreatorId(theRef.getCreatorId());
        theNew.setCreatorName(theRef.getCreatorName());
        theNew.setGuestPrinted(GuestPrinted.UNPRINT);
        theNew.setIssueStatus(null);
        theNew.setBatchNo(null);
        theNew.setChanged(true);
        return theNew;
    }

    /**
     * 根据theRef中的信息新建一个TradeItem对象
     *
     * @param theRef        被克隆的对象
     * @param newParentUuid 新TradeItem对象的parentUuid
     * @return
     */
    private static TradeItem relateCopy(TradeItem theRef, String newParentUuid) {
        TradeItem theNew = new TradeItem();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.validateCreate();
            theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setParentId(null);
            theNew.setParentUuid(newParentUuid);
            theNew.setStatusFlag(StatusFlag.VALID);
            theNew.setInvalidType(null);
            theNew.setRelateTradeItemId(theRef.getId());
            theNew.setRelateTradeItemUuid(theRef.getUuid());
            theNew.setServerCreateTime(null);
            theNew.setServerUpdateTime(null);
            theNew.validateCreate();
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 根据theRef中的信息新建一个TradeItemProperty对象
     *
     * @param theRef           参考对象
     * @param newTradeItemUuid 新对象的tradeItemUuid
     * @return
     */
    private static TradeItemProperty newEntity(TradeItemProperty theRef, String newTradeItemUuid) {
        TradeItemProperty newProperty = new TradeItemProperty();
        try {
            Beans.copyProperties(theRef, newProperty);
            newProperty.setId(null);
            newProperty.setUuid(uuid());
            newProperty.setTradeItemId(null);
            newProperty.setTradeItemUuid(newTradeItemUuid);
            newProperty.setServerCreateTime(null);
            newProperty.setServerUpdateTime(null);
            newProperty.validateCreate();
            return newProperty;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 根据theRef中的信息新建一个TradePrivilege对象
     *
     * @param theRef           参考对象
     * @param newTradeItemUuid 新对象的tradeItemUuid
     * @return
     */
    private static TradePrivilege newEntity(TradePrivilege theRef, String newTradeItemUuid) {
        TradePrivilege newPrivilege = new TradePrivilege();
        try {
            Beans.copyProperties(theRef, newPrivilege);
            newPrivilege.setId(null);
            newPrivilege.setUuid(uuid());
            newPrivilege.setTradeItemId(null);
            newPrivilege.setTradeItemUuid(newTradeItemUuid);
            newPrivilege.setTradeId(null);
            newPrivilege.setTradeUuid(null);
            newPrivilege.setServerCreateTime(null);
            newPrivilege.setServerUpdateTime(null);
            newPrivilege.validateCreate();
            return newPrivilege;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 拷贝菜品操作记录
     *
     * @Title: newEntity
     * @Param @param theRef
     * @Param @param newTradeItemUuid
     * @Return TradeItemOperation 返回类型
     */
    private static TradeItemOperation newEntity(TradeItemOperation theRef, String newTradeItemUuid) {
        TradeItemOperation tradeItemOperation = new TradeItemOperation();
        try {
            Beans.copyProperties(theRef, tradeItemOperation);
            tradeItemOperation.setId(null);
            tradeItemOperation.setTradeItemId(null);
            tradeItemOperation.setPrintOperationId(null);
            tradeItemOperation.setPrintStatus(PrintStatus.UNPRINT);
            tradeItemOperation.setTradeItemId(null);
            tradeItemOperation.setTradeItemUuid(newTradeItemUuid);
            tradeItemOperation.setServerCreateTime(null);
            tradeItemOperation.setServerUpdateTime(null);
            tradeItemOperation.validateCreate();
            return tradeItemOperation;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 根据theRef中的信息新建一个tradeReasonRel对象
     *
     * @param theRef           参考对象
     * @param newTradeItemUuid 新对象的tradeUuid
     * @return
     */
    private static TradeReasonRel newEntity(TradeReasonRel theRef, String newTradeItemUuid) {
        TradeReasonRel theNew = new TradeReasonRel();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setRelateId(null);
            theNew.setRelateUuid(newTradeItemUuid);
            theNew.setServerCreateTime(null);
            theNew.setServerUpdateTime(null);
            theNew.validateCreate();
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 根据theRef中的信息新建一个tradeItemExtraDinner对象
     *
     * @param theRef           参考对象
     * @param newTradeItemUuid 新对象的tradeUuid
     * @return
     */
    private static TradeItemExtraDinner newEntity(TradeItemExtraDinner theRef, String newTradeItemUuid) {
        TradeItemExtraDinner theNew = new TradeItemExtraDinner();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.setId(null);
            theNew.setTradeItemId(null);
            theNew.setTradeItemUuid(newTradeItemUuid);
            theNew.validateCreate();
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }


    /**
     * 根据theRef中的信息新建一个TradeItemExtra对象
     *
     * @param theRef           参考对象
     * @param newTradeItemUuid 新对象的tradeUuid
     * @return
     */
    private static TradeItemExtra newEntity(TradeItemExtra theRef, String newTradeItemUuid) {
        TradeItemExtra theNew = new TradeItemExtra();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setServerCreateTime(null);
            theNew.setServerUpdateTime(null);
            theNew.setClientUpdateTime(System.currentTimeMillis());
            theNew.setTradeItemId(null);
            theNew.setTradeItemUuid(newTradeItemUuid);
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 根据theRef中的信息新建一个TradeTable对象
     *
     * @param theRef       参考对象
     * @param newTradeUuid 新对象的tradeUuid
     * @return
     */
    private static TradeTable newEntity(TradeTable theRef, String newTradeUuid) {
        TradeTable theNew = new TradeTable();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setTradeId(null);
            theNew.setTradeUuid(newTradeUuid);
            theNew.setServerCreateTime(null);
            theNew.setServerUpdateTime(null);
            theNew.validateCreate();
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 将桌台信息加到拆单操作新创建的TradeVo中
     *
     * @param tradeVo
     * @param originTreadTables
     * @return
     */
    public static TradeVo completeTradeTableOfSplit(TradeVo tradeVo, List<TradeTable> originTreadTables) {
        if (!Utils.isEmpty(tradeVo.getTradeTableList())) {
            return tradeVo;
        }
        Map<String, TradeTable> finder = new HashMap<String, TradeTable>();
        for (TradeTable tradeTable : originTreadTables) {
            finder.put(tradeTable.getUuid(), tradeTable);
            Log.i(TAG, "refTradeTable.uuid=" + tradeTable.getUuid());
        }
        Map<String, TradeTable> tradeTables = new HashMap<String, TradeTable>();// 记录原桌台uuid与新桌台的对应关系
        String tradeUuid = tradeVo.getTrade().getUuid();
        for (TradeItemVo tradeItemVo : tradeVo.getTradeItemList()) {
            TradeItem tradeItem = tradeItemVo.getTradeItem();
            String refTradeTableUuid = tradeItem.getTradeTableUuid();
            TradeTable theNew = tradeTables.get(refTradeTableUuid);
            if (theNew == null) {
                Log.i(TAG, "tradeItem: uuid=" + tradeItem.getUuid() + ", tradeTableUuid=" + refTradeTableUuid);
                TradeTable theRef = finder.get(refTradeTableUuid);
                theNew = newEntity(theRef, tradeUuid);
//				Log.i(TAG, "newTradeTable: uuid=" + theNew.getUuid());
                theNew.setTablePeopleCount(0);// 拆单中的人数为0
                tradeTables.put(refTradeTableUuid, theNew);
            }
            tradeItem.setTradeTableUuid(theNew.getUuid());
        }
        tradeVo.getTrade().setTradePeopleCount(0);
        tradeVo.setTradeTableList(new ArrayList<TradeTable>(tradeTables.values()));
        return tradeVo;
    }

    /**
     * 清除从原单克隆出的tradeVo中所有条目的被拆单状态
     *
     * @Title: cancelSplitMess
     * @Description: 移除原单中的拆单状态
     * @Param @param tradeVo
     * @Return void 返回类型
     */
    public static void clearAllSplitStatus(TradeVo tradeVo) {
        List<TradeItemVo> itemVoList = tradeVo.getTradeItemList();
        if (itemVoList != null) {
            for (TradeItemVo itemVo : itemVoList) {
                TradeItem tradeItem = itemVo.getTradeItem();
                if (tradeItem.isChanged() && tradeItem.getInvalidType() == InvalidType.SPLIT) {
                    tradeItem.setStatusFlag(StatusFlag.VALID);
                    tradeItem.setInvalidType(null);
                }
            }
        }
    }

    public static ShopcartItem createShopcartItem(DishVo dishVo, BigDecimal qty) {
        OrderDish orderDish = dishVo.toOrderDish();
        ShopcartItem shopcartItem = new ShopcartItem(uuid(), orderDish);
        // 设置菜品数量
        shopcartItem.changeQty(qty);
        // shopcartItem.setTotalQty(increaseUnit);
        // 设置规格属性
        List<OrderProperty> properties = new ArrayList<OrderProperty>();
        Set<DishProperty> standards = orderDish.getStandards();
        if (standards != null) {
            for (DishProperty dishProperty : standards) {
                Long propertyTypeId = dishProperty.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                properties.add(new OrderProperty(propertyType, dishProperty));
            }
        }
        shopcartItem.setProperties(properties);
        return shopcartItem;
    }

    /**
     * 转成套餐明细条目列表（针对于改菜）
     *
     * @param newParent
     * @param refParent
     * @return
     */
    static void createSetmealShopcartItemsMD(ShopcartItem newParent, ReadonlyShopcartItem refParent) {
        List<ReadonlySetmealShopcartItem> readonlySetmealItems = refParent.getSetmealItems();
        if (Utils.isNotEmpty(readonlySetmealItems)) {
            List<SetmealShopcartItem> setmealItemList = new ArrayList<>();
            for (ReadonlySetmealShopcartItem setmealRef : readonlySetmealItems) {
                TradeItem tradeItem = setmealRef.tradeItem;
                OrderSetmeal orderSetmeal = toOrderSetmeal(newParent, tradeItem);
                if (orderSetmeal == null) {
                    continue;
                }
                SetmealShopcartItem setmealNew = new SetmealShopcartItem(uuid(), orderSetmeal, newParent);
                if (setmealNew == null) {
                    continue;
                }
                setmealNew.setShopcartItemType(setmealRef.getShopcartItemType());
                setmealNew.creatorId = setmealRef.tradeItem.getCreatorId();
                setmealNew.creatorName = setmealRef.tradeItem.getCreatorName();
                completeSetmealItem(setmealNew, setmealRef, newParent, refParent);
                setmealNew.setIsGroupDish(setmealRef.isGroupDish());
                if (setmealRef.isGroupDish()) {
                    setmealNew.changeQty(setmealRef.getSigleDeskQuantity());
                }
                setmealRef.setupModifyDishState();
                if (tradeItem.getIsChangePrice() == Bool.YES) {
                    setmealNew.changePrice(tradeItem.getPrice());
                }
                setmealItemList.add(setmealNew);
            }

            newParent.setSetmealItems(setmealItemList);
        }
    }


    static <T extends ShopcartItemBase<?>> void completeSetmealItem(T setmealNew, ReadonlyShopcartItemBase setmealRef,
                                                                    ShopcartItemBase<?> newParent, ReadonlyShopcartItemBase refParent) {
        setmealNew.setRelateInfo(setmealRef.getId(), setmealRef.getUuid());
        setmealNew.changePrice(setmealRef.getPrice());
        TradeItem tradeItem = setmealRef.tradeItem;

        BigDecimal parentQty = null;//为null时不需要计算加料数量
        BigDecimal totalQty = tradeItem.getQuantity();
        if (newParent != null && newParent.getSaleType() != SaleType.WEIGHING) {
            try {
                BigDecimal singleQty = BigDecimal.ZERO;
                if (newParent.getTotalQty() != null && BigDecimal.ZERO.compareTo(newParent.getTotalQty()) != 0) {
                    singleQty = tradeItem.getQuantity().divide(newParent.getTotalQty(), 2);
                }
                setmealNew.getOrderDish().setQty(singleQty, totalQty);
                parentQty = newParent.getTotalQty();
            } catch (Exception e) {
                Log.e(TAG, "计算套餐明细单份数量出错：qty=" + tradeItem.getQuantity() + ", parentQty=" + newParent.getTotalQty(), e);
                return;
            }
        } else {
            setmealNew.getOrderDish().setQty(totalQty, totalQty);
        }
        if (setmealNew.getSaleType() != SaleType.WEIGHING) {
            parentQty = totalQty;
        }

        // 添加属性
        if (Utils.isNotEmpty(setmealRef.getProperties())) {
            List<OrderProperty> properties = completeSetmealProperties(setmealNew.getOrderDish(), setmealRef.getProperties());
            if (properties == null) {
                return;
            }
            setmealNew.setProperties(properties);
        }

        // 添加套餐明细的加料条目
        Collection<ReadonlyExtraShopcartItem> setmealExtraList = setmealRef.getExtraItems();
        if (Utils.isNotEmpty(setmealExtraList)) {
            List<ExtraShopcartItem> extraItems = completeSetmealExtraItems(setmealNew, setmealExtraList, parentQty);
            if (extraItems == null) {
                return;
            }
            setmealNew.setExtraItems(extraItems);
        }

        // 优惠信息。会员折扣不复制
        TradePrivilege refPrivilege = setmealRef.getPrivilege();
        if (refPrivilege != null && refPrivilege.isValid()
                && refPrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT
                && refPrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE) {
            TradePrivilege newPrivilege = newEntity(refPrivilege, setmealNew.getUuid());
            setmealNew.setPrivilege(newPrivilege);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = setmealRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, setmealNew.getUuid());
            if (newReasonRel == null) {
                return;
            }
            setmealNew.setDiscountReasonRel(newReasonRel);
        }

        //礼品劵
        CouponPrivilegeVo couponPrivilegeVo = setmealRef.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null) {
            CouponPrivilegeVo newCouponPrivilegeVo = new CouponPrivilegeVo();
            try {
                Beans.copyProperties(couponPrivilegeVo, newCouponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    TradePrivilege newPrivilege = newEntity(couponPrivilegeVo.getTradePrivilege(), setmealNew.getUuid());
                    newCouponPrivilegeVo.setTradePrivilege(newPrivilege);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            setmealNew.setCouponPrivilegeVo(newCouponPrivilegeVo);
        }

        if (setmealRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(setmealRef.getTradeItemExtraDinner(), setmealNew.getUuid());
            setmealNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }

        setmealNew.setMemo(setmealRef.getMemo());
    }

    /**
     * 转为加料条目列表
     *
     * @param newParent
     * @param readonlyExtraItems
     * @param parentQty
     * @return
     */
    static List<ExtraShopcartItem> completeSetmealExtraItems(ShopcartItemBase<?> newParent, Collection<ReadonlyExtraShopcartItem> readonlyExtraItems,
                                                             BigDecimal parentQty) {
        if (readonlyExtraItems == null || readonlyExtraItems.isEmpty()) {
            return null;
        }
        List<ExtraShopcartItem> newExtraItemList = new ArrayList<ExtraShopcartItem>();
        for (ReadonlyExtraShopcartItem readonlyExtraItem : readonlyExtraItems) {
            DishShop setmealDishShop = DishCache.getExtraHolder().get(readonlyExtraItem.getSkuUuid());
            if (setmealDishShop == null) {
                return null;
            }
            Long dishId = newParent.getOrderDish().getBrandDishId();
            Long childDishId = setmealDishShop.getBrandDishId();
            DishSetmeal dishSetmeal = DishCache.getDishExtraHolder().get(dishId, childDishId);
//			if (dishSetmeal == null) {
//				// 获取加料对应的公共DishSetmeal
//				dishSetmeal = DishCache.getDishExtraHolder().get(childDishId);
//				if(dishSetmeal == null) {
//					continue;
//				}
//			}
            OrderExtra orderExtra = new OrderExtra(setmealDishShop, dishSetmeal);
            if (parentQty != null) {
                try {
                    orderExtra.setQty(readonlyExtraItem.getSingleQty(), readonlyExtraItem.getTotalQty());
                } catch (Exception e) {
                    Log.e(TAG, "计算加料份数量出错：qty=" + readonlyExtraItem.getTotalQty() + ", parentQty=" + parentQty, e);
                    continue;
                }
            } else {
                orderExtra.setQty(readonlyExtraItem.getTotalQty(), readonlyExtraItem.getTotalQty());
            }
            ExtraShopcartItem newExtraItem = new ExtraShopcartItem(uuid(), orderExtra, newParent);
            newExtraItem.setRelateInfo(readonlyExtraItem.getId(), readonlyExtraItem.getUuid());
            newExtraItem.setIsGroupDish(readonlyExtraItem.isGroupDish());
            newExtraItem.setShopcartItemType(readonlyExtraItem.getShopcartItemType());
            newExtraItemList.add(newExtraItem);
            readonlyExtraItem.setupModifyDishState();
        }
        return newExtraItemList;
    }

    /**
     * 转为属性列表
     *
     * @param orderDish
     * @param readonlyProperties
     * @return
     */
    static List<OrderProperty> completeSetmealProperties(OrderDish orderDish, List<ReadonlyOrderProperty> readonlyProperties) {
        List<OrderProperty> resultList = new ArrayList<OrderProperty>();
        // 添加规格类属性。根据一个商品就可以找出其规格列表，所以就算规格有变化也不影响取挂单
        for (DishProperty property : orderDish.getStandards()) {
            Long propertyTypeId = property.getPropertyTypeId();
            DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
            if (propertyType == null) {
                continue;
            }
            resultList.add(new OrderProperty(propertyType, property));
        }
        // 判断非规格类属性
        for (ReadonlyOrderProperty readonlyProperty : readonlyProperties) {
            if (readonlyProperty.getPropertyKind() != PropertyKind.STANDARD) {
                String propertyUuid = readonlyProperty.getPropertyUuid();
                DishProperty property = DishCache.getPropertyHolder().get(propertyUuid);
                if (property == null) {
                    continue;
                }
                Long propertyTypeId = property.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                if (propertyType == null) {
                    continue;
                }
//				DishBrandProperty dishProperty = DishCache.getDishPropertyHolder().get(orderDish.getBrandDishId(), property.getId());
//				if (dishProperty == null) {
//					continue;
//				}
                resultList.add(new OrderProperty(propertyType, property));
            }

            //原菜所有属性置为无效
            readonlyProperty.tradeItemProperty.setStatusFlag(StatusFlag.INVALID);
            readonlyProperty.tradeItemProperty.validateUpdate();
        }
        return resultList;
    }

    public static TradeUser copyTradeUser(TradeUser parent) {
        TradeUser tradeUser = null;
        if (parent != null) {
            tradeUser = new TradeUser();
            tradeUser.setUserId(parent.getUserId());
            tradeUser.setUserName(parent.getUserName());
            tradeUser.setType(parent.getType());
            tradeUser.setTradeId(parent.getTradeId());
            tradeUser.setType(parent.getType());
            tradeUser.setRoleId(parent.getRoleId());
            tradeUser.setRoleName(parent.getRoleName());
            tradeUser.setTradeItemId(parent.getTradeItemId());
            tradeUser.setTradeItemUuid(parent.getTradeItemUuid());
            tradeUser.setTradeUuid(parent.getTradeUuid());
            tradeUser.setChanged(true);
        }
        return tradeUser;
    }

    /**
     * 获取展示数量
     *
     * @param shopcartItem
     * @param deskCount
     * @return
     */
    public static BigDecimal getDisplyQty(IShopcartItem shopcartItem, BigDecimal deskCount) {
        if (shopcartItem.isGroupDish() && shopcartItem instanceof ReadonlyShopcartItem && shopcartItem.getStatusFlag() == StatusFlag.VALID) {
            return MathDecimal.div(shopcartItem.getSingleQty(), deskCount);
        }
        return shopcartItem.getSingleQty();
    }

    /**
     * 获取团餐区别显示的数量
     *
     * @param shopcartItem
     * @return
     */
    public static BigDecimal getDisplyQty(IShopcartItemBase shopcartItem, BigDecimal deskCount) {
        if (shopcartItem.isGroupDish() && (shopcartItem instanceof ReadonlyShopcartItem || shopcartItem instanceof ReadonlyExtraShopcartItem) && (shopcartItem.getStatusFlag() == StatusFlag.VALID)) {
            return MathDecimal.div(shopcartItem.getSingleQty(), deskCount);
        }
        return shopcartItem.getSingleQty();
    }

/*****************************************************************************************/
    /**
     * 菜品复制，生成相同属性的菜品,针对新菜
     *
     * @param theRef
     * @return
     */


    public static ShopcartItem shopcartItemCopy(ShopcartItem theRef) {
        String skuUuid = theRef.getSkuUuid();
        DishShop dishShop = DishCache.getDishHolder().get(skuUuid);
        if (dishShop != null) {
            DishManager dishManager = new DishManager();
            List<DishVo> dishVos = dishManager.getDishsVo(Arrays.asList(dishShop));
            if (Utils.isNotEmpty(dishVos)) {
                ShopcartItem theNew = createShopcartItem(dishVos.get(0), theRef.getSingleQty());
                theNew.setIsGroupDish(theRef.isGroupDish());
                //临时菜改名后名字变化
                theNew.changeName(theRef.getSkuName());
                completeByMd(theRef, theNew);
                if (theRef.hasSetmeal()) {
                    createSetmealShopcartItemsMD(theNew, theRef);
                }
                return theNew;
            }
        }

        return null;
    }

    public static ReadonlyShopcartItem shopcartItemCopy(ReadonlyShopcartItem theRef) {
        TradeItem newTradeItem = tradeItemCopy(theRef.tradeItem, theRef.getParentUuid());
        ReadonlyShopcartItem theNew = new ReadonlyShopcartItem(newTradeItem);
        theNew.setIsGroupDish(theRef.isGroupDish());
        theNew.setShopcartItemType(theRef.getShopcartItemType());
        // 复制加料
        if (theRef.hasExtra()) {
            BigDecimal feedsAmount = BigDecimal.ZERO;
            List<ReadonlyExtraShopcartItem> newExtraItems = new ArrayList<ReadonlyExtraShopcartItem>();
            for (ReadonlyExtraShopcartItem extraRef : theRef.getExtraItems()) {
                if (extraRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                TradeItem tradeItem = tradeItemCopy(extraRef.tradeItem, theNew.getUuid());
                ReadonlyExtraShopcartItem extraNew = new ReadonlyExtraShopcartItem(tradeItem, theNew);
                extraNew.setShopcartItemType(extraRef.getShopcartItemType());
                newExtraItems.add(extraNew);
                feedsAmount = MathDecimal.add(feedsAmount, tradeItem.getAmount());
            }
            theNew.setExtraItems(newExtraItems);
            theNew.tradeItem.setFeedsAmount(feedsAmount);
        }
        // 复制属性
        if (theRef.hasProperty()) {
            BigDecimal propertyAmount = BigDecimal.ZERO;
            List<ReadonlyOrderProperty> newProperties = new ArrayList<ReadonlyOrderProperty>();
            for (ReadonlyOrderProperty propertyRef : theRef.getProperties()) {
                if (propertyRef.tradeItemProperty.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                TradeItemProperty itemProperty = newEntity(propertyRef.tradeItemProperty, theNew.getUuid());
                newProperties.add(new ReadonlyOrderProperty(itemProperty));
                propertyAmount = MathDecimal.add(propertyAmount, itemProperty.getAmount());
            }
            theNew.setProperties(newProperties);
            theNew.tradeItem.setPropertyAmount(propertyAmount);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            theNew.setDiscountReasonRel(newReasonRel);
        }
        //拷贝菜品操作记录
        if (theRef instanceof IShopcartItem && Utils.isNotEmpty(theRef.getTradeItemOperations())) {
            List<TradeItemOperation> newOperations = new ArrayList<TradeItemOperation>();
            for (TradeItemOperation operationRef : theRef.getTradeItemOperations()) {
                if (operationRef.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                TradeItemOperation itemOperation = newEntity(operationRef, theNew.getUuid());
                newOperations.add(itemOperation);
            }
            if (Utils.isNotEmpty(newOperations) && theNew instanceof IShopcartItem) {
                theNew.setTradeItemOperations(newOperations);
            }
        }

        if (theRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(theRef.getTradeItemExtraDinner(), theNew.getUuid());
            theNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }

        //子菜
        if (theRef.hasSetmeal()) {
            List<ReadonlySetmealShopcartItem> setmealItems = new ArrayList<ReadonlySetmealShopcartItem>();
            for (ReadonlySetmealShopcartItem setmealRef : theRef.getSetmealItems()) {
                if (setmealRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                ReadonlySetmealShopcartItem itemNew = setmealShopcartItemCopy(setmealRef, theNew);
                itemNew.setIsGroupDish(setmealRef.isGroupDish());
                itemNew.setShopcartItemType(setmealRef.getShopcartItemType());
                setmealItems.add(itemNew);
            }
            theNew.setSetmealItems(setmealItems);
        }

        return theNew;
    }

    public static ReadonlySetmealShopcartItem setmealShopcartItemCopy(ReadonlySetmealShopcartItem theRef, ReadonlyShopcartItem newParent) {
        TradeItem newTradeItem = tradeItemCopy(theRef.tradeItem, newParent.getUuid());
        ReadonlySetmealShopcartItem theNew = new ReadonlySetmealShopcartItem(newTradeItem, newParent);
        theNew.setIsGroupDish(theRef.isGroupDish());
        theNew.setShopcartItemType(theRef.getShopcartItemType());
        // 复制加料
        if (theRef.hasExtra()) {
            BigDecimal feedsAmount = BigDecimal.ZERO;
            List<ReadonlyExtraShopcartItem> newExtraItems = new ArrayList<ReadonlyExtraShopcartItem>();
            for (ReadonlyExtraShopcartItem extraRef : theRef.getExtraItems()) {
                if (extraRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                TradeItem tradeItem = tradeItemCopy(extraRef.tradeItem, theNew.getUuid());
                ReadonlyExtraShopcartItem extraNew = new ReadonlyExtraShopcartItem(tradeItem, theNew);
                extraNew.setShopcartItemType(extraRef.getShopcartItemType());
                newExtraItems.add(extraNew);
                feedsAmount = MathDecimal.add(feedsAmount, tradeItem.getAmount());
            }
            theNew.setExtraItems(newExtraItems);
            theNew.tradeItem.setFeedsAmount(feedsAmount);
        }
        // 复制属性
        if (theRef.hasProperty()) {
            BigDecimal propertyAmount = BigDecimal.ZERO;
            List<ReadonlyOrderProperty> newProperties = new ArrayList<ReadonlyOrderProperty>();
            for (ReadonlyOrderProperty propertyRef : theRef.getProperties()) {
                if (propertyRef.tradeItemProperty.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }
                TradeItemProperty itemProperty = newEntity(propertyRef.tradeItemProperty, theNew.getUuid());
                newProperties.add(new ReadonlyOrderProperty(itemProperty));
                propertyAmount = MathDecimal.add(propertyAmount, itemProperty.getAmount());
            }
            theNew.setProperties(newProperties);
            theNew.tradeItem.setPropertyAmount(propertyAmount);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            theNew.setDiscountReasonRel(newReasonRel);
        }
        //拷贝菜品操作记录
        if (theRef instanceof IShopcartItem && Utils.isNotEmpty(theRef.getTradeItemOperations())) {
            List<TradeItemOperation> newOperations = new ArrayList<TradeItemOperation>();
            for (TradeItemOperation operationRef : theRef.getTradeItemOperations()) {
                if (operationRef.getStatusFlag() == StatusFlag.INVALID) {
                    continue;
                }
                TradeItemOperation itemOperation = newEntity(operationRef, theNew.getUuid());
                newOperations.add(itemOperation);
            }
            if (Utils.isNotEmpty(newOperations) && theNew instanceof IShopcartItem) {
                theNew.setTradeItemOperations(newOperations);
            }
        }

        if (theRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(theRef.getTradeItemExtraDinner(), theNew.getUuid());
            theNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }

        return theNew;
    }

    public static TradeItem tradeItemCopy(TradeItem theRef, String newParentUuid) {
        TradeItem theNew = new TradeItem();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setParentId(null);
            theNew.setParentUuid(newParentUuid);
            theNew.setStatusFlag(StatusFlag.VALID);
            theNew.setInvalidType(null);
            theNew.setRelateTradeItemId(null);
            theNew.setRelateTradeItemUuid(null);
            theNew.setServerCreateTime(null);
            theNew.setServerUpdateTime(null);
            theNew.validateCreate();
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }

        return null;
    }

    private static void completeByMd(ShopcartItem theRef, ShopcartItem theNew) {
//		theNew.setRelateInfo(theRef.getId(), theRef.getUuid());
        theNew.changePrice(theRef.getPrice());
        // 复制加料
        if (theRef.hasExtra()) {
            List<ExtraShopcartItem> newExtraItems = new ArrayList<ExtraShopcartItem>();
            for (ExtraShopcartItem itemRef : theRef.getExtraItems()) {
                if (itemRef.getStatusFlag() != StatusFlag.VALID) {
                    continue;
                }

                String extraSkuUuid = itemRef.getSkuUuid();
                DishShop setmealDishShop = DishCache.getExtraHolder().get(extraSkuUuid);
                if (setmealDishShop == null) {
                    continue;
                }
                Long dishId = theNew.getOrderDish().getBrandDishId();
                Long childDishId = setmealDishShop.getBrandDishId();
                DishSetmeal dishSetmeal = DishCache.getDishExtraHolder().get(dishId, childDishId);

                OrderExtra orderExtra = new OrderExtra(setmealDishShop, dishSetmeal);
                BigDecimal parentQty = theNew.getTotalQty();
                if (parentQty != null) {
                    try {
                        //?
                        orderExtra.setQty(itemRef.getSingleQty(), itemRef.getTotalQty());
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    orderExtra.setQty(itemRef.getSingleQty(), itemRef.getTotalQty());
                }
                ExtraShopcartItem newExtraItem = new ExtraShopcartItem(uuid(), orderExtra, theNew);
                //称重商品存的实际数量
                newExtraItem.setIsGroupDish(theRef.isGroupDish());
//				newExtraItem.setRelateInfo(itemRef.getId(), itemRef.getUuid());
                newExtraItems.add(newExtraItem);
            }
            theNew.setExtraItems(newExtraItems);
        }
        // 复制属性
        if (theRef.hasProperty()) {
            List<OrderProperty> newProperties = new ArrayList<>();
            OrderDish orderDish = theNew.getOrderDish();
            // 添加规格类属性。根据一个商品就可以找出其规格列表
            for (DishProperty property : orderDish.getStandards()) {
                Long propertyTypeId = property.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                if (propertyType == null) {
                    continue;
                }
                newProperties.add(new OrderProperty(propertyType, property));
            }

            // 判断非规格类属性
            for (OrderProperty propertyRef : theRef.getProperties()) {
                if (propertyRef.getPropertyKind() != PropertyKind.STANDARD) {
                    String propertyUuid = propertyRef.getPropertyUuid();
                    DishProperty property = DishCache.getPropertyHolder().get(propertyUuid);
                    if (property == null) {
                        continue;
                    }
                    Long propertyTypeId = property.getPropertyTypeId();
                    DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                    if (propertyType == null) {
                        continue;
                    }

                    newProperties.add(new OrderProperty(propertyType, property));
                }

            }
            theNew.setProperties(newProperties);
        }

    }


    /**
     * 转成套餐明细条目列表
     *
     * @param newParent
     * @param refParent
     * @return
     */
    static void createSetmealShopcartItemsMD(ShopcartItem newParent, ShopcartItem refParent) {
        List<SetmealShopcartItem> readonlySetmealItems = refParent.getSetmealItems();
        if (Utils.isNotEmpty(readonlySetmealItems)) {
            List<SetmealShopcartItem> setmealItemList = new ArrayList<>();
            for (SetmealShopcartItem setmealRef : readonlySetmealItems) {
                OrderSetmeal orderSetmeal = toOrderSetmeal(newParent, setmealRef);
                if (orderSetmeal == null) {
                    continue;
                }
                SetmealShopcartItem setmealNew = new SetmealShopcartItem(uuid(), orderSetmeal, newParent);
                if (setmealNew == null) {
                    continue;
                }
                completeNewSetmealItem(setmealNew, setmealRef, newParent, refParent);
                setmealNew.setIsGroupDish(setmealRef.isGroupDish());
                if (refParent.getIsChangePrice() == Bool.YES) {
                    setmealNew.changePrice(refParent.getPrice());
                }
                setmealItemList.add(setmealNew);
            }

            newParent.setSetmealItems(setmealItemList);
        }
    }

    static <T extends ShopcartItemBase<?>> void completeNewSetmealItem(T setmealNew, SetmealShopcartItem setmealRef,
                                                                       ShopcartItemBase<?> newParent, ShopcartItemBase refParent) {
//		setmealNew.setRelateInfo(setmealRef.getId(), setmealRef.getUuid());
        setmealNew.changePrice(setmealRef.getPrice());

        BigDecimal parentQty = null;//为null时不需要计算加料数量
        BigDecimal totalQty = setmealRef.getTotalQty();
        if (newParent != null && newParent.getSaleType() != SaleType.WEIGHING) {
            try {
                BigDecimal singleQty = BigDecimal.ZERO;
                if (newParent.getTotalQty() != null && BigDecimal.ZERO.compareTo(newParent.getTotalQty()) != 0) {
                    singleQty = setmealRef.getTotalQty().divide(newParent.getTotalQty(), 2);
                }
                setmealNew.getOrderDish().setQty(singleQty, totalQty);
                parentQty = newParent.getTotalQty();
            } catch (Exception e) {
                Log.e(TAG, "计算套餐明细单份数量出错：qty=" + setmealRef.getTotalQty() + ", parentQty=" + newParent.getTotalQty(), e);
                return;
            }
        } else {
            setmealNew.getOrderDish().setQty(totalQty, totalQty);
        }
        if (setmealNew.getSaleType() != SaleType.WEIGHING) {
            parentQty = totalQty;
        }

        // 添加属性
        if (Utils.isNotEmpty(setmealRef.getProperties())) {
            List<OrderProperty> properties = completeNewSetmealProperties(setmealNew.getOrderDish(), setmealRef.getProperties());
            if (properties == null) {
                return;
            }
            setmealNew.setProperties(properties);
        }

        // 添加套餐明细的加料条目
        Collection<ExtraShopcartItem> setmealExtraList = setmealRef.getExtraItems();
        if (Utils.isNotEmpty(setmealExtraList)) {
            List<ExtraShopcartItem> extraItems = completeNewSetmealExtraItems(setmealNew, setmealExtraList, parentQty);
            if (extraItems == null) {
                return;
            }
            setmealNew.setExtraItems(extraItems);
        }

        // 优惠信息。会员折扣不复制
        TradePrivilege refPrivilege = setmealRef.getPrivilege();
        if (refPrivilege != null && refPrivilege.isValid()
                && refPrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT
                && refPrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE) {
            TradePrivilege newPrivilege = newEntity(refPrivilege, setmealNew.getUuid());
            setmealNew.setPrivilege(newPrivilege);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = setmealRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, setmealNew.getUuid());
            if (newReasonRel == null) {
                return;
            }
            setmealNew.setDiscountReasonRel(newReasonRel);
        }

        //礼品劵
        CouponPrivilegeVo couponPrivilegeVo = setmealRef.getCouponPrivilegeVo();
        if (couponPrivilegeVo != null) {
            CouponPrivilegeVo newCouponPrivilegeVo = new CouponPrivilegeVo();
            try {
                Beans.copyProperties(couponPrivilegeVo, newCouponPrivilegeVo);
                if (couponPrivilegeVo.getTradePrivilege() != null) {
                    TradePrivilege newPrivilege = newEntity(couponPrivilegeVo.getTradePrivilege(), setmealNew.getUuid());
                    newCouponPrivilegeVo.setTradePrivilege(newPrivilege);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            setmealNew.setCouponPrivilegeVo(newCouponPrivilegeVo);
        }

        if (setmealRef.getTradeItemExtraDinner() != null) {
            TradeItemExtraDinner tradeItemExtraDinner = newEntity(setmealRef.getTradeItemExtraDinner(), setmealNew.getUuid());
            setmealNew.setTradeItemExtraDinner(tradeItemExtraDinner);
        }

        setmealNew.setMemo(setmealRef.getMemo());
    }


    /**
     * 转为属性列表
     *
     * @param orderDish
     * @param readonlyProperties
     * @return
     */
    static List<OrderProperty> completeNewSetmealProperties(OrderDish orderDish, List<OrderProperty> readonlyProperties) {
        List<OrderProperty> resultList = new ArrayList<OrderProperty>();
        // 添加规格类属性。根据一个商品就可以找出其规格列表，所以就算规格有变化也不影响取挂单
        for (DishProperty property : orderDish.getStandards()) {
            Long propertyTypeId = property.getPropertyTypeId();
            DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
            if (propertyType == null) {
                continue;
            }
            resultList.add(new OrderProperty(propertyType, property));
        }
        // 判断非规格类属性
        for (OrderProperty readonlyProperty : readonlyProperties) {
            if (readonlyProperty.getPropertyKind() != PropertyKind.STANDARD) {
                String propertyUuid = readonlyProperty.getPropertyUuid();
                DishProperty property = DishCache.getPropertyHolder().get(propertyUuid);
                if (property == null) {
                    continue;
                }
                Long propertyTypeId = property.getPropertyTypeId();
                DishPropertyType propertyType = DishCache.getPropertyTypeHolder().get(propertyTypeId);
                if (propertyType == null) {
                    continue;
                }

                resultList.add(new OrderProperty(propertyType, property));
            }

        }
        return resultList;
    }

    static List<ExtraShopcartItem> completeNewSetmealExtraItems(ShopcartItemBase<?> newParent, Collection<ExtraShopcartItem> readonlyExtraItems,
                                                                BigDecimal parentQty) {
        if (readonlyExtraItems == null || readonlyExtraItems.isEmpty()) {
            return null;
        }
        List<ExtraShopcartItem> newExtraItemList = new ArrayList<ExtraShopcartItem>();
        for (ExtraShopcartItem readonlyExtraItem : readonlyExtraItems) {
            DishShop setmealDishShop = DishCache.getExtraHolder().get(readonlyExtraItem.getSkuUuid());
            if (setmealDishShop == null) {
                return null;
            }
            Long dishId = newParent.getOrderDish().getBrandDishId();
            Long childDishId = setmealDishShop.getBrandDishId();
            DishSetmeal dishSetmeal = DishCache.getDishExtraHolder().get(dishId, childDishId);

            OrderExtra orderExtra = new OrderExtra(setmealDishShop, dishSetmeal);
            if (parentQty != null) {
                try {
                    orderExtra.setQty(readonlyExtraItem.getSingleQty(), readonlyExtraItem.getTotalQty());
                } catch (Exception e) {
                    Log.e(TAG, "计算加料份数量出错：qty=" + readonlyExtraItem.getTotalQty() + ", parentQty=" + parentQty, e);
                    continue;
                }
            } else {
                orderExtra.setQty(readonlyExtraItem.getTotalQty(), readonlyExtraItem.getTotalQty());
            }
            ExtraShopcartItem newExtraItem = new ExtraShopcartItem(uuid(), orderExtra, newParent);
            newExtraItem.setRelateInfo(readonlyExtraItem.getId(), readonlyExtraItem.getUuid());
            newExtraItem.setIsGroupDish(readonlyExtraItem.isGroupDish());
            newExtraItemList.add(newExtraItem);
        }
        return newExtraItemList;
    }

    static OrderSetmeal toOrderSetmeal(ShopcartItemBase<?> parent, TradeItem tradeItem) {
        return toOrderSetmeal(tradeItem.getSkuUuid(), parent, tradeItem.getDishSetmealGroupId());
    }

    static OrderSetmeal toOrderSetmeal(ShopcartItemBase<?> parent, SetmealShopcartItem setmealRef) {
        return toOrderSetmeal(setmealRef.getSkuUuid(), parent, setmealRef.getSetmealGroupId());
    }

    static OrderSetmeal toOrderSetmeal(String skuUuid, ShopcartItemBase<?> parent, Long setmealGroupId) {
        DishShop dishShop = DishCache.getDishHolder().get(skuUuid);
        if (dishShop == null) {
            return null;
        }
        DishUnitDictionary unit = DishCache.getUnitHolder().get(dishShop.getUnitId());
        Long comboDishId = parent.getOrderDish().getBrandDishId();
        Long childDishId = dishShop.getBrandDishId();
        DishSetmeal dishSetmeal = DishCache.getSetmealHolder().get(comboDishId, childDishId, setmealGroupId);
        if (dishSetmeal == null) {
            return null;
        }
        Set<DishProperty> standards = DishManager.filterStandards(dishShop);
        OrderSetmeal orderSetmeal = new OrderSetmeal(dishShop, standards, dishSetmeal, unit);

        return orderSetmeal;
    }


    /***********************************联台批量菜在子单中拆菜***********************************************/
    public static void splitBatchItem(IShopcartItemBase shopcartItem) {
        if (shopcartItem instanceof ReadonlyShopcartItem) {
            splitBatchItem((ReadonlyShopcartItem) shopcartItem);
        }
    }

    public static void splitBatchItem(ReadonlyShopcartItem shopcartItem) {
        if (shopcartItem.getShopcartItemType() != ShopcartItemType.SUBBATCH) {
            return;
        }
        shopcartItem.setShopcartItemType(ShopcartItemType.SUBBATCHMODIFY);
        if (shopcartItem.getMainShopcartItem() == null) {
            return;
        }
        //有拆单操作，更改主单批量菜价格和数量
        BigDecimal mainItemQty = shopcartItem.getMainShopcartItem().getTotalQty();
        mainItemQty = mainItemQty.subtract(shopcartItem.getTotalQty());
        modifyReadonlyItemByQty(shopcartItem.getMainShopcartItem(), mainItemQty);
    }

    /**
     * 修改数量，并改变价格
     *
     * @param shopcartItem
     * @param newQuantity  将要修改成的数量
     */
    public static void modifyReadonlyItemByQty(ReadonlyShopcartItem shopcartItem, BigDecimal newQuantity) {
        BigDecimal parentQty = shopcartItem.getTotalQty();
        //数量计算要再验证?
        shopcartItem.modifyQty(newQuantity);
        modiyExtraItemByQty(shopcartItem.getExtraItems(), parentQty, newQuantity);
        shopcartItem.setChanged(true);
        if (shopcartItem.hasSetmeal()) {
            for (ReadonlySetmealShopcartItem setmealShopcartItem : shopcartItem.getSetmealItems()) {
                setmealShopcartItem.setShopcartItemType(shopcartItem.getShopcartItemType());
                BigDecimal newQty = setmealShopcartItem.getTotalQty().multiply(newQuantity).divide(parentQty);
                setmealShopcartItem.modifyQty(newQty);
                modiyExtraItemByQty(setmealShopcartItem.getExtraItems(), parentQty, newQuantity);
            }
        }
    }

    public static void modiyExtraItemByQty(Collection<ReadonlyExtraShopcartItem> readonlyExtraShopcartItemList, BigDecimal parentQty, BigDecimal newParentQty) {
        if (Utils.isEmpty(readonlyExtraShopcartItemList)) {
            return;
        }
        for (ReadonlyExtraShopcartItem readonlyExtraShopcartItem : readonlyExtraShopcartItemList) {
            BigDecimal newExtraQty = readonlyExtraShopcartItem.getTotalQty().multiply(newParentQty).divide(parentQty);
            readonlyExtraShopcartItem.modifyQty(newExtraQty);
        }
    }

    public static void modifyPropertyByQty(ReadonlyOrderProperty readonlyOrderProperty, BigDecimal newQuantity) {
        readonlyOrderProperty.tradeItemProperty.setQuantity(newQuantity);
        if (readonlyOrderProperty.tradeItemProperty.getPrice() == null) {
            return;
        }
        readonlyOrderProperty.tradeItemProperty.setAmount(newQuantity.multiply(readonlyOrderProperty.tradeItemProperty.getPrice()));
    }

    /**
     * 复制菜品
     *
     * @param ref
     * @param isNeedId 是否需要复制id
     */
    public static ReadonlyShopcartItem copyReadonlyShopcartItem(ReadonlyShopcartItem ref, boolean isNeedId) {
        ReadonlyShopcartItem newItem = null;
        if (ref.ensureCopy()) {
            TradeItem newTradeItem = relateCopyPropertites(ref.tradeItem, null, isNeedId);
            if (newTradeItem == null) {
                return null;
            }
            TradeReasonRel returnReasonRel = ref.getReturnQtyReason();
            TradeReasonRel newReasonRel = null;
            if (returnReasonRel != null && returnReasonRel.isValid()) {
                newReasonRel = newEntity(returnReasonRel, newTradeItem.getUuid());
            }
            newItem = completeCopy(ref, new ReadonlyShopcartItem(newTradeItem, newReasonRel), isNeedId);
            if (newItem == null) {
                return null;
            }
            if (ref.hasSetmeal()) {
                newItem.setSetmealItems(new ArrayList<ReadonlySetmealShopcartItem>());
                for (ReadonlySetmealShopcartItem refSetmealItem : ref.getSetmealItems()) {
                    ReadonlySetmealShopcartItem newSetmealItem = copySetmeal(refSetmealItem, newItem, isNeedId);
                    if (newSetmealItem == null) {
                        return null;
                    }
                    newItem.getSetmealItems().add(newSetmealItem);
                }
            }
        }
        return newItem;
    }


    private static void copyTradeItemPrivilege(IShopcartItemBase ref, IShopcartItemBase newItem) {
        // 优惠信息。会员折扣不复制
        TradePrivilege refPrivilege = ref.getPrivilege();
        if (refPrivilege != null && refPrivilege.isValid()
                && refPrivilege.getPrivilegeType() != PrivilegeType.AUTO_DISCOUNT
                && refPrivilege.getPrivilegeType() != PrivilegeType.MEMBER_PRICE) {
            TradePrivilege newPrivilege = newEntity(refPrivilege, ref.getUuid());
            newItem.setPrivilege(newPrivilege);
        }
        //复制折扣理由
        TradeReasonRel tradeReasonRel = ref.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, ref.getUuid());
            newItem.setDiscountReasonRel(newReasonRel);
        }
    }


    /**
     * 根据theRef中的信息新建一个TradeItem对象
     *
     * @param theRef        被克隆的对象
     * @param newParentUuid 新TradeItem对象的parentUuid
     * @param isNeedId      是否需要id
     * @return
     */
    private static TradeItem relateCopyPropertites(TradeItem theRef, String newParentUuid, boolean isNeedId) {
        TradeItem theNew = new TradeItem();
        try {
            Beans.copyProperties(theRef, theNew);
            theNew.validateCreate();
            if (!isNeedId)
                theNew.setId(null);
            theNew.setUuid(uuid());
            theNew.setParentId(null);
            theNew.setParentUuid(newParentUuid);
            theNew.setRelateTradeItemId(theRef.getId());
            theNew.setRelateTradeItemUuid(theRef.getUuid());
//			theNew.setServerCreateTime(null);
//			theNew.setServerUpdateTime(null);
            theNew.setTradeId(null);
            theNew.setTradeUuid(null);
            theNew.validateCreate();
            return theNew;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error! theRef=" + theRef, e);
        }
        return null;
    }

    /**
     * 对theRef条目中的加料、属性、优惠进行拆单操作，并将生成的新条目设置到theNew中
     *
     * @param theRef
     * @param theNew
     * @return
     */
    private static <T extends ReadonlyShopcartItemBase> T completeCopy(T theRef, T theNew, boolean isNeedId) {
        if (theRef.hasExtra()) {
            // 复制加料
            List<ReadonlyExtraShopcartItem> newExtraItems = new ArrayList<ReadonlyExtraShopcartItem>();
            for (ReadonlyExtraShopcartItem refExtraItem : theRef.getExtraItems()) {
                TradeItem newExtraTradeItem = relateCopyPropertites(refExtraItem.tradeItem, theNew.getUuid(), isNeedId);
                if (newExtraTradeItem == null) {
                    return null;
                }
                newExtraItems.add(new ReadonlyExtraShopcartItem(newExtraTradeItem, theNew));
            }
            theNew.setExtraItems(newExtraItems);
        }
        if (theRef.hasProperty()) {
            // 复制属性
            List<ReadonlyOrderProperty> newProperties = new ArrayList<ReadonlyOrderProperty>();
            for (ReadonlyOrderProperty refProperty : theRef.getProperties()) {
                TradeItemProperty newTradeItemProperty = newEntity(refProperty.tradeItemProperty, theNew.getUuid());
                if (newTradeItemProperty == null) {
                    return null;
                }
                newProperties.add(new ReadonlyOrderProperty(newTradeItemProperty));
            }
            theNew.setProperties(newProperties);
        }

        if (Utils.isNotEmpty(theRef.getTradeItemOperations())) {
            List<TradeItemOperation> tradeItemOperationList = new ArrayList<>();
            for (TradeItemOperation tradeItemOperation : theRef.getTradeItemOperations()) {
                TradeItemOperation newOperation = newEntity(tradeItemOperation, theNew.getUuid());
                tradeItemOperationList.add(newOperation);
            }
            theNew.setTradeItemOperations(tradeItemOperationList);
        }

        if (theRef.getTradeItemExtra() != null) {
            TradeItemExtra newTradeItemExtra = newEntity(theRef.getTradeItemExtra(), theNew.getUuid());
            theNew.setTradeItemExtra(newTradeItemExtra);
        }
        TradeReasonRel tradeReasonRel = theRef.getDiscountReasonRel();
        if (tradeReasonRel != null && tradeReasonRel.isValid()) {
            TradeReasonRel newReasonRel = newEntity(tradeReasonRel, theNew.getUuid());
            if (newReasonRel == null) {
                return null;
            }
            theNew.setDiscountReasonRel(newReasonRel);
        }

        copyTradeItemPrivilege(theRef, theNew);

        return theNew;
    }


    /**
     * 对指定的套餐明细条目进行拆单操作，返回新的套餐明细条目
     *
     * @param ref
     * @param newParent 新套餐明细的父条目
     * @return
     */
    private static ReadonlySetmealShopcartItem copySetmeal(ReadonlySetmealShopcartItem ref,
                                                           ReadonlyShopcartItem newParent, boolean isNeedId) {
        if (ref.ensureCopy()) {
            TradeItem newTradeItem = relateCopyPropertites(ref.tradeItem, newParent.getUuid(), isNeedId);
            if (newTradeItem == null) {
                Log.w(TAG, "splite failed! ref=" + ref);
                return null;
            }
            return completeCopy(ref, new ReadonlySetmealShopcartItem(newTradeItem, newParent), isNeedId);
        }
        Log.w(TAG, "splite failed! ref=" + ref);
        return null;
    }

}
