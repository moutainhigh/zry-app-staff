package com.zhongmei.bty.basemodule.trade.bean;

import com.zhongmei.yunfu.db.entity.trade.Tables;

/**
 * @version: 1.0
 * @date 2015年7月27日
 */
public class TablesVo {

    private final Tables table;
    //之前已经被预订 , 被预定为true
    private final boolean enable;

    // 0:未被选 1：被选中
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
