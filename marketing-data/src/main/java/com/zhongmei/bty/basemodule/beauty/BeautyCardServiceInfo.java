package com.zhongmei.bty.basemodule.beauty;

import com.zhongmei.yunfu.db.entity.dish.DishShop;

import java.util.ArrayList;
import java.util.List;

/**
 * 次卡服务信息
 *
 * @date 2018/6/14
 */
public class BeautyCardServiceInfo {

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


    public List<DishShop> listDishShops=new ArrayList<>();

    public Long cardExpireDate;

    public Integer cardType;//3部分，4全部


    /**
     * 是否过期
     * @return true 过期  false 未过期
     */
    public boolean isOutTime(){
        return false;
    }
}
