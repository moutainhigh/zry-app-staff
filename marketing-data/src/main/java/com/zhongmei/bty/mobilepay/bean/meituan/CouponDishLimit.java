package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 * 美团券限制菜品
 */

public class CouponDishLimit {
    //对应mappingType 包含
    public static final int TYPE_CONTAIN = 1;
    //    排除
    public static final int TYPE_EXCULE = 2;

    public Long brandIdenty;
    public Long shopIdenty;
    public Long dishId;
    public String dishUuid;
    public String couponId;
    public BigDecimal num;//菜品数量
    public Long dishType;//菜品类别
    public int mappingType;// 1包含 2排除
}
