package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.bty.basemodule.orderdish.entity.DishCarte;
import com.zhongmei.bty.basemodule.orderdish.entity.DishCarteDetail;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class BuffetComboVo implements Serializable {
    private BigDecimal totalCount = BigDecimal.ZERO;//总人数
    private DishCarte dishCarte;
    private List<DishCarteDetail> dishCarteDetails;
    private List<CustomerTypeBean> dishCarteNorms;

    public DishCarte getDishCarte() {
        return dishCarte;
    }

    public void setDishCarte(DishCarte dishCarte) {
        this.dishCarte = dishCarte;
    }

    public List<DishCarteDetail> getDishCarteDetails() {
        return dishCarteDetails;
    }

    public void setDishCarteDetails(List<DishCarteDetail> dishCarteDetails) {
        this.dishCarteDetails = dishCarteDetails;
    }

    public List<CustomerTypeBean> getDishCarteNorms() {
        return dishCarteNorms;
    }

    public void setDishCarteNorms(List<CustomerTypeBean> dishCarteNorms) {
        this.dishCarteNorms = dishCarteNorms;
    }

    public BigDecimal getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(BigDecimal totalCount) {
        this.totalCount = totalCount;
    }
}
