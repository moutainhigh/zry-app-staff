package com.zhongmei.beauty.interfaces;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface ITableOperator {
    /**
     * 刷新桌台区域信息
     *
     * @param zoneModes
     */
    void refreshZones(List<ZoneModel> zoneModes);

    void refreshTableTrades(List<ZoneModel> zoneModes);

    void refreshTables(List<DinnertableModel> tableModes);
}
