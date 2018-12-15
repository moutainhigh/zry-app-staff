package com.zhongmei.beauty.interfaces;

import com.zhongmei.yunfu.db.entity.trade.Tables;
import com.zhongmei.bty.dinner.table.model.DinnertableModel;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public interface ITableChoice {
    public void choiceTables(List<Tables> tables);
}
