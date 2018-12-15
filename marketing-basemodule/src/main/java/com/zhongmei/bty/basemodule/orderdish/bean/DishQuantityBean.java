package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.math.BigDecimal;

/**
 * Created by demo on 2018/12/15
 */
public class DishQuantityBean {
    public BigDecimal quantity;
    public IShopcartItem shopcartItem;
    public TableSeat tableSeat;

    public DishQuantityBean() {
    }

    ;

    public DishQuantityBean(IShopcartItem shopcartItem) {
        this.quantity = shopcartItem.getSingleQty();
        this.shopcartItem = shopcartItem;
    }

    ;

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public boolean isMoveAll() {
        return false;
    }

}
