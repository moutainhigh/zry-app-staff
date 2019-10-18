package com.zhongmei.bty.basemodule.reportcenter.message;

import com.zhongmei.bty.basemodule.reportcenter.message.GoodsSellRankPrintResp;

import java.util.List;


public class GoodsSellRankResp {
    List<GoodsSellRankPrintResp.SaleItem> dishSort;

    public List<GoodsSellRankPrintResp.SaleItem> getDishSort() {
        return dishSort;
    }

    public void setDishSort(List<GoodsSellRankPrintResp.SaleItem> dishSort) {
        this.dishSort = dishSort;
    }
}
