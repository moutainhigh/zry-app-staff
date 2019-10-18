package com.zhongmei.bty.basemodule.database.operates.impl;

import com.j256.ormlite.dao.Dao;
import com.zhongmei.bty.basemodule.database.operates.EcCardDal;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardLevelSetting;
import com.zhongmei.bty.basemodule.devices.mispos.data.EcCardSettingDetail;
import com.zhongmei.bty.commonmodule.data.operate.AbstractOpeartesImpl;
import com.zhongmei.yunfu.orm.DBHelperManager;
import com.zhongmei.yunfu.orm.DatabaseHelper;
import com.zhongmei.yunfu.db.enums.StatusFlag;

import java.sql.SQLException;
import java.util.List;


public class EcCardDalImpl extends AbstractOpeartesImpl implements EcCardDal {

    private static final String TAG = AbstractOpeartesImpl.class.getSimpleName();

    public EcCardDalImpl(ImplContext context) {
        super(context);
    }

    @Override
    public EcCardLevelSetting findEcCardLevelSetting(Long levelId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<EcCardLevelSetting, Long> dao = helper.getDao(EcCardLevelSetting.class);
            return dao.queryBuilder().where().eq(EcCardLevelSetting.$.cardLevelId, levelId).and()
                    .eq(EcCardLevelSetting.$.statusFlag, StatusFlag.VALID).queryForFirst();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

    @Override
    public List<EcCardSettingDetail> findEcCardSettingDetail(Long levelId) throws SQLException {
        DatabaseHelper helper = DBHelperManager.getHelper();

        try {
            Dao<EcCardSettingDetail, Long> dao = helper.getDao(EcCardSettingDetail.class);
            return dao.queryBuilder().where().eq(EcCardSettingDetail.$.cardLevelId, levelId).and()
                    .eq(EcCardSettingDetail.$.statusFlag, StatusFlag.VALID).query();
        } finally {
            DBHelperManager.releaseHelper(helper);
        }
    }

}
