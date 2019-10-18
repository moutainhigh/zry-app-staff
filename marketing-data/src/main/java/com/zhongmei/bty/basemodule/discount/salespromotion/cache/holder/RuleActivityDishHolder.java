package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMruleActivityDish;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.List;


public class RuleActivityDishHolder extends BasicHolder<LoytMruleActivityDish> {

    public RuleActivityDishHolder() {
        super(LoytMruleActivityDish.class);
    }

    @Override
    protected List<LoytMruleActivityDish> query(DatabaseHelper helper, Dao<LoytMruleActivityDish, Long> dao) throws Exception {
        QueryBuilder<LoytMruleActivityDish, Long> qb = dao.queryBuilder();
        qb.where().eq(LoytMruleActivityDish.$.validFlag, StatusFlag.VALID);
        return qb.orderBy(LoytMruleActivityDish.$.serverUpdateTime, false).query();
    }

}
