package com.zhongmei.bty.mobilepay.bean.meituan;

import java.math.BigDecimal;



public class CouponDishLimit {
        public static final int TYPE_CONTAIN = 1;
        public static final int TYPE_EXCULE = 2;

    public Long brandIdenty;
    public Long shopIdenty;
    public Long dishId;
    public String dishUuid;
    public String couponId;
    public BigDecimal num;    public Long dishType;    public int mappingType;}
