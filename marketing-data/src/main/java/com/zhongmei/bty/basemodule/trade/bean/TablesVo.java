package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.trade.Tables;


public class TablesVo {

    private final Tables table;
        private final boolean enable;

        private boolean selected = false;

    public TablesVo(Tables table) {
        this(table, true);
    }

    public TablesVo(Tables table, boolean enable) {
        this.table = table;
        this.enable = enable;
    }

    public String getTableName() {
        return table.getTableName();
    }

    public Tables getTable() {
        return table;
    }

    public boolean isEnable() {
        return enable;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
