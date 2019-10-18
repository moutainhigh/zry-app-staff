package com.zhongmei.bty.mobilepay.message;

import com.zhongmei.bty.mobilepay.bean.meituan.CouponDishLimit;
import com.zhongmei.bty.mobilepay.bean.meituan.IGroupBuyingCoupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;



public class TuanGouCouponDetail implements IGroupBuyingCoupon {
    Long grouponId;    String serialNumber;
    String buyDate;    String mobile;    int status;    String statusDesc;
    int minConsume;
    int maxConsume;
    String dealTitle;    Long dealId;    int canCancel;    Long beginTime;
    Long endTime;
    private BigDecimal marketPrice = BigDecimal.ZERO;    private BigDecimal price = BigDecimal.ZERO;     String remark;    int couponType = -1;    private List<CouponDishLimit> dishMapping;
    public Long getGrouponId() {
        return grouponId;
    }

    public String getSerialNumber() {
                return serialNumber;
    }

    public String getBuyDate() {
        return buyDate;
    }

    public String getMobile() {
        return mobile;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public int getMinConsume() {
        return minConsume;
    }

    public int getMaxConsume() {
        return maxConsume;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    @Override
    public Long getDealId() {
        return dealId;
    }

    public int getCanCancel() {
        return canCancel;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getRemark() {
        return remark;
    }

    @Override
    public int getCouponType() {
        return couponType;
    }

    @Override
    public List<CouponDishLimit> getLimitDish() {
        return dishMapping;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((serialNumber == null) ? 0 : serialNumber.hashCode());
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
        TuanGouCouponDetail other = (TuanGouCouponDetail) obj;
        if (serialNumber == null) {
            if (other.serialNumber != null)
                return false;
        } else if (!serialNumber.equals(other.serialNumber))
            return false;
        return true;
    }

    public void initTestData(String ticketNo) {
        this.dealTitle = "测试券";
        this.couponType = 1;
        this.marketPrice = BigDecimal.valueOf(60);
        this.price = BigDecimal.valueOf(20);
        this.maxConsume = 1;
        this.minConsume = 1;
        this.serialNumber = ticketNo + "1236";
        if (dishMapping == null || dishMapping.size() == 0) {
            dishMapping = new ArrayList<CouponDishLimit>();
            CouponDishLimit ob1 = new CouponDishLimit();
            dishMapping.add(ob1);
            ob1.num = new BigDecimal(1);
            ob1.mappingType = 2;
            ob1.dishUuid = "2932bde9a7d9469ab567eae6f477097f";
            CouponDishLimit ob2 = new CouponDishLimit();
            dishMapping.add(ob2);
            ob1.num = new BigDecimal(2);
            ob1.mappingType = 2;
            ob1.dishUuid = "76e20e34cdd540e7b4c25ad8e14683ea";
        }
    }
}
