package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.trade.entity.TradeBuffetPeople;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteNorms;

import java.io.Serializable;
import java.math.BigDecimal;



public class CustomerTypeBean extends DishCarteNorms implements Serializable {
    private BigDecimal count;
    private BigDecimal minValue = BigDecimal.ZERO;
    private BigDecimal maxValue = BigDecimal.valueOf(999l);

    public CustomerTypeBean(DishCarteNorms dishCarteNorm) {
        this.count = BigDecimal.ZERO;
        setId(dishCarteNorm.getId());
        setName(dishCarteNorm.getName());
        setPrice(dishCarteNorm.getPrice());
        setCarteId(dishCarteNorm.getCarteId());
        setStatusFlag(dishCarteNorm.getStatusFlag());
        setCreateId(dishCarteNorm.getCreateId());
        setUpdateId(dishCarteNorm.getUpdateId());
        setBrandIdenty(dishCarteNorm.getBrandIdenty());
        setServerCreateTime(dishCarteNorm.getServerCreateTime());
        setServerUpdateTime(dishCarteNorm.getServerUpdateTime());
    }


    public CustomerTypeBean(TradeBuffetPeople buffetPeople, Long carteId) {
        this.count = BigDecimal.ZERO;
        setId(buffetPeople.getCarteNormsId());
        setName(buffetPeople.getCarteNormsName());
        setPrice(buffetPeople.getCartePrice());
        setCarteId(carteId);
        setStatusFlag(buffetPeople.getStatusFlag());
        setCreateId(buffetPeople.getCreatorId());
        setUpdateId(buffetPeople.getUpdatorId());
        setBrandIdenty(buffetPeople.getBrandIdenty());
        setServerCreateTime(buffetPeople.getServerCreateTime());
        setServerUpdateTime(buffetPeople.getServerUpdateTime());
        setCount(buffetPeople.getPeopleCount());
    }

    public CustomerTypeBean(String name, BigDecimal count) {
        setName(name);
        this.count = count;
    }

    public void setBorderValue(BigDecimal minV, BigDecimal maxV) {
        this.minValue = minV;
        this.maxValue = maxV;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(BigDecimal minValue) {
        this.minValue = minValue;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }
}
