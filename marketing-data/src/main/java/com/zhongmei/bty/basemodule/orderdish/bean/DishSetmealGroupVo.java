package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishSetmealGroup;
import com.zhongmei.yunfu.util.MathDecimal;

import java.math.BigDecimal;


public class DishSetmealGroupVo {

    private DishSetmealGroup setmealGroup;
    private BigDecimal selectedQty;

    public DishSetmealGroupVo(DishSetmealGroup setmealGroup) {
        this.setmealGroup = setmealGroup;
    }

    public DishSetmealGroup getSetmealGroup() {
        return setmealGroup;
    }

    public BigDecimal getSelectedQty() {
        return selectedQty;
    }

    public void setSelectedQty(BigDecimal selectedQty) {
        this.selectedQty = selectedQty;
    }

    public boolean isValid() {
        BigDecimal qty = MathDecimal.nullToZero(selectedQty);
        return qty.compareTo(setmealGroup.getOrderMin()) >= 0;
    }

}
