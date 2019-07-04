package com.zhongmei.bty.basemodule.orderdish.bean;

import android.text.TextUtils;
import android.util.Log;

import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.shoppingcart.utils.AppletUtil;
import com.zhongmei.bty.basemodule.shoppingcart.utils.CardServiceTool;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.yunfu.db.entity.dish.DishTimeChargingRule;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.util.MathDecimal;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishMakeStatus;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.PrintStatus;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.basemodule.orderdish.cache.DishCache;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.util.ValueEnums;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年9月20日
 */
public abstract class ReadonlyShopcartItemBase implements IShopcartItemBase {

    private static final String TAG = ReadonlyShopcartItemBase.class.getSimpleName();

    public final TradeItem tradeItem;
    private TradePrivilege privilege;
    private List<ReadonlyOrderProperty> properties;
    private List<ReadonlyExtraShopcartItem> extraItems;
    private boolean selected;
    private CouponPrivilegeVo couponPrivilegeVo;
    protected TradeItemExtra tradeItemExtra;

    private boolean isGroupDish = false;
    //单桌菜品数量
    private BigDecimal sigleDeskQuantity;

    /**
     * 是否打包
     */
    private boolean isPack = false;
    /**
     * 购物车菜品加入循序
     */
    private int index;
    /**
     * 是否退回库存
     */
    private boolean isReturnInventory;

    private List<TradeItemOperation> tradeItemOperations;
    /**
     * 座位号
     */
    private TradeItemExtraDinner tradeItemExtraDinner;

    private BigDecimal kdsScratchDishQty = BigDecimal.ZERO; //已划菜份数

    private ShopcartItemType shopcartItemType = ShopcartItemType.COMMON;
    //主单批量菜关联
    private List<TradeItemMainBatchRel> tradeItemMainBatchRelList;
    //主单批量菜在子单时关联的主单批量菜原数据
    private ReadonlyShopcartItem mainShopcartItem;
    //技师与服务关联表
    private List<TradeUser> tradeItemUserList;
    //次卡项目优惠vo
    private CardServicePrivilegeVo cardServicePrivilegeVo;
    //小程序优惠vo
    AppletPrivilegeVo appletPrivilegeVo;

    @Override
    public CardServicePrivilegeVo getCardServicePrivilgeVo() {
        return cardServicePrivilegeVo;
    }

    @Override
    public void setCardServicePrivilegeVo(CardServicePrivilegeVo cardServicePrivilegeVo) {
        this.cardServicePrivilegeVo = cardServicePrivilegeVo;
    }

    public BigDecimal getKdsScratchDishQty() {
        return kdsScratchDishQty;
    }

    public void setKdsScratchDishQty(BigDecimal kdsScratchDishQty) {
        this.kdsScratchDishQty = kdsScratchDishQty;
    }

    protected ReadonlyShopcartItemBase(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public abstract ReadonlyShopcartItemBase getParent();

    public void setProperties(List<ReadonlyOrderProperty> properties) {
        this.properties = properties;
    }

    public void setExtraItems(List<ReadonlyExtraShopcartItem> extraItems) {
        this.extraItems = extraItems;
    }

    public boolean hasProperty() {
        return Utils.isNotEmpty(properties);
    }

    public boolean hasExtra() {
        return Utils.isNotEmpty(extraItems);
    }

    @Override
    public Long getId() {
        return tradeItem.getId();
    }

    @Override
    public Long getBatchId() {
        return tradeItem.getBatchId();
    }

    @Override
    public String getUuid() {
        return tradeItem.getUuid();
    }

    @Override
    public Long getServerUpdateTime() {
        return tradeItem.getServerUpdateTime();
    }

    @Override
    public Long getServerCreateTime() {
        return tradeItem.getServerCreateTime();
    }

    @Override
    public String getParentUuid() {
        return tradeItem.getParentUuid();
    }

    @Override
    public DishShop getDishShop() {
        return DishCache.getDishHolder().get(getSkuId());
    }

    @Override
    public Long getSkuId() {
        // TODO 由于后台没有增加sku_id，暂时先从缓存中取sku_id。
        // 存在风险：需要使用sku_id时此菜品有可能被后台禁用了，这时会返回null
        if (tradeItem.getDishId() == null) {
            DishShop dishShop = DishCache.getDishHolder().get(getSkuUuid());
            if (dishShop != null) {
                return dishShop.getBrandDishId();
            }
        }
        return tradeItem.getDishId();
    }

    @Override
    public String getSkuUuid() {
        if(getDishShop()!=null){
            return getDishShop().getUuid();
        }
        return tradeItem.getSkuUuid();
    }

    @Override
    public String getSkuName() {
        return tradeItem.getDishName();
    }

    @Override
    public DishType getType() {
        return tradeItem.getType();
    }

    @Override
    public Bool getIsChangePrice() {
        return tradeItem.getIsChangePrice();
    }

    @Override
    public BigDecimal getPrice() {
        return tradeItem.getPrice();
    }

    @Override
    public String getUnitName() {
        return tradeItem.getUnitName();
    }

    @Override
    public SaleType getSaleType() {
        return tradeItem.getSaleType();
    }

    @Override
    public BigDecimal getTotalQty() {
        return tradeItem.getQuantity();
    }

    @Override
    public BigDecimal getAmount() {
        return tradeItem.getAmount();
    }

    @Override
    public BigDecimal getFeedsAmount() {
        return tradeItem.getFeedsAmount();
    }

    @Override
    public BigDecimal getPropertyAmount() {
        return tradeItem.getPropertyAmount();
    }

    @Override
    public BigDecimal getActualAmount() {
        //区分是否需要计算，通过tradeItemSaleType
        if(DishCache.getDishTimeChargingRuleHolder().getRuleByDishId(tradeItem.getDishId())!=null){
            BigDecimal actualAmount=calculTimeChargingAmount();
            if(tradeItem.getActualAmount().compareTo(actualAmount)!=0){//不相等
                tradeItem.setActualAmount(actualAmount);
                tradeItem.setChanged(true);
            }
            return actualAmount;//计算时间
        }
        return tradeItem.getActualAmount();
    }

    @Override
    public Bool getEnableWholePrivilege() {
        return tradeItem.getEnableWholePrivilege();
    }

    @Override
    public String getMemo() {
        return tradeItem.getTradeMemo();
    }

    public Long getTradeId() {
        return tradeItem.getTradeId();
    }

    public String getTradeUuid() {
        return tradeItem.getTradeUuid();
    }

    @Override
    public void deleteReturnQty() {
        tradeItem.setInvalidType(InvalidType.DELETE_RETURN_QTY);
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.INVALID);
        }
        if (extraItems != null) {
            for (ReadonlyExtraShopcartItem item : extraItems) {
                item.delete();
            }
        }
        if (tradeItemExtra != null) {
            tradeItemExtra.setIsPack(Bool.NO);
            tradeItemExtra.setStatusFlag(StatusFlag.INVALID);
        }
        tradeItem.validateUpdate();
    }

    @Override
    public void deleteModifyDish() {
        tradeItem.setInvalidType(InvalidType.DELETE_MODIY_DISH);
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.INVALID);
        }
        if (extraItems != null) {
            for (ReadonlyExtraShopcartItem item : extraItems) {
                item.delete();
            }
        }
        if (tradeItemExtra != null) {
            tradeItemExtra.setIsPack(Bool.NO);
            tradeItemExtra.setStatusFlag(StatusFlag.INVALID);
        }
        tradeItem.validateUpdate();
    }

    @Override
    public void delete() {
        tradeItem.setInvalidType(InvalidType.DELETE);
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.INVALID);
        }
        if (extraItems != null) {
            for (ReadonlyExtraShopcartItem item : extraItems) {
                item.delete();
            }
        }
        if (tradeItemExtra != null) {
            tradeItemExtra.setIsPack(Bool.NO);
            tradeItemExtra.setStatusFlag(StatusFlag.INVALID);
        }
        tradeItem.validateUpdate();

        CardServiceTool.removeService(this);
        AppletUtil.removeApplet(this);
    }

    @Override
    public void recoveryDelete() {
        if (tradeItem.getInvalidType() != InvalidType.DELETE
                || tradeItem.getStatusFlag() != StatusFlag.INVALID) {
            throw new RuntimeException("The item's state is wrong!");
        }
        tradeItem.setInvalidType(null);
        tradeItem.setStatusFlag(StatusFlag.VALID);
        if (privilege != null) {
            privilege.setStatusFlag(StatusFlag.VALID);
        }
        if (extraItems != null) {
            for (ReadonlyExtraShopcartItem item : extraItems) {
                item.recoveryDelete();
            }
        }
        tradeItem.setChanged(true);
    }

    @Override
    public StatusFlag getStatusFlag() {
        return tradeItem.getStatusFlag();
    }

    public void setStatusFlag(StatusFlag statusFlag) {
        tradeItem.setStatusFlag(statusFlag);
    }

    @Override
    public InvalidType getInvalidType() {
        return tradeItem.getInvalidType();
    }

    @Override
    public Long getRelateTradeItemId() {
        return tradeItem.getRelateTradeItemId();
    }

    @Override
    public String getRelateTradeItemUuid() {
        return tradeItem.getRelateTradeItemUuid();
    }

    @Override
    public BigDecimal getReturnQty() {
        return tradeItem.getReturnQuantity();
    }

    @Override
    public boolean isChanged() {
        return tradeItem.isChanged();
    }

    @Override
    public void setChanged(boolean changed) {
        tradeItem.setChanged(changed);
    }

    @Override
    public String getBatchNo() {
        return tradeItem.getBatchNo();
    }

    @Override
    public String getTradeTableUuid() {
        return tradeItem.getTradeTableUuid();
    }

    @Override
    public Long getTradeTableId() {
        return tradeItem.getTradeTableId();
    }

    @Override
    public IssueStatus getIssueStatus() {
        return tradeItem.getIssueStatus();
    }

    @Override
    public void setIssueStatus(IssueStatus mIssueStatus) {
        tradeItem.setIssueStatus(mIssueStatus);
        tradeItem.setChanged(true);
        //更新加料的打印状态
        if (Utils.isNotEmpty(getExtraItems())) {
            for (ReadonlyExtraShopcartItem extraShopcartItem : getExtraItems()) {
                extraShopcartItem.setIssueStatus(mIssueStatus);
            }
        }
    }

    /**
     * 计算计时消费的费用
     * StartChargingTime小时内StartChargingPrice元
     * 超过部分每ChargingUnit小时ChargingPrice元
     * 其中：满FullUnit分钟算FullUnitCharging小时
     *      不满NoFullUnit分钟算NoFullUnitCharging小时
     * @return
     */
    private BigDecimal calculTimeChargingAmount() {
        BigDecimal actualAmount = BigDecimal.ZERO;
        DishTimeChargingRule rule = DishCache.getDishTimeChargingRuleHolder().getRuleByDishId(tradeItem.getDishId());
        if (rule == null) {
            return tradeItem.getActualAmount();
        }
        BigDecimal currentTime = new BigDecimal(System.currentTimeMillis());
        BigDecimal currentMintes = currentTime.subtract(new BigDecimal(tradeItem.getServerCreateTime())).divide(new BigDecimal(60 * 1000),2,BigDecimal.ROUND_HALF_DOWN);
        BigDecimal serviceTimeHour = new BigDecimal(Math.floor(currentMintes.divide(new BigDecimal(60),2,BigDecimal.ROUND_HALF_DOWN).doubleValue()));//


        //不满最低时间，按照最低时间算
        if (rule.getStartChargingTimes().compareTo(serviceTimeHour) >= 0) {
            //按照最低消费时间来算
            return MathDecimal.round(rule.getStartChargingPrice().multiply(tradeItem.getQuantity()),2);
        }

        //超过最低时间
        BigDecimal overMinutes = currentMintes.divideAndRemainder(new BigDecimal(60))[1];//取余操作
        if (rule.getFullUnit()!=null && rule.getFullUnit().compareTo(overMinutes) <= 0) {
            //按不满算
            serviceTimeHour = serviceTimeHour.add(rule.getFullUnitCharging());
        } else if(rule.getNoFullUnit()!=null && rule.getNoFullUnit().compareTo(overMinutes) < 0){
            //按满了算
            serviceTimeHour = serviceTimeHour.add(rule.getNoFullUnitCharging());
        }else{
            //普通计算
            serviceTimeHour=serviceTimeHour.add(overMinutes.divide(new BigDecimal(60),2,BigDecimal.ROUND_HALF_DOWN));
        }

        //计算价格(最低消费)
        actualAmount=actualAmount.add(rule.getStartChargingPrice().multiply(tradeItem.getQuantity()));//基础费用

        //计算真实的价格（加上超过的部分费用）
        serviceTimeHour = serviceTimeHour.subtract(rule.getStartChargingTimes());
        actualAmount=actualAmount.add(serviceTimeHour.divide(rule.getChargingUnit(),2,BigDecimal.ROUND_HALF_DOWN).multiply(rule.getChargingPrice()).multiply(tradeItem.getQuantity()));
        return MathDecimal.round(actualAmount,2);
    }

    public GuestPrinted getGuestPrinted() {
        return tradeItem.getGuestPrinted();
    }

    public void setGuestPrinted(GuestPrinted guestPrinted) {
        tradeItem.setGuestPrinted(guestPrinted);
        tradeItem.setChanged(true);
        //更新加料的打印状态
        if (Utils.isNotEmpty(getExtraItems())) {
            for (ReadonlyExtraShopcartItem extraShopcartItem : getExtraItems()) {
                extraShopcartItem.setGuestPrinted(guestPrinted);
            }
        }
    }

    @Override
    public void setIssueStatusWithoutSetmeal(IssueStatus issueStatus) {
        tradeItem.setIssueStatus(issueStatus);
        tradeItem.setChanged(true);
        //更新加料的打印状态
        if (Utils.isNotEmpty(getExtraItems())) {
            for (ReadonlyExtraShopcartItem extraShopcartItem : getExtraItems()) {
                extraShopcartItem.setIssueStatus(issueStatus);
            }
        }
    }

    @Override
    public void setMemo(String memo) {
        tradeItem.setTradeMemo(memo);
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
    public List<ReadonlyOrderProperty> getProperties() {
        return properties;
    }

    @Override
    public Collection<ReadonlyExtraShopcartItem> getExtraItems() {
        return extraItems;
    }

    @Override
    public ServingStatus getServingStatus() {
        return tradeItem.getServingStatus() == null ? ServingStatus.UNSERVING : tradeItem.getServingStatus();
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean value) {
        selected = value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((tradeItem == null) ? 0 : tradeItem.hashCode());
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
        ReadonlyShopcartItemBase other = (ReadonlyShopcartItemBase) obj;
        if (tradeItem == null) {
            if (other.tradeItem != null)
                return false;
        } else if (!tradeItem.equals(other.tradeItem))
            return false;
        return true;
    }

    private boolean splited = false;
    private boolean splitedChangedBak = false;

    /**
     * @param isSplit true:拆单
     * @return
     */
    public boolean ensureCopy(boolean isSplit) {
        if (isSplit) {
            return ensureSplit();
        }
        return ensureCopy();
    }

    public boolean ensureCopy() {
        if (splited
                || getStatusFlag() != StatusFlag.VALID
                || getInvalidType() == InvalidType.DELETE
                || getInvalidType() == InvalidType.RETURN_QTY
                || getInvalidType() == InvalidType.SPLIT) {
            Log.w(TAG, "ensureSplit failed! tradeItem.uuid=" + tradeItem.getUuid()
                    + "statusFlag=" + getStatusFlag() + ", invalidType=" + getInvalidType());
            return false;
        }
        tradeItem.setChanged(true);
        return true;
    }

    public boolean ensureSplit() {
        // 没被拆单过的才能拆单
        if (splited
                || getStatusFlag() != StatusFlag.VALID
                || getInvalidType() == InvalidType.DELETE
                || getInvalidType() == InvalidType.RETURN_QTY
                || getInvalidType() == InvalidType.SPLIT) {
            Log.w(TAG, "ensureSplit failed! tradeItem.uuid=" + tradeItem.getUuid()
                    + "statusFlag=" + getStatusFlag() + ", invalidType=" + getInvalidType());
            return false;
        }
        splited = true;
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        tradeItem.setInvalidType(InvalidType.SPLIT);
        // 备份拆单操作前的 changed 状态
        splitedChangedBak = tradeItem.isChanged();
        tradeItem.setChanged(true);
        return true;
    }

    public boolean ensureCancelSplit() {
        if (splited
                && getStatusFlag() == StatusFlag.INVALID
                && getInvalidType() == InvalidType.SPLIT) {
            splited = false;
            tradeItem.setStatusFlag(StatusFlag.VALID);
            tradeItem.setInvalidType(null);
            // 恢复为拆单前的 changed 状态
            tradeItem.setChanged(splitedChangedBak);
            return true;
        }
        return false;
    }

    /**
     * 将此条目修改为被改菜状态
     */
    public void setupModifyDishState() {
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        tradeItem.setInvalidType(InvalidType.MODIFY_DISH);
        tradeItem.validateUpdate();
    }

    /**
     * 清除此条目的被改菜状态
     */
    public void clearModifyDishState() {
        tradeItem.setStatusFlag(StatusFlag.VALID);
        tradeItem.setInvalidType(null);
        tradeItem.validateUpdate();
    }

    /**
     * 将此条目修改为被退菜状态
     *
     * @param returnQty
     */
    public void setupReturnQtyState(BigDecimal returnQty) {
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        tradeItem.setInvalidType(InvalidType.RETURN_QTY);
        tradeItem.setReturnQuantity(returnQty);
        tradeItem.validateUpdate();
    }

    /**
     * 清除此条目的被退菜状态
     */
    public void clearReturnQtyState() {
        tradeItem.setStatusFlag(StatusFlag.VALID);
        tradeItem.setInvalidType(null);
        tradeItem.setReturnQuantity(BigDecimal.ZERO);
        tradeItem.validateUpdate();
    }

    /**
     * 联台批量菜、拆菜
     */
    public void setupUnionSplitState() {
        tradeItem.setId(null);
        tradeItem.validateCreate();
        tradeItem.setServerCreateTime(null);
        tradeItem.setServerUpdateTime(null);
    }


    public int getIndex() {
        return index;
    }

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
            if (tradeItem.getDishId() == null) {
                DishShop dishShop = DishCache.getDishHolder().get(getSkuUuid());
                if (dishShop != null) {
                    if (getPrice().compareTo(dishShop.getMarketPrice()) != 0) {
                        return Bool.YES;
                    }
                }
            }
        }
        return Bool.NO;
    }

    @Override
    public boolean getPack() {
        if (tradeItemExtra != null && tradeItemExtra.getIsPack() == Bool.YES) {
            return true;
        }
        return false;
    }

    @Override
    public void setPack(boolean pack) {
        if (tradeItemExtra != null) {
            if (pack) {
                tradeItemExtra.setIsPack(Bool.YES);
            } else {
                tradeItemExtra.setIsPack(Bool.NO);
            }

        }
    }

    @Override
    public void setReturnInventory(boolean value) {
        isReturnInventory = value;
    }

    @Override
    public boolean getReturnInventory() {
        return isReturnInventory;
    }

    public TradeItemExtra getTradeItemExtra() {
        return tradeItemExtra;
    }

    public void setTradeItemExtra(TradeItemExtra tradeItemExtra) {
        this.tradeItemExtra = tradeItemExtra;
    }

    /**
     * 菜品是否正在等待中
     */
    public boolean isWaiting() {
        if (tradeItemExtra == null || tradeItemExtra.getDishMakeStatus() == DishMakeStatus.WAITING) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setIsGroupDish(boolean isGroup) {
        isGroupDish = isGroup;
    }

    @Override
    public boolean isGroupDish() {
        return isGroupDish;
    }

    public BigDecimal getSigleDeskQuantity() {
        return sigleDeskQuantity;
    }

    public void setSigleDeskQuantity(BigDecimal sigleDeskQuantity) {
        this.sigleDeskQuantity = sigleDeskQuantity;
    }

    @Override
    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    @Override
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
        // 已上菜的菜品，不能等叫
        if (getServingStatus() == ServingStatus.SERVING) {
            return CANNOT_WAKE_UP;
        }
        if (getIssueStatus() == IssueStatus.PAUSE) {
            return CANNOT_WAKE_UP;
        }

        boolean hasWakeUp = false;

        if (TextUtils.isEmpty(getBatchNo())) {
            if (tradeItemOperations != null) {
                for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                    switch (tradeItemOperation.getOpType()) {
                        case WAKE_UP:
                            if (tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
                                if (tradeItemOperation.getId() != null)
                                    return CANNOT_WAKE_UP;  // 等叫操作已经保存服务器的菜品，不能进行等叫操作
                                else
                                    hasWakeUp = true;
                            }
                            break;
                        case RISE_DISH:
                            if (tradeItemOperation.getStatusFlag() == StatusFlag.VALID)
                                return CANNOT_WAKE_UP;
                            break;
                        default:
                            break;
                    }
                }
            }
            if (hasWakeUp)
                return HAS_WAKE_UP;
            else
                return NOT_WAKE_UP;
        } else {
            return CANNOT_WAKE_UP;
        }
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
        // 已上菜的菜品，不能等叫
        if (getServingStatus() == ServingStatus.SERVING) {
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
                    if (tradeItemOperation.getId() != null)
                        hasWakeUp = true;  // 等叫操作已经保存服务器
                    else
                        hasWakeUp = false;
                }
            }
            return hasWakeUp;
        } else
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

        boolean hasWakeUp = false;
        boolean hasRiseDish = false;

        if (tradeItemOperations != null) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                switch (tradeItemOperation.getOpType()) {
                    case WAKE_UP:
                        if (tradeItemOperation.getStatusFlag() == StatusFlag.VALID)
                            hasWakeUp = true;
                        break;
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
		/*
		else if(!TextUtils.isEmpty(getBatchNo())){
			if(hasWakeUp)
				return true;
			else
				return false;
		}*/
        else {
            return true;
        }
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
        if (getStatusFlag() == StatusFlag.INVALID) {
            return false;
        }
        //数目为0的才，不能催菜
        if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
        // 已上菜的菜品，不能催菜
        if (getServingStatus() == ServingStatus.SERVING) {
            return false;
        }

        if (getIssueStatus() != IssueStatus.FINISHED) {
            return false;
        }

        boolean hasWakeUp = false;
        boolean hasRiseDish = false;

        if (tradeItemOperations != null) {
            for (TradeItemOperation tradeItemOperation : tradeItemOperations) {
                if (tradeItemOperation.getStatusFlag() != StatusFlag.VALID)
                    continue;
                switch (tradeItemOperation.getOpType()) {
                    case WAKE_UP:
                        hasWakeUp = true;
                        break;
                    case RISE_DISH:
                        hasRiseDish = true;
                        break;
                    default:
                        break;
                }
            }
        }

        if (hasWakeUp) {
            if (hasRiseDish) {
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }

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
            case REMIND_DISH:
                if (!hasUnsaveRemindDish()) {
                    needAdd = true;
                }
                break;
            default:
                needAdd = true;
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
            case RISE_DISH:
            case WAKE_UP_CANCEL:
            case RISE_DISH_CANCEL:
            case REMIND_DISH:
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
                if (tradeItemOperation.getOpType() == PrintOperationOpType.RISE_DISH
                        && tradeItemOperation.getStatusFlag() == StatusFlag.VALID) {
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

    public ShopcartItemType getShopcartItemType() {
        return shopcartItemType;
    }

    public void setShopcartItemType(ShopcartItemType shopcartItemType) {
        this.shopcartItemType = shopcartItemType;
    }

    public List<TradeItemMainBatchRel> getTradeItemMainBatchRelList() {
        return tradeItemMainBatchRelList;
    }

    public void setTradeItemMainBatchRelList(List<TradeItemMainBatchRel> tradeItemMainBatchRelList) {
        this.tradeItemMainBatchRelList = tradeItemMainBatchRelList;
    }


    public ReadonlyShopcartItem getMainShopcartItem() {
        return mainShopcartItem;
    }

    public void setMainShopcartItem(ReadonlyShopcartItem mainShopcartItem) {
        this.mainShopcartItem = mainShopcartItem;
    }

    /**
     * 修改数量，并修改对应的价格
     *
     * @param quantity
     */
    public void modifyQty(BigDecimal quantity) {
        BigDecimal newAmount = MathDecimal.divDown(getAmount().multiply(quantity), getTotalQty(), 2);
        tradeItem.setAmount(newAmount);
        BigDecimal newActualAmount = MathDecimal.divDown(getActualAmount().multiply(quantity), getTotalQty(), 2);
        tradeItem.setActualAmount(newActualAmount);
        BigDecimal newPropertyAmount = MathDecimal.divDown(getPropertyAmount().multiply(quantity), getTotalQty(), 2);
        tradeItem.setPropertyAmount(newPropertyAmount);
        BigDecimal newFeedAmount = MathDecimal.divDown(getFeedsAmount().multiply(quantity), getTotalQty(), 2);
        tradeItem.setFeedsAmount(newFeedAmount);
        tradeItem.setQuantity(quantity);
        tradeItem.setChanged(true);
    }

    public List<TradeUser> getTradeItemUserList() {
        return tradeItemUserList;
    }

    public void setTradeItemUserList(List<TradeUser> tradeItemUserList) {
        this.tradeItemUserList = tradeItemUserList;
    }

    public AppletPrivilegeVo getAppletPrivilegeVo() {
        return appletPrivilegeVo;
    }

    public void setAppletPrivilegeVo(AppletPrivilegeVo appletPrivilegeVo) {
        this.appletPrivilegeVo = appletPrivilegeVo;
    }
}
