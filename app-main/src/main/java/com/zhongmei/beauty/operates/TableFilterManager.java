package com.zhongmei.beauty.operates;

import com.zhongmei.bty.basemodule.trade.bean.DinnertableState;
import com.zhongmei.bty.basemodule.trade.enums.DinnertableStatus;
import com.zhongmei.beauty.interfaces.ITableOperator;
import com.zhongmei.yunfu.db.enums.TableStatus;
import com.zhongmei.bty.dinner.table.manager.DinnerTableBatchOperationManager;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;
import com.zhongmei.bty.dinner.table.model.ZoneModel;

import java.util.ArrayList;
import java.util.List;



public class TableFilterManager {
    private static TableFilterManager mTableFilter;

    private TableManager mTableManager;

    public TableFilterManager() {
        mTableManager = new TableManager();
    }


    public void setTableListener(ITableOperator listener) {
        if (mTableManager != null) {
            mTableManager.setmTableOperatorListener(listener);
        }
    }

    public void removeTableListener(ITableOperator listener) {
        if (mTableManager != null) {
            mTableManager.removeTableOperatorListener(listener);
        }
    }


    public void loadTables() {
        mTableManager.loadTableInfos();    }



    public List<DinnertableModel> filter(Long zoneId, TableStatus tableStatus) {
        List<DinnertableModel> listTableMode = null;
        List<DinnertableModel> listTargetTableMode = new ArrayList<>();
        if (zoneId != null && zoneId > 0) {
            listTableMode = mTableManager.getTablesByZones(zoneId);
        } else {
            listTableMode = mTableManager.getAllTables();
        }

        if (tableStatus != null && listTableMode != null) {
            int index = listTableMode.size() - 1;

            for (int i = index; i >= 0; i--) {
                DinnertableModel tableMode = listTableMode.get(i);

                if (tableMode.getPhysicsTableStatus().equalsValue(tableStatus.value())) {
                    listTargetTableMode.add(tableMode);
                }
            }
        } else {
            listTargetTableMode.addAll(listTableMode);
        }

        return listTargetTableMode;
    }

    public List<ZoneModel> getZoneModels() {
        return mTableManager.getAllZoneModel();
    }

    public void onDestory() {
        if (mTableManager != null) {
            mTableManager.onDestory();
        }
    }

}
