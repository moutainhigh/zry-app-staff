package com.zhongmei.bty.snack.orderdish.InterfaceListener;

import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;

import java.math.BigDecimal;
import java.util.List;


public interface OrderDishInterfaceListener {


    void setDiscount(TradePrivilege mTradePrivilege);


    void addExperience(OrderProperty property, boolean needAdd);


    void deleteExperience(OrderProperty property);


    void onAddMaterial(ExtraInfo extraInfo, BigDecimal qty);


    void onMemoChanged(String memo, boolean isOrderRemark);


    void onBatchAddProperty(List<OrderProperty> propertyList);


    void onBatchAddExtra(List<ExtraInfo> extraInfoList);


    void onBatchMemoChange(String memo);
}
