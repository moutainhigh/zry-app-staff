package com.zhongmei.bty.dinner.table.event;

import com.zhongmei.bty.dinner.vo.DinnerConnectTablesVo;

import java.util.List;



public class EventBatchOperationTables {

    private List<DinnerConnectTablesVo> tablesVoList;

    private DinnerConnectTablesVo tablesVo;

    public EventBatchOperationTables(List<DinnerConnectTablesVo> tablesVoList) {
        this.tablesVoList = tablesVoList;
    }

    public EventBatchOperationTables(DinnerConnectTablesVo tablesVo) {
        this.tablesVo = tablesVo;
    }

    public List<DinnerConnectTablesVo> getTablesVoList() {
        return tablesVoList;
    }

    public DinnerConnectTablesVo getTablesVo() {
        return tablesVo;
    }
}
