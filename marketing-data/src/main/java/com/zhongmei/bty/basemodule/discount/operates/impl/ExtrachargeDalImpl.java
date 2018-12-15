package com.zhongmei.bty.basemodule.discount.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.zhongmei.bty.basemodule.discount.entity.ExtraCharge;
import com.zhongmei.bty.basemodule.discount.manager.ExtraManager;
import com.zhongmei.bty.basemodule.discount.operates.ExtraChargeDal;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.bty.commonmodule.data.operate.IOperates;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.Bool;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.util.List;

/**
 * Created by demo on 2018/12/15
 */

public class ExtrachargeDalImpl extends AbstractOpeartesImpl implements ExtraChargeDal {

    private static final String TAG = ExtrachargeDalImpl.class.getName();

    public ExtrachargeDalImpl(IOperates.ImplContext context) {
        super(context);
    }

    @Override
    public List<ExtraCharge> queryExtraChargeRules(boolean isQueryOrder)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ExtraCharge, String> dao = helper.getDao(ExtraCharge.class);
            QueryBuilder<ExtraCharge, String> qb = dao.queryBuilder();
            Where<ExtraCharge, String> where =
                    qb.where().eq(ExtraCharge.$.statusFlag, StatusFlag.VALID).and().eq(ExtraCharge.$.enabledFlag, Bool.YES)
                            .and().ne(ExtraCharge.$.code, "ZZCSF").and().ne(ExtraCharge.$.code, "ZDXFCE");
            if (!isQueryOrder) {
                return where.query();
            } else {
                return where.and().eq(ExtraCharge.$.orderFlag, Bool.YES).query();
            }
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public ExtraCharge queryExtraById(Long extraId)
            throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ExtraCharge, String> dao = helper.getDao(ExtraCharge.class);
            QueryBuilder<ExtraCharge, String> qb = dao.queryBuilder();
            return qb.where()
                    .eq(ExtraCharge.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(ExtraCharge.$.enabledFlag, Bool.YES)
                    .and()
                    .eq(ExtraCharge.$.id, extraId)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public ExtraCharge queryExtraChargeChf() throws Exception {
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<ExtraCharge, String> dao = helper.getDao(ExtraCharge.class);
            QueryBuilder<ExtraCharge, String> qb = dao.queryBuilder();
            return qb.where()
                    .eq(ExtraCharge.$.statusFlag, StatusFlag.VALID)
                    .and()
                    .eq(ExtraCharge.$.enabledFlag, Bool.YES)
                    .and()
                    .eq(ExtraCharge.$.code, ExtraManager.mealFee)
                    .queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}
