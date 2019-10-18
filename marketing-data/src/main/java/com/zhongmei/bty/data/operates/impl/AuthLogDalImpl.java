package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.commonmodule.database.db.local.LocalDBManager;
import com.zhongmei.bty.commonmodule.database.entity.AuthorizedLog;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.basemodule.auth.permission.operates.AuthLogDal;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;

import java.util.List;
import java.util.concurrent.Callable;



public class AuthLogDalImpl extends AbstractOpeartesImpl implements AuthLogDal {
    private static final String Tag = "AuthLogDalImpl";

    public AuthLogDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public void insert(final AuthorizedLog authorizedLog) {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
                        final Dao<AuthorizedLog, String> authorizedDal = helper.getDao(AuthorizedLog.class);
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    authorizedDal.createOrUpdate(authorizedLog);
                    return null;
                }
            });
                    } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void update(final AuthorizedLog authorizedLog) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        try {
                        final Dao<AuthorizedLog, String> authorizedDal = helper.getDao(AuthorizedLog.class);
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    authorizedDal.update(authorizedLog);
                    return null;
                }
            });
                    } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void delete(String uuid) {
        try {
                        DBHelperManager.deleteById(AuthorizedLog.class, uuid);
                    } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        }
    }

    @Override
    public AuthorizedLog queryByUuid(String uuid) {

        DatabaseHelper helper = LocalDBManager.getHelper();
        Dao<AuthorizedLog, String> authorizedDal = null;
        try {
            authorizedDal = helper.getDao(AuthorizedLog.class);
            QueryBuilder<AuthorizedLog, String> builder = authorizedDal.queryBuilder();
            builder.where().eq(AuthorizedLog.$.uuid, uuid);
            return builder.queryForFirst();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            LocalDBManager.releaseHelper();
        }
        return null;
    }

    @Override
    public List<AuthorizedLog> queryList(long count) {
        DatabaseHelper helper = LocalDBManager.getHelper();
        Dao<AuthorizedLog, String> authorizedDal = null;
        try {
            authorizedDal = helper.getDao(AuthorizedLog.class);
            QueryBuilder<AuthorizedLog, String> builder = authorizedDal.queryBuilder();
            if (count == -1) {
                                builder.orderBy(AuthorizedLog.$.clientCreateTime, true)
                        .where().eq(AuthorizedLog.$.statusFlag, StatusFlag.VALID);
            } else {
                builder.orderBy(AuthorizedLog.$.clientCreateTime, true).limit(count)
                        .where().eq(AuthorizedLog.$.statusFlag, StatusFlag.VALID);
            }

            return builder.query();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            LocalDBManager.releaseHelper();

        }
        return null;
    }

    @Override
    public void batchSave(final List<AuthorizedLog> authLogList) {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    DBHelperManager.saveEntities(helper, AuthorizedLog.class, authLogList);
                    return null;
                }
            });
                    } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            LocalDBManager.releaseHelper();
        }
    }

    @Override
    public void batchDelete(final List<AuthorizedLog> authLogList) {
        final DatabaseHelper helper = LocalDBManager.getHelper();
        try {
                        final Dao<AuthorizedLog, Integer> authorizedDal = helper.getDao(AuthorizedLog.class);
            helper.callInTransaction(new Callable<Void>() {
                @Override
                public Void call()
                        throws Exception {
                    authorizedDal.delete(authLogList);
                    return null;
                }
            });
                    } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            LocalDBManager.releaseHelper();
        }
    }
}
