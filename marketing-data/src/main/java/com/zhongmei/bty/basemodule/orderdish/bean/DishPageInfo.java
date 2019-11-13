package com.zhongmei.bty.basemodule.orderdish.bean;

import com.zhongmei.yunfu.db.entity.dish.DishBrandType;

import java.util.List;

public class DishPageInfo { //存放品牌商品的pageInfo
    public DishBrandType dishBrand;
    public List<DishVo> listDishVos;
    public final boolean scanResult;
    public int position;//当前index


    public DishPageInfo(DishBrandType dishType, List<DishVo> dishList,int index, boolean scanResult){
        this.dishBrand = dishType;
        this.listDishVos = dishList;
        this.position=index;
        this.scanResult = scanResult;
    }


}
