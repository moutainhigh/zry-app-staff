package com.zhongmei.bty.snack.orderdish;

import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;

public interface PropertySelectListener {
    boolean addProperty(OrderProperty property, boolean needAdd);

    boolean deleteProperty(OrderProperty property);

    void deleteProperties();
}
