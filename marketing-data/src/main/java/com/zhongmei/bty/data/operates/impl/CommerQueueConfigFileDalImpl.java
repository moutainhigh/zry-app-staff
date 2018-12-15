package com.zhongmei.bty.data.operates.impl;

import com.zhongmei.bty.basemodule.database.queue.QueueDetailImage;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;

import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.commonmodule.database.entity.CommercialQueueConfigFile;
import com.zhongmei.bty.data.db.common.queue.QueueImage;
import com.zhongmei.bty.data.operates.CommerQueueConfigFileDal;

/**
 * 语音下载列表
 */
public class CommerQueueConfigFileDalImpl extends AbstractOpeartesImpl implements CommerQueueConfigFileDal {

    public CommerQueueConfigFileDalImpl(ImplContext context) {
        super(context);
    }


    @Override
    public List<CommercialQueueConfigFile> listSysFileBroadCast() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialQueueConfigFile, Long> dao = helper.getDao(CommercialQueueConfigFile.class);
            return dao.queryForEq(CommercialQueueConfigFile.$.statusFlag, StatusFlag.VALID);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<CommercialQueueConfigFile> listSysFileBroadCastInValid() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialQueueConfigFile, Long> dao = helper.getDao(CommercialQueueConfigFile.class);
            return dao.queryForEq(CommercialQueueConfigFile.$.statusFlag, StatusFlag.INVALID);
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    @Override
    public QueueImage getQueueCallZip() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<QueueImage, Long> dao = helper.getDao(QueueImage.class);
            QueryBuilder<QueueImage, Long> qb = dao.queryBuilder();
            qb.where().isNotNull(QueueImage.$.voice);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }


    @Override
    public boolean validFile(String fileName) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialQueueConfigFile, Long> dao = helper.getDao(CommercialQueueConfigFile.class);
            QueryBuilder<CommercialQueueConfigFile, Long> qb = dao.queryBuilder();
            qb.where().eq(CommercialQueueConfigFile.$.name, fileName)
                    .and().eq(CommercialQueueConfigFile.$.statusFlag, StatusFlag.VALID);
            CommercialQueueConfigFile file = qb.queryForFirst();
            if (file != null) {
                return file.getStatusFlag() == StatusFlag.VALID;
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return false;
    }

    @Override
    public QueueImage getQueueImage() throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<QueueImage, Long> dao = helper.getDao(QueueImage.class);
            QueryBuilder<QueueImage, Long> qb = dao.queryBuilder();
            qb.where().eq(QueueImage.$.type, 0)
                    .and()
                    .eq(QueueImage.$.status, 0);
            return qb.queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<QueueDetailImage> getQueueDetailImageList(String queueImgId) throws Exception {
        final DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<QueueDetailImage, Long> queueDetailImageLongDao = helper.getDao(QueueDetailImage.class);
            QueryBuilder<QueueDetailImage, Long> queueDetailImageLongQueryBuilder = queueDetailImageLongDao.queryBuilder();
            queueDetailImageLongQueryBuilder.orderBy(QueueDetailImage.$.modifyDateTime, false)
                    .where()
                    .eq(QueueDetailImage.$.queueImgId, queueImgId)
                    .and()
                    .eq(QueueDetailImage.$.status, 0);
            return queueDetailImageLongQueryBuilder.query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }
}
