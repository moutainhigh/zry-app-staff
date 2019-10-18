package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMruleMarketRule;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.List;


public class RuleMarketRuleHolder extends BasicHolder<LoytMruleMarketRule> {

    public RuleMarketRuleHolder() {
        super(LoytMruleMarketRule.class);
    }

    @Override
    protected List<LoytMruleMarketRule> query(DatabaseHelper helper, Dao<LoytMruleMarketRule, Long> dao) throws Exception {
        QueryBuilder<LoytMruleMarketRule, Long> qb = dao.queryBuilder();
        qb.where().eq(LoytMruleMarketRule.$.validFlag, StatusFlag.VALID);
        return qb.orderBy(LoytMruleMarketRule.$.serverUpdateTime, false).query();
    }
}
