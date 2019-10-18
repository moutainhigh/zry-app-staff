package com.zhongmei.beauty.interfaces;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;

import java.util.List;



public interface ITableChoice {
    public void choiceTables(List<Tables> tables);
}
