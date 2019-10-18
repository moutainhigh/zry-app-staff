package com.zhongmei.bty.cashier.shoppingcart.vo;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.enums.ClearStatus;


public class DishStandardVo extends DishPropertyVo {


    public static final int ENABLE = 0;

    public static final int DISABLE = 1;

    private final int state;

    private final ClearStatus clearStatus;

    public DishStandardVo(DishProperty property, boolean selected, int state) {
        this(property, selected, state, ClearStatus.SALE);
    }

    public DishStandardVo(DishProperty property, boolean selected, int state, ClearStatus clearStatus) {
        super(property, selected);
        this.state = state;
        this.clearStatus = clearStatus;
    }

    public int getState() {
        return state;
    }

    public ClearStatus getClearStatus() {
        return clearStatus;
    }

}
