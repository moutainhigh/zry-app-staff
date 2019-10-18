package com.zhongmei.bty.cashier.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import com.zhongmei.yunfu.db.entity.dish.DishProperty;
import com.zhongmei.yunfu.db.entity.trade.TradeItemProperty;
import com.zhongmei.bty.basemodule.orderdish.bean.ExtraShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.SetmealShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItemBase;
import com.zhongmei.bty.basemodule.orderdish.bean.TradeItemVo;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;


public class CreateDishTool {


    public static List<ShopcartItem> tradeToDish(TradeVo mTradeVo) {
        List<ShopcartItem> listShopcartItem = new ArrayList<ShopcartItem>();
        List<TradeItemVo> listTradeItemVo = mTradeVo.getTradeItemList();

        return listShopcartItem;
    }

    private static ShopcartItemBase buildShopcartItem(TradeItemVo tradeItem) {
        return null;
    }


    private static SetmealShopcartItem buildSetmealShopcartItem(TradeItemVo tradeItem) {
        return null;
    }

    private static ExtraShopcartItem buildExtraDishItem(TradeItemVo tradeItem) {
        return null;
    }


    private static DishProperty getDishProperty(TradeItemProperty property) {
        DishProperty dishProperty = new DishProperty();
        dishProperty.setCreatorId(property.getCreatorId());
        dishProperty.setCreatorName(property.getCreatorName());
        dishProperty.setName(property.getPropertyName());
        dishProperty.setReprice(property.getAmount());
        dishProperty.setUuid(property.getPropertyUuid());

        return dishProperty;
    }

}
