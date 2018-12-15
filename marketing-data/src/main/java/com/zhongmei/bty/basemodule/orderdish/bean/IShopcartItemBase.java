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

/**
 * @version: 1.0
 * @date 2015年7月10日
 */
public interface IShopcartItemBase {

    //可以进行等叫操作，但还未等叫
    final static int NOT_WAKE_UP = 0;
    //可以进行等叫操作，并且已经等叫
    final static int HAS_WAKE_UP = 1;
    //不能进行等叫操作
    final static int CANNOT_WAKE_UP = 2;

    /**
     * 未保存时此字段为null
     *
     * @return
     */
    Long getId();

    String getUuid();

    //联台子单中 针对主单菜时关联的主单批量菜的tradeItem id
    Long getBatchId();

    Long getServerUpdateTime();

    String getParentUuid();

    DishShop getDishShop();

    Long getSkuId();

    String getSkuUuid();

    String getSkuName();

    DishType getType();

    /**
     * 是否可以修改单价
     *
     * @return
     */
    Bool getIsChangePrice();

    /**
     * 是否变价
     */
    Bool getIsChangedPrice();

    BigDecimal getPrice();

    String getUnitName();

    SaleType getSaleType();

    /**
     * 获取不受父一级数量影响的单份数量
     *
     * @return
     */
    BigDecimal getSingleQty();

    /**
     * 获取受父一级数量影响的数量
     *
     * @return
     */
    BigDecimal getTotalQty();

    BigDecimal getAmount();

    //获取加料价格
    BigDecimal getFeedsAmount();

    BigDecimal getPropertyAmount();

    BigDecimal getActualAmount();

    String getMemo();

    /**
     * 是否参与整单优惠的计算
     *
     * @return
     */
    Bool getEnableWholePrivilege();

    List<? extends IOrderProperty> getProperties();

    Collection<? extends IExtraShopcartItem> getExtraItems();

    /**
     * 退菜的新菜被删除，表示退菜被撤回
     */
    void deleteReturnQty();

    /**
     * 改菜的新菜被删除，表示改菜被撤回
     */
    void deleteModifyDish();

    /**
     * 将条目置为无效，在删除已保存的菜品使用
     */
    void delete();

    /**
     * 已删除的菜品恢复
     */
    void recoveryDelete();

    /**
     * 返回条目的有效(无效)状态
     *
     * @return
     */
    StatusFlag getStatusFlag();

    /**
     * 返回导致条目无效的操作类型
     *
     * @return
     */
    InvalidType getInvalidType();

    /**
     * 返回此条目的来源条目ID(即是由哪个条目进行拆单或修改而来的)
     *
     * @return
     */
    Long getRelateTradeItemId();

    /**
     * 返回此条目的来源条目UUID(即是由哪个条目进行拆单或修改而来的)
     *
     * @return
     */
    String getRelateTradeItemUuid();

    /**
     * 获取退菜数量
     *
     * @return
     */
    BigDecimal getReturnQty();

    /**
     * 获取退菜理由
     *
     * @return
     */
    TradeReasonRel getReturnQtyReason();

    /**
     * 出单批次号
     *
     * @return
     */
    String getBatchNo();

    /**
     * 菜品所属的桌台信息记录的UUID
     *
     * @return
     */
    String getTradeTableUuid();

    /**
     * 菜品所属的桌台信息记录的ID
     *
     * @return
     */
    Long getTradeTableId();

    /**
     * 出单状态
     *
     * @return
     */
    IssueStatus getIssueStatus();

    /**
     * 设置出单状态，包含子菜和加料
     *
     * @param mIssueStatus
     */
    void setIssueStatus(IssueStatus mIssueStatus);

    void setIssueStatusWithoutSetmeal(IssueStatus issueStatus);

    TradePrivilege getPrivilege();

    void setPrivilege(TradePrivilege privilege);

    CouponPrivilegeVo getCouponPrivilegeVo();

    void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegeVo);

    /**
     * 返回上菜状态
     *
     * @return
     */
    ServingStatus getServingStatus();

    /**
     * 判断是否被修改
     *
     * @return
     */
    boolean isChanged();

    void setChanged(boolean changed);

    boolean isSelected();

    void setSelected(boolean value);

    int getIndex();

    void setIndex(int index);

    void setPack(boolean value);

    boolean getPack();

    /**
     * 是否退回库存
     */
    void setReturnInventory(boolean value);

    boolean getReturnInventory();


    /**
     * 设置菜品折扣理由
     *
     * @param reasonRel
     */
    void setDiscountReasonRel(TradeReasonRel reasonRel);

    /**
     * 获取菜品折扣理由
     *
     * @return
     */
    TradeReasonRel getDiscountReasonRel();

    /**
     * 获取已划菜数量
     * @return
     */
    //int getKdsScratchDishQty();

    //void setKdsScratchDishQty(int kdsScratchDishQty);

    /**
     * 是否是团餐、自助餐的组点
     *
     * @param isGroup
     */
    void setIsGroupDish(boolean isGroup);

    boolean isGroupDish();

    /**
     * 获取催菜列表（目前只针对于单菜和套餐外壳）
     *
     * @Title: getRemindDishes
     * @Return List<PrintOperation> 返回类型
     */
    List<TradeItemOperation> getTradeItemOperations();

    /**
     * 设置催菜列表（目前只针对于单菜和套餐外壳）
     *
     * @Title: setRemindDishes
     * @Return void 返回类型
     */
    void setTradeItemOperations(List<TradeItemOperation> tradeItemOperations);

    /**
     * 此菜是否可以等叫
     *
     * @Title: canWakeUp
     * @Return boolean 返回类型 0表示未等叫，1表示已等叫，2表示禁用等叫
     */
    int canWakeUp();

    /**
     * 此菜是否可以起菜
     *
     * @Title: canRiseDish
     * @Return boolean 返回类型
     */
    boolean canRiseDish();

    /**
     * 此菜是否可以取消等叫
     *
     * @Title: canWakeUp
     * @Return boolean 返回类型
     */
    boolean canWakeUpCancel();

    /**
     * 此菜是否可以取消起菜
     *
     * @Title: canRiseDish
     * @Return boolean 返回类型
     */
    boolean canRiseDishCancel();


    /**
     * 此菜是否可以催菜
     *
     * @Title: canRemindDish
     * @Return boolean 返回类型
     */
    boolean canRemindDish();

    /**
     * 添加菜品操作
     *
     * @Title: addOperation
     * @Return void 返回类型
     */
    void addOperation(PrintOperationOpType opType);

    /**
     * 移除菜品操作
     *
     * @Title: removeOperation
     * @Return void 返回类型
     */
    void removeOperation(PrintOperationOpType opType);

    /**
     * 是否有等叫操作
     *
     * @Title: hasUnsaveWakeUp
     * @Return boolean 返回类型
     */
    boolean hasWakeUp();

    /**
     * 是否有起操作
     *
     * @Title: hasRiseUp
     * @Return boolean 返回类型
     */
    boolean hasRiseUp();

    /**
     * 是否有未保存的催菜
     *
     * @Title: hasUnsaveRemindDish
     * @Description: TODO
     * @Param @return TODO
     * @Return boolean 返回类型
     */
    boolean hasUnsaveRemindDish();

    TradeItemExtraDinner getTradeItemExtraDinner();

    void setTradeItemExtraDinner(TradeItemExtraDinner tradeItemExtraDinner);

    public ShopcartItemType getShopcartItemType();

    public void setShopcartItemType(ShopcartItemType shopcartItemType);

    /**
     * 设置备注
     *
     * @param memo
     */
    public void setMemo(String memo);

    public List<TradeUser> getTradeItemUserList();

    public void setTradeItemUserList(List<TradeUser> tradeItemUserList);

    public CardServicePrivilegeVo getCardServicePrivilgeVo();

    public void setCardServicePrivilegeVo(CardServicePrivilegeVo cardServicePrivilegeVo);

    public AppletPrivilegeVo getAppletPrivilegeVo();

    public void setAppletPrivilegeVo(AppletPrivilegeVo appletPrivilegeVo);
}
