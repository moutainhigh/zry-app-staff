package com.zhongmei.bty.basemodule.shoppingcart.bean;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */

public class MathVo {

    public BigDecimal saleAmount = BigDecimal.ZERO;

    //能参与整单折扣的金额
    public BigDecimal dishAllAmout = BigDecimal.ZERO;

    //优惠总金额
    public BigDecimal totalPrivilegeAmount = BigDecimal.ZERO;

    // 用于保存不能参整单打折菜品总价

    public BigDecimal noDiscountAllAmout = BigDecimal.ZERO;

    // 用于保存不能参加整单打折菜品优惠总价

    public BigDecimal noDiscPrivilegeAmout = BigDecimal.ZERO;

    //税费计算原始金额
    public BigDecimal taxableInAmount = BigDecimal.ZERO;

    //储值优惠金额
    public BigDecimal chargePriviegeAmount = BigDecimal.ZERO;
}
