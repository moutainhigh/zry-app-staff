package com.zhongmei.bty.basemodule.discount.bean.salespromotion;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <pre>
 *     该实体，主要用于单商品促销，统计每个才的总数量
 * com.zhongmei.bty.basemodule.discount.bean.salespromotion
 *
 * *-------------------------------------------------------------------*
 *     scott
 *                                    江城子 . 程序员之歌
 *     /\__/\
 *    /`    '\                     十年生死两茫茫，写程序，到天亮。
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!          千行代码，Bug何处藏。
 *    \  --  /                     纵使上线又怎样，朝令改，夕断肠。
 *   /        \                    领导每天新想法，天天改，日日忙。
 *  /          \                       相顾无言，惟有泪千行。
 * |            |                  每晚灯火阑珊处，夜难寐，加班狂。
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * Created by demo on 2018/12/15
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public class SalesPromotionDishQuantity implements Serializable {

    /**
     * 菜品类型id
     */
    public Long dishTypeId;

    /**
     * 菜品id
     */
    public Long brandDishId;
    /**
     * 菜品数量
     */
    public BigDecimal singleQty;

    /**
     * 菜品价格
     */
    public BigDecimal price;

    /**
     * 购物车对应的菜品
     */
    public IShopcartItem shopcartItem;
}
