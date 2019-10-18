package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.data.db.common.Commercial;
import com.zhongmei.bty.data.operates.CommercialDal;

import java.util.ArrayList;
import java.util.List;


public class CommercialImpl extends AbstractOpeartesImpl implements CommercialDal {

    public CommercialImpl(ImplContext context) {
        super(context);
    }


    @Override
    public List<Long> queryAllShopIdenty() {
        List<Long> listShopIdenty = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<Commercial, String> dao = helper.getDao(Commercial.class);

            QueryBuilder<Commercial, String> qb = dao.queryBuilder();

            qb.selectColumns(Commercial.$.commercialID);
            qb.groupBy(Commercial.$.commercialID);

            List<Commercial> itemList = qb.query();
            if (itemList != null) {
                listShopIdenty = new ArrayList<>();
                for (Commercial item : itemList) {
                    listShopIdenty.add(item.getCommercialID());
                }
            }
            return listShopIdenty;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return listShopIdenty;

    }
}
