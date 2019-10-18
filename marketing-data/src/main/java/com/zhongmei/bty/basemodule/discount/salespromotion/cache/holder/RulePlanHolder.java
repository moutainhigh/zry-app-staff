package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePlan;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.util.List;


public class RulePlanHolder extends BasicHolder<LoytMrulePlan> {

    public RulePlanHolder() {
        super(LoytMrulePlan.class);
    }

    @Override
    protected List<LoytMrulePlan> query(DatabaseHelper helper, Dao<LoytMrulePlan, Long> dao) throws Exception {
        QueryBuilder<LoytMrulePlan, Long> qb = dao.queryBuilder();
        qb.where().eq(LoytMrulePlan.$.planState, 2);
        return qb.orderBy(LoytMrulePlan.$.serverUpdateTime, false).query();
    }

}
