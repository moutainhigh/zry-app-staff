package com.zhongmei.bty.basemodule.beauty;

import com.zhongmei.yunfu.db.entity.dish.DishShop;

import java.io.Serializable;
import java.util.List;


public class BeautyCardServiceAccount implements Serializable {
    public Long brandIdenty;

    public Long cardInstanceId;

    public Long serviceId;

    public String serviceName;

    public Integer serviceTotalTime;

    public Integer serviceRemainderTime;


    public List<DishShop> listDishShops;


}
