package com.zhongmei.bty.basemodule.beauty;

import com.zhongmei.yunfu.db.entity.dish.DishShop;

import java.util.ArrayList;
import java.util.List;


public class BeautyCardServiceInfo {

    public Long brandIdenty;
    
    public Long cardInstanceId;
    
    public Long serviceId;
    
    public String serviceName;
    
    public Integer serviceTotalTime;
    
    public Integer serviceRemainderTime;


    public List<DishShop> listDishShops=new ArrayList<>();

    public Long cardExpireDate;

    public Integer cardType;


    
    public boolean isOutTime(){
        return false;
    }
}
