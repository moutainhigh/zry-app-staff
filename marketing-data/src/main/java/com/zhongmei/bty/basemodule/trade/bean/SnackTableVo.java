package com.zhongmei.bty.basemodule.trade.bean;

import java.util.List;

import com.zhongmei.yunfu.db.entity.CommercialArea;

public class SnackTableVo {

    private CommercialArea commercialArea;

    private List<TableTradeVo> tableTradeVo;

    public CommercialArea getCommercialArea() {
        return commercialArea;
    }

    public void setCommercialArea(CommercialArea commercialArea) {
        this.commercialArea = commercialArea;
    }

    public List<TableTradeVo> getTableTradeVo() {
        return tableTradeVo;
    }

    public void setTableTradeVo(List<TableTradeVo> tableTradeVo) {
        this.tableTradeVo = tableTradeVo;
    }


}
