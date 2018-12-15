package com.zhongmei.bty.dinner.vo;

import com.zhongmei.bty.basemodule.database.db.TableSeat;
import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class EmptyTableVo {

    private Tables emptyTables;

    private CommercialArea tableArea;

    private List<TableSeat> tableSeats;

    public Tables getEmptyTables() {
        return emptyTables;
    }

    public void setEmptyTables(Tables emptyTables) {
        this.emptyTables = emptyTables;
    }

    public CommercialArea getTableArea() {
        return tableArea;
    }

    public void setTableArea(CommercialArea tableArea) {
        this.tableArea = tableArea;
    }

    public List<TableSeat> getTableSeats() {
        return tableSeats;
    }

    public void setTableSeats(List<TableSeat> tableSeats) {
        this.tableSeats = tableSeats;
    }
}
