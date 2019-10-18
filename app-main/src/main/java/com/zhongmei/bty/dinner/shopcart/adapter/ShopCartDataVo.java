package com.zhongmei.bty.dinner.shopcart.adapter;

import com.zhongmei.bty.basemodule.orderdish.bean.IShopcartItem;

import java.util.ArrayList;
import java.util.List;


public class ShopCartDataVo {
    private String printNo;
    private List<IShopcartItem> baseData;
    public ShopCartDataVo(String printno) {
        this.printNo = printno;
        baseData = new ArrayList<IShopcartItem>();
    }

    public void addData(IShopcartItem dataItem) {
        baseData.add(dataItem);
    }

    public String getPrintNo() {
        return printNo;
    }

    public List<IShopcartItem> getData() {
        return baseData;
    }

    public boolean isEmpty() {
        if (baseData == null) {
            return true;
        } else {
            return baseData.isEmpty();
        }
    }
}