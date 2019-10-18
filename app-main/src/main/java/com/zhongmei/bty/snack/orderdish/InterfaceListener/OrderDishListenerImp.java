package com.zhongmei.bty.snack.orderdish.InterfaceListener;

import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;

import java.math.BigDecimal;
import java.util.List;


public class OrderDishListenerImp implements OrderDishInterfaceListener {
    @Override
    public void setDiscount(TradePrivilege mTradePrivilege) {

    }

    @Override
    public void addExperience(OrderProperty property, boolean needAdd) {

    }

    @Override
    public void deleteExperience(OrderProperty property) {

    }

    @Override
    public void onAddMaterial(ExtraInfo extraInfo, BigDecimal qty) {

    }

    @Override
    public void onMemoChanged(String memo, boolean isOrderRemark) {

    }

    @Override
    public void onBatchAddProperty(List<OrderProperty> propertyList) {

    }

    @Override
    public void onBatchAddExtra(List<ExtraInfo> extraInfoList) {

    }

    @Override
    public void onBatchMemoChange(String memo) {

    }
}
