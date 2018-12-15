package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.discount.bean.salespromotion.SalesPromotionRuleVo;
import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.bty.basemodule.orderdish.utils.ShopcartItemUtils;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.Utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 单品或套餐条目
 *
 * @version: 1.0
 * @date 2015年7月10日
 */
public class ShopcartItem extends ShopcartItemBase<OrderDish> implements IShopcartItem {

    /**
     * 套餐的Manager。单品时为null
     */
    private final DishSetmealManager setmealManager;

    /**
     * 套餐明细列表。单品时为null
     */
    private List<SetmealShopcartItem> setmealItems;

    //tradePlanActivity表的uuid
    private String tradePlanUuid;
    /**
     * 理由rel
     */
    private TradeReasonRel reasonRel;

    private long clientCreateTime;

    private int policyType;//营销策略类型
    private SalesPromotionRuleVo salesPromotionRuleVo;


    public int getPolicyType() {
        return policyType;
    }

    public void setPolicyType(int policyType) {
        this.policyType = policyType;
    }

    public SalesPromotionRuleVo getSalesPromotionRuleVo() {
        return salesPromotionRuleVo;
    }

    public void setSalesPromotionRuleVo(SalesPromotionRuleVo salesPromotionRuleVo) {
        this.salesPromotionRuleVo = salesPromotionRuleVo;
    }

    public ShopcartItem(String uuid, OrderDish orderDish) {
        this(uuid, orderDish, null);
    }

    public ShopcartItem(String uuid, OrderDish orderDish, ItemMetadata metadata) {
        super(uuid, orderDish, null, metadata);
        if (orderDish.getDishShop().getType() == DishType.COMBO) {
            setmealManager = new DishSetmealManager(this);
        } else {
            setmealManager = null;
        }
        clientCreateTime = System.currentTimeMillis();
    }

    public void setSetmealItems(List<SetmealShopcartItem> setmealItems) {
        this.setmealItems = setmealItems;
    }

    public boolean hasSetmeal() {
        return Utils.isNotEmpty(setmealItems);
    }

    @Override
    public IShopcartItem split() {
        if (getStatusFlag() != StatusFlag.VALID || getInvalidType() == InvalidType.DELETE
                || getInvalidType() == InvalidType.RETURN_QTY || getInvalidType() == InvalidType.SPLIT) {
            return null;
        }
        return ShopcartItemUtils.splitCopy(this);
    }

    @Override
    public void cancelSplit() {
        if (getStatusFlag() != StatusFlag.INVALID && getInvalidType() != InvalidType.SPLIT) {
            throw new RuntimeException("The item's state is wrong!");
        }
        ShopcartItemUtils.cancelSplit(this);
    }

    @Override
    public ShopcartItem modifyDish() {
        throw new UnsupportedOperationException("Use changeQty!");
    }

    @Override
    public void cancelModifyDish() {
        throw new UnsupportedOperationException("Use changeQty!");
    }

    @Override
    public ReadonlyShopcartItem returnQty(BigDecimal qty, TradeReasonRel reason) {
        throw new UnsupportedOperationException("Use changeQty!");
    }

    @Override
    public IShopcartItem returnQty(TradeReasonRel reason) {
        throw new UnsupportedOperationException("Use changeQty!");
    }

    @Override
    public void cancelReturnQty() {
        throw new UnsupportedOperationException("Use changeQty!");
    }

    @Override
    public DishSetmealManager getSetmealManager() {
        return setmealManager;
    }

    @Override
    public List<SetmealShopcartItem> getSetmealItems() {
        return setmealItems;
    }

    @Override
    public BigDecimal getActualAmount() {
        BigDecimal actualPrice = BigDecimal.ZERO;
        actualPrice = super.getActualAmount();
        if (setmealItems != null) {
            for (SetmealShopcartItem setmealItem : setmealItems) {
                actualPrice = actualPrice.add(setmealItem.getActualAmount());
            }
        }
        return actualPrice;
    }

    @Override
    public void changeQty(BigDecimal singleQty) {
        super.changeQty(singleQty);
        // 修改套餐明细的数量
        if (setmealItems != null) {
            for (SetmealShopcartItem setmealItem : setmealItems) {
                setmealItem.changeQty(setmealItem.getSingleQty());
            }
        }
    }

    @Override
    public void deleteReturnQty() {
        super.deleteReturnQty();
        if (setmealItems != null) {
            for (SetmealShopcartItem setmealItem : setmealItems) {
                setmealItem.deleteReturnQty();
            }
        }
    }

    @Override
    public void deleteModifyDish() {
        super.deleteModifyDish();
        if (setmealItems != null) {
            for (SetmealShopcartItem setmealItem : setmealItems) {
                setmealItem.deleteModifyDish();
            }
        }
    }

    @Override
    public void delete() {
        super.delete();
        if (setmealItems != null) {
            for (SetmealShopcartItem setmealItem : setmealItems) {
                setmealItem.delete();
            }
        }
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
    public void setTradeTable(String tradeTableUuid, Long tradeTableId) {
        super.setTradeTable(tradeTableUuid, tradeTableId);
        if (setmealItems != null) {
            for (SetmealShopcartItem setmealItem : setmealItems) {
                setmealItem.setTradeTable(tradeTableUuid, tradeTableId);
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

        if (setmealShopcartItem != null && setmealShopcartItem instanceof SetmealShopcartItem) {
            setmealItems.add((SetmealShopcartItem) setmealShopcartItem);
        }
    }

    @Override
    public void deleteSetmeal(ISetmealShopcartItem setmealShopcartItem) {
        if (Utils.isEmpty(setmealItems)) {
            return;
        }
        if (setmealShopcartItem != null && setmealShopcartItem instanceof SetmealShopcartItem) {
            for (int i = setmealItems.size() - 1; i >= 0; i--) {
                SetmealShopcartItem setmealItem = setmealItems.get(i);
                if (setmealShopcartItem.getUuid().equals(setmealItem.getUuid())) {
                    setmealItems.remove(i);
                    break;
                }
            }
        }
    }

    @Override
    public long getClientCreateTime() {
        return clientCreateTime;
    }

    @Override
    public void setIssueStatus(IssueStatus issueStatus) {
        super.setIssueStatus(issueStatus);
        //更新子菜的打印状态
        if (Utils.isNotEmpty(getSetmealItems())) {
            for (SetmealShopcartItem setmealShopcartItem : getSetmealItems()) {
                setmealShopcartItem.setIssueStatus(issueStatus);
            }
        }
    }
}
