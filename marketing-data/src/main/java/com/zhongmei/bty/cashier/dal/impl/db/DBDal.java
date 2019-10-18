package com.zhongmei.bty.cashier.dal.impl.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.cashier.dal.BaseDal;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;

import java.sql.SQLException;


public abstract class DBDal extends BaseDal {

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Context context) {
        super.onCreate(context);
    }

    protected <D extends Dao<E, ?>, E> D getDao(Class<E> classType) throws SQLException {
        return dbHelper.getDao(classType);
    }

    public synchronized void prepare() {
        dbHelper = DBHelperManager.getHelper();
    }

    public synchronized void release() {
        DBHelperManager.releaseHelper(dbHelper);
    }

}
