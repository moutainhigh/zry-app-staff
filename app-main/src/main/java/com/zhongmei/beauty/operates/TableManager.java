package com.zhongmei.beauty.operates;

import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



public class TableManager extends TableManagerBase {

    @Override
    public TableTradeCacheBase getTableTradeCache() {
        return null;
    }

    public List<ZoneModel> getAllZoneModel() {
        Collection zones = zoneModelFinder.values();

        if (zones != null) {
            return new ArrayList<>(zones);
        }

        return null;
    }


    public List<DinnertableModel> getAllTables() {
        Collection tables = dinnertableFinder.values();

        if (tables != null) {
            return new ArrayList<>(tables);
        }

        return null;
    }


    public List<DinnertableModel> getTablesByZones(Long zoneId) {
        ZoneModel zoneModel = zoneModelFinder.get(zoneId);

        if (zoneModel != null) {
            return zoneModel.getDinnertableModels();
        }

        return null;
    }

}
