package com.zhongmei.bty.basemodule.shoppingcart.utils;

import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.trade.bean.TradeVo;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;
import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.yunfu.context.util.SystemUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Date：2015年9月14日 下午7:47:32
 * @Description: 用于操作正餐订单数据信息
 * @Version: 1.0
 */
public class CreateTradeTableTool {

    /**
     * @Title: openTable
     * @Description: 添加开台信息
     * @Param @param mTradeVo
     * @Param @param mTradeTable TODO
     * @Return void 返回类型
     */
    public static void openTable(TradeVo mTradeVo, TradeTable mTradeTable) {
        List<TradeTable> mTradetables = mTradeVo.getTradeTableList();
        if (mTradetables == null) {
            mTradetables = new ArrayList<TradeTable>();
            mTradeVo.setTradeTableList(mTradetables);
        }
        Boolean isHave = false;
        for (int i = 0; i < mTradetables.size(); i++) {
            //判断是新加桌台还是修改桌台数据信息
            TradeTable table = mTradetables.get(i);
            if (mTradeTable.getUuid().equals(table.getUuid())) {
                mTradetables.add(i, table);
                isHave = true;
                break;
            }
        }
        if (!isHave) {
            mTradetables.add(mTradeTable);
        }
    }

    /**
     * 改变桌台选择
     *
     * @param tradeTableList
     * @param tables
     * @param tradeUuid
     * @return
     */
    public static List<TradeTable> changeTables(List<TradeTable> tradeTableList, List<Tables> tables, String tradeUuid) {
        if (tradeTableList == null) {
            tradeTableList = new ArrayList<>();
        }
        Map<Long, Tables> tablesMap = new HashMap<>();
        for (Tables table : tables) {
            tablesMap.put(table.getId(), table);
        }
        Iterator iterator = tradeTableList.iterator();
        while (iterator.hasNext()) {
            TradeTable tradeTable = (TradeTable) iterator.next();
            Tables table = tablesMap.get(tradeTable.getTableId());
            if (table != null) {
                if (tradeTable.getStatusFlag() == StatusFlag.INVALID) {
                    tradeTable.setStatusFlag(StatusFlag.VALID);
                    tradeTable.validateUpdate();
                }
                tables.remove(table);
            } else if (tradeTable.getId() == null) {
                iterator.remove();
            } else {
                tradeTable.setStatusFlag(StatusFlag.INVALID);
                tradeTable.validateUpdate();
            }
        }
        for (Tables table : tables) {
            TradeTable newTable = createTradeTable(tradeUuid, table.getId(), table.getTableName(), 1);
            tradeTableList.add(newTable);
        }
        return tradeTableList;
    }


    public static TradeTable createTradeTable(String tradeUuid, Long tableId, String tableName, int people) {
        TradeTable tradeTable = new TradeTable();
        tradeTable.validateCreate();
        tradeTable.setTableId(tableId);
        tradeTable.setTableName(tableName);
        tradeTable.setUuid(SystemUtils.genOnlyIdentifier());
        tradeTable.setTablePeopleCount(people);
        tradeTable.setTradeUuid(tradeUuid);

        //设置登录操作员为默认服务员
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            tradeTable.setWaiterId(user.getId());
            tradeTable.setWaiterName(user.getName());
        }

        return tradeTable;
    }

}
