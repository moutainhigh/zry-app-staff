package com.zhongmei.bty.data.operates.impl;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.basemodule.auth.user.entity.AuthBrandBusiness;
import com.zhongmei.bty.commonmodule.database.entity.AuthBusiness;
import com.zhongmei.yunfu.db.enums.StatusFlag;
import com.zhongmei.bty.data.operates.AuthBusinessDal;

/**
 * 业务权限查询dal
 * Created by demo on 2018/12/15
 */

public class AuthBusinessDalImpl extends AbstractOpeartesImpl implements AuthBusinessDal {

    public static final String Tag = "AuthBusinessDalImpl";

    public AuthBusinessDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public AuthBusiness queryBusinessByCode(String code) {
        DatabaseHelper helper = DBHelperManager.getHelper();
        Dao<AuthBusiness, String> authorizedDal = null;
        try {
            authorizedDal = helper.getDao(AuthBusiness.class);
            QueryBuilder<AuthBusiness, String> builder = authorizedDal.queryBuilder();

            Dao<AuthBrandBusiness, String> brandBusinessDao = helper.getDao(AuthBrandBusiness.class);
            QueryBuilder<AuthBrandBusiness, String> brandQueryBuilder = brandBusinessDao.queryBuilder();
            brandQueryBuilder.selectColumns(AuthBrandBusiness.$.businessId).where().eq(AuthBrandBusiness.$.statusFlag, StatusFlag.VALID);
            builder.where().ge(AuthBusiness.$.supportVersion, 5)
                    .and()
                    .eq(AuthBusiness.$.code, code)
                    .and()
                    .eq(AuthBusiness.$.sourceFlag, 1)
                    .and()
                    .eq(AuthBusiness.$.statusFlag, StatusFlag.VALID)
                    .and().in(AuthBusiness.$.id, brandQueryBuilder);
            return builder.queryForFirst();
        } catch (Exception e) {
            Log.e(Tag, e.getMessage());
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return null;
    }
}
