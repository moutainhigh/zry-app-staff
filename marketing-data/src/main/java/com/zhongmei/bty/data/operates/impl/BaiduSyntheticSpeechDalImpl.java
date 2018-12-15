package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.local.BaiduSyntheticSpeech;
import com.zhongmei.yunfu.context.util.Utils;
import com.zhongmei.bty.data.operates.BaiduSyntheticSpeechDal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 语音数据
 */
public class BaiduSyntheticSpeechDalImpl extends AbstractOpeartesImpl implements BaiduSyntheticSpeechDal {
    private static final String TAG = BaiduSyntheticSpeechDalImpl.class.getSimpleName();

    public BaiduSyntheticSpeechDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public void saveQueue(final BaiduSyntheticSpeech speech) throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, BaiduSyntheticSpeech.class, speech);
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同步

        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void saveQueueList(final List<BaiduSyntheticSpeech> speechList) throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, BaiduSyntheticSpeech.class, speechList);
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同步

        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void updateQueue(final BaiduSyntheticSpeech speech) throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            // modify 20170217 start 添加事务来实现线程同步
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, BaiduSyntheticSpeech.class, speech);
                    return null;
                }
            });
            // modify 20170217 end 添加事务来实现线程同步
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public List<BaiduSyntheticSpeech> listSyntherticSpeech() throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<BaiduSyntheticSpeech, String> dao = helper.getDao(BaiduSyntheticSpeech.class);
            return dao.queryForEq(BaiduSyntheticSpeech.$.type, 2);
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public BaiduSyntheticSpeech getCallSpeech() throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<BaiduSyntheticSpeech, String> dao = helper.getDao(BaiduSyntheticSpeech.class);
            QueryBuilder<BaiduSyntheticSpeech, String> qb = dao.queryBuilder();
            qb.where().eq(BaiduSyntheticSpeech.$.type, 1);
            return qb.queryForFirst();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void delete(BaiduSyntheticSpeech speech) throws Exception {
        try {
            DBHelperManager.deleteById(BaiduSyntheticSpeech.class, speech.getUuid());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Map<Integer, BaiduSyntheticSpeech> getQueueVoiceMap() throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        Map<Integer, BaiduSyntheticSpeech> speechMap = new HashMap<>();
        List<BaiduSyntheticSpeech> speechList;
        try {
            Dao<BaiduSyntheticSpeech, String> dao = helper.getDao(BaiduSyntheticSpeech.class);
            QueryBuilder<BaiduSyntheticSpeech, String> qb = dao.queryBuilder();
            qb.where().eq(BaiduSyntheticSpeech.$.type, 1);
            speechList = qb.query();
        } finally {
            LocalDBManager.releaseHelper();
        }
        if (Utils.isNotEmpty(speechList)) {
            for (BaiduSyntheticSpeech speech : speechList) {
                speechMap.put(speech.getQueueVoiceType(), speech);
            }
        }
        return speechMap;
    }

    @Override
    public List<BaiduSyntheticSpeech> getQueueVoiceList() throws Exception {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            Dao<BaiduSyntheticSpeech, String> dao = helper.getDao(BaiduSyntheticSpeech.class);
            QueryBuilder<BaiduSyntheticSpeech, String> qb = dao.queryBuilder();
            qb.where().eq(BaiduSyntheticSpeech.$.type, 1);
            return qb.query();
        } finally {
            LocalDBManager.releaseHelper();
        }
    }
}
