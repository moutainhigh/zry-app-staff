package com.zhongmei.bty.basemodule.discount.salespromotion.cache.holder;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePolicyDish;
import com.zhongmei.bty.basemodule.salespromotion.bean.LoytMrulePolicyDish.$;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.List;


public class RulePolicyDishHolder extends BasicHolder<LoytMrulePolicyDish> {

    public RulePolicyDishHolder() {
        super(LoytMrulePolicyDish.class);
    }

    @Override
    protected List<LoytMrulePolicyDish> query(DatabaseHelper helper, Dao<LoytMrulePolicyDish, Long> dao) throws Exception {
        QueryBuilder<LoytMrulePolicyDish, Long> qb = dao.queryBuilder();
        qb.where().eq($.validFlag, StatusFlag.VALID);
        return qb.orderBy(LoytMrulePolicyDish.$.serverUpdateTime, false).query();
    }

}
