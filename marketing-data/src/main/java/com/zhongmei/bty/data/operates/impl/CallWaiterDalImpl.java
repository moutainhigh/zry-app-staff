package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.http.OpsRequest;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.basemodule.shopmanager.utils.DateTimeUtil;
import com.zhongmei.bty.basemodule.commonbusiness.utils.ServerAddressUtil;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.notifycenter.entity.CallWaiter;
import com.zhongmei.bty.basemodule.notifycenter.CallWaiterDal;
import com.zhongmei.yunfu.resp.ResponseListener;
import com.zhongmei.bty.basemodule.notifycenter.enums.BizStatus;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */
public class CallWaiterDalImpl extends AbstractOpeartesImpl implements CallWaiterDal {

    public CallWaiterDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<CallWaiter> getCallWaiterList(BizStatus status) throws SQLException, ParseException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        Date date = DateTimeUtil.getOpenTime();
        try {
            Dao<CallWaiter, Long> dao = helper.getDao(CallWaiter.class);
            return dao.queryBuilder().where().eq(CallWaiter.$.bizStatus, status).and().eq(CallWaiter.$.statusFlag, StatusFlag.VALID)
                    .and().ge(CallWaiter.$.lastCallTime, date.getTime()).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long getCallWaiterCount(BizStatus status) throws SQLException, ParseException {
        DatabaseHelper helper = DBHelperManager.getHelper();
        Date date = DateTimeUtil.getOpenTime();
        try {
            Dao<CallWaiter, Long> dao = helper.getDao(CallWaiter.class);
            List<CallWaiter> callWaiterList = dao.queryBuilder().selectColumns(CallWaiter.$.count)
                    .where().ge(CallWaiter.$.lastCallTime, date.getTime()).query();
            long count = 0L;
            for (CallWaiter callWaiter : callWaiterList) {
                if (callWaiter.getCount() != null) {
                    count += callWaiter.getCount();
                }
            }

            return count;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void updateCallWaiter(CallWaiter callWaiter, ResponseListener<CallWaiter> listener) {
        String url = ServerAddressUtil.getInstance().updateCallWaiter();
        //设置确认服务铃的服务员信息
        AuthUser user = Session.getAuthUser();
        if (user != null) {
            callWaiter.setWaiterId(user.getId());
            callWaiter.setWaiterName(user.getName());
        }
        OpsRequest.Executor<CallWaiter, CallWaiter> executor = OpsRequest.Executor.create(url);
        executor.requestValue(callWaiter)
                .responseClass(CallWaiter.class)
                .responseProcessor(new CallWaiterProcessor())
                .execute(listener, "updateCallWaiter");
    }

    /**
     * 将CallWaiter保存到数据库的处理器
     * <p>
     */
    private static class CallWaiterProcessor extends OpsRequest.SaveDatabaseResponseProcessor<CallWaiter> {
        @Override
        protected Callable<Void> getCallable(final DatabaseHelper helper, final CallWaiter callWaiter) {
            return new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    DBHelperManager.saveEntities(helper, CallWaiter.class, Arrays.asList(callWaiter));
                    return null;
                }
            };
        }
    }

}
