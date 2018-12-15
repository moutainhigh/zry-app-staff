package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.CommercialArea;

/**
 * 桌台区域
 */
public class TableAreaPopupVo {
    private boolean isSelect;

    private CommercialArea tableArea;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public CommercialArea getTableArea() {
        return tableArea;
    }

    public void setTableArea(CommercialArea tableArea) {
        this.tableArea = tableArea;
    }

}
