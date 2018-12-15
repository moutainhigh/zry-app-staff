package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.zhongmei.yunfu.context.session.Session;
import com.zhongmei.yunfu.context.session.core.user.AuthUser;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.OperatesFactory;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.AsyncHttpRecord;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpState;
import com.zhongmei.bty.commonmodule.database.enums.AsyncHttpType;
import com.zhongmei.bty.basemodule.async.operates.AsyncDal;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by demo on 2018/12/15
 */
public class AsyncDalImpl extends AbstractOpeartesImpl implements AsyncDal {

    public AsyncDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public boolean update(String recUuid, Long tradeId, String json, String serialNumber) throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
        try {
            final Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            //查询并赋值
            final AsyncHttpRecord rec = asyncDal.query(recUuid);
            if (rec == null) {
                return false;
            }
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                rec.setUpdatorId(user.getId());
                rec.setUpdatorName(user.getName());
            }
            rec.setTradeId(tradeId);
            rec.setReqStr(json);
            rec.setSerialNumber(serialNumber);
            rec.validateUpdate();
            //更新后的数据插入数据库
            // modify 20170216 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    boolean change = dao.update(rec) > 0;
                    if (change) {
                        helper.getChangeSupportable().addChange(AsyncHttpRecord.class);
                    }
                    return null;
                }
            });
            // modify 20170216 end 添加事务来实现线程同步
            return true;
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public AsyncHttpRecord update(final AsyncHttpRecord rec, AsyncHttpState state, String reason) throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        AsyncDal asyncDal = OperatesFactory.create(AsyncDal.class);
        try {
            final Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            //查询并赋值
//            final AsyncHttpRecord rec = asyncDal.query(recUuid);
            if (rec == null) {
                return null;
            }
            AuthUser user = Session.getAuthUser();
            if (user != null) {
                rec.setUpdatorId(user.getId());
                rec.setUpdatorName(user.getName());
            }
            rec.setStatus(state);
            rec.setReason(reason);
            rec.validateUpdate();
            //更新后的数据插入数据库
            // modify 20170216 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    boolean change = dao.update(rec) > 0;
                    if (change) {
                        helper.getChangeSupportable().addChange(AsyncHttpRecord.class);
                    }
                    return null;
                }
            });
            // modify 20170216 end 添加事务来实现线程同步
            return rec;
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public AsyncHttpRecord retryCountPlus(final AsyncHttpRecord rec) throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
//        AsyncDal asyncDal= OperatesFactory.create(AsyncDal.class);
        try {
            final Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            //查询并赋值
//            final AsyncHttpRecord rec = asyncDal.query(recUuid);
            if (rec == null) {
                return null;
            }
            Integer retryCount = rec.getRetryCount() == null ? 0 : rec.getRetryCount();
            rec.setRetryCount(++retryCount);
            rec.validateUpdate();
            //更新后的数据插入数据库
            // modify 20170216 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    dao.update(rec);
                    return null;
                }
            });
            // modify 20170216 end 添加事务来实现线程同步

            return rec;
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    public void deleteRecordByStatus(AsyncHttpState state) throws SQLException {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            final Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            DeleteBuilder<AsyncHttpRecord, Long> deleteBuilder = dao.deleteBuilder();
            deleteBuilder.where().eq(AsyncHttpRecord.$.status, state.value());
            int deleteCount = deleteBuilder.delete();
//            final List<AsyncHttpRecord> recList = dao.queryBuilder().where().eq(AsyncHttpRecord.$.status, state).query();
//            // modify 20170216 start 添加事务来实现线程同步
//            boolean change = helper.callInTransaction(new Callable<Boolean>() {
//                @Override
//                public Boolean call()
//                        throws Exception {
//                    return dao.delete(recList) > 0;
//                }
//            });
            // modify 20170216 end 添加事务来实现线程同步
            if (deleteCount > 0) {
                helper.getChangeSupportable().addChange(AsyncHttpRecord.class);
            }
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    public List<AsyncHttpRecord> queryAllRecord() throws SQLException {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            return dao.queryBuilder().orderBy(AsyncHttpRecord.$.clientUpdateTime, false).query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    /**
     * 查询规则:查询请求失败的,或者重试次数大于0的。
     * 正在执行,首次执行成功的条目都不显示
     *
     * @return
     * @throws SQLException
     */
    @Override
    public List<AsyncHttpRecord> queryAllBesideExcuting() throws Exception {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            return dao.queryBuilder().orderBy(AsyncHttpRecord.$.clientUpdateTime, false).where().eq(AsyncHttpRecord.$.status, AsyncHttpState.FAILED.value()).or().gt(AsyncHttpRecord.$.retryCount, 0).query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public List<AsyncHttpRecord> queryNotSuccess(long tradeId) throws SQLException {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            return dao.queryBuilder()
                    .orderBy(AsyncHttpRecord.$.clientUpdateTime, false)
                    .where()
                    .eq(AsyncHttpRecord.$.tradeId, tradeId)
                    .and()
                    .ne(AsyncHttpRecord.$.status, AsyncHttpState.SUCCESS).query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public List<AsyncHttpRecord> query(String tradeUuid, Iterable<AsyncHttpType> types) throws SQLException {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            return dao.queryBuilder()
                    .where()
                    .eq(AsyncHttpRecord.$.tradeUuId, tradeUuid)
                    .and()
                    .in(AsyncHttpRecord.$.type, types).query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public AsyncHttpRecord query(String Uuid) throws Exception {
        DatabaseHelper helper = LocalDBManager.getHelper();
        Dao<AsyncHttpRecord, String> dao = helper.getDao(AsyncHttpRecord.class);
        try {
            return dao.queryForId(Uuid);
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public List<AsyncHttpRecord> queryFailRecord() throws SQLException {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            return dao.queryBuilder()
                    .orderBy(AsyncHttpRecord.$.clientUpdateTime, false)
                    .where()
                    .ne(AsyncHttpRecord.$.status, AsyncHttpState.SUCCESS).query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public List<AsyncHttpRecord> queryFailOpenTable() throws SQLException {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            return dao.queryBuilder()
                    .orderBy(AsyncHttpRecord.$.clientUpdateTime, false)
                    .where()
                    .ne(AsyncHttpRecord.$.status, AsyncHttpState.SUCCESS).and().eq(AsyncHttpRecord.$.type, AsyncHttpType.OPENTABLE).query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void deleteByUUID(String recUUID) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            DeleteBuilder<AsyncHttpRecord, Long> tipDeleteBuilder = dao.deleteBuilder();
            tipDeleteBuilder.where().eq(AsyncHttpRecord.$.uuid, recUUID);
            tipDeleteBuilder.delete();
            helper.getChangeSupportable().addChange(AsyncHttpRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void deleteByTradeUUID(String tradeUUID) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<AsyncHttpRecord, Long> dao = helper.getDao(AsyncHttpRecord.class);
            DeleteBuilder<AsyncHttpRecord, Long> tipDeleteBuilder = dao.deleteBuilder();
            tipDeleteBuilder.where().eq(AsyncHttpRecord.$.tradeUuId, tradeUUID);
            tipDeleteBuilder.delete();
            helper.getChangeSupportable().addChange(AsyncHttpRecord.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

}
