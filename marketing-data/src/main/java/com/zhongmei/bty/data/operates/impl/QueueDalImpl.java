package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.database.queue.Queue;
import com.zhongmei.bty.basemodule.database.queue.QueueExtra;
import com.zhongmei.bty.basemodule.database.queue.QueueOrderSource;
import com.zhongmei.bty.basemodule.database.queue.QueueStatus;
import com.zhongmei.bty.basemodule.database.queue.SuccessOrFaild;
import com.zhongmei.bty.basemodule.print.entity.PrintContent;
import com.zhongmei.bty.basemodule.queue.CommercialQueueLine;
import com.zhongmei.bty.basemodule.queue.QueueDal;
import com.zhongmei.yunfu.context.util.DateTimeUtils;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.entity.QrCodeInfo;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.enums.YesOrNo;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * 排队单数据
 */
public class QueueDalImpl extends AbstractOpeartesImpl implements QueueDal {
    private static final String TAG = QueueDalImpl.class.getSimpleName();

    public QueueDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public List<Queue> findAllDataByDate(Date date, Long queueLineId, QueueStatus queueStatus) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            long startTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            qb.orderBy(Queue.$.createDateTime, true);
            Where<Queue, String> where = qb.where();
            where.between(Queue.$.createDateTime, startTime, endTime);
            where.and();
            where.eq(Queue.$.isZeroOped, YesOrNo.NO);
            if (queueLineId != null) {
                where.and();
                where.eq(Queue.$.queueLineId, queueLineId);
            }
            if (queueStatus != null) {
                where.and();
                where.eq(Queue.$.queueStatus, queueStatus.value());
            }
            List<Queue> queueList = qb.query();
            if (queueList.size() > 0) {
                List<Long> queueIds = new ArrayList<>();
                for (Queue queue : queueList) {
                    if (queue.getId() != null) {
                        queueIds.add(queue.getId());
                    }
                }
                // 排队扩展表
                List<QueueExtra> queueExtraList = helper.getDao(QueueExtra.class)
                        .queryBuilder()
                        .where()
                        .in(QueueExtra.$.queue_id, queueIds.toArray())
                        .and()
                        .eq(QueueExtra.$.status_flag, StatusFlag.VALID)
                        .query();
                for (QueueExtra queueExtra : queueExtraList) {
                    for (Queue queue : queueList) {
                        if (queue.getId() != null) {
                            if (queue.getId().equals(queueExtra.queueID)) {
                                queue.queueExtra = queueExtra;
                            }
                        }
                    }
                }
            }

            return queueList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CommercialQueueLine findQueueLineIdByPersonCount(int personCount) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialQueueLine, Long> queueLineDao = helper.getDao(CommercialQueueLine.class);
            QueryBuilder<CommercialQueueLine, Long> qb = queueLineDao.queryBuilder();

            qb.where()
                    .ge(CommercialQueueLine.$.maxPersonCount, personCount)
                    .and()
                    .le(CommercialQueueLine.$.minPersonCount, personCount);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public int getQueueNumber(long queueLineId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        int sno = 1;
        try {
            Date date = new Date();
            long startTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            qb.where().between(Queue.$.createDateTime, startTime, endTime)
                    // .and()
                    // .eq(Queue.$.queueSource,
                    // QueueOrderSource.DaoDian)
                    .and()
                    .eq(Queue.$.isZeroOped, YesOrNo.NO)
                    .and()
                    .eq(Queue.$.queueLineId, queueLineId);
            qb.orderBy(Queue.$.queueNumber, false);
            Queue queue = qb.queryForFirst();
            if (queue != null) {
                sno = queue.getQueueNumber() + 1;
            }

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return sno;
    }

    @Override
    public void saveQueue(final Queue queue) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Queue.class, queue);
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同步
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }

    @Override
    public void updateQueue(final Queue queue) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, Queue.class, queue);
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public CommercialQueueLine findQueueLineByid(Long queueLineId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialQueueLine, Long> queueLineDao = helper.getDao(CommercialQueueLine.class);
            return queueLineDao.queryForId(queueLineId);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long getCountByQueueUuid(Queue queue) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        long sno = 0L;
        try {
            Date date = new Date(queue.getCreateDateTime());
            long startTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            qb.where()
                    .between(Queue.$.createDateTime, startTime, endTime)
                    .and()
                    .eq(Queue.$.queueLineId, queue.getQueueLineId())
                    .and()
                    .eq(Queue.$.queueStatus, QueueStatus.QUEUEING)
                    .and()
                    .eq(Queue.$.isZeroOped, YesOrNo.NO)
                    .and()
                    .lt(Queue.$.queueNumber, queue.getQueueNumber());
            sno = qb.countOf();

        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return sno;
    }

    @Override
    public List<CommercialQueueLine> queryQueueLineList() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialQueueLine, Long> queueLineDao = helper.getDao(CommercialQueueLine.class);
            return queueLineDao.queryForAll();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Queue> queueListRemind(Long... queueLindIds) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            //Dao<Queue, Long> queueLineDao = helper.getDao(Queue.class);
            //StringBuilder query = new StringBuilder();
            //query.append("SELECT " + Queue.$.id + " FROM queue");
            //query.append(" WHERE " + Queue.$.remindTime + " = (SELECT max(" + Queue.$.remindTime + ") FROM queue GROUP BY " + Queue.$.queueLineId + ")");
            //query.append(" AND " + Queue.$.queueLineId + " in (" + queueLindIds + ")");

            //query.append("SELECT id FROM queue GROUP BY " + Queue.$.queueLineId + " HAVING max(" + Queue.$.remindTime + ") IS NOT NULL");
            //GenericRawResults<String[]> results = queueLineDao.queryRaw(query.toString());
            //return results.getFirstResult();

            Date date = new Date();
            long startTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            qb.where()
                    .between(Queue.$.createDateTime, startTime, endTime)
                    .and()
                    .eq(Queue.$.isZeroOped, YesOrNo.NO)
                    .and()
                    .eq(Queue.$.queueStatus, QueueStatus.QUEUEING)
                    .and()
                    .isNotNull(Queue.$.remindCount);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public void cleanQueueList(final List<Queue> queueList) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Callable<Object> callable = new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    List<Queue> mQueueList = new ArrayList<Queue>();
                    Date now = new Date();
                    for (Queue queue : queueList) {
                        queue.setIsZeroOped(YesOrNo.YES);
                        queue.setIsOk(SuccessOrFaild.FAILD);
//						queue.setModifyDateTime(now.getTime());
                        mQueueList.add(queue);
                    }
                    DBHelperManager.saveEntities(helper, Queue.class, mQueueList);
                    return null;
                }
            };
            helper.callInTransaction(callable);
        } catch (Exception e) {
            Log.e(TAG, "", e);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Queue> listUnProcess() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Date date = new Date();
            long startTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            qb.where()
                    .between(Queue.$.createDateTime, startTime, endTime)
                    .and()
                    .notIn(Queue.$.queueSource, QueueOrderSource.DaoDian, QueueOrderSource.DianHuaYuDing)
                    .and()
                    .eq(Queue.$.queueStatus, QueueStatus.QUEUEING)
                    .and()
                    .eq(Queue.$.isZeroOped, YesOrNo.NO);
            qb.orderBy(Queue.$.createDateTime, false);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public long getCountUnProcess() throws Exception {

        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Date date = new Date();
            long startTime = DateTimeUtils.getDayStart(date);
            long endTime = DateTimeUtils.getDayEnd(date);
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            qb.where()
                    .between(Queue.$.createDateTime, startTime, endTime)
                    .and()
                    .notIn(Queue.$.queueSource, QueueOrderSource.DaoDian, QueueOrderSource.DianHuaYuDing)
                    .and()
                    .eq(Queue.$.queueStatus, QueueStatus.QUEUEING)
                    .and()
                    .eq(Queue.$.isZeroOped, YesOrNo.NO);
            return qb.countOf();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

    }

    @Override
    public Queue getQueueByUuid(String uuid) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            return queueDao.queryForId(uuid);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public QrCodeInfo getQrCodeInfo() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<QrCodeInfo, Long> codeInfoDao = helper.getDao(QrCodeInfo.class);
            QueryBuilder<QrCodeInfo, Long> qb = codeInfoDao.queryBuilder();
            qb.where().eq(QrCodeInfo.$.type, 3);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<PrintContent> getPrintContent(String commercialId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<PrintContent, Long> printContentDao = helper.getDao(PrintContent.class);
            QueryBuilder<PrintContent, Long> qb = printContentDao.queryBuilder();
            qb.where().eq(PrintContent.$.status, 0)
                    .and()
                    .eq(PrintContent.$.commercialId, commercialId);
            qb.orderBy(PrintContent.$.modifyDateTime, false);
            return qb.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<QueueExtra> getQueueExtraList(List<Long> queueIdList) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            // 排队扩展表
            List<QueueExtra> queueExtraList = helper.getDao(QueueExtra.class)
                    .queryBuilder()
                    .where()
                    .in(QueueExtra.$.queue_id, queueIdList)
                    .and()
                    .eq(QueueExtra.$.status_flag, StatusFlag.VALID)
                    .query();
            return queueExtraList;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public QueueExtra getQueueExtra(Long queueId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            // 排队扩展表
            QueueExtra queueExtra = helper.getDao(QueueExtra.class)
                    .queryBuilder()
                    .where()
                    .eq(QueueExtra.$.queue_id, queueId)
                    .and()
                    .eq(QueueExtra.$.status_flag, StatusFlag.VALID)
                    .queryForFirst();
            return queueExtra;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<Queue> listQueueBetweenTime(Long queueLineId, Date startDate, Date endDate) throws SQLException {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Queue, String> queueDao = helper.getDao(Queue.class);
            QueryBuilder<Queue, String> qb = queueDao.queryBuilder();
            Where<Queue, String> where = qb.where();
            if (queueLineId != null && queueLineId > 0) {
                where.eq(Queue.$.queueLineId, queueLineId).and();
            }

            return where.between(Queue.$.createDateTime, startDate.getTime(), endDate.getTime()).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}
