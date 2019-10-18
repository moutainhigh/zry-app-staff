package com.zhongmei.beauty.interfaces;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.List;



public interface ITableOperator {

    void refreshZones(List<ZoneModel> zoneModes);

    void refreshTableTrades(List<ZoneModel> zoneModes);

    void refreshTables(List<DinnertableModel> tableModes);
}
