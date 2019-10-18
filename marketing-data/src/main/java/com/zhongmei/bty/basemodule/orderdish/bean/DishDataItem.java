package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.orderdish.enums.ExtraItemType;
import com.zhongmei.bty.basemodule.orderdish.enums.ItemType;
import com.zhongmei.yunfu.db.entity.trade.TradeUser;
import com.zhongmei.yunfu.db.enums.InvalidType;

import java.math.BigDecimal;


public class DishDataItem {
        private IShopcartItem item;

        private IShopcartItemBase base;

    private ItemType type;

    private ExtraItemType extraType;

    private DishDataItem relateItem;

    private double value;
    private String name;        private String dishDesc;

    private Integer count = 0;
    private IOrderProperty property;

    private String standText;
    private CouponPrivilegeVo couponPrivilegeVo;
    private IntegralCashPrivilegeVo integralCashPrivilegeVo;
    private WeiXinCouponsVo weiXinCouponsVo;
    private boolean isEnabled = false;
    private boolean isDishServing = false;
    private boolean isMemberDiscount = false;
    private boolean canEditNumber = false;
    private BigDecimal[] discountPrices;
    private String discountReason;
    private String tradeReason;
        private ExtraCharge extraCharge;

    private DishCheckStatus checkStatus = DishCheckStatus.INVALIATE_CHECK;
    private String tradePlanActivityUuid;
    private boolean needTopLine = true;        private boolean isSelected = false;
        private Long dishTypeId;
        private boolean isCategory = false;

    private boolean isPaid = false;

    private TradeUser tradeItemUser;

    private TradeUser tradeUser;

    private String serverTime;
    private String chargingRule;
    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public String getChargingRule() {
        return chargingRule;
    }

    public void setChargingRule(String chargingRule) {
        this.chargingRule = chargingRule;
    }

    public DishDataItem(ItemType type) {
        this.type = type;
    }

    public ItemType getType() {
        return type;
    }

    public ExtraItemType getExtraType() {
        return extraType;
    }

    public void setExtraType(ExtraItemType extraType) {
        this.extraType = extraType;
    }

    public IShopcartItem getItem() {
        return item;
    }

    public void setItem(IShopcartItem item) {
        this.item = item;
    }

    public IShopcartItemBase getBase() {
        return base;
    }

    public void setBase(IShopcartItemBase base) {
        this.base = base;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CouponPrivilegeVo getCouponPrivilegeVo() {
        return couponPrivilegeVo;
    }

    public void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegeVo) {
        this.couponPrivilegeVo = couponPrivilegeVo;
    }

    public IntegralCashPrivilegeVo getIntegralCashPrivilegeVo() {
        return integralCashPrivilegeVo;
    }

    public void setIntegralCashPrivilegeVo(IntegralCashPrivilegeVo integralCashPrivilegeVo) {
        this.integralCashPrivilegeVo = integralCashPrivilegeVo;
    }

    public WeiXinCouponsVo getWeiXinCouponsVo() {
        return weiXinCouponsVo;
    }

    public void setWeiXinCouponsVo(WeiXinCouponsVo weiXinCouponsVo) {
        this.weiXinCouponsVo = weiXinCouponsVo;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public boolean isDishServing() {
        return isDishServing;
    }

    public void setDishServing(boolean isDishServing) {
        this.isDishServing = isDishServing;
    }

    public BigDecimal[] getDiscountPrices() {
        return discountPrices;
    }

    public void setDiscountPrices(BigDecimal[] discountPrices) {
        this.discountPrices = discountPrices;
    }

    public DishDataItem getRelateItem() {
        return relateItem;
    }

    public void setRelateItem(DishDataItem relateItem) {
        this.relateItem = relateItem;
    }


    public boolean isModifyDishItem() {
        if ((type == ItemType.SINGLE || type == ItemType.COMBO)
                && base != null && base.getInvalidType() == InvalidType.MODIFY_DISH) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isReturnDishItem() {
        if ((type == ItemType.SINGLE || type == ItemType.COMBO)
                && base != null && base.getInvalidType() == InvalidType.RETURN_QTY) {
            return true;
        } else {
            return false;
        }
    }

    public String getTradeReason() {
        return tradeReason;
    }

    public void setTradeReason(String tradeReason) {
        this.tradeReason = tradeReason;
    }

    public boolean isMemberDiscount() {
        return isMemberDiscount;
    }

    public void setMemberDiscount(boolean isMemberDiscount) {
        this.isMemberDiscount = isMemberDiscount;
    }

    public ExtraCharge getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(ExtraCharge extraCharge) {
        this.extraCharge = extraCharge;
    }

    public DishCheckStatus getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(DishCheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public enum DishCheckStatus {
        NOT_CHECK, CHECKED, INVALIATE_CHECK
    }

    public String getTradePlanActivityUuid() {
        return tradePlanActivityUuid;
    }

    public void setTradePlanActivityUuid(String tradePlanActivityUuid) {
        this.tradePlanActivityUuid = tradePlanActivityUuid;
    }

    public boolean isNeedTopLine() {
        return needTopLine;
    }

    public void setNeedTopLine(boolean needTopLine) {
        this.needTopLine = needTopLine;
    }

    public String getDiscountReason() {
        return discountReason;
    }

    public void setDiscountReason(String discountReason) {
        this.discountReason = discountReason;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getStandText() {
        return standText;
    }

    public void setStandText(String standText) {
        this.standText = standText;
    }

    public IOrderProperty getProperty() {
        return property;
    }

    public void setProperty(IOrderProperty property) {
        this.property = property;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public boolean isCategory() {
        return isCategory;
    }

    public Long getDishTypeId() {
        return dishTypeId;
    }

    public void setDishTypeId(Long dishTypeId) {
        this.dishTypeId = dishTypeId;
    }

    public void setCategory(boolean category) {
        isCategory = category;
    }

    public boolean isCanEditNumber() {
        return canEditNumber;
    }

    public void setCanEditNumber(boolean canEditNumber) {
        this.canEditNumber = canEditNumber;
    }

    public String getDishDesc() {
        return dishDesc;
    }

    public void setDishDesc(String dishDesc) {
        this.dishDesc = dishDesc;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public void setPaid(boolean paid) {
        isPaid = paid;
    }

    public TradeUser getTradeItemUser() {
        return tradeItemUser;
    }

    public void setTradeItemUser(TradeUser tradeItemUser) {
        this.tradeItemUser = tradeItemUser;
    }

    public TradeUser getTradeUser() {
        return tradeUser;
    }

    public void setTradeUser(TradeUser tradeUser) {
        this.tradeUser = tradeUser;
    }
}
