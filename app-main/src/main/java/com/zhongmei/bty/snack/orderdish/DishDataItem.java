package com.zhongmei.bty.snack.orderdish;

import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItemBase;
import com.zhongmei.bty.basemodule.discount.bean.CouponPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.IntegralCashPrivilegeVo;
import com.zhongmei.bty.basemodule.discount.bean.WeiXinCouponsVo;

/**
 * @Date： 16/8/31
 * @Description:
 * @Version: 1.0
 */
public class DishDataItem {
    private IShopcartItemBase base;

    private int type;

    private double value;// 金额

    private String name;// 名字

    private String uuid;// 单菜或套餐uuid

    private CouponPrivilegeVo couponPrivilegeVo;// 优惠劵信息

    private WeiXinCouponsVo weiXinCouponsVo;//微信优惠券信息

    private IntegralCashPrivilegeVo integralCashPrivilegeVo;// 积分抵现

    private ExtraCharge extraCharge;//附加费

    private boolean isCouponEnabled;

    private Integer depositType;//押金类型

    private DishCheckStatus checkStatus;

    private String tradePlanActivityUuid;// 营销活动uuid

    private boolean needTopLine = true;//展示时是否需要topline，默认为true

    // private boolean isIntegralEnable;


    public Integer getDepositType() {
        return depositType;
    }

    public void setDepositType(Integer depositType) {
        this.depositType = depositType;
    }

    public DishDataItem(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CouponPrivilegeVo getCouponPrivilegeVo() {
        return couponPrivilegeVo;
    }

    public void setCouponPrivilegeVo(CouponPrivilegeVo couponPrivilegeVo) {
        this.couponPrivilegeVo = couponPrivilegeVo;
    }

    public WeiXinCouponsVo getWeiXinCouponsVo() {
        return weiXinCouponsVo;
    }

    public void setWeiXinCouponsVo(WeiXinCouponsVo weiXinCouponsVo) {
        this.weiXinCouponsVo = weiXinCouponsVo;
    }

    public IntegralCashPrivilegeVo getIntegralCashPrivilegeVo() {
        return integralCashPrivilegeVo;
    }

    public void setIntegralCashPrivilegeVo(IntegralCashPrivilegeVo integralCashPrivilegeVo) {
        this.integralCashPrivilegeVo = integralCashPrivilegeVo;
    }

    public boolean isCouponEnabled() {
        return isCouponEnabled;
    }

    public void setCouponEnabled(boolean isCouponEnabled) {
        this.isCouponEnabled = isCouponEnabled;
    }

    public void setCheckStatus(DishCheckStatus checkStatus) {
        this.checkStatus = checkStatus;
    }

    public DishCheckStatus getCheckStatus() {
        return checkStatus;
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

    public ExtraCharge getExtraCharge() {
        return extraCharge;
    }

    public void setExtraCharge(ExtraCharge extraCharge) {
        this.extraCharge = extraCharge;
    }


    /*
     * public boolean isIntegralEnable() { return
     * isIntegralEnable; }
     *
     * public void setIntegralEnable(boolean
     * isIntegralEnable) { this.isIntegralEnable =
     * isIntegralEnable; }
     */

    public enum DishCheckStatus {
        NOT_CHECK, CHECKED, INVALIATE_CHECK
    }
}
