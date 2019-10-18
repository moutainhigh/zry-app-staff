package com.zhongmei.bty.basemodule.orderdish.bean;

import android.util.Log;

import com.zhongmei.bty.basemodule.devices.mispos.data.CardSaleInfo;
import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRel;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.util.Beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TradeItemVo implements java.io.Serializable, NoProGuard {


    private static final long serialVersionUID = 1L;

    private static final String TAG = TradeItemVo.class.getSimpleName();


    private TradeItem tradeItem;


    private TradePrivilege tradeItemPrivilege;


    private List<TradeItemProperty> tradeItemPropertyList;


    private List<TradeReasonRel> reasonRelList;


    private List<CardSaleInfo> cardSaleInfos;


    private List<TradeItemOperation> tradeItemOperations;


    private CouponPrivilegeVo couponPrivilegeVo;


    private TradeItemExtra tradeItemExtra;

    private BigDecimal kdsScratchDishQty;

    private TradeItemExtraDinner tradeItemExtraDinner;



    private BigDecimal modifyQuantity;
    private List<TradeItemMainBatchRel> tradeItemMainBatchRelList;


    private List<TradeUser> tradeItemUserList;

    private CardServicePrivilegeVo cardServicePrivilegeVo;

    private AppletPrivilegeVo appletPrivilegeVo;

    private ShopcartItemType shopcartItemType = ShopcartItemType.COMMON;



    public CouponPrivilegeVo getCouponPrivilegeVo() {
        return couponPrivilegeVo;
    }

    public void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegeVo) {
        this.couponPrivilegeVo = couponPrivilegeVo;
    }

    public TradeItem getTradeItem() {
        return tradeItem;
    }

    public void setTradeItem(TradeItem tradeItem) {
        this.tradeItem = tradeItem;
    }

    public TradePrivilege getTradeItemPrivilege() {
        return tradeItemPrivilege;
    }

    public void setTradeItemPrivilege(TradePrivilege tradeItemPrivilege) {
        this.tradeItemPrivilege = tradeItemPrivilege;
    }

    public List<TradeItemProperty> getTradeItemPropertyList() {
        return tradeItemPropertyList;
    }

    public void setTradeItemPropertyList(List<TradeItemProperty> tradeItemPropertyList) {
        this.tradeItemPropertyList = tradeItemPropertyList;
    }

    public List<TradeReasonRel> getReasonRelList() {
        return reasonRelList;
    }

    public void setReasonRelList(List<TradeReasonRel> reasonRelList) {
        this.reasonRelList = reasonRelList;
    }

    public TradeReasonRel getRejectQtyReason() {
        return getReason(OperateType.ITEM_RETURN_QTY);
    }


    public void setRejectQtyReason(TradeReasonRel rejectQtyReason) {
        setReason(rejectQtyReason, OperateType.ITEM_RETURN_QTY);
    }


    @Deprecated
    public TradeReasonRel getDiscountReason() {
        return getReason(OperateType.ITEM_GIVE);
    }

    public void setDiscountReason(TradeReasonRel discountReason) {
        setReason(discountReason, discountReason.getOperateType());
    }

    public TradeReasonRel getReason(OperateType operateType) {
        if (reasonRelList != null) {
            for (TradeReasonRel tradeReasonRel : reasonRelList) {
                if (tradeReasonRel.getOperateType() == operateType && tradeReasonRel.isValid()) {
                    return tradeReasonRel;
                }
            }
        }
        return null;
    }

    public TradeReasonRel getReasonLast() {
        if (reasonRelList != null && reasonRelList.size() > 0) {
            TradeReasonRel tradeReasonRel = reasonRelList.get(reasonRelList.size() - 1);
            return tradeReasonRel.isValid() ? tradeReasonRel : null;
        }
        return null;
    }


    public TradeReasonRel getReasonLast2() {
        if (reasonRelList != null && reasonRelList.size() > 0) {
            return reasonRelList.get(reasonRelList.size() - 1);
        }
        return null;
    }

    public void setReason(TradeReasonRel reason, OperateType operateType) {
        if (reasonRelList == null) {
            reasonRelList = new ArrayList<>();
        }

                for (TradeReasonRel tradeReasonRel : reasonRelList) {
            if (tradeReasonRel.getOperateType() == operateType) {
                try {
                    Beans.copyProperties(reason, tradeReasonRel);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }

                return;
            }
        }

                reasonRelList.add(reason);
    }

    public void removeTradeReasonRel(TradeReasonRel tradeReasonRel, OperateType operateType) {
        if (reasonRelList == null || reasonRelList.isEmpty()) {
            return;
        }
        for (int i = reasonRelList.size() - 1; i >= 0; i--) {
            if (reasonRelList.get(i).getOperateType() == operateType) {
                if (tradeReasonRel.getId() == null) {
                    reasonRelList.remove(i);
                } else {
                    reasonRelList.get(i).setStatusFlag(StatusFlag.INVALID);
                    reasonRelList.get(i).setChanged(true);
                }
            }
        }
    }


    public boolean isChanged() {
        if (TradeItem.isChanged(tradeItem)) {
            return true;
        }
        if (TradeItem.isChanged(tradeItemPrivilege)) {
            return true;
        }
        if (tradeItemPropertyList != null) {
            for (TradeItemProperty entity : tradeItemPropertyList) {
                if (TradeItem.isChanged(entity)) {
                    return true;
                }
            }
        }

        if (reasonRelList != null) {
            for (TradeReasonRel reasonRel : reasonRelList) {
                if (TradeItem.isChanged(reasonRel)) {
                    return true;
                }
            }
        }

        if (tradeItemOperations != null) {
            for (TradeItemOperation entity : tradeItemOperations) {
                if (TradeItem.isChanged(entity)) {
                    return true;
                }
            }
        }
        if (tradeItemMainBatchRelList != null) {
            for (TradeItemMainBatchRel entity : tradeItemMainBatchRelList) {
                if (TradeItem.isChanged(entity)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public TradeItemVo clone() {
        TradeItemVo vo = new TradeItemVo();
        try {
            if (tradeItem != null) {
                vo.setTradeItem(tradeItem.clone());
            }
            if (tradeItemPrivilege != null) {
                vo.setTradeItemPrivilege(tradeItemPrivilege.clone());
            }
            if (couponPrivilegeVo != null) {
                vo.setCouponPrivilegeVo(couponPrivilegeVo.clone());
            }
            if (tradeItemPropertyList != null) {
                List<TradeItemProperty> newList = new ArrayList<TradeItemProperty>();
                for (TradeItemProperty source : tradeItemPropertyList) {
                    newList.add(source.clone());
                }
                vo.setTradeItemPropertyList(newList);
            }

            if (tradeItemExtra != null) {
                vo.setTradeItemExtra(tradeItemExtra.clone());
            }

            if (reasonRelList != null && !reasonRelList.isEmpty()) {
                List<TradeReasonRel> newReasonRelList = new ArrayList<TradeReasonRel>();
                for (TradeReasonRel reasonRel : reasonRelList) {
                    newReasonRelList.add(Beans.copyEntity(reasonRel, new TradeReasonRel()));
                }
                vo.setReasonRelList(newReasonRelList);
            }

            if (cardSaleInfos != null) {
                List<CardSaleInfo> newCardSaleInfoList = new ArrayList<CardSaleInfo>();

                for (CardSaleInfo source : cardSaleInfos) {
                    newCardSaleInfoList.add(Beans.copyEntity(source, new CardSaleInfo()));
                }
                vo.setCardSaleInfos(newCardSaleInfoList);
            }
                        if (tradeItemOperations != null) {
                List<TradeItemOperation> tradeItemOperationList = new ArrayList<TradeItemOperation>();
                for (TradeItemOperation source : tradeItemOperations) {
                    tradeItemOperationList.add(Beans.copyEntity(source, new TradeItemOperation()));
                }
                vo.setTradeItemOperations(tradeItemOperationList);
            }

            vo.setKdsScratchDishQty(kdsScratchDishQty);
            if (tradeItemExtraDinner != null) {
                vo.setTradeItemExtraDinner(tradeItemExtraDinner);
            }
            if (tradeItemMainBatchRelList != null) {
                List<TradeItemMainBatchRel> tradeItemBatchList = new ArrayList<TradeItemMainBatchRel>();
                for (TradeItemMainBatchRel tradeItemMainBatchRel : tradeItemBatchList) {
                    tradeItemBatchList.add(tradeItemMainBatchRel);
                }
                vo.setTradeItemMainBatchRelList(tradeItemBatchList);
            }

            if (tradeItemUserList != null) {
                List<TradeUser> tradeItemUserList = new ArrayList<TradeUser>();
                for (TradeUser tradeItemUser : tradeItemUserList) {
                    tradeItemUserList.add(tradeItemUser);
                }
                vo.setTradeItemUserList(tradeItemUserList);
            }
            if (cardServicePrivilegeVo != null) {
                vo.setCardServicePrivilegeVo(cardServicePrivilegeVo);
            }

            if (appletPrivilegeVo != null) {
                vo.setAppletPrivilegeVo(appletPrivilegeVo);
            }
            return vo;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error!", e);
        }
        return null;
    }


    public void updateAsyncTradeInfo(Long tradeId, String tradeUuid, TradeTable tradeTable) {
        if (tradeItem != null) {
            tradeItem.setTradeId(tradeId);
            tradeItem.setTradeUuid(tradeUuid);
            tradeItem.setTradeTableId(tradeTable.getId());
            tradeItem.setTradeTableUuid(tradeTable.getUuid());
        }
        if (tradeItemPrivilege != null) {
            tradeItemPrivilege.setTradeId(tradeId);
            tradeItemPrivilege.setTradeUuid(tradeUuid);
        }
        if (couponPrivilegeVo != null && couponPrivilegeVo.getTradePrivilege() != null) {
            couponPrivilegeVo.getTradePrivilege().setTradeId(tradeId);
            couponPrivilegeVo.getTradePrivilege().setTradeUuid(tradeUuid);
        }

        if (cardSaleInfos != null) {
            for (CardSaleInfo cardSaleInfo : cardSaleInfos) {
                cardSaleInfo.setTradeId(tradeId);
            }
        }
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
        TradeItemVo other = (TradeItemVo) obj;
        if (tradeItem == null) {
            if (other.tradeItem != null)
                return false;
        } else if (!tradeItem.equals(other.tradeItem))
            return false;
        return true;
    }

    public List<CardSaleInfo> getCardSaleInfos() {
        return cardSaleInfos;
    }

    public void setCardSaleInfos(List<CardSaleInfo> cardSaleInfos) {
        this.cardSaleInfos = cardSaleInfos;
    }

    public List<TradeItemOperation> getTradeItemOperations() {
        return tradeItemOperations;
    }

    public void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations) {
        this.tradeItemOperations = tradeItemOperations;
    }

    public TradeItemExtra getTradeItemExtra() {
        return tradeItemExtra;
    }

    public void setTradeItemExtra(TradeItemExtra tradeItemExtra) {
        this.tradeItemExtra = tradeItemExtra;
    }


    public boolean isPack() {
        return this.tradeItemExtra != null && this.tradeItemExtra.getIsPack() != null && this.tradeItemExtra.getIsPack() == Bool.YES;
    }


    public boolean isHasCouponPrivileage() {
        return couponPrivilegeVo != null && couponPrivilegeVo.getTradePrivilege() != null;
    }

    public BigDecimal getKdsScratchDishQty() {
        return kdsScratchDishQty;
    }

    public void setKdsScratchDishQty(BigDecimal kdsScratchDishQty) {
        this.kdsScratchDishQty = kdsScratchDishQty;
    }

    public TradeItemExtraDinner getTradeItemExtraDinner() {
        return tradeItemExtraDinner;
    }

    public void setTradeItemExtraDinner(TradeItemExtraDinner tradeItemExtraDinner) {
        this.tradeItemExtraDinner = tradeItemExtraDinner;
    }

    public Integer getServingOrder() {
        return (tradeItemExtraDinner == null || tradeItemExtraDinner.getServingOrder() == null) ? 0 : tradeItemExtraDinner.getServingOrder();
    }

    public BigDecimal getModifyQuantity() {
        return modifyQuantity;
    }

    public void setModifyQuantity(BigDecimal modifyQuantity) {
        this.modifyQuantity = modifyQuantity;
    }



    public List<TradeItemMainBatchRel> getTradeItemMainBatchRelList() {
        return tradeItemMainBatchRelList;
    }

    public void setTradeItemMainBatchRelList(List<TradeItemMainBatchRel> tradeItemMainBatchRelList) {
        this.tradeItemMainBatchRelList = tradeItemMainBatchRelList;
    }

    public ShopcartItemType getShopcartItemType() {
        return shopcartItemType;
    }

    public void setShopcartItemType(ShopcartItemType shopcartItemType) {
        this.shopcartItemType = shopcartItemType;
    }

    public List<TradeUser> getTradeItemUserList() {
        return tradeItemUserList;
    }

    public void setTradeItemUserList(List<TradeUser> tradeItemUserList) {
        this.tradeItemUserList = tradeItemUserList;
    }

    public CardServicePrivilegeVo getCardServicePrivilegeVo() {
        return cardServicePrivilegeVo;
    }

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
