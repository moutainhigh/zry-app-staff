package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.basemodule.erp.bean.ErpCurrency;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.entity.ErpCommercialRelation;
import com.zhongmei.bty.basemodule.erp.operates.ErpCommercialRelationDal;

import java.sql.SQLException;
import java.util.List;

public class ErpCommercialRelationDalImpl extends AbstractOpeartesImpl implements ErpCommercialRelationDal {
    final String TAG = "ErpCommercialRelationDalImpl";

    public ErpCommercialRelationDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public boolean findIsShukeInErp() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ErpCommercialRelation, String> dao = helper.getDao(ErpCommercialRelation.class);
            QueryBuilder<ErpCommercialRelation, String> qb = dao.queryBuilder();
            ErpCommercialRelation relation = qb.queryForFirst();

            return relation.getIsShuke() == 1;
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public ErpCommercialRelation findErpCommercialRelation() {
        ErpCommercialRelation erpCommercialRelation = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ErpCommercialRelation, Long> erpComRelDao = helper.getDao(ErpCommercialRelation.class);
            QueryBuilder<ErpCommercialRelation, Long> erpComRelQb = erpComRelDao.queryBuilder();
            erpComRelQb.orderBy(ErpCommercialRelation.$.id, false);
            erpCommercialRelation = erpComRelQb.queryForFirst();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return erpCommercialRelation;
    }

    @Override
    public List<ErpCurrency> queryErpCurrenctList() {
        List<ErpCurrency> countryList = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ErpCurrency, String> dao = helper.getDao(ErpCurrency.class);
            countryList = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return countryList;
    }

    @Override
    public ErpCurrency queryErpCurrenctByAreaCode(String areaCode) {
        ErpCurrency erpCurrency = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ErpCurrency, String> dao = helper.getDao(ErpCurrency.class);
            QueryBuilder<ErpCurrency, String> qb = dao.queryBuilder();
            qb.where().eq(ErpCurrency.$.areaCode, areaCode);
            erpCurrency = qb.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return erpCurrency;
    }

    @Override
    public ErpCurrency getErpCurrency(Long currencyId) {
        ErpCurrency erpCurrency = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ErpCurrency, String> dao = helper.getDao(ErpCurrency.class);
            QueryBuilder<ErpCurrency, String> qb = dao.queryBuilder();
            qb.where().eq(ErpCurrency.$.id, currencyId);
            erpCurrency = qb.queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
        return erpCurrency;
    }
}
