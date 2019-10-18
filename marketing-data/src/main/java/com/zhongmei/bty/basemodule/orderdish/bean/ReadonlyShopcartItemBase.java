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
        private BigDecimal sigleDeskQuantity;


    private boolean isPack = false;

    private int index;

    private boolean isReturnInventory;

    private List<TradeItemOperation> tradeItemOperations;

    private TradeItemExtraDinner tradeItemExtraDinner;

    private BigDecimal kdsScratchDishQty = BigDecimal.ZERO;
    private ShopcartItemType shopcartItemType = ShopcartItemType.COMMON;
        private List<TradeItemMainBatchRel> tradeItemMainBatchRelList;
        private ReadonlyShopcartItem mainShopcartItem;
        private List<TradeUser> tradeItemUserList;
        private CardServicePrivilegeVo cardServicePrivilegeVo;
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
                if(DishCache.getDishTimeChargingRuleHolder().getRuleByDishId(tradeItem.getDishId())!=null){
            BigDecimal actualAmount=calculTimeChargingAmount();
            if(tradeItem.getActualAmount().compareTo(actualAmount)!=0){                tradeItem.setActualAmount(actualAmount);
                tradeItem.setChanged(true);
            }
            return actualAmount;        }
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
                if (Utils.isNotEmpty(getExtraItems())) {
            for (ReadonlyExtraShopcartItem extraShopcartItem : getExtraItems()) {
                extraShopcartItem.setIssueStatus(mIssueStatus);
            }
        }
    }


    private BigDecimal calculTimeChargingAmount() {
        BigDecimal actualAmount = BigDecimal.ZERO;
        DishTimeChargingRule rule = DishCache.getDishTimeChargingRuleHolder().getRuleByDishId(tradeItem.getDishId());
        if (rule == null) {
            return tradeItem.getActualAmount();
        }
        BigDecimal currentTime = new BigDecimal(System.currentTimeMillis());
        BigDecimal currentMintes = currentTime.subtract(new BigDecimal(tradeItem.getServerCreateTime())).divide(new BigDecimal(60 * 1000),2,BigDecimal.ROUND_HALF_DOWN);
        BigDecimal currentTimeParts = new BigDecimal(Math.floor(currentMintes.divide(rule.getChargingUnit().multiply(new BigDecimal(60)),2,BigDecimal.ROUND_HALF_DOWN).doubleValue()));        BigDecimal serviceTimeHour = currentTimeParts.multiply(rule.getChargingUnit());

                if (rule.getStartChargingTimes().compareTo(serviceTimeHour) >= 0) {
                        return MathDecimal.round(rule.getStartChargingPrice().multiply(tradeItem.getQuantity()),2);
        }

                BigDecimal overMinutes = currentMintes.divideAndRemainder(rule.getChargingUnit().multiply(new BigDecimal(60)))[1];        if (rule.getFullUnit()!=null && rule.getFullUnit().compareTo(overMinutes) <= 0) {
                        serviceTimeHour = serviceTimeHour.add(rule.getFullUnitCharging());
        } else if(rule.getNoFullUnit()!=null && rule.getNoFullUnit().compareTo(overMinutes) >= 0){
                        serviceTimeHour = serviceTimeHour.add(rule.getNoFullUnitCharging());
        }else{
                        serviceTimeHour=serviceTimeHour.add(overMinutes.divide(new BigDecimal(60),2,BigDecimal.ROUND_HALF_DOWN));
        }

                actualAmount=actualAmount.add(rule.getStartChargingPrice().multiply(tradeItem.getQuantity()));
                Log.e("TimeCharging","serviceTimeHour:"+serviceTimeHour);
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
                        tradeItem.setChanged(splitedChangedBak);
            return true;
        }
        return false;
    }


    public void setupModifyDishState() {
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        tradeItem.setInvalidType(InvalidType.MODIFY_DISH);
        tradeItem.validateUpdate();
    }


    public void clearModifyDishState() {
        tradeItem.setStatusFlag(StatusFlag.VALID);
        tradeItem.setInvalidType(null);
        tradeItem.validateUpdate();
    }


    public void setupReturnQtyState(BigDecimal returnQty) {
        tradeItem.setStatusFlag(StatusFlag.INVALID);
        tradeItem.setInvalidType(InvalidType.RETURN_QTY);
        tradeItem.setReturnQuantity(returnQty);
        tradeItem.validateUpdate();
    }


    public void clearReturnQtyState() {
        tradeItem.setStatusFlag(StatusFlag.VALID);
        tradeItem.setInvalidType(null);
        tradeItem.setReturnQuantity(BigDecimal.ZERO);
        tradeItem.validateUpdate();
    }


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
                if (getTotalQty().equals(BigDecimal.ZERO)) {
            return CANNOT_WAKE_UP;
        }
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
                                    return CANNOT_WAKE_UP;                                  else
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
                if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
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
                        hasWakeUp = true;                      else
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
                if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
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

        else {
            return true;
        }
    }

    @Override
    public boolean canRiseDishCancel() {
        if (getStatusFlag() == StatusFlag.INVALID) {
            return false;
        }
                if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
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
                if (getTotalQty().equals(BigDecimal.ZERO)) {
            return false;
        }
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
