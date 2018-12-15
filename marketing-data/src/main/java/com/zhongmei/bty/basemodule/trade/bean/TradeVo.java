package com.zhongmei.bty.basemodule.trade.bean;

import android.util.Log;

import com.zhongmei.bty.basemodule.devices.mispos.data.message.ChangeCardReq;
import com.zhongmei.bty.basemodule.devices.mispos.data.message.JCRechargeCardReq;
import com.zhongmei.bty.basemodule.discount.bean.BanquetVo;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.discount.manager.PrivilegeApportionManager;
import com.zhongmei.bty.basemodule.inventory.bean.InventoryVo;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.orderdish.entity.TradeItemExtraDinner;
import com.zhongmei.yunfu.db.entity.trade.PaymentItem;
import com.zhongmei.bty.basemodule.shoppingcart.utils.ExtraChargeTool;
import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.yunfu.db.entity.trade.TradeCreditLog;
import com.zhongmei.bty.basemodule.trade.entity.TradeDepositPayRelation;
import com.zhongmei.bty.basemodule.trade.entity.TradeExtraSecrecyPhone;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoice;
import com.zhongmei.bty.basemodule.trade.entity.TradeInvoiceNo;
import com.zhongmei.bty.basemodule.trade.entity.TradeItemMainBatchRelExtra;
import com.zhongmei.bty.basemodule.trade.entity.TradeReceiveLog;
import com.zhongmei.bty.commonmodule.database.entity.TradeEarnestMoney;
import com.zhongmei.bty.commonmodule.database.entity.TradeInitConfig;
import com.zhongmei.bty.commonmodule.database.entity.TradeTax;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.bty.basemodule.trade.message.TradeResp;
import com.zhongmei.yunfu.util.Beans;
import com.zhongmei.yunfu.context.util.NoProGuard;
import com.zhongmei.yunfu.db.entity.trade.Trade;
import com.zhongmei.yunfu.db.entity.trade.TradeCustomer;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.db.entity.trade.TradeExtra;
import com.zhongmei.yunfu.db.entity.trade.TradeItemExtra;
import com.zhongmei.yunfu.db.entity.discount.TradeItemPlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePlanActivity;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilegeExtra;
import com.zhongmei.yunfu.db.entity.discount.TradePromotion;
import com.zhongmei.yunfu.db.entity.trade.TradeItem;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;
import com.zhongmei.bty.basemodule.trade.entity.TradeStatusLog;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.CustomerType;
import com.zhongmei.yunfu.db.enums.OperateType;
import com.zhongmei.yunfu.db.enums.PrivilegeType;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.TradeInitConfigKeyId;
import com.zhongmei.yunfu.db.enums.TradePayStatus;
import com.zhongmei.yunfu.db.enums.TradeType;
import com.zhongmei.yunfu.context.util.GsonUtil;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.db.entity.VerifyKoubeiOrder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**

 */
public class TradeVo implements java.io.Serializable, NoProGuard {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static final String TAG = TradeVo.class.getSimpleName();

    /**
     * 交易主单
     */
    private Trade trade;
    /**
     * 交易扩展
     */
    private TradeExtra tradeExtra;
    /**
     * 第三方隐私手机号
     */
    private TradeExtraSecrecyPhone tradeExtraSecrecyPhone;
    /**
     * 整单优惠和附加费
     */
    private List<TradePrivilege> tradePrivileges;
    /**
     * 附加费
     */
    private Map<Long, ExtraCharge> extraChargeMap;


    //最低消费差额附加费
    private ExtraCharge minconExtraCharge;

    /**
     * 存放优惠的核销 优惠包括：附加费，优惠卷，积分
     */
    private Map<Long, TradePrivilegeExtra> tradePrivilegeExtraMap;

    /**
     * 餐标外壳vo
     */
    private MealShellVo mealShellVo;

    /**
     * 宴请vo
     */
    private BanquetVo banquetVo;

    /**
     * 能参与整单折扣的附加费总金额
     */
    private BigDecimal discountExtracharge = BigDecimal.ZERO;

    /**
     * 烘焙收预定金金额
     */
    private BigDecimal bakeryBookingAmount = BigDecimal.ZERO;

    /**
     * 优惠券
     */
    private CouponPrivilegeVo couponPrivilege;

    /**
     * 超时费记录
     */
    private List<TradePrivilege> outTimeFeePrivaleges;

    /**
     * v7.7修改为多张优惠券
     */

    private List<CouponPrivilegeVo> couponPrivilegeVoList;
    /**
     * 会员积分抵现
     */
    private IntegralCashPrivilegeVo integralCashPrivilegeVo;
    /**
     * 交易的顾客信息列表
     */
    private List<TradeCustomer> tradeCustomerList;
    /**
     * 交易所占用的桌台列表
     */
    private List<TradeTable> tradeTableList;
    /**
     * 交易明细列表
     */
    private List<TradeItemVo> tradeItemList;
    /**
     * 是否是退货单
     */
    private Boolean isSalesReturn = false;

//    private TradeReasonRel operateReason;

    private List<TradeReasonRel> tradeReasonRelList;

    /**
     * 营销活动与订单关系
     */
    private List<TradePlanActivity> tradePlanActivityList;
    /**
     * 营销活动与菜品的关系
     */
    private List<TradeItemPlanActivity> tradeItemPlanActivityList;

    /**
     * 促销活动赠送菜品
     */
//    private List<TradeItemActivityGift> tradeItemActivityGiftList = new ArrayList<>();

    /**
     * 微信卡卷
     */
    private List<WeiXinCouponsVo> mWeiXinCouponsVo;
    /**
     * 订单押金信息
     */
    private TradeDeposit tradeDeposit;

    /**
     * 订单对应的支付信息，状态等
     */
    private TradeDepositPayRelation tradeDepositPayRelation;

    /**
     * 押金支付状态
     */
    private PaymentItem tradeDepositPaymentItem;

    /**
     * TradeItem扩展
     */
    private List<TradeItemExtra> tradeItemExtraList;

    /**
     * 订单接受方
     */
    private TradeReceiveLog tradeReceiveLog;

    /**
     * 不参与折扣金额
     */
    private BigDecimal noJoinDiscount = BigDecimal.ZERO;

    private List<TradeCreditLog> tradeCreditLogList;//挂账信息

    private List<TradeStatusLog> tradeStatusLogList;//用于取就餐时长

    private BigDecimal beforePrivilegeAmount;

    private List<TradePromotion> tradePromotions;//订单促销活动
    /**
     * 自助对象及数量
     */
    private List<TradeBuffetPeople> tradeBuffetPeoples;

    private List<TradeItemVo> tradeBuffetPeopleTradeItems;//本地购物车使用

    private TradeInvoice tradeInvoice;//订单中心展示发票信息

    /**
     * 排队 2、预定 1
     */
    private Integer relatedType;
    /**
     * 预定排队的id
     */
    private Long relatedId;
    //未修改桌台前的桌台数
    private BigDecimal oldDeskCount = BigDecimal.ONE;

    private BigDecimal paidAmount = BigDecimal.ZERO;

    private TradeGroupInfo tradeGroup;

    private ChangeCardReq changeCardReq;
    private JCRechargeCardReq jcRechargeCardReq;

    public InventoryVo inventoryVo;

    private List<TradeItemExtraDinner> tradeItemExtraDinners;

    private TradeUser tradeUser;//add v8.1 订单推销员信息（一单一个推销员）

    private List<TradeUser> tradeUsers;  //add 20180309 增加多销售员支持

    private boolean enableMinConsum = true;     //自助最低消费附加开关

    //子单tradeId列表
    private List<Long> subTradeIdList;

    //缓存子单tradeTable key:TradeTable的id
    private Map<Long, TradeTable> subTableMap;
    //子单 tradeId与tradeTable的缓存
    private Map<Long, TradeTable> subTradeTableMap;

    private List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtraList;

    private List<TradeTax> tradeTaxs;
    private List<TradeInitConfig> tradeInitConfigs;
    //收银改单之前是否是联台主单
    private boolean isBUnionMainTrade = false;
    //当前订单所在桌台id
    private Long tableId;
    //桌台名
    private String tableName;

    private TradeInvoiceNo tradeInvoiceNo;//add 9.0 消费税号

    private List<TradeEarnestMoney> tradeEarnestMoneys;//add v8.13 预定金记录

    private VerifyKoubeiOrder verifyKoubeiOrder;
    // ***********************************************************
    // *  特别注意！添加属性时要注意修改clone()方法与isChanged()方法
    // ***********************************************************

    /**
     * 商品优惠分摊
     */
    public List<PrivilegeApportionManager.ItemApportion> itemApportionList;

    public TradeGroupInfo getTradeGroup() {
        return tradeGroup;
    }

    public void setTradeGroup(TradeGroupInfo tradeGroup) {
        this.tradeGroup = tradeGroup;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public TradeInvoice getTradeInvoice() {
        return tradeInvoice;
    }

    public void setTradeInvoice(TradeInvoice tradeInvoice) {
        this.tradeInvoice = tradeInvoice;
    }

    public List<TradeItemVo> getTradeBuffetPeopleTradeItems() {
        return tradeBuffetPeopleTradeItems;
    }

    public void setTradeBuffetPeopleTradeItems(List<TradeItemVo> tradeBuffetPeopleTradeItems) {
        this.tradeBuffetPeopleTradeItems = tradeBuffetPeopleTradeItems;
    }

    public List<TradePrivilege> getOutTimeFeePrivaleges() {
        return outTimeFeePrivaleges;
    }

    public void setOutTimeFeePrivaleges(List<TradePrivilege> outTimeFeePrivaleges) {
        this.outTimeFeePrivaleges = outTimeFeePrivaleges;
    }

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public TradeExtra getTradeExtra() {
        return tradeExtra;
    }

    public TradeExtraSecrecyPhone getTradeExtraSecrecyPhone() {
        return tradeExtraSecrecyPhone;
    }

    public TradeReceiveLog getTradeReceiveLog() {
        return tradeReceiveLog;
    }

    public void setTradeReceiveLog(TradeReceiveLog tradeReceiveLog) {
        this.tradeReceiveLog = tradeReceiveLog;
    }

    public BigDecimal getBakeryBookingAmount() {
        return bakeryBookingAmount;
    }

    public void setBakeryBookingAmount(BigDecimal bakeryBookingAmount) {
        this.bakeryBookingAmount = bakeryBookingAmount;
    }

    public void setTradeExtra(TradeExtra tradeExtra) {
        this.tradeExtra = tradeExtra;
    }

    public void setTradeExtraSecrecyPhone(TradeExtraSecrecyPhone tradeExtraSecrecyPhone) {
        this.tradeExtraSecrecyPhone = tradeExtraSecrecyPhone;
    }

    public List<WeiXinCouponsVo> getmWeiXinCouponsVo() {
        return mWeiXinCouponsVo;
    }

    public void setmWeiXinCouponsVo(List<WeiXinCouponsVo> mWeiXinCouponsVo) {
        this.mWeiXinCouponsVo = mWeiXinCouponsVo;
    }

    public BigDecimal getNoJoinDiscount() {
        return noJoinDiscount;
    }

    public void setNoJoinDiscount(BigDecimal noJoinDiscount) {
        this.noJoinDiscount = noJoinDiscount;
    }

    public List<TradePromotion> getTradePromotions() {
        return tradePromotions;
    }

    public void setTradePromotions(List<TradePromotion> tradePromotions) {
        this.tradePromotions = tradePromotions;
    }

    public List<TradeBuffetPeople> getTradeBuffetPeoples() {
        return tradeBuffetPeoples;
    }

    public void setTradeBuffetPeoples(List<TradeBuffetPeople> tradeBuffetPeoples) {
        this.tradeBuffetPeoples = tradeBuffetPeoples;
    }

    public Map<Long, TradePrivilegeExtra> getTradePrivilegeExtraMap() {
        return tradePrivilegeExtraMap;
    }

    public void setTradePrivilegeExtraMap(Map<Long, TradePrivilegeExtra> tradePrivilegeExtraMap) {
        this.tradePrivilegeExtraMap = tradePrivilegeExtraMap;
    }

    public List<TradeItemMainBatchRelExtra> getTradeItemMainBatchRelExtraList() {
        return tradeItemMainBatchRelExtraList;
    }

    public void setTradeItemMainBatchRelExtraList(List<TradeItemMainBatchRelExtra> tradeItemMainBatchRelExtraList) {
        this.tradeItemMainBatchRelExtraList = tradeItemMainBatchRelExtraList;
    }

    /**
     * @Title: getTradePrivilege
     * @Description: 获取整单手动折扣
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
    public TradePrivilege getTradePrivilege() {
        if (tradePrivileges != null) {
            for (TradePrivilege mTradePrivilege : tradePrivileges) {
                if (!mTradePrivilege.isValid()) {
                    continue;
                }
                if (mTradePrivilege.getPrivilegeType() == PrivilegeType.DISCOUNT
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.REBATE
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.FREE
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.BUSINESS
                        || mTradePrivilege.getPrivilegeType() == PrivilegeType.PLATFORM) {
                    return mTradePrivilege;
                }
            }
        }
//		return Utils.isEmpty(tradePrivileges) ? null : tradePrivileges.get(0);
        return null;
    }

    /**
     * @Title: getTradePrivilege
     * @Description: 获取整单手动折扣
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
    public TradePrivilege getTradePrivilege(PrivilegeType privilegeType) {
        if (tradePrivileges != null) {
            for (TradePrivilege mTradePrivilege : tradePrivileges) {
                if (mTradePrivilege.getPrivilegeType() == privilegeType) {
                    return mTradePrivilege;
                }

            }
        }
        return null;
    }

    /**
     * 获取某个类别下的优惠信息列表
     */
    public List<TradePrivilege> getTradePrivilegeList(PrivilegeType privilegeType) {
        List<TradePrivilege> tradePrivilegeList = new ArrayList<>();
        if (tradePrivileges != null) {
            for (TradePrivilege mTradePrivilege : tradePrivileges) {
                if (mTradePrivilege.getPrivilegeType() == privilegeType) {
                    tradePrivilegeList.add(mTradePrivilege);
                }

            }
        }
        return tradePrivilegeList;
    }


    /**
     * @Title: getTradePrivilege
     * @Description: 获取整单手动折扣
     * @Param @return TODO
     * @Return TradePrivilege 返回类型
     */
    public TradePrivilege getTradePrivilege(PrivilegeType... privilegeTypes) {
        if (tradePrivileges != null) {
            for (TradePrivilege mTradePrivilege : tradePrivileges) {
                for (PrivilegeType privilegeType : privilegeTypes) {
                    if (mTradePrivilege.getPrivilegeType() == privilegeType && mTradePrivilege.isValid()) {
                        return mTradePrivilege;
                    }
                }
            }
        }
        return null;
    }

    public List<TradePrivilege> getTradePrivileges() {
        return tradePrivileges;
    }

    /**
     * @Title: replaceTradePrivilege
     * @Description: 替换整单折扣
     * @Param @param tradePrivilege TODO
     * @Return void 返回类型
     */
    public void replaceTradePrivilege(TradePrivilege tradePrivilege) {
        if (tradePrivilege == null) {
            removeTradePrivileges(PrivilegeType.DISCOUNT);
            removeTradePrivileges(PrivilegeType.REBATE);
            removeTradePrivileges(PrivilegeType.FREE);
//			setTradePrivileges(null);
        } else {
            if (tradePrivileges == null) {
                tradePrivileges = new ArrayList<TradePrivilege>();
            } else {
                removeTradePrivileges(PrivilegeType.DISCOUNT);
                removeTradePrivileges(PrivilegeType.REBATE);
                removeTradePrivileges(PrivilegeType.FREE);
//				tradePrivileges.clear();
            }
            tradePrivileges.add(tradePrivilege);
        }
    }

    /**
     * @Title: replaceExtraCharge
     * @Description: 替换附加费和手动整单折扣
     * @Param @param tradePrivileges TODO
     * @Return void 返回类型
     */
    public void replaceAllPrivilege(List<TradePrivilege> listTradePrivilege) {
        if (listTradePrivilege == null) {
            setTradePrivileges(null);
        } else {
            if (tradePrivileges == null) {
                tradePrivileges = new ArrayList<TradePrivilege>();
            } else {
                tradePrivileges.clear();
            }
            tradePrivileges.addAll(listTradePrivilege);
        }
    }

    private void removeTradePrivileges(PrivilegeType mPrivilegeType) {
        if (tradePrivileges != null) {
            for (int i = tradePrivileges.size() - 1; i >= 0; i--) {
                TradePrivilege mTradePrivilege = tradePrivileges.get(i);
                if (mTradePrivilege.getPrivilegeType() == mPrivilegeType
                        && mTradePrivilege.getStatusFlag() == StatusFlag.VALID) {
                    if (mTradePrivilege.getId() == null) {
                        tradePrivileges.remove(i);
                    } else {
                        mTradePrivilege.setStatusFlag(StatusFlag.INVALID);
                        mTradePrivilege.validateUpdate();
                    }
                }
            }
        }

    }

    public void setTradePrivileges(List<TradePrivilege> tradePrivileges) {
        this.tradePrivileges = tradePrivileges;
    }

    public CouponPrivilegeVo getCouponPrivilege() {
        return couponPrivilege;
    }

    public void setCouponPrivilege(CouponPrivilegeVo couponPrivilege) {
        this.couponPrivilege = couponPrivilege;
    }

    public List<TradeCustomer> getTradeCustomerList() {
        return tradeCustomerList;
    }

    public void setTradeCustomerList(List<TradeCustomer> tradeCustomerList) {
        this.tradeCustomerList = tradeCustomerList;
    }

    public List<TradeTable> getTradeTableList() {
        return tradeTableList;
    }

    public Map<Long, TradeTable> getSubTableMap() {
        return subTableMap;
    }

    public void setSubTableMap(Map<Long, TradeTable> subTableMap) {
        this.subTableMap = subTableMap;
    }

    public Map<Long, TradeTable> getSubTradeTableMap() {
        return subTradeTableMap;
    }

    public void setSubTradeTableMap(Map<Long, TradeTable> subTradeTableMap) {
        this.subTradeTableMap = subTradeTableMap;
    }

    /**
     * 获取桌数
     *
     * @return
     */
    public BigDecimal getDeskCount() {
        BigDecimal count = BigDecimal.ZERO;
        if (tradeTableList == null) {
            return BigDecimal.ONE;
        }
        for (TradeTable tradeTable : tradeTableList) {
            if (tradeTable.isValid()) {
                count = count.add(BigDecimal.ONE);
            }
        }
        //默认为1桌
        if (count.compareTo(BigDecimal.ZERO) == 0) {
            count = BigDecimal.ONE;
        }
        return count;
    }

    public void setTradeTableList(List<TradeTable> tradeTable) {
        this.tradeTableList = tradeTable;
    }

    public List<TradeItemVo> getTradeItemList() {
        return tradeItemList;
    }

    public void setTradeItemList(List<TradeItemVo> tradeItemList) {
        if (tradeItemList == null) {
            tradeItemList = new ArrayList<>();
        }
        this.tradeItemList = tradeItemList;
    }

    public Boolean getIsSalesReturn() {
        return isSalesReturn;
    }

    public void setIsSalesReturn(Boolean isSalesReturn) {
        this.isSalesReturn = isSalesReturn;
    }

    public IntegralCashPrivilegeVo getIntegralCashPrivilegeVo() {
        return integralCashPrivilegeVo;
    }

    public void setIntegralCashPrivilegeVo(IntegralCashPrivilegeVo integralCashPrivilegeVo) {
        this.integralCashPrivilegeVo = integralCashPrivilegeVo;
    }

    public List<CouponPrivilegeVo> getCouponPrivilegeVoList() {
        return couponPrivilegeVoList;
    }

    public void setCouponPrivilegeVoList(List<CouponPrivilegeVo> couponPrivilegeVoList) {
        this.couponPrivilegeVoList = couponPrivilegeVoList;
    }

    //	public TradeReasonRel getOperateReason() {
//		return operateReason;
//	}
//
//	public void setOperateReason(TradeReasonRel operateReason) {
//		this.operateReason = operateReason;
//	}

    public List<TradeReasonRel> getTradeReasonRelList() {
        return tradeReasonRelList;
    }

    public void setTradeReasonRelList(List<TradeReasonRel> tradeReasonRelList) {
        this.tradeReasonRelList = tradeReasonRelList;
    }

    public TradeReasonRel getOperateReason(OperateType operateType) {
        if (tradeReasonRelList != null && operateType != null) {
            //for (TradeReasonRel entity : tradeReasonRelList) {
            for (int i = tradeReasonRelList.size() - 1; i >= 0; i--) {
                TradeReasonRel entity = tradeReasonRelList.get(i);
                if (entity.getOperateType() == operateType && entity.isValid()) {
                    return entity;
                }
            }
        }
        return null;
    }

    public List<TradeReasonRel> getOperateReasons(OperateType operateType) {
        List<TradeReasonRel> resultList = new ArrayList<TradeReasonRel>();
        if (tradeReasonRelList != null) {
            for (TradeReasonRel entity : tradeReasonRelList) {
                if (entity.getOperateType() == operateType) {
                    resultList.add(entity);
                }
            }
        }
        return resultList;
    }

    /**
     * 如果对象中已经被修改过就返回true
     *
     * @return
     */
    public boolean isChanged() {
        if (Trade.isChanged(trade)) {
            return true;
        }
        if (Trade.isChanged(tradeExtra)) {
            return true;
        }
        if (tradeTableList != null) {
            for (TradeTable tradeTable : tradeTableList) {
                if (Trade.isChanged(tradeTable)) {
                    return true;
                }
            }
        }
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                if (Trade.isChanged(tradePrivilege)) {
                    return true;
                }
            }
        }
        if (couponPrivilege != null && Trade.isChanged(couponPrivilege.getTradePrivilege())) {
            return true;
        }
        if (integralCashPrivilegeVo != null && Trade.isChanged(integralCashPrivilegeVo.getTradePrivilege())) {
            return true;
        }
        if (tradeItemList != null) {
            for (TradeItemVo itemVo : tradeItemList) {
                if (itemVo.isChanged() || (itemVo.getTradeItemExtraDinner() != null && itemVo.getTradeItemExtraDinner().isChanged())) {
                    return true;
                }
            }
        }
        if (tradeCustomerList != null) {
            for (TradeCustomer entity : tradeCustomerList) {
                if (Trade.isChanged(entity)) {
                    return true;
                }
            }
        }
        if (tradeReasonRelList != null) {
            for (TradeReasonRel entity : tradeReasonRelList) {
                if (Trade.isChanged(entity)) {
                    return true;
                }
            }
        }

        if (Trade.isChanged(tradeDeposit)) {
            return true;
        }

        if (Utils.isNotEmpty(tradeBuffetPeoples)) {
            for (TradeBuffetPeople buffetPeople : tradeBuffetPeoples) {
                if (TradeBuffetPeople.isChanged(buffetPeople)) {
                    return true;
                }
            }
        }
        if (this.tradeUser != null && this.tradeUser.isChanged()) {
            return true;
        }
        return false;
    }

    public void setChangedFalse() {
        if (trade != null) {
            trade.setChanged(false);
        }
        if (tradeExtra != null) {
            tradeExtra.setChanged(false);
        }
        if (tradeTableList != null) {
            for (TradeTable tradeTable : tradeTableList) {
                tradeTable.setChanged(false);
            }
        }
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                tradePrivilege.setChanged(false);
            }
        }
        if (couponPrivilege != null && couponPrivilege.getTradePrivilege() != null) {
            couponPrivilege.getTradePrivilege().setChanged(false);
        }
        if (integralCashPrivilegeVo != null && integralCashPrivilegeVo.getTradePrivilege() != null) {
            integralCashPrivilegeVo.getTradePrivilege().setChanged(false);
        }
        //TradeItemVo先不处理
//		if (tradeItemList != null) {
//			for (TradeItemVo itemVo : tradeItemList) {
//			}
//		}
        if (tradeCustomerList != null) {
            for (TradeCustomer entity : tradeCustomerList) {
                entity.setChanged(false);
            }
        }
        if (tradeReasonRelList != null) {
            for (TradeReasonRel entity : tradeReasonRelList) {
                entity.setChanged(false);
            }
        }

        if (tradeDeposit != null) {
            tradeDeposit.setChanged(false);
        }
    }

    /**
     * @param source
     * @param target
     * @return
     * @throws Exception
     */
    static <T> T copyEntity(T source, T target) throws Exception {
        Beans.copyProperties(source, target);
        return target;
    }

    /**
     *
     */
    @Override
    public TradeVo clone() {
        TradeVo vo = new TradeVo();
        try {
            if (trade != null) {
                vo.setTrade(copyEntity(trade, new Trade()));
            }
            if (tradeExtra != null) {
                vo.setTradeExtra(copyEntity(tradeExtra, new TradeExtra()));
            }
            if (this.tradeDepositPaymentItem != null) {// add v8.2
                vo.setTradeDepositPaymentItem(copyEntity(this.tradeDepositPaymentItem, new PaymentItem()));
            }
            if (this.tradeDepositPayRelation != null) {// add v8.2
                vo.setTradeDepositPayRelation(copyEntity(this.tradeDepositPayRelation, new TradeDepositPayRelation()));
            }
            if (this.tradeUser != null) {// add v8.2  销售员
                vo.setTradeUser(copyEntity(this.tradeUser, new TradeUser()));
            }
            if (tradePrivileges != null) {
                List<TradePrivilege> newList = new ArrayList<TradePrivilege>();
                for (TradePrivilege tradePrivilege : tradePrivileges) {
                    newList.add(copyEntity(tradePrivilege, new TradePrivilege()));
                }
                vo.setTradePrivileges(newList);
            }

            if (tradeTaxs != null) {
                List<TradeTax> newList = new ArrayList<TradeTax>();
                for (TradeTax tradeTax : tradeTaxs) {
                    newList.add(copyEntity(tradeTax, new TradeTax()));
                }
                vo.setTradeTaxs(newList);
            }

            if (tradeInitConfigs != null) {
                List<TradeInitConfig> newList = new ArrayList<>();
                for (TradeInitConfig initConfig : tradeInitConfigs) {
                    newList.add(copyEntity(initConfig, new TradeInitConfig()));
                }
                vo.setTradeInitConfigs(newList);
            }

            if (couponPrivilege != null) {
                CouponPrivilegeVo newVo = couponPrivilege.clone();
                if (newVo == null) {
                    return null;
                }
                vo.setCouponPrivilege(newVo);
            }
            if (integralCashPrivilegeVo != null) {
                IntegralCashPrivilegeVo newVo = integralCashPrivilegeVo.clone();
                if (newVo == null) {
                    return null;
                }
                vo.setIntegralCashPrivilegeVo(newVo);
            }
            if (tradeCustomerList != null) {
                List<TradeCustomer> newList = new ArrayList<TradeCustomer>();
                for (TradeCustomer source : tradeCustomerList) {
                    newList.add(copyEntity(source, new TradeCustomer()));
                }
                vo.setTradeCustomerList(newList);
            }
            if (tradeTableList != null) {
                List<TradeTable> newList = new ArrayList<TradeTable>();
                for (TradeTable source : tradeTableList) {
                    newList.add(copyEntity(source, new TradeTable()));
                }
                vo.setTradeTableList(newList);
            }
            if (tradeItemList != null) {
                List<TradeItemVo> newList = new ArrayList<TradeItemVo>();
                for (TradeItemVo itemVo : tradeItemList) {
                    TradeItemVo newItemVo = itemVo.clone();
                    if (newItemVo == null) {
                        return null;
                    }
                    newList.add(newItemVo);
                }
                vo.setTradeItemList(newList);
            }
            if (tradeReasonRelList != null) {
                List<TradeReasonRel> newList = new ArrayList<TradeReasonRel>();
                for (TradeReasonRel source : tradeReasonRelList) {
                    newList.add(copyEntity(source, new TradeReasonRel()));
                }
                vo.setTradeReasonRelList(newList);
            }

            if (tradePlanActivityList != null) {
                List<TradePlanActivity> newList = new ArrayList<TradePlanActivity>();
                for (TradePlanActivity planActivity : tradePlanActivityList) {
                    newList.add(copyEntity(planActivity, new TradePlanActivity()));
                }
                vo.setTradePlanActivityList(newList);
            }

            if (tradeItemPlanActivityList != null) {
                List<TradeItemPlanActivity> newList = new ArrayList<TradeItemPlanActivity>();
                for (TradeItemPlanActivity itemPlanActivity : tradeItemPlanActivityList) {
                    newList.add(copyEntity(itemPlanActivity, new TradeItemPlanActivity()));
                }
                vo.setTradeItemPlanActivityList(newList);
            }

            if (tradeDeposit != null) {
                vo.setTradeDeposit(copyEntity(tradeDeposit, new TradeDeposit()));
            }

            // extraChargeMap 不需要深克隆
            if (extraChargeMap != null) {
                vo.setExtraChargeMap(extraChargeMap);
            }

            if (minconExtraCharge != null) {
                vo.setMinconExtraCharge(minconExtraCharge);
            }

            vo.setIsSalesReturn(isSalesReturn);
            vo.setRelatedId(relatedId);
            vo.setRelatedType(relatedType);

            //复制微信卡劵
            if (mWeiXinCouponsVo != null) {
                List<WeiXinCouponsVo> newWeixinCouponsVo = new ArrayList<WeiXinCouponsVo>();
                for (WeiXinCouponsVo weiXinCouponsVo : mWeiXinCouponsVo) {
                    newWeixinCouponsVo.add(copyEntity(weiXinCouponsVo, new WeiXinCouponsVo()));
                }
                vo.setmWeiXinCouponsVo(newWeixinCouponsVo);
            }
            if (banquetVo != null) {
                vo.setBanquetVo(banquetVo.clone());
            }

            if (couponPrivilegeVoList != null) {
                List<CouponPrivilegeVo> newCouponPrivilegeVo = new ArrayList<CouponPrivilegeVo>();
                for (CouponPrivilegeVo couponPrivilegeVo : couponPrivilegeVoList) {
                    CouponPrivilegeVo newVo = couponPrivilegeVo.clone();
                    newCouponPrivilegeVo.add(newVo);
                }
                vo.setCouponPrivilegeVoList(newCouponPrivilegeVo);
            }

            if (tradeBuffetPeoples != null) {
                List<TradeBuffetPeople> tmpbuffetPeoples = new ArrayList<TradeBuffetPeople>();
                for (TradeBuffetPeople buffetPeople : tradeBuffetPeoples) {
                    TradeBuffetPeople newVo = copyEntity(buffetPeople, new TradeBuffetPeople());
                    tmpbuffetPeoples.add(newVo);
                }
                vo.setTradeBuffetPeoples(tmpbuffetPeoples);
            }
            if (mealShellVo != null) {
                vo.setMealHullVo(mealShellVo);
            }

            if (paidAmount != null)
                vo.setPaidAmount(paidAmount);

            if (tradeGroup != null) {
                vo.setTradeGroup(tradeGroup);
            }
            if (changeCardReq != null) {
                vo.setChangeCardReq(changeCardReq);
            }
            if (jcRechargeCardReq != null) {
                vo.setJcRechargeReq(jcRechargeCardReq);
            }
            if (tradeInvoiceNo != null) {
                vo.setTradeInvoiceNo(tradeInvoiceNo);
            }
            if (tradeEarnestMoneys != null) {
                vo.setTradeEarnestMoneys(tradeEarnestMoneys);
            }
            return vo;
        } catch (Exception e) {
            Log.e(TAG, "Copy properties error!", e);
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((trade == null) ? 0 : trade.hashCode());
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
        TradeVo other = (TradeVo) obj;
        if (trade == null) {
            if (other.trade != null)
                return false;
        } else if (!trade.equals(other.trade))
            return false;
        return true;
    }

    public Map<Long, ExtraCharge> getExtraChargeMap() {
        return extraChargeMap;
    }

    public void setExtraChargeMap(Map<Long, ExtraCharge> extraChargeMap) {
        this.extraChargeMap = extraChargeMap;
    }

    public BigDecimal getDiscountExtracharge() {
        return discountExtracharge;
    }

    public void setDiscountExtracharge(BigDecimal discountExtracharge) {
        this.discountExtracharge = discountExtracharge;
    }

    public List<TradePlanActivity> getTradePlanActivityList() {
        return tradePlanActivityList;
    }

    public void setTradePlanActivityList(List<TradePlanActivity> tradePlanActivityList) {
        this.tradePlanActivityList = tradePlanActivityList;
    }

    public List<TradeItemPlanActivity> getTradeItemPlanActivityList() {
        return tradeItemPlanActivityList;
    }

    public void setTradeItemPlanActivityList(List<TradeItemPlanActivity> tradeItemPlanActivityList) {
        this.tradeItemPlanActivityList = tradeItemPlanActivityList;
    }
//
//    public List<TradeItemActivityGift> getTradeItemActivityGiftList() {
//        return tradeItemActivityGiftList;
//    }
//
//    public void setTradeItemActivityGiftList(List<TradeItemActivityGift> tradeItemActivityGiftList) {
//        this.tradeItemActivityGiftList = tradeItemActivityGiftList;
//    }

    public TradeDeposit getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }

    public TradeDepositPayRelation getTradeDepositPayRelation() {
        return tradeDepositPayRelation;
    }

    public void setTradeDepositPayRelation(TradeDepositPayRelation tradeDepositPayRelation) {
        this.tradeDepositPayRelation = tradeDepositPayRelation;
    }

    public PaymentItem getTradeDepositPaymentItem() {
        return tradeDepositPaymentItem;
    }

    public void setTradeDepositPaymentItem(PaymentItem tradeDepositPaymentItem) {
        this.tradeDepositPaymentItem = tradeDepositPaymentItem;
    }

    public BigDecimal getBeforePrivilegeAmount() {
        return beforePrivilegeAmount;
    }

    public void setBeforePrivilegeAmount(BigDecimal beforePrivilegeAmount) {
        this.beforePrivilegeAmount = beforePrivilegeAmount;
    }

    public List<TradeCreditLog> getTradeCreditLogList() {
        return tradeCreditLogList;
    }

    public void setTradeCreditLogList(List<TradeCreditLog> tradeCreditLogList) {
        this.tradeCreditLogList = tradeCreditLogList;
    }


    public void setRelatedId(Long relatedId) {
        this.relatedId = relatedId;
    }

    public Long getRelatedId() {
        return relatedId;
    }

    public void setRelatedType(Integer relatedType) {
        this.relatedType = relatedType;
    }

    public Integer getRelatedType() {
        return relatedType;
    }

    public BanquetVo getBanquetVo() {
        return banquetVo;
    }

    public void setBanquetVo(BanquetVo banquetVo) {
        this.banquetVo = banquetVo;
    }

    public List<TradeItemExtra> getTradeItemExtraList() {
        return tradeItemExtraList;
    }

    public void setTradeItemExtraList(List<TradeItemExtra> tradeItemExtraList) {
        this.tradeItemExtraList = tradeItemExtraList;
    }

    public List<TradeStatusLog> getTradeStatusLogList() {
        return tradeStatusLogList;
    }

    public void setTradeStatusLogList(List<TradeStatusLog> tradeStatusLogList) {
        this.tradeStatusLogList = tradeStatusLogList;
    }

    public MealShellVo getMealShellVo() {
        return mealShellVo;
    }

    public void setMealHullVo(MealShellVo mealShellVo) {
        this.mealShellVo = mealShellVo;
    }

    public String getTradeBuffetPeopleTip() {
        if (Utils.isEmpty(tradeBuffetPeoples)) {
            return "";
        }

        StringBuffer strBuff = new StringBuffer();

        strBuff.append("(");

        for (TradeBuffetPeople tradeBuffetPeople : tradeBuffetPeoples) {
            if (tradeBuffetPeople.getPeopleCount().compareTo(BigDecimal.ZERO) > 0) {
                strBuff.append(tradeBuffetPeople.getCarteNormsName() + "x" + tradeBuffetPeople.getPeopleCount());
                strBuff.append(",");
            }
        }
        return strBuff.substring(0, strBuff.length() - 1) + ")";
    }

    /**
     * 获取团餐的顾客
     *
     * @return
     */
    public TradeCustomer getGroupCustomer() {
        for (TradeCustomer customer : getTradeCustomerList()) {
            if (customer.getCustomerType() == CustomerType.BOOKING) {
                return customer;
            }
        }
        return null;
    }

    /**
     * 是否已支付押金
     *
     * @return
     */
    public boolean isPaidTradeposit() {
        return getTradeDepositPaymentItem() != null && getTradeDepositPaymentItem().getPayStatus() == TradePayStatus.PAID;
    }

    /**
     * @return
     */
    public boolean isTradeDeposit() {    //按订单或自定义类型押金
        return getTradeDeposit() != null && (getTradeDeposit().getType() == 2 || getTradeDeposit().getType() == 3);
    }

    public BigDecimal getOldDeskCount() {
        return oldDeskCount;
    }

    public void setOldDeskCount(BigDecimal oldDeskCount) {
        this.oldDeskCount = oldDeskCount;
    }

    public ChangeCardReq getChangeCardReq() {
        return changeCardReq;
    }

    public void setChangeCardReq(ChangeCardReq changeCardReq) {
        this.changeCardReq = changeCardReq;
    }

    public JCRechargeCardReq getRechargeCardReq() {
        return jcRechargeCardReq;
    }

    public void setJcRechargeReq(JCRechargeCardReq jcRechargeCardReq) {
        this.jcRechargeCardReq = jcRechargeCardReq;
    }

    public List<TradeItemExtraDinner> getTradeItemExtraDinners() {
        if (tradeItemExtraDinners == null) {
            tradeItemExtraDinners = new ArrayList<TradeItemExtraDinner>();
        }
        return tradeItemExtraDinners;
    }

    public void setTradeItemExtraDinners(List<TradeItemExtraDinner> tradeItemExtraDinners) {
        this.tradeItemExtraDinners = tradeItemExtraDinners;
    }

    public TradeUser getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }

    public boolean isEnableMinConsum() {
        return enableMinConsum;
    }

    public void setEnableMinConsum(boolean enableMinConsum) {
        this.enableMinConsum = enableMinConsum;
    }

    public ExtraCharge getMinconExtraCharge() {
        return minconExtraCharge;
    }

    public void setMinconExtraCharge(ExtraCharge minconExtraCharge) {
        this.minconExtraCharge = minconExtraCharge;
    }

    public List<TradeTax> getTradeTaxs() {
        return tradeTaxs;
    }

    public void setTradeTaxs(List<TradeTax> tradeTaxs) {
        this.tradeTaxs = tradeTaxs;
    }

    public List<TradeInitConfig> getTradeInitConfigs() {
        return tradeInitConfigs;
    }

    public void setTradeInitConfigs(List<TradeInitConfig> tradeInitConfigs) {
        this.tradeInitConfigs = tradeInitConfigs;
    }

    public TradeInitConfig getTradeInitConfig(TradeInitConfigKeyId keyId) {
        if (tradeInitConfigs != null) {
            for (TradeInitConfig tradeInitConfig : tradeInitConfigs) {
                if (tradeInitConfig.getKeyId() == keyId && tradeInitConfig.isValid()) {
                    return tradeInitConfig;
                }
            }
        }
        return null;
    }

    public <T> T getTradeInitConfig(TradeInitConfigKeyId keyId, Class<T> c) {
        TradeInitConfig tradeInitConfig = getTradeInitConfig(keyId);
        if (tradeInitConfig != null) {
            return GsonUtil.jsonToObject(tradeInitConfig.getValue(), c);
        }
        return null;
    }

    //判断订单是否有积分抵现 add 20180510
    public boolean isHaveIntegralCash() {
        if (integralCashPrivilegeVo != null) {
            return true;
        }
        return false;
    }

    //判断订单是否有优惠 add 20171115
    public boolean isHavePrivilege() {
        //获取整单优惠
        if (this.tradePrivileges != null) {
            for (TradePrivilege tp : this.tradePrivileges) {
                //排除附加费
                if (tp.isValid() && (tp.getPrivilegeType() != PrivilegeType.ADDITIONAL
                        && tp.getPrivilegeType() != PrivilegeType.SERVICE)) {
                    return true;
                }
            }
        }
        //获取整单券优惠
        if (this.couponPrivilege != null && this.couponPrivilege.isValid() && this.couponPrivilege.isActived()) {
            return true;
        }
        if (this.couponPrivilegeVoList != null) {
            for (CouponPrivilegeVo cPrivilegeVo : this.couponPrivilegeVoList) {
                if (cPrivilegeVo != null && cPrivilegeVo.isValid() && cPrivilegeVo.isActived()) {
                    return true;
                }
            }
        }
        //获取营销活动
        if (this.tradePlanActivityList != null && this.tradePlanActivityList.size() > 0) {
            for (TradePlanActivity tpa : this.tradePlanActivityList) {
                if (tpa.isValid()) {
                    return true;
                }
            }
        }
        //微信卡卷
        if (mWeiXinCouponsVo != null && mWeiXinCouponsVo.size() > 0) {
            for (WeiXinCouponsVo weiXinCVo : this.mWeiXinCouponsVo) {
                if (weiXinCVo.isValid() && weiXinCVo.isActived()) {
                    return true;
                }
            }
        }
        //积分抵现
        if (integralCashPrivilegeVo != null && integralCashPrivilegeVo.isActived()) {
            return true;
        }
        //单菜优惠
        if (this.tradeItemList != null) {
            TradePrivilege tp = null;
            TradeItem tradeItem = null;
            for (TradeItemVo itemVo : this.tradeItemList) {
                tp = itemVo.getTradeItemPrivilege();
                tradeItem = itemVo.getTradeItem();
                if (tradeItem != null && tradeItem.isValid() && tp != null && tp.isValid() && tp.getPrivilegeAmount() != null && tp.getPrivilegeAmount().compareTo(BigDecimal.ZERO) != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 是否售货单
     *
     * @return
     */
    public boolean isSellTrade() {
        return trade != null && trade.getTradeType() == TradeType.SELL;
    }

    /**
     * 是否是联台主单
     *
     * @return
     */
    public boolean isUnionMainTrade() {
        return trade != null && trade.getTradeType() == TradeType.UNOIN_TABLE_MAIN;
    }

    /**
     * 是否是联台子单
     *
     * @return
     */
    public boolean isUnionSubTrade() {
        return trade != null && trade.getTradeType() == TradeType.UNOIN_TABLE_SUB;
    }


    /**
     * 是否是自助联台主单
     *
     * @return
     */
    public boolean isBuffetUnionMainTrade() {
        return isBuffet() && isUnionMainTrade();
    }

    /**
     * 是否是自助
     *
     * @return
     */
    public boolean isBuffet() {
        return trade != null && trade.getBusinessType() == BusinessType.BUFFET;
    }

    public boolean isBeauty() {
        return trade != null && trade.getBusinessType() == BusinessType.BEAUTY;
    }

    /**
     * 是否是自助联台子单
     *
     * @return
     */
    public boolean isBuffetUnionSubTrade() {
        return isBuffet() && isUnionSubTrade();
    }

    public List<Long> getSubTradeIdList() {
        return subTradeIdList;
    }

    public void setSubTradeIdList(List<Long> subTradeIdList) {
        this.subTradeIdList = subTradeIdList;
    }

    public BigDecimal getSubTradeCount() {
        if (Utils.isEmpty(subTradeIdList)) {
            return BigDecimal.ONE;
        }
        return new BigDecimal(subTradeIdList.size());
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public boolean isBUnionMainTrade() {
        return isBUnionMainTrade;
    }

    public void setBUnionMainTrade(boolean BUnionMainTrade) {
        isBUnionMainTrade = BUnionMainTrade;
    }

    public List<TradeUser> getTradeUsers() {
        return tradeUsers;
    }

    public void setTradeUsers(List<TradeUser> tradeUsers) {
        this.tradeUsers = tradeUsers;
    }

    public TradeInvoiceNo getTradeInvoiceNo() {
        return tradeInvoiceNo;
    }

    public void setTradeInvoiceNo(TradeInvoiceNo tradeInvoiceNo) {
        this.tradeInvoiceNo = tradeInvoiceNo;
    }

    public void setTradeEarnestMoneys(List<TradeEarnestMoney> tradeEarnestMoneys) {
        this.tradeEarnestMoneys = tradeEarnestMoneys;
    }

    public List<TradeEarnestMoney> getTradeEarnestMoneys() {
        return tradeEarnestMoneys;
    }

    public VerifyKoubeiOrder getVerifyKoubeiOrder() {
        return verifyKoubeiOrder;
    }

    public void setVerifyKoubeiOrder(VerifyKoubeiOrder verifyKoubeiOrder) {
        this.verifyKoubeiOrder = verifyKoubeiOrder;
    }

    public double getTradeEarnestMoney() {
        double earnestMoney = 0;
        if (tradeEarnestMoneys != null) {
            for (TradeEarnestMoney tradeEarnestMoney : tradeEarnestMoneys) {
                if (tradeEarnestMoney.getEarnestMoney() != null) {
                    earnestMoney += tradeEarnestMoney.getEarnestMoney().doubleValue();
                }
            }
        }
        return earnestMoney;
    }

    public BigDecimal getTradeEarnestDecimal() {
        BigDecimal earnestDecimal = BigDecimal.ZERO;
        if (tradeEarnestMoneys != null) {
            for (TradeEarnestMoney tradeEarnestMoney : tradeEarnestMoneys) {
                if (tradeEarnestMoney.getEarnestMoney() != null) {
                    earnestDecimal = earnestDecimal.add(tradeEarnestMoney.getEarnestMoney());
                }
            }
        }
        return earnestDecimal;
    }

    /**
     * 获取未支付押金金额
     *
     * @return
     */
    public BigDecimal getUnPayDepositAmount() {
        BigDecimal depositAmount = BigDecimal.ZERO;
        if (getTrade().getBusinessType() == BusinessType.BOOKING_LIST && getTradeDeposit() != null && getTradeDeposit().isValid()) {
            depositAmount = getTradeDeposit().getDepositPay();
        }
        return depositAmount;
    }

    /**
     * 获取已支付押金
     *
     * @return
     */
    public BigDecimal getPayDepositAmount() {
        BigDecimal depositAmount = BigDecimal.ZERO;
        if (getTrade().getBusinessType() == BusinessType.BOOKING_LIST && getTradeDeposit() != null && getTradeDeposit().isValid()) {
            if (getTradeDepositPaymentItem() != null && getTradeDepositPaymentItem().getPayStatus() == TradePayStatus.PAID) {
                depositAmount = BigDecimal.ZERO;
            } else {
                depositAmount = getTradeDeposit().getDepositPay();
            }
        }
        return depositAmount;
    }

    // add v8.5 by yutang for openplatform tradeInfo save
    public TradeResp toTradeResp() {

        TradeResp tradeResp = new TradeResp();

        if (this.getTrade() != null) {
            tradeResp.setTrade(this.getTrade());
        }
        if (this.getTradeExtra() != null) {
            tradeResp.setTradeExtra(this.getTradeExtra());
        }
        if (this.getTradeItemList() != null && this.getTradeItemList().size() > 0) {
            List<TradeItem> tradeItems = new ArrayList<TradeItem>();
            List<TradeItemExtra> tradeItemExtras = new ArrayList<TradeItemExtra>();
            for (TradeItemVo tradeItemVo : this.getTradeItemList()) {
                if (tradeItemVo.getTradeItem() != null)
                    tradeItems.add(tradeItemVo.getTradeItem());
                if (tradeItemVo.getTradeItemExtra() != null)
                    tradeItemExtras.add(tradeItemVo.getTradeItemExtra());
            }
            tradeResp.setTradeItems(tradeItems);
            tradeResp.setTradeItemExtras(tradeItemExtras);
        }
        return tradeResp;
    }

    public boolean isNeedToPayDeposit() {
        //押金大于o 才可以支付押金
        if (tradeDeposit != null && tradeDeposit.isNeedToPay()) {
            return true;
        }
        return false;
    }

    public boolean buffetOrderOperationLimit() {
        if (isBuffet() && mealShellVo != null && Utils.isNotEmpty(tradeBuffetPeoples)) {
            int peopleCount = 0;
            for (TradeBuffetPeople buffetPeople : tradeBuffetPeoples) {
                peopleCount += buffetPeople.getPeopleCount().intValue();
            }
            if (peopleCount == 0) return false;
            else return true;
        }
        return true;
    }

    public List<ExtraCharge> getExtraChargesOfTradePrivileges() {
        List<ExtraCharge> result = new ArrayList<>();
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                ExtraCharge extraCharge = getMinconExtraCharge();
                if (extraCharge != null && extraCharge.isValid()
                        && tradePrivilege.getPromoId() != null && extraCharge.getId().compareTo(tradePrivilege.getPromoId()) == 0) {
                    result.add(extraCharge);
                } else {
                    extraCharge = ExtraManager.getExtraChargeById(this, tradePrivilege.getPromoId());
                    if (extraCharge != null && extraCharge.isValid() && !ExtraChargeTool.isServiceCharge(this, extraCharge)) {
                        result.add(extraCharge);
                    }
                }
            }
        }
        return result;
    }

    public ExtraCharge getServiceChargeOfTradePrivileges() {
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                ExtraCharge extraCharge = ExtraManager.getExtraChargeById(this, tradePrivilege.getPromoId());
                if (ExtraChargeTool.isServiceCharge(this, extraCharge)) {
                    return extraCharge;
                }
            }
        }
        return null;
    }

    public BigDecimal getServiceAmount() {
        if (tradePrivileges != null) {
            for (TradePrivilege tradePrivilege : tradePrivileges) {
                ExtraCharge extraCharge = ExtraManager.getExtraChargeById(this, tradePrivilege.getPromoId());
                if (ExtraChargeTool.isServiceCharge(this, extraCharge)) {
                    return tradePrivilege.getPrivilegeAmount();
                }
            }
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getTradeTaxAmount() {
        if (tradeTaxs != null) {
            for (TradeTax tradeTax : tradeTaxs) {
                if (tradeTax.isValid()) {
                    return tradeTax.getTaxAmount();
                }
            }
        }
        return BigDecimal.ZERO;
    }

    /**
     * 是否有有效的桌台
     *
     * @return
     */
    public boolean isHasValidTable() {
        if (Utils.isEmpty(tradeTableList)) {
            return false;
        }
        boolean isHasVaild = false;
        for (TradeTable tradeTable : tradeTableList) {
            if (tradeTable.isValid()) {
                isHasVaild = true;
                break;
            }
        }
        return isHasVaild;
    }

    public List<Long> getTableIds() {
        List<Long> tableIds = new ArrayList<>();
        tableIds.add(tableId);
        if (subTradeTableMap != null) {
            for (Map.Entry<Long, TradeTable> entry : subTradeTableMap.entrySet()) {
                if (!tableIds.contains(entry.getValue().getTableId())) {
                    tableIds.add(entry.getValue().getTableId());
                }
            }
        }
        return new ArrayList<>(tableIds);
    }

    /**
     * 只用于烘焙预定 除去押金的金额
     *
     * @return
     */
    public BigDecimal getTradeAmountWithoutDepositAmount() {
        BigDecimal depositAmount = getDepositAmount();
        return getTrade().getTradeAmount().subtract(depositAmount);
    }

    public BigDecimal getDepositAmount() {
        BigDecimal depositAmount = BigDecimal.ZERO;
        if (getTradeDeposit() != null && getTradeDeposit().isValid() && getTradeDeposit().getDepositPay() != null) {
            depositAmount = getTradeDeposit().getDepositPay();
        }
        return depositAmount;
    }

    /**
     * 判断是否为预约单
     *
     * @return
     */
    public boolean isAppointmentOrder() {
        return tradeExtra != null
                && tradeExtra.getExpectTime() != null;
    }
}
