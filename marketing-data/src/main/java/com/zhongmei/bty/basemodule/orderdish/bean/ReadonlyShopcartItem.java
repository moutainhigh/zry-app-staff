package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.GuestPrinted;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @version: 1.0
 * @date 2015年9月20日
 */
public class ReadonlyShopcartItem extends ReadonlyShopcartItemBase implements IShopcartItem {

    private List<ReadonlySetmealShopcartItem> setmealItems;

    private List<ReadonlySetmealShopcartItem> serverSetmealItems;

    private TradeReasonRel returnQtyReason;

    //tradePlanActivity表的uuid
    private String tradePlanUuid;
    /**
     * 理由rel
     */
    private TradeReasonRel reasonRel;

    public ReadonlyShopcartItem(TradeItem tradeItem) {
        this(tradeItem, null);
    }

    public ReadonlyShopcartItem(TradeItem tradeItem, TradeReasonRel returnQtyReason) {
        super(tradeItem);
        this.returnQtyReason = returnQtyReason;
    }

    @Override
    public ReadonlyShopcartItemBase getParent() {
        return null;
    }

    public void setSetmealItems(List<ReadonlySetmealShopcartItem> setmealItems) {
        this.setmealItems = setmealItems;
    }

    public boolean hasSetmeal() {
        return Utils.isNotEmpty(setmealItems);
    }

    public void setServerSetmealItems(List<ReadonlySetmealShopcartItem> serverSetmealItems) {
        this.serverSetmealItems = serverSetmealItems;
    }

    public boolean hasServerSetmeal() {
        return Utils.isNotEmpty(serverSetmealItems);
    }

    @Override
    public ShopcartItem modifyDish() {
        if (getStatusFlag() != StatusFlag.VALID || getInvalidType() == InvalidType.DELETE
                || getInvalidType() == InvalidType.RETURN_QTY || getInvalidType() == InvalidType.SPLIT
                || getInvalidType() == InvalidType.MODIFY_DISH) {
            throw new RuntimeException("The item's state is wrong!");
        }
        return ShopcartItemUtils.modifyDishCopy(this);
    }

    @Override
    public void cancelModifyDish() {
        if (getInvalidType() != InvalidType.MODIFY_DISH || getStatusFlag() != StatusFlag.INVALID) {
            throw new RuntimeException("The item's state is wrong!");
        }

        ShopcartItemUtils.cancelModifyDish(this);
    }

    @Override
    public IShopcartItem split() {
        if (getStatusFlag() != StatusFlag.VALID || getInvalidType() == InvalidType.DELETE
                || getInvalidType() == InvalidType.RETURN_QTY || getInvalidType() == InvalidType.SPLIT
                || getInvalidType() == InvalidType.MODIFY_DISH) {
            return null;
        }
        return ShopcartItemUtils.spliteCopy(this);
    }

    @Override
    public void cancelSplit() {
        if (getStatusFlag() != StatusFlag.INVALID && getInvalidType() != InvalidType.SPLIT) {
            throw new RuntimeException("The item's state is wrong!");
        }
        ShopcartItemUtils.cancelSplit(this);
    }

    @Override
    public ReadonlyShopcartItem returnQty(BigDecimal qty, TradeReasonRel reason) {
		/*if (getStatusFlag() != StatusFlag.VALID || getInvalidType() == InvalidType.DELETE
				|| getInvalidType() == InvalidType.RETURN_QTY || getInvalidType() == InvalidType.SPLIT
				|| getInvalidType() == InvalidType.MODIFY_DISH) {
			throw new RuntimeException("The item's state is wrong!");
		}*/
        if (reason == null) {
            returnQtyReason = null;
        } else {
            if (returnQtyReason != null) {
                returnQtyReason.validateUpdate();
                returnQtyReason.setStatusFlag(StatusFlag.VALID);
                returnQtyReason.setReasonId(reason.getReasonId());
                returnQtyReason.setReasonContent(reason.getReasonContent());
            } else {
                reason.validateCreate();
                reason.setOperateType(OperateType.ITEM_RETURN_QTY);
                reason.setRelateId(getId());
                reason.setRelateUuid(getUuid());
                this.returnQtyReason = reason;
            }
        }
        return ShopcartItemUtils.returnQtyCopy(this, qty);
    }

    @Override
    public IShopcartItem returnQty(TradeReasonRel reason) {
        return returnQty(getTotalQty(), reason);
    }

    @Override
    public void cancelReturnQty() {
		/*if (returnQtyReason == null || getInvalidType() != InvalidType.RETURN_QTY
			|| getStatusFlag() != StatusFlag.INVALID) {
			throw new RuntimeException("The item's state is wrong!");
		}*/

        if (returnQtyReason != null) {
            if (returnQtyReason.getId() == null) {
                returnQtyReason = null;
            } else {
                returnQtyReason.validateUpdate();
                returnQtyReason.setStatusFlag(StatusFlag.INVALID);
            }
        }
        ShopcartItemUtils.cancelReturnQty(this);
    }

    @Override
    public TradeReasonRel getReturnQtyReason() {
        return returnQtyReason;
    }

    @Override
    public void setDiscountReasonRel(TradeReasonRel reasonRel) {
        this.reasonRel = reasonRel;
    }

    @Override
    public TradeReasonRel getDiscountReasonRel() {
        return reasonRel;
    }

    @Override
    public BigDecimal getSingleQty() {
        return tradeItem.getQuantity();
    }

    @Override
    public DishSetmealManager getSetmealManager() {
        return null;
    }

    @Override
    public List<ReadonlySetmealShopcartItem> getSetmealItems() {
        return setmealItems;
    }

    @Override
    public List<? extends ISetmealShopcartItem> getServerItems() {
        return serverSetmealItems;
    }

    @Override
    public void setProperties(List<ReadonlyOrderProperty> properties) {
        super.setProperties(properties);
    }

    @Override
    public void setExtraItems(List<ReadonlyExtraShopcartItem> extraItems) {
        super.setExtraItems(extraItems);
    }

    @Override
    public void deleteReturnQty() {
        super.deleteReturnQty();
        if (setmealItems != null) {
            for (ReadonlySetmealShopcartItem item : setmealItems) {
                item.deleteReturnQty();
            }
        }
    }

    @Override
    public void deleteModifyDish() {
        super.deleteModifyDish();
        if (setmealItems != null) {
            for (ReadonlySetmealShopcartItem item : setmealItems) {
                item.deleteModifyDish();
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        if (setmealItems != null) {
            for (ReadonlySetmealShopcartItem item : setmealItems) {
                item.delete();
            }
        }
    }


    @Override
    public String getTradePlanUUID() {
        // TODO Auto-generated method stub
        return tradePlanUuid;
    }

    @Override
    public void setTradePlanUUID(String tradePlanUuid) {
        // TODO Auto-generated method stub
        this.tradePlanUuid = tradePlanUuid;
    }

    @Override
    public void addSetmeal(ISetmealShopcartItem setmealShopcartItem) {
        if (setmealItems == null) {
            setmealItems = new ArrayList<>();
        }

        if (setmealShopcartItem != null && setmealShopcartItem instanceof ReadonlySetmealShopcartItem) {
            setmealItems.add((ReadonlySetmealShopcartItem) setmealShopcartItem);
        }
    }

    @Override
    public void deleteSetmeal(ISetmealShopcartItem setmealShopcartItem) {
        if (Utils.isEmpty(setmealItems)) {
            return;
        }
        if (setmealShopcartItem != null && setmealShopcartItem instanceof ReadonlySetmealShopcartItem) {
            for (int i = setmealItems.size() - 1; i >= 0; i--) {
                ReadonlySetmealShopcartItem setmealItem = setmealItems.get(i);
                if (setmealShopcartItem.getUuid().equals(setmealItem.getUuid())) {
                    setmealItems.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public boolean isWaiting() {
        //套餐是否等待中，需要判断所有子菜，有一个子菜不是等待中，就返回false，即不是等待中
        if (hasSetmeal()) {
            for (ReadonlySetmealShopcartItem setmeal : setmealItems) {
                if (!setmeal.isWaiting()) {
                    return false;
                }
            }

            return true;
        } else {
            return super.isWaiting();
        }
    }

    @Override
    public long getClientCreateTime() {
        return 0;
    }

    @Override
    public void setIssueStatus(IssueStatus mIssueStatus) {
        super.setIssueStatus(mIssueStatus);
        //更新子菜的打印状态
        if (Utils.isNotEmpty(getSetmealItems())) {
            for (ReadonlySetmealShopcartItem setmealShopcartItem : getSetmealItems()) {
                setmealShopcartItem.setIssueStatus(mIssueStatus);
            }
        }
    }

    @Override
    public void setGuestPrinted(GuestPrinted guestPrinted) {
        super.setGuestPrinted(guestPrinted);
        //更新子菜的打印状态
        if (Utils.isNotEmpty(getSetmealItems())) {
            for (ReadonlySetmealShopcartItem setmealShopcartItem : getSetmealItems()) {
                setmealShopcartItem.setGuestPrinted(guestPrinted);
            }
        }
    }
}
