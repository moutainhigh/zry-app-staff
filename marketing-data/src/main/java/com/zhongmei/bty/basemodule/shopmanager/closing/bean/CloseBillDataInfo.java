package com.zhongmei.bty.basemodule.shopmanager.closing.bean;

import com.google.gson.annotations.SerializedName;
import com.zhongmei.bty.basemodule.trade.entity.TradeDeposit;
import com.zhongmei.yunfu.context.base.BaseApplication;
import com.zhongmei.yunfu.data.R;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 关账明细表
 * Created by demo on 2018/12/15
 */
public class CloseBillDataInfo implements Serializable {

    public static final int stateTotalBillList = 1 << 1;
    public static final int stateHangBillList = 1 << 2;
    public static final int statePrivilegeGroups = 1 << 3;
    public static final int stateActualDetailList = 1 << 4;
    public static final int stateTradeTypeList = 1 << 5;
    public static final int stateDishTypeList = 1 << 6;
    public static final int stateDishBigTypeList = 1 << 7;
    public static final int stateThirdTakeOutList = 1 << 8;

    int state = stateTotalBillList
            | stateHangBillList
            | statePrivilegeGroups
            | stateActualDetailList
            | stateTradeTypeList
            | stateDishTypeList
            | stateDishBigTypeList
            | stateThirdTakeOutList;

    /*****新增******/
    private Long id;

    private List<OrderSale> orderSaleList;//订单销售实收

    private String orderSaleAmount;

    private List<OrderSale> orderSaleInnerList;//订单销售实收（店内）

    private String orderSaleAmountInner;//店内销售实收（店内）

    private List<KeyValue> chargeOffsList; //销账收入

    private String chargeOffsAmount;

    private List<KeyValue> expendAccountList;//支出
    private String bookingActualAmount;//预定金

    private List<KeyValue> bookingPayList;//预定金支付列表

    private String expendAccount;

    private List<KeyValue> otherIncomeList;//其他收入

    private String otherIncomeAmount;

    private List<KeyValue> hangAccountList;//挂账

    private String hangAccountAmount;

    private List<DishKeyValue> dishSaleUp;

    private String memberConsumeAmount;//储值消费金额

    private String returnAmount;//退货金额;

    private BigDecimal closeBillAmount;//关账总金额

    private BigDecimal closeBillAmountInner;//关账总金额(店内)

    private BigDecimal onOffCreditAmount;//当日挂账且当日销账金额

    private List<PrivilegeItem> privilegeGroups;//优惠项目跟明细

    private List<PrivilegeItem> privilegeInnerGroups;//优惠项目跟明细（店内）

    private List<DetailsKeyValue> actualDetailList;//销售实收明细

    private List<DetailsKeyValue> actualDetailInnerList;//销售实收明细(店内)

    private List<DetailsKeyValue> totalBillList;//关账总金额明细

    private List<DetailsKeyValue> totalBillInnerList;//关账总金额明细（店内）

    private List<DetailsKeyValue> hangBillList;//挂账
    /******新增结束******/

    /**
     * 归账日
     */
    private String belongDate;
    /**
     * 开始时间
     */
    private String startDate;
    /**
     * 结束时间
     */
    private String endDate;
    /**
     * 营业概况
     */
    private BizAmountInfo bizInfo;

    /**
     * 营业概况
     */
    private BizAmountInfo bizInfoInner;


    /**
     * 营业概况  总额
     */
    private String bizAmount;

    /**
     * 营业概况  总额(店内)
     */
    private String bizAmountInner;

    private String dd;//test


    /**
     * 收入类型
     */
    private List<ReceiptPayType> busAmountList;

    /**
     * 营业收入  总额
     */
    private String bizComeAmount;

    /**
     * 储值实收
     */
    private List<ReceiptPayType> memberAmountList;

    /**
     * 订单类型  总额
     */
    private String bizMemberAmount;

    /**
     * 订单销售实收
     */
    private List<TradeTypeCount> tradeTypeList;

    /**
     * 订单类型  总额
     */
    private String tradeTypeAmount;

    /**
     * 核数
     */
    private List<TradeAVGCount> checkTradeList;

    /**
     * 核数  总额
     */
    private String checkAmount;

    /**
     * 销售商品(中类)
     */
    private List<BillDishTypeInfo> dishTypeList;

    /**
     * 销售商品(大类)
     */
    @SerializedName("typeBigList")
    private List<BillDishTypeInfo> dishBigTypeList;

    /**
     * 商品  总额
     */
    private String dishAmount;

    /**
     * 折扣优惠明细
     */
    private DiscountDetailVo discountDetailVo;

    /**
     * 折扣优惠明细  总额
     */
    private String discountAmount;
    /**
     * 请求返回信息
     */
    private String message;
    /**
     * 请求状态 1成功 0 失败
     */
    private Integer status;

    /**
     * 操作人ID
     */
    private String creatorId;
    /**
     * 操作员名称
     */
    private String creatorName;
    //挂账金额
    private BigDecimal onCredit;
    //销账金额
    private BigDecimal offCredit;

    //挂账笔数
    private Integer onCreditNum;

    //优惠总金额 包含促销
    private BigDecimal privilegeAmountTotal;

    //优惠总金额 包含促销(店内)
    private BigDecimal privilegeInnerAmountTotal;

    //销账笔数
    private Integer offCreditNum;

    //押金
    private TradeDeposit tradeDeposit;

    //附加费总额
    private BigDecimal extraChargeSum;
    // 收押金总额
    private BigDecimal depositPaySum;

    public List<OrderSale> getOrderSaleInnerList() {
        return orderSaleInnerList;
    }

    public void setOrderSaleInnerList(List<OrderSale> orderSaleInnerList) {
        this.orderSaleInnerList = orderSaleInnerList;
    }

    public String getOrderSaleAmountInner() {
        return orderSaleAmountInner;
    }

    public void setOrderSaleAmountInner(String orderSaleAmountInner) {
        this.orderSaleAmountInner = orderSaleAmountInner;
    }

    public BigDecimal getCloseBillAmountInner() {
        return closeBillAmountInner;
    }

    public void setCloseBillAmountInner(BigDecimal closeBillAmountInner) {
        this.closeBillAmountInner = closeBillAmountInner;
    }

    public List<PrivilegeItem> getPrivilegeInnerGroups() {
        return privilegeInnerGroups;
    }

    public void setPrivilegeInnerGroups(List<PrivilegeItem> privilegeInnerGroups) {
        this.privilegeInnerGroups = privilegeInnerGroups;
    }

    public List<DetailsKeyValue> getActualDetailInnerList() {
        return actualDetailInnerList;
    }

    public void setActualDetailInnerList(List<DetailsKeyValue> actualDetailInnerList) {
        this.actualDetailInnerList = actualDetailInnerList;
    }

    public List<DetailsKeyValue> getTotalBillInnerList() {
        return totalBillInnerList;
    }

    public void setTotalBillInnerList(List<DetailsKeyValue> totalBillInnerList) {
        this.totalBillInnerList = totalBillInnerList;
    }

    public BizAmountInfo getBizInfoInner() {
        return bizInfoInner;
    }

    public void setBizInfoInner(BizAmountInfo bizInfoInner) {
        this.bizInfoInner = bizInfoInner;
    }

    public String getBizAmountInner() {
        return bizAmountInner;
    }

    public void setBizAmountInner(String bizAmountInner) {
        this.bizAmountInner = bizAmountInner;
    }

    public BigDecimal getPrivilegeInnerAmountTotal() {
        return privilegeInnerAmountTotal;
    }

    public void setPrivilegeInnerAmountTotal(BigDecimal privilegeInnerAmountTotal) {
        this.privilegeInnerAmountTotal = privilegeInnerAmountTotal;
    }

    //退押金总额
    private BigDecimal depositRefundSum;
    //退押益收
    private BigDecimal depositAmount;

    private BigDecimal busThirdAmount;//第三方平台实收金额

    private BigDecimal thirdUsefulAmount;//第三方预计实付

    private BigDecimal expectedTotalAmount;//预计总收款

    private List<ThirtyPayDetails> busThirdAmountList;//第三方营业收入，订单销售实收

    public String getBelongDate() {
        return belongDate;
    }

    public void setBelongDate(String belongDate) {
        this.belongDate = belongDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BizAmountInfo getBizInfo() {
        return bizInfo;
    }

    public void setBizInfo(BizAmountInfo bizInfo) {
        this.bizInfo = bizInfo;
    }

    public String getBizAmount() {
        return bizAmount;
    }

    public void setBizAmount(String bizAmount) {
        this.bizAmount = bizAmount;
    }

    public List<ReceiptPayType> getBusAmountList() {
        return busAmountList;
    }

    public void setBusAmountList(List<ReceiptPayType> busAmountList) {
        this.busAmountList = busAmountList;
    }

    public String getBizComeAmount() {
        return bizComeAmount;
    }

    public void setBizComeAmount(String bizComeAmount) {
        this.bizComeAmount = bizComeAmount;
    }

    public List<ReceiptPayType> getMemberAmountList() {
        return memberAmountList;
    }

    public void setMemberAmountList(List<ReceiptPayType> memberAmountList) {
        this.memberAmountList = memberAmountList;
    }

    public String getBizMemberAmount() {
        return bizMemberAmount;
    }

    public void setBizMemberAmount(String bizMemberAmount) {
        this.bizMemberAmount = bizMemberAmount;
    }

    public List<TradeTypeCount> getTradeTypeList() {
        return tradeTypeList;
    }

    public void setTradeTypeList(List<TradeTypeCount> tradeTypeList) {
        this.tradeTypeList = tradeTypeList;
    }

    public String getTradeTypeAmount() {
        return tradeTypeAmount;
    }

    public void setTradeTypeAmount(String tradeTypeAmount) {
        this.tradeTypeAmount = tradeTypeAmount;
    }

    public List<TradeAVGCount> getCheckTradeList() {
        return checkTradeList;
    }

    public void setCheckTradeList(List<TradeAVGCount> checkTradeList) {
        this.checkTradeList = checkTradeList;
    }

    public String getCheckAmount() {
        return checkAmount;
    }

    public void setCheckAmount(String checkAmount) {
        this.checkAmount = checkAmount;
    }

    public List<BillDishTypeInfo> getDishTypeList() {
        return dishTypeList;
    }

    public void setDishTypeList(List<BillDishTypeInfo> dishTypeList) {
        this.dishTypeList = dishTypeList;
    }

    public List<BillDishTypeInfo> getDishBigTypeList() {
        return dishBigTypeList;
    }

    public void setDishBigTypeList(List<BillDishTypeInfo> dishBigTypeList) {
        this.dishBigTypeList = dishBigTypeList;
    }

    public String getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(String dishAmount) {
        this.dishAmount = dishAmount;
    }

    public DiscountDetailVo getDiscountDetailVo() {
        return discountDetailVo;
    }

    public void setDiscountDetailVo(DiscountDetailVo discountDetailVo) {
        this.discountDetailVo = discountDetailVo;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public BigDecimal getOnCredit() {
        return onCredit;
    }

    public void setOnCredit(BigDecimal onCredit) {
        this.onCredit = onCredit;
    }

    public BigDecimal getOffCredit() {
        return offCredit;
    }

    public void setOffCredit(BigDecimal offCredit) {
        this.offCredit = offCredit;
    }

    public BigDecimal getExtraChargeSum() {
        return extraChargeSum;
    }

    public void setExtraChargeSum(BigDecimal extraChargeSum) {
        this.extraChargeSum = extraChargeSum;
    }

    public Integer getOnCreditNum() {
        return onCreditNum;
    }

    public void setOnCreditNum(Integer onCreditNum) {
        this.onCreditNum = onCreditNum;
    }

    public Integer getOffCreditNum() {
        return offCreditNum;
    }

    public void setOffCreditNum(Integer offCreditNum) {
        this.offCreditNum = offCreditNum;
    }

    public TradeDeposit getTradeDeposit() {
        return tradeDeposit;
    }

    public void setTradeDeposit(TradeDeposit tradeDeposit) {
        this.tradeDeposit = tradeDeposit;
    }

    public BigDecimal getDepositPaySum() {
        return depositPaySum;
    }

    public BigDecimal getDepositRefundSum() {
        return depositRefundSum;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderSale> getOrderSaleList() {
        return orderSaleList;
    }

    public void setOrderSaleList(List<OrderSale> orderSaleList) {
        this.orderSaleList = orderSaleList;
    }

    public String getOrderSaleAmount() {
        return orderSaleAmount;
    }

    public void setOrderSaleAmount(String orderSaleAmount) {
        this.orderSaleAmount = orderSaleAmount;
    }

    public List<KeyValue> getChargeOffsList() {
        return chargeOffsList;
    }

    public void setChargeOffsList(List<KeyValue> chargeOffsList) {
        this.chargeOffsList = chargeOffsList;
    }

    public String getChargeOffsAmount() {
        return chargeOffsAmount;
    }

    public void setChargeOffsAmount(String chargeOffsAmount) {
        this.chargeOffsAmount = chargeOffsAmount;
    }

    public List<KeyValue> getExpendAccountList() {
        return expendAccountList;
    }

    public void setExpendAccountList(List<KeyValue> expendAccountList) {
        this.expendAccountList = expendAccountList;
    }

    public String getExpendAccount() {
        return expendAccount;
    }

    public void setExpendAccount(String expendAccount) {
        this.expendAccount = expendAccount;
    }

    public List<KeyValue> getHangAccountList() {
        return hangAccountList;
    }

    public void setHangAccountList(List<KeyValue> hangAccountList) {
        this.hangAccountList = hangAccountList;
    }

    public String getHangAccountAmount() {
        return hangAccountAmount;
    }

    public void setHangAccountAmount(String hangAccountAmount) {
        this.hangAccountAmount = hangAccountAmount;
    }

    public List<DishKeyValue> getDishSaleUp() {
        return dishSaleUp;
    }

    public void setDishSaleUp(List<DishKeyValue> dishSaleUp) {
        this.dishSaleUp = dishSaleUp;
    }

    public String getDd() {
        return dd;
    }

    public void setDd(String dd) {
        this.dd = dd;
    }

    public void setDepositPaySum(BigDecimal depositPaySum) {
        this.depositPaySum = depositPaySum;
    }

    public void setDepositRefundSum(BigDecimal depositRefundSum) {
        this.depositRefundSum = depositRefundSum;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public List<KeyValue> getOtherIncomeList() {
        return otherIncomeList;
    }

    public void setOtherIncomeList(List<KeyValue> otherIncomeList) {
        this.otherIncomeList = otherIncomeList;
    }

    public String getOtherIncomeAmount() {
        return otherIncomeAmount;
    }

    public void setOtherIncomeAmount(String otherIncomeAmount) {
        this.otherIncomeAmount = otherIncomeAmount;
    }

    public String getMemberConsumeAmount() {
        return memberConsumeAmount;
    }

    public void setMemberConsumeAmount(String memberConsumeAmount) {
        this.memberConsumeAmount = memberConsumeAmount;
    }

    public String getReturnAmount() {
        return returnAmount;
    }

    public void setReturnAmount(String returnAmount) {
        this.returnAmount = returnAmount;
    }

    public BigDecimal getCloseBillAmount() {
        return closeBillAmount;
    }

    public void setCloseBillAmount(BigDecimal closeBillAmount) {
        this.closeBillAmount = closeBillAmount;
    }

    public BigDecimal getOnOffCreditAmount() {
        return onOffCreditAmount;
    }

    public void setOnOffCreditAmount(BigDecimal onOffCreditAmount) {
        this.onOffCreditAmount = onOffCreditAmount;
    }

    public List<PrivilegeItem> getPrivilegeGroups() {
        return privilegeGroups;
    }

    public void setPrivilegeGroups(List<PrivilegeItem> privilegeGroups) {
        this.privilegeGroups = privilegeGroups;
    }

    public BigDecimal getPrivilegeAmountTotal() {
        return privilegeAmountTotal;
    }

    public void setPrivilegeAmountTotal(BigDecimal privilegeAmountTotal) {
        this.privilegeAmountTotal = privilegeAmountTotal;
    }

    public List<DetailsKeyValue> getActualDetailList() {
        return actualDetailList;
    }

    public void setActualDetailList(List<DetailsKeyValue> actualDetailList) {
        this.actualDetailList = actualDetailList;
    }

    public List<DetailsKeyValue> getTotalBillList() {
        return totalBillList;
    }

    public void setTotalBillList(List<DetailsKeyValue> totalBillList) {
        this.totalBillList = totalBillList;
    }

    public List<DetailsKeyValue> getHangBillList() {
        return hangBillList;
    }

    public void setHangBillList(List<DetailsKeyValue> hangBillList) {
        this.hangBillList = hangBillList;
    }


    public BigDecimal getBusThirdAmount() {
        return busThirdAmount;
    }

    public void setBusThirdAmount(BigDecimal busThirdAmount) {
        this.busThirdAmount = busThirdAmount;
    }

    public BigDecimal getThirdUsefulAmount() {
        return thirdUsefulAmount;
    }

    public void setThirdUsefulAmount(BigDecimal thirdUsefulAmount) {
        this.thirdUsefulAmount = thirdUsefulAmount;
    }

    public BigDecimal getExpectedTotalAmount() {
        return expectedTotalAmount;
    }

    public void setExpectedTotalAmount(BigDecimal expectedTotalAmount) {
        this.expectedTotalAmount = expectedTotalAmount;
    }

    public List<ThirtyPayDetails> getBusThirdAmountList() {
        return busThirdAmountList;
    }

    public void setBusThirdAmountList(List<ThirtyPayDetails> busThirdAmountList) {
        this.busThirdAmountList = busThirdAmountList;
    }

    public String getBookingActualAmount() {
        return bookingActualAmount;
    }

    public void setBookingActualAmount(String bookingActualAmount) {
        this.bookingActualAmount = bookingActualAmount;
    }

    public List<KeyValue> getBookingPayList() {
        return bookingPayList;
    }

    public void setBookingPayList(List<KeyValue> bookingPayList) {
        this.bookingPayList = bookingPayList;
    }

    /**
     * 判断当前板块统计是否选中
     *
     * @param _state
     * @return
     */
    public boolean isItemChecked(int _state) {
        return (state & _state) == _state;
    }

    public List<TitleBean> getTitleList() {
        List<TitleBean> result = new ArrayList<>();
        addTitleBean(result, R.string.handover_print_collect_pay_detail, totalBillList, stateTotalBillList);
        addTitleBean(result, R.string.handover_print_hang_account_total, hangBillList, stateHangBillList);
        addTitleBean(result, R.string.handover_print_sales_promotion_privilege_detail, privilegeGroups, statePrivilegeGroups);
        addTitleBean(result, R.string.handover_print_actual_collect_detail, actualDetailList, stateActualDetailList);
        addTitleBean(result, R.string.handover_print_business_overview, tradeTypeList, stateTradeTypeList);
        addTitleBean(result, R.string.handover_print_sale_type_statistical, dishTypeList, stateDishTypeList);
        addTitleBean(result, R.string.handover_print_sale_big_type_statistical, dishBigTypeList, stateDishBigTypeList);
        addTitleBean(result, R.string.handover_print_third_expense_details, busThirdAmountList, stateThirdTakeOutList);
        return result;
    }

    public void addTitleBean(List<TitleBean> result, int titleResId, List<?> contentList, int checkState) {
        if (contentList != null && contentList.size() > 0) {
            result.add(new TitleBean(titleResId, contentList, checkState));
        }
    }

    /**
     * 清除未选择项
     *
     * @param titleBeanList
     */
    public void setItemChecked(List<TitleBean> titleBeanList) {
        state = 0;
        for (TitleBean bean : titleBeanList) {
            state |= bean.checkState;
        }
    }

    static public class TitleBean {
        int titleResId;
        List<?> contentList;
        int checkState;

        public TitleBean(int titleResId, List<?> contentList, int checkState) {
            this.titleResId = titleResId;
            this.contentList = contentList;
            this.checkState = checkState;
        }

        @Override
        public String toString() {
            return BaseApplication.sInstance.getString(titleResId);
        }
    }

}
