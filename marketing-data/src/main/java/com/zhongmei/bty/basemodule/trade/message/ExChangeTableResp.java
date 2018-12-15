package com.zhongmei.bty.basemodule.trade.message;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.yunfu.db.entity.trade.TradeTable;

import java.util.List;

/**
 * @Date： 2016/11/8
 * @Description:快餐换桌返回对象
 * @Version: 1.0
 */
public class ExChangeTableResp {

    private List<Tables> tables;
    private List<TradeTable> tradeTables;

    public List<Tables> getTables() {
        return tables;
    }

    public void setTables(List<Tables> tables) {
        this.tables = tables;
    }

    public List<TradeTable> getTradeTables() {
        return tradeTables;
    }

    public void setTradeTables(List<TradeTable> tradeTables) {
        this.tradeTables = tradeTables;
    }
}
