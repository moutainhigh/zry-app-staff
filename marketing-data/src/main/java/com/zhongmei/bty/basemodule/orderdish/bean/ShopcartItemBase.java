package com.zhongmei.bty.basemodule.orderdish.bean;

import android.util.Log;

import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @version: 1.0
 * @date 2015年7月10日
 */
public abstract class ShopcartItemBase<T extends OrderDish> implements IShopcartItemBase {

    private static final String TAG = ShopcartItemBase.class.getSimpleName();

    protected final String uuid;
    /**
     * 商品信息
     */
    public final T orderDish;
    /**
     *
     */
    public final ItemMetadata metadata;
    protected final ShopcartItemBase<?> parent;
    /**
     * 优惠信息
     */
    private TradePrivilege privilege;
    /**
     * 属性。做法、口味
     */
    private List<OrderProperty> properties;
    /**
     * 加料列表
     */
    private Map<String, ExtraShopcartItem> extraItemMap;
    /**
     * 此条目所属的桌台信息记录的UUID
     */
    private String tradeTableUuid;
    /**
     * 此条目所属的桌台信息记录的ID
     */
    private Long tradeTableId;
    /**
     * 有效(无效)状态
     */
    private StatusFlag statusFlag;
    /**
     * 导致无效的原因类型
     */
    private InvalidType invalidType;
    /**
     * 返回此条目的来源条目ID(即是由哪个条目进行拆单或修改而来的)
     */
    private Long relateTradeItemId;
    private String relateTradeItemUuid;
    /**
     *
     */
    private boolean selected;
    /**
     * 购物车菜品加入循序
     */
    private int index;

    /**
     * 是否打包
     */

    private boolean isPack = false;

    /**
     * 是否退回库存
     */
    private boolean isReturnInventory;

    /**
     * 优惠券信息
     */
    private CouponPrivilegeVo couponPrivilegeVo;
    /**
     * 团餐或者自助餐组点菜品
     */
    private boolean isGroupDish = false;

    /**
     * 座位号
     */
    private TradeItemExtraDinner tradeItemExtraDinner;

    /**
     * 催菜列表
     */
    private List<TradeItemOperation> tradeItemOperations;

    private ShopcartItemType shopcartItemType = ShopcartItemType.COMMON;

    //主单批量菜关联
    private List<TradeItemMainBatchRel> tradeItemMainBatchRelList;

    private List<TradeUser> tradeItemUserList;

    public Long creatorId;
    public String creatorName;
    //次卡项目优惠vo
    CardServicePrivilegeVo cardServicePrivilegeVo;
    //小程序优惠vo
    AppletPrivilegeVo appletPrivilegeVo;

    protected ShopcartItemBase(String uuid, T orderDish) {
        this(uuid, orderDish, null);
    }

    protected ShopcartItemBase(String uuid, T orderDish, ShopcartItemBase<?> parent) {
        this(uuid, orderDish, parent, new ItemMetadata());
    }

    protected ShopcartItemBase(String uuid, T orderDish, ShopcartItemBase<?> parent, ItemMetadata metadata) {
        this.uuid = uuid;
        this.orderDish = orderDish;
        this.parent = parent;
        this.metadata = (metadata == null ? new ItemMetadata() : metadata);
        statusFlag = StatusFlag.VALID;
        extraItemMap = new LinkedHashMap<String, ExtraShopcartItem>();
    }

    public T getOrderDish() {
        return orderDish;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public Long getBatchId() {
        return null;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public Long getServerUpdateTime() {
        return null;
    }

    @Override
    public String getParentUuid() {
        if (parent == null) {
            return null;
        }
        return parent.getUuid();
    }

    public ShopcartItemBase<?> getParent() {
        return parent;
    }

    @Override
    public DishShop getDishShop() {
        return orderDish.getDishShop();
    }

    @Override
    public Long getSkuId() {
        return orderDish.getSkuId();
    }

    @Override
    public String getSkuUuid() {
        return orderDish.getSkuUuid();
    }

    @Override
    public String getSkuName() {
        return orderDish.getSkuName();
    }

    @Override
    public BigDecimal getPrice() {
        return orderDish.getPrice();
    }

    @Override
    public Bool getIsChangePrice() {
        return orderDish.getIsChangePrice();
    }

    @Override
    public DishType getType() {
        return orderDish.getDishType();
    }

    @Override
    public String getUnitName() {
        return orderDish.getUnitName();
    }

    @Override
    public SaleType getSaleType() {
        return orderDish.getSaleType();
    }

    @Override
    public Bool getEnableWholePrivilege() {
        return orderDish.getDishShop().getIsDiscountAll();
    }

    @Override
    public String getMemo() {
        return metadata.memo;
    }

    @Override
    public InvalidType getInvalidType() {
        return invalidType;
    }

    @Override
    public StatusFlag getStatusFlag() {
        return statusFlag;
    }

    @Override
    public Long getRelateTradeItemId() {
        return relateTradeItemId;
    }

    @Override
    public String getRelateTradeItemUuid() {
        return relateTradeItemUuid;
    }

    @Override
    public boolean isChanged() {
        return true;
    }

    @Override
    public void setChanged(boolean changed) {
    }

    @Override
    public String getBatchNo() {
        return null;
    }

    @Override
    public String getTradeTableUuid() {
        return tradeTableUuid;
    }

    @Override
    public Long getTradeTableId() {
        return tradeTableId;
    }

    public void setTradeTable(TradeTable tradeTable) {
        this.tradeTableId = tradeTable.getId();
        this.tradeTableUuid = tradeTable.getUuid();
    }

    public void setMemo(String memo) {
        metadata.memo = memo;
    }

    /**
     * 修改商品的单价
     *
     * @param price
     */
    public void changePrice(BigDecimal price) {
        orderDish.changePrice(price);
    }

    /**
     * 修改商品名称
     *
     * @param name name
     */
    public void changeName(String name) {
        orderDish.setDefineName(name);
    }

    @Override
    public IssueStatus getIssueStatus() {
        return metadata.issueStatus;
    }

    @Override
    public void setIssueStatus(IssueStatus issueStatus) {
        metadata.issueStatus = issueStatus;
        //更新加料的打印状态
        if (Utils.isNotEmpty(getExtraItems())) {
            for (ExtraShopcartItem extraShopcartItem : getExtraItems()) {
                extraShopcartItem.setIssueStatus(issueStatus);
            }
        }
    }

    @Override
    public void setIssueStatusWithoutSetmeal(IssueStatus issueStatus) {
        metadata.issueStatus = issueStatus;
        //更新加料的打印状态
        if (Utils.isNotEmpty(getExtraItems())) {
            for (ExtraShopcartItem extraShopcartItem : getExtraItems()) {
                extraShopcartItem.setIssueStatus(issueStatus);
            }
        }
    }

    @Override
    public TradePrivilege getPrivilege() {
        return privilege;
    }

    @Override
    public void setPrivilege(TradePrivilege privilege) {
        this.privilege = privilege;
    }

    @Override
    public List<OrderProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<OrderProperty> properties) {
        this.properties = properties;
    }

    public boolean hasProperty() {
        return Utils.isNotEmpty(properties);
    }

    @Override
    public Collection<ExtraShopcartItem> getExtraItems() {
        return extraItemMap.values();
    }

    public void setExtraItems(Collection<ExtraShopcartItem> extraItems) {
        extraItemMap.clear();
        for (ExtraShopcartItem extraItem : extraItems) {
            extraItemMap.put(extraItem.getSkuUuid(), extraItem);
        }
    }

    public boolean hasExtra() {
        return !extraItemMap.isEmpty();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.selected = isSelected;
    }

    /**
     * 获取不受父条目影响的单份数量
     *
     * @return
     */
    @Override
    public BigDecimal getSingleQty() {
        return orderDish.getSingleQty();
    }

    /**
     * 获取受父条目影响的数量
     *
     * @return
     */
    @Override
    public BigDecimal getTotalQty() {
        return orderDish.getTotalQty();
    }

    @Override
    public BigDecimal getAmount() {
        return orderDish.getActualAmount();
    }

    @Override
    public BigDecimal getPropertyAmount() {
        BigDecimal value = BigDecimal.ZERO;
        if (properties != null) {
            for (IOrderProperty property : properties) {
                if (property.getPropertyPrice() != null) {
                    value = value.add(property.getPropertyPrice().multiply(getTotalQty()));
                }
            }
        }
        return value;
    }

    @Override
    public BigDecimal getFeedsAmount() {
        BigDecimal value = BigDecimal.ZERO;
        if (!extraItemMap.isEmpty()) {
            for (ExtraShopcartItem extraItem : extraItemMap.values()) {
                value = value.add(extraItem.getActualAmount());
            }
        }
        return value;
    }

    @Override
    public BigDecimal getActualAmount() {
        return getAmount().add(getPropertyAmount()).add(getFeedsAmount());
    }

    @Override
    public BigDecimal getReturnQty() {
        return null;
    }

    @Override
    public TradeReasonRel getReturnQtyReason() {
        return null;
    }

    /**
     * 设置加料数量
     *
     * @param extra
     * @param singleQty 为0时表示删除加料
     */
    public void setExtra(OrderExtra extra, BigDecimal singleQty) {
        String skuUuid = extra.getSkuUuid();
        if (singleQty == null || singleQty.compareTo(BigDecimal.ZERO) < 0) {
            // 删除加料条目
            extraItemMap.remove(skuUuid);
        } else if (singleQty.compareTo(BigDecimal.ZERO) == 0) {
            // 修改加料条目的数量
            ExtraShopcartItem extraItem = extraItemMap.get(skuUuid);
            BigDecimal totalQty = ShopcartItemUtils.computeTotalQty(singleQty, this);
            extraItem.setIsGroupDish(isGroupDish);
            extraItem.getOrderDish().setQty(singleQty, totalQty);
            // 删除加料条目
            extraItemMap.remove(skuUuid);
        } else {
            ExtraShopcartItem extraItem = extraItemMap.get(skuUuid);
            if (extraItem == null) {
                // 新增加料条目
                String extraItemUuid = SystemUtils.genOnlyIdentifier();
                extraItem = new ExtraShopcartItem(extraItemUuid, extra, this);
                extraItemMap.put(skuUuid, extraItem);
            }
            extraItem.setIsGroupDish(isGroupDish);
            // 修改加料条目的数量
            BigDecimal totalQty = ShopcartItemUtils.computeTotalQty(singleQty, this);
            extraItem.getOrderDish().setQty(singleQty, totalQty);
        }
    }

    /**
     * 获取加料的数量
     *
     * @param extra
     * @return
     */
    public BigDecimal getExtraQty(OrderExtra extra) {
        String skuUuid = extra.getSkuUuid();
        ExtraShopcartItem extraItem = extraItemMap.get(skuUuid);
        BigDecimal qty = BigDecimal.ZERO;
        if (extraItem != null && extraItem.getOrderDish() != null
                && extraItem.getOrderDish().getSingleQty() != null) {
            qty = extraItem.getOrderDish().getSingleQty();
        }

        return qty;
    }

    /**
     * 修改数量。此方法将同时修改受父条目影响的数量以及子条目的数量
     *
     * @param singleQty
     */
    public void changeQty(BigDecimal singleQty) {
        /*
         * 1、本条目为非称重商品时：加料总数量=加料数量*本商品总数量
         * 2、本条目为称重商品但父条目为非称重商品时：加料总数量=加料数量*父条目总数量
         */
        BigDecimal totalQty = ShopcartItemUtils.computeTotalQty(singleQty, parent);
        orderDish.setQty(singleQty, totalQty);

        BigDecimal qtyRef = ShopcartItemUtils.getParentQtyRef(this);
        if (qtyRef != null && qtyRef.compareTo(BigDecimal.ZERO) != 0) { //update by Zhaos 之前判断为one
            for (ExtraShopcartItem extraItem : extraItemMap.values()) {
                BigDecimal extraSingleQty = extraItem.getSingleQty();
                BigDecimal extraTotalQty = extraSingleQty.multiply(qtyRef);
                extraItem.getOrderDish().setQty(extraSingleQty, extraTotalQty);
            }
        }
    }

    @Override
    public void deleteReturnQty() {
        invalidType = InvalidType.DELETE_RETURN_QTY;
        statusFlag = StatusFlag.INVALID;
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.INVALID);
        }
        if (hasExtra()) {
            for (ExtraShopcartItem item : getExtraItems()) {
                item.delete();
            }
        }
    }

    @Override
    public void deleteModifyDish() {
        invalidType = InvalidType.DELETE_MODIY_DISH;
        statusFlag = StatusFlag.INVALID;
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.INVALID);
        }
        if (hasExtra()) {
            for (ExtraShopcartItem item : getExtraItems()) {
                item.delete();
            }
        }
    }

    @Override
    public void delete() {
        invalidType = InvalidType.DELETE;
        statusFlag = StatusFlag.INVALID;
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.INVALID);
        }
        if (hasExtra()) {
            for (ExtraShopcartItem item : getExtraItems()) {
                item.delete();
            }
        }
    }

    @Override
    public void recoveryDelete() {
        throw new UnsupportedOperationException("Use addDishToShoppingCart!");
    }

    public void setTradeTable(String tradeTableUuid, Long tradeTableId) {
        this.tradeTableUuid = tradeTableUuid;
        this.tradeTableId = tradeTableId;
        if (hasExtra()) {
            for (ExtraShopcartItem item : getExtraItems()) {
                item.setTradeTable(tradeTableUuid, tradeTableId);
            }
        }
    }

    @Override
    public ServingStatus getServingStatus() {
        return ServingStatus.UNSERVING;
    }

    public void setRelateInfo(Long relateId, String relateUuid) {
        this.relateTradeItemId = relateId;
        this.relateTradeItemUuid = relateUuid;
    }

    private boolean splited = false;

    public boolean ensureSplit() {
        if (splited
                || statusFlag != StatusFlag.VALID
                || getInvalidType() == InvalidType.DELETE
                || getInvalidType() == InvalidType.RETURN_QTY
                || getInvalidType() == InvalidType.SPLIT) {
            Log.w(TAG, "ensureSplit failed! dish.name=" + orderDish.getSkuName()
                    + "statusFlag=" + getStatusFlag() + ", invalidType=" + getInvalidType());
            return false;
        }
        splited = true;
        statusFlag = StatusFlag.INVALID;
        invalidType = InvalidType.SPLIT;
        return true;
    }

    public boolean ensureCancelSplit() {
        if (splited
                && statusFlag == StatusFlag.INVALID
                && invalidType == InvalidType.SPLIT) {
            splited = false;
            statusFlag = StatusFlag.VALID;
            invalidType = null;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ShopcartItemBase<?> other = (ShopcartItemBase<?>) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    /**
     * @version: 1.0
     * @date 2015年11月3日
     */
    static class ItemMetadata {
        /**
         * 出单状态
         */
        private IssueStatus issueStatus = IssueStatus.DIRECTLY;

        /**
         * 备注
         */
        private String memo;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegevo) {
        couponPrivilegeVo = couponPrivilegevo;
    }

    @Override
    public CouponPrivilegeVo getCouponPrivilegeVo() {
        return couponPrivilegeVo;
    }

    @Override
    public Bool getIsChangedPrice() {
        if (getIsChangePrice() == Bool.NO) {
            return Bool.NO;
        } else {
            if (getPrice().compareTo(orderDish.dish.getPrice()) != 0) {
                return Bool.YES;
            }
        }
        return Bool.NO;
    }

    @Override
    public void setPack(boolean value) {
        isPack = value;
    }

    @Override
    public boolean getPack() {
        return isPack;
    }

    @Override
    public void setReturnInventory(boolean value) {
        isReturnInventory = value;
    }

    @Override
    public boolean getReturnInventory() {
        return isReturnInventory;
    }

    @Override
    public void setIsGroupDish(boolean isGroup) {
        isGroupDish = isGroup;
    }

    @Override
    public boolean isGroupDish() {
        return isGroupDish;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    @Override
    public int canWakeUp() {
        if (getStatusFlag() == StatusFlag.INVALID) {
            return CANNOT_WAKE_UP;
        }
        //数目为0的才，不能等叫
        if (getTotalQty().equals(BigDecimal.ZERO)) {
            return CANNOT_WAKE_UP;
        }
        if (getIssueStatus() == IssueStatus.PAUSE) {
            return CANNOT_WAKE_UP;
        }

        if (tradeItemOperations != null) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getOpType() == PrintOperationOpType.WAKE_UP
                        && tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
                    return HAS_WAKE_UP;
                }
            }
        }

        return NOT_WAKE_UP;
    }

    @Override
    public boolean canWakeUpCancel() {
        if (getStatusFlag() == StatusFlag.INVALID) {
            return false;
        }
        //数目为0的才，不能等叫
        if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }

        if (getIssueStatus() == IssueStatus.PAUSE) {
            return false;
        }

        boolean hasWakeUp = false;

        if (tradeItemOperations != null) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getOpType() == PrintOperationOpType.WAKE_UP
                        && tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
                    hasWakeUp = true;
                }
            }
            return hasWakeUp;
        }
        return false;
    }

    @Override
    public boolean canRiseDish() {
        if (getStatusFlag() == StatusFlag.INVALID) {
            return false;
        }
        //数目为0的才，不能起菜
        if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
        // 已上菜的菜品，不能起菜
        if (getServingStatus() == ServingStatus.SERVING) {
            return false;
        }

        if (getIssueStatus() == IssueStatus.PAUSE) {
            return false;
        }

        boolean hasRiseDish = false;

        if (tradeItemOperations != null) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                switch (tradeItemOperation.getOpType()) {
                    case RISE_DISH:
                        if (tradeItemOperation.getStatusFlag() == StatusFlag.VALID)
                            hasRiseDish = true;
                        break;
                    default:
                        break;
                }
            }
        }

        if (hasRiseDish)
            return false;
        else
            return true;
    }

    @Override
    public boolean canRiseDishCancel() {
        if (getStatusFlag() == StatusFlag.INVALID) {
            return false;
        }
        //数目为0的才，不能起菜
        if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
        // 已上菜的菜品，不能起菜
        if (getServingStatus() == ServingStatus.SERVING) {
            return false;
        }

        if (getIssueStatus() != IssueStatus.FINISHED) {
            return false;
        }

        boolean hasRiseDish = false;
        if (tradeItemOperations != null) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getOpType() == PrintOperationOpType.RISE_DISH
                        && tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
                    hasRiseDish = true;
                }
            }
            return hasRiseDish;
        }
        return false;
    }

    @Override
    public boolean canRemindDish() {
        return false;
    }

    @Override
    public void addOperation(PrintOperationOpType opType) {
        boolean needAdd = false;
        switch (opType) {
            case WAKE_UP:
                // 已有未保存的等叫，就不能再添加等叫了，以免重复
                if (!hasWakeUp()) {
                    needAdd = true;
                }
                break;
            case RISE_DISH:
                if (!hasRiseUp()) {
                    needAdd = true;
                }
                break;
            default:
                break;
        }
        if (needAdd) {
            if (tradeItemOperations == null) {
                tradeItemOperations = new ArrayList<TradeItemOperation>();
            }
            TradeItemOperation tradeItemOperation = new TradeItemOperation();
            tradeItemOperation.setOpType(opType);
            tradeItemOperation.setPrintStatus(PrintStatus.UNPRINT);
            tradeItemOperation.setSkuName(getSkuName());
            tradeItemOperation.setTradeItemUuid(getUuid());
            tradeItemOperation.validateCreate();
            tradeItemOperations.add(tradeItemOperation);
        }
    }

    @Override
    public void removeOperation(PrintOperationOpType opType) {
        switch (opType) {
            case WAKE_UP:
                if (tradeItemOperations != null) {
                    for (int i = tradeItemOperations.size() - 1; i >= 0; i--) {
                        TradeItemOperation tradeItemOperation = tradeItemOperations.get(i);
                        if (tradeItemOperation.getOpType() == opType && tradeItemOperation.getId() == null) {
                            tradeItemOperations.remove(i);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean hasWakeUp() {
        if (tradeItemOperations != null && !tradeItemOperations.isEmpty()) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getOpType() == PrintOperationOpType.WAKE_UP
                        && tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasRiseUp() {
        if (tradeItemOperations != null && !tradeItemOperations.isEmpty()) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getOpType() == PrintOperationOpType.RISE_DISH) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean hasUnsaveRemindDish() {
        if (tradeItemOperations != null && !tradeItemOperations.isEmpty()) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getOpType() == PrintOperationOpType.REMIND_DISH
                        && tradeItemOperation.getId() == null) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public TradeItemExtraDinner getTradeItemExtraDinner() {
        return tradeItemExtraDinner;
    }

    @Override
    public void setTradeItemExtraDinner(TradeItemExtraDinner tradeItemExtraDinner) {
        this.tradeItemExtraDinner = tradeItemExtraDinner;
    }

    public void setShopcartItemType(ShopcartItemType shopcartItemType) {
        this.shopcartItemType = shopcartItemType;
    }

    public ShopcartItemType getShopcartItemType() {
        return shopcartItemType;
    }

    public List<TradeItemMainBatchRel> getTradeItemMainBatchRelList() {
        return tradeItemMainBatchRelList;
    }

    public void setTradeItemMainBatchRelList(List<TradeItemMainBatchRel> tradeItemMainBatchRelList) {
        this.tradeItemMainBatchRelList = tradeItemMainBatchRelList;
    }

    public List<TradeUser> getTradeItemUserList() {
        return tradeItemUserList;
    }

    public void setTradeItemUserList(List<TradeUser> tradeItemUserList) {
        this.tradeItemUserList = tradeItemUserList;
    }

    @Override
    public CardServicePrivilegeVo getCardServicePrivilgeVo() {
        return cardServicePrivilegeVo;
    }

    @Override
    public void setCardServicePrivilegeVo(CardServicePrivilegeVo cardServicePrivilegeVo) {
        this.cardServicePrivilegeVo = cardServicePrivilegeVo;
    }

    public AppletPrivilegeVo getAppletPrivilegeVo() {
        return appletPrivilegeVo;
    }

    public void setAppletPrivilegeVo(AppletPrivilegeVo appletPrivilegeVo) {
        this.appletPrivilegeVo = appletPrivilegeVo;
    }
}
