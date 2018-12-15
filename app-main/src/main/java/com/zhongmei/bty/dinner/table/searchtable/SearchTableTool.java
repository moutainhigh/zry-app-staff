package com.zhongmei.bty.dinner.table.searchtable;

import com.zhongmei.yunfu.db.entity.CommercialArea;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.bty.basemodule.trade.operates.TablesDal;
import com.zhongmei.bty.basemodule.trade.operates.TradeDal;
import com.zhongmei.yunfu.db.enums.BusinessType;
import com.zhongmei.yunfu.db.enums.TableStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by demo on 2018/12/15
 */

public class SearchTableTool {
    private TradeDal mTradeDal = OperatesFactory.create(TradeDal.class);
    private TablesDal mTablesDal = OperatesFactory.create(TablesDal.class);
    private BusinessType mBusinessType;

    public SearchTableTool(BusinessType businessType) {
        mBusinessType = businessType;
    }

    public List<TableAreaVo> findDishTables(List<String> dishUuids) {
        if (mTradeDal == null)
            mTradeDal = OperatesFactory.create(TradeDal.class);
        if (mTablesDal == null)
            mTablesDal = OperatesFactory.create(TablesDal.class);
        List<TableAreaVo> voList = null;
        try {
            List<Long> tableIdList = mTradeDal.searchTableWithDish(dishUuids, this.mBusinessType);

            if (tableIdList != null && !tableIdList.isEmpty()) {
                List<Tables> tablesList = mTablesDal.findTablesByIds(tableIdList, TableStatus.OCCUPIED);

                if (tablesList != null && !tablesList.isEmpty()) {
                    List<CommercialArea> areaList = mTablesDal.listDinnerArea();

                    if (areaList != null && !areaList.isEmpty()) {
                        voList = new ArrayList<TableAreaVo>();

                        Map<Long, CommercialArea> areaMap = new HashMap<Long, CommercialArea>();
                        //将区域数据放入map 用于查取
                        for (int i = 0; i < areaList.size(); i++) {
                            areaMap.put(areaList.get(i).getId(), areaList.get(i));
                        }
                        //遍历桌台查找区域数据

                        for (int i = 0; i < tablesList.size(); i++) {
                            TableAreaVo tableAreaVo = new TableAreaVo();
                            tableAreaVo.table = tablesList.get(i);
                            tableAreaVo.CommercialArea = areaMap.get(tablesList.get(i).getAreaId());
                            voList.add(tableAreaVo);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return voList;
    }

    public static class TableAreaVo {
        public CommercialArea CommercialArea;
        public Tables table;
    }
}
