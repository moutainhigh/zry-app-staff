package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.bty.basemodule.orderdish.manager.DishSetmealManager;
import com.zhongmei.yunfu.db.entity.trade.TradeReasonRel;

import java.math.BigDecimal;
import java.util.List;

/**
 * 单品或套餐条目
 *
 * @version: 1.0
 * @date 2015年7月10日
 */
public interface IShopcartItem extends IShopcartItemBase {

    List<? extends ISetmealShopcartItem> getSetmealItems();

    List<? extends ISetmealShopcartItem> getServerItems();

    DishSetmealManager getSetmealManager();

    boolean hasSetmeal();

    /**
     * 将本条目进行拆单支付
     *
     * @return 返回新拆出的条目
     */
    IShopcartItem split();

    /**
     * 撤消拆单支付
     */
    void cancelSplit();

    /**
     * 改菜（针对已保存过服务器的菜品）
     *
     * @return
     */
    ShopcartItem modifyDish();

    void cancelModifyDish();

    /**
     * 将本条目进行指定数量的退菜
     *
     * @param qty    被退掉的菜数量，为负值。
     * @param reason
     * @return 返回新生成的条目
     */
    IShopcartItem returnQty(BigDecimal qty, TradeReasonRel reason);

    /**
     * 将本条目全部退菜
     *
     * @param reason
     * @return 返回新生成的条目
     */
    IShopcartItem returnQty(TradeReasonRel reason);

    /**
     * 撤消退菜
     */
    void cancelReturnQty();

    //获得item参加的活动的uuid，仅本地使用
    String getTradePlanUUID();

    /**
     * @Title: setTradePlanUUID
     * @Description: 将tradePlanActivity表的uuid缓存，方便使用
     * @Param @param tradePlanUuid TODO
     * @Return void 返回类型
     */
    void setTradePlanUUID(String tradePlanUuid);

    void addSetmeal(ISetmealShopcartItem setmealShopcartItem);

    void deleteSetmeal(ISetmealShopcartItem setmealShopcartItem);

    long getClientCreateTime();

}
