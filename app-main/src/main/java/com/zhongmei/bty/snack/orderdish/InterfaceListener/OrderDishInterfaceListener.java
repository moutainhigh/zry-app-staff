package com.zhongmei.bty.snack.orderdish.InterfaceListener;

import com.zhongmei.bty.snack.orderdish.buinessview.ExtraInfo;
import com.zhongmei.bty.snack.orderdish.buinessview.ExtraView;
import com.zhongmei.yunfu.db.entity.discount.TradePrivilege;
import com.zhongmei.bty.basemodule.orderdish.bean.OrderProperty;

import java.math.BigDecimal;
import java.util.List;

/**
 * 点菜界面接口回调
 */
public interface OrderDishInterfaceListener {

    /**
     * 设置手动折扣回调方法
     *
     * @param mTradePrivilege
     */
    void setDiscount(TradePrivilege mTradePrivilege);

    /**
     * 添加做法
     *
     * @param property
     * @param needAdd
     */
    void addExperience(OrderProperty property, boolean needAdd);

    /**
     * 移除做法
     *
     * @param property
     */
    void deleteExperience(OrderProperty property);

    /**
     * 加料
     *
     * @param extraInfo
     * @param qty
     */
    void onAddMaterial(ExtraInfo extraInfo, BigDecimal qty);

    /**
     * 备注
     *
     * @param memo
     * @param isOrderRemark 是否整单备注
     */
    void onMemoChanged(String memo, boolean isOrderRemark);

    /**
     * 批量操作，选择口味做法
     *
     * @param propertyList
     */
    void onBatchAddProperty(List<OrderProperty> propertyList);

    /**
     * 批量操作时，添加加料
     *
     * @param extraInfoList
     */
    void onBatchAddExtra(List<ExtraInfo> extraInfoList);

    /**
     * 批量操作时修改备注
     *
     * @param memo
     */
    void onBatchMemoChange(String memo);
}
