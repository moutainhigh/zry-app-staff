package com.zhongmei.bty.basemodule.shoppingcart.bean;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;
import com.zhongmei.bty.basemodule.orderdish.bean.ShopcartItem;

import java.util.ArrayList;
import java.util.List;



public class GiftShopcartItemSingleton {

    private GiftShopcartItemSingleton() {

    }

    private static GiftShopcartItemSingleton instance = new GiftShopcartItemSingleton();

    public static GiftShopcartItemSingleton getInstance() {
        return instance;
    }

    public List<ShopcartItem> getListPolicyDishshopVo() {
        return listPolicyDishshopVos;
    }

    public void setListPolicyDishshopVo(List<ShopcartItem> listPolicyDishshopVo) {
        this.listPolicyDishshopVos.clear();
        if (listPolicyDishshopVo != null) {
            this.listPolicyDishshopVos.addAll(listPolicyDishshopVo);
        }
    }

        protected List<ShopcartItem> listPolicyDishshopVos = new ArrayList<ShopcartItem>();

}
