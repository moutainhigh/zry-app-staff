
package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.ContractOverdue;
import com.zhongmei.bty.basemodule.hetong.HeTongExpireNotifyDal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class HeTongExpireNotifyDalImpl extends AbstractOpeartesImpl implements HeTongExpireNotifyDal {

    private static final String TAG = HeTongExpireNotifyDalImpl.class.getSimpleName();

    public HeTongExpireNotifyDalImpl(ImplContext context) {
        super(context);
    }


    @Override
    public void insertMsgs(final ContractOverdue contractOverdue) throws SQLException {
        if (contractOverdue == null)
            return;

        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            /*Dao<ContractOverdue, String> tableDao = helper.getDao(ContractOverdue.class);*/
            contractOverdue.setTime(System.currentTimeMillis());

            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, ContractOverdue.class, contractOverdue);
                    return null;
                }
            });
            /*	tableDao.create(contractOverdue);*/
            // modify 20170217 end 添加事务来实现线程同步
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public List<ContractOverdue> getUnClearMsg() throws SQLException {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<ContractOverdue, String> tableDao = helper.getDao(ContractOverdue.class);

            QueryBuilder<ContractOverdue, String> qb = tableDao.queryBuilder();

//			qb.where().eq(ContractOverdue.$.isLocalLogClear, Bool.NO);
            List<ContractOverdue> list = qb.query();

            return list == null ? new ArrayList<ContractOverdue>() : list;
        } finally {
            LocalDBManager.releaseHelper();
        }

    }


    /**
     * 删除所有数据;
     *
     * @throws SQLException
     */
    public void clearAll() throws SQLException {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            //删除全部数据
            Dao<ContractOverdue, String> tableDao = helper.getDao(ContractOverdue.class);
            final DeleteBuilder<ContractOverdue, String> delBuidler = tableDao.deleteBuilder();
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    delBuidler.delete();
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同步
        } finally {
            LocalDBManager.releaseHelper();
        }
    }


    /**
     * 创建新的或者更新;
     *
     * @param contractOverdue
     * @throws SQLException
     */
    public void update(ContractOverdue contractOverdue) throws SQLException {
        if (contractOverdue == null)
            return;

        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            Dao<ContractOverdue, String> tableDao = helper.getDao(ContractOverdue.class);
            final UpdateBuilder<ContractOverdue, String> updateBuilder = tableDao.updateBuilder();
            updateBuilder.where().eq(ContractOverdue.$.serviceId, contractOverdue.getServiceId());
            updateBuilder.updateColumnValue(ContractOverdue.$.serviceName, contractOverdue.getServiceName());
            updateBuilder.updateColumnValue(ContractOverdue.$.salesName, contractOverdue.getSalesName());
            updateBuilder.updateColumnValue(ContractOverdue.$.salesPhone, contractOverdue.getSalesPhone());
            updateBuilder.updateColumnValue(ContractOverdue.$.expireDate, contractOverdue.getExpireDate());
            updateBuilder.updateColumnValue(ContractOverdue.$.remainDays, contractOverdue.getRemainDays());
            updateBuilder.updateColumnValue(ContractOverdue.$.time, contractOverdue.getTime());
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    updateBuilder.update();
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同步
        } finally {
            LocalDBManager.releaseHelper();
        }
    }


    /**
     * 删除数据
     *
     * @param contractOverdue
     * @throws SQLException
     */
    @Override
    public void deleteData(ContractOverdue contractOverdue) throws SQLException {

        try {
            // modify 20170217 start 添加事务来实现线程同步
            DBHelperManager.deleteById(ContractOverdue.class, contractOverdue.getId());
            // modify 20170217 end 添加事务来实现线程同步
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    /**
     * 过滤掉已有数据;
     *
     * @param list
     * @return
     * @throws SQLException
     */
    @Override
    public void filterData(List<ContractOverdue> list) throws SQLException {

        if (list == null || list.size() <= 0)
            return;

        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<ContractOverdue, String> tableDao = helper.getDao(ContractOverdue.class);
            QueryBuilder<ContractOverdue, String> qb = tableDao.queryBuilder();

            for (ContractOverdue item : list) {

                qb.where().eq(ContractOverdue.$.serviceId, item.getServiceId())
                        .and()
                        .eq(ContractOverdue.$.shopId, item.getShopId());
                List<ContractOverdue> contractOverdues = qb.query();

                if (contractOverdues.size() <= 0) {//没有这种消息类型加入新队列,并保存到数据库;
                    item.setTime(System.currentTimeMillis());
                    insertMsgs(item);
                } else {

                    for (ContractOverdue contractOverdue : contractOverdues) {
                        Log.e("HeTongExpireImpl", "filterData()::contractOverdue days = " + contractOverdue.getRemainDays());
                        Log.e("HeTongExpireImpl", "filterData()::item days = " + item.getRemainDays());

                        item.setTime(System.currentTimeMillis());
                        update(item);
                    }
                }
            }
        } finally {
            LocalDBManager.releaseHelper();
        }
    }
}

