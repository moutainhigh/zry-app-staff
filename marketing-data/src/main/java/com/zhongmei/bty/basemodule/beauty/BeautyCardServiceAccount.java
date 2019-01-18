package com.zhongmei.bty.basemodule.beauty;

import com.zhongmei.yunfu.db.entity.dish.DishShop;

import java.io.Serializable;
import java.util.List;

/**
 * 次卡服务
 *
 * @date 2018/6/14
 */
public class BeautyCardServiceAccount implements Serializable {
    public Long brandIdenty;
    /**
     * 次卡id
     */
    public Long cardInstanceId;
    /**
     * 服务id = brand_dish_id
     */
    public Long serviceId;
    /**
     * 服务名
     */
    public String serviceName;
    /**
     * 服务总次数
     */
    public Integer serviceTotalTime;
    /**
     * 服务剩余次数
     */
    public Integer serviceRemainderTime;


    public List<DishShop> listDishShops;


//    /**
//     * 服务商品
//     */
//    public DishShop dishShop;
}
