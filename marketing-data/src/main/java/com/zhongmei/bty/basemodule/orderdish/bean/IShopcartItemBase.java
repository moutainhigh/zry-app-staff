package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.discount.bean.AppletPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.CardServicePrivilegeVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.yunfu.db.entity.dish.DishShop;
import com.zhongmei.yunfu.db.entity.dish.TradeItemOperation;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.enums.ShopcartItemType;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.DishType;
import com.zhongmei.yunfu.db.enums.InvalidType;
import com.zhongmei.yunfu.db.enums.IssueStatus;
import com.zhongmei.yunfu.db.enums.PrintOperationOpType;
import com.zhongmei.yunfu.db.enums.SaleType;
import com.zhongmei.yunfu.db.enums.ServingStatus;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;


public interface IShopcartItemBase {

        final static int NOT_WAKE_UP = 0;
        final static int HAS_WAKE_UP = 1;
        final static int CANNOT_WAKE_UP = 2;


    Long getId();

    String getUuid();

        Long getBatchId();

    Long getServerUpdateTime();

    Long getServerCreateTime();

    String getParentUuid();

    DishShop getDishShop();

    Long getSkuId();

    String getSkuUuid();

    String getSkuName();

    DishType getType();


    Bool getIsChangePrice();


    Bool getIsChangedPrice();

    BigDecimal getPrice();

    String getUnitName();

    SaleType getSaleType();


    BigDecimal getSingleQty();


    BigDecimal getTotalQty();

    BigDecimal getAmount();

        BigDecimal getFeedsAmount();

    BigDecimal getPropertyAmount();

    BigDecimal getActualAmount();

    String getMemo();


    Bool getEnableWholePrivilege();

    List<? extends IOrderProperty> getProperties();

    Collection<? extends IExtraShopcartItem> getExtraItems();


    void deleteReturnQty();


    void deleteModifyDish();


    void delete();


    void recoveryDelete();


    StatusFlag getStatusFlag();


    InvalidType getInvalidType();


    Long getRelateTradeItemId();


    String getRelateTradeItemUuid();


    BigDecimal getReturnQty();


    TradeReasonRel getReturnQtyReason();


    String getBatchNo();


    String getTradeTableUuid();


    Long getTradeTableId();


    IssueStatus getIssueStatus();


    void setIssueStatus(IssueStatus mIssueStatus);

    void setIssueStatusWithoutSetmeal(IssueStatus issueStatus);

    TradePrivilege getPrivilege();

    void setPrivilege(TradePrivilege privilege);

    CouponPrivilegeVo getCouponPrivilegeVo();

    void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegeVo);


    ServingStatus getServingStatus();


    boolean isChanged();

    void setChanged(boolean changed);

    boolean isSelected();

    void setSelected(boolean value);

    int getIndex();

    void setIndex(int index);

    void setPack(boolean value);

    boolean getPack();


    void setReturnInventory(boolean value);

    boolean getReturnInventory();



    void setDiscountReasonRel(TradeReasonRel reasonRel);


    TradeReasonRel getDiscountReasonRel();





    void setIsGroupDish(boolean isGroup);

    boolean isGroupDish();


    List<TradeItemOperation> getTradeItemOperations();


    void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations);


    int canWakeUp();


    boolean canRiseDish();


    boolean canWakeUpCancel();


    boolean canRiseDishCancel();



    boolean canRemindDish();


    void addOperation(PrintOperationOpType opType);


    void removeOperation(PrintOperationOpType opType);


    boolean hasWakeUp();


    boolean hasRiseUp();


    boolean hasUnsaveRemindDish();

    TradeItemExtraDinner getTradeItemExtraDinner();

    void setTradeItemExtraDinner(TradeItemExtraDinner tradeItemExtraDinner);

    public ShopcartItemType getShopcartItemType();

    public void setShopcartItemType(ShopcartItemType shopcartItemType);


    public void setMemo(String memo);

    public List<TradeUser> getTradeItemUserList();

    public void setTradeItemUserList(List<TradeUser> tradeItemUserList);

    public CardServicePrivilegeVo getCardServicePrivilgeVo();

    public void setCardServicePrivilegeVo(CardServicePrivilegeVo cardServicePrivilegeVo);

    public AppletPrivilegeVo getAppletPrivilegeVo();

    public void setAppletPrivilegeVo(AppletPrivilegeVo appletPrivilegeVo);
}
