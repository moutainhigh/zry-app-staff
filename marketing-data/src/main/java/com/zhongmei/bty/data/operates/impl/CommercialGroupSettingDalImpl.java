package com.zhongmei.bty.data.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.bty.data.db.common.CommercialGroupSetting;
import com.zhongmei.bty.data.operates.CommercialGroupSettingDal;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by demo on 2018/12/15
 * 获取系统设置默认语言
 */
public class CommercialGroupSettingDalImpl extends AbstractOpeartesImpl implements CommercialGroupSettingDal {

    public CommercialGroupSettingDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public String getDefaultLanguage() {
        String language = null;
        DatabaseHelper helper = DBHelperManager.getHelper();
        try {
            Dao<CommercialGroupSetting, String> tableDao = helper.getDao(CommercialGroupSetting.class);
            QueryBuilder<CommercialGroupSetting, String> qb = tableDao.queryBuilder();
//            qb.where().eq(CommercialGroupSetting.$.commercialGroupId, commercialId);
            List<CommercialGroupSetting> listData = qb.query();
            if (listData != null && listData.size() > 0) {
                CommercialGroupSetting mCommercialGroupSetting = listData.get(0);
                language = mCommercialGroupSetting.getLanguageDefault()
                ;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }

        return language;
    }

}
