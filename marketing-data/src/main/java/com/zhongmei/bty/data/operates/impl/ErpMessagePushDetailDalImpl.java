package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.erp.bean.ErpMessagePushDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc
 *
 * @created 2017/9/14
 */
public class ErpMessagePushDetailDalImpl extends AbstractOpeartesImpl implements ErpMessagePushDetailDal {

    public ErpMessagePushDetailDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    public List<ErpMessagePushDetail> findAll() {
        return null;
    }

    @Override
    public List<ErpMessagePushDetail> findByMsgNotice() {
        final List<ErpMessagePushDetail> result = new ArrayList<>();
        db(new DatabaseHelperCallback() {
            @Override
            public void onDatabaseHelper(DatabaseHelper helper) throws Exception {
                Dao<ErpMessagePushDetail, Long> queueDao = helper.getDao(ErpMessagePushDetail.class);
                List<ErpMessagePushDetail> erpMessagePushDetailList = queueDao.queryBuilder()
                        .where()
                        .eq(ErpMessagePushDetail.$.categoryId, ErpMessagePushDetail.CategoryNotice)
                        .and()
                        .eq(ErpMessagePushDetail.$.readedLocal, 0)
                        .and()
                        .eq(ErpMessagePushDetail.$.status, StatusFlag.VALID)
                        /*.and()
                        .ge(ErpMessagePushDetail.$.effectiveDate, System.currentTimeMillis())
                        .and()
                        .le(ErpMessagePushDetail.$.expiryDate, System.currentTimeMillis())*/
                        .query();
                result.addAll(erpMessagePushDetailList);
            }
        });
        return result;
    }

    @Override
    public void updateReadLocal(final Long id) {
        db(new DatabaseHelperCallback() {
            @Override
            public void onDatabaseHelper(DatabaseHelper helper) throws Exception {
                Dao<ErpMessagePushDetail, Long> queueDao = helper.getDao(ErpMessagePushDetail.class);
                ErpMessagePushDetail erpMessagePushDetail = queueDao.queryForId(id);
                if (erpMessagePushDetail != null) {
                    erpMessagePushDetail.setReadedLocal(erpMessagePushDetail.getReadedLocal() + 1);
                    queueDao.update(erpMessagePushDetail);
                }
            }
        });
    }

    @Override
    public void update(final ErpMessagePushDetail erpMessagePushDetail) {
        db(new DatabaseHelperCallback() {
            @Override
            public void onDatabaseHelper(DatabaseHelper helper) throws Exception {
                Dao<ErpMessagePushDetail, Long> queueDao = helper.getDao(ErpMessagePushDetail.class);
                queueDao.update(erpMessagePushDetail);
            }
        });
    }
}
