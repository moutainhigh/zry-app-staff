package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.data.db.common.OrderNotify;
import com.zhongmei.bty.entity.enums.NotifyOrderType;
import com.zhongmei.bty.commonmodule.database.enums.Status;
import com.zhongmei.bty.data.operates.CallDishNotifyDal;

public class CallDishNotifyDalImpl extends AbstractOpeartesImpl implements CallDishNotifyDal {

    public CallDishNotifyDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<OrderNotify> queryOrderNotify(NotifyOrderType notifyOrderType) throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<OrderNotify, Long> orderNotifyDao = helper.getDao(OrderNotify.class);
            QueryBuilder<OrderNotify, Long> queryBuilder = orderNotifyDao.queryBuilder();
            queryBuilder.where().eq(OrderNotify.$.orderType, notifyOrderType)
                    .and().eq(OrderNotify.$.status, Status.VALID);
            return queryBuilder.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}
